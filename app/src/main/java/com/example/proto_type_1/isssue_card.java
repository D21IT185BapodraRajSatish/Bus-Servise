package com.example.proto_type_1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ekalips.fancybuttonproj.FancyButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class isssue_card extends Fragment {

    View Issue_CardFragment;
    //TextInputLayout
    static TextInputLayout cardID;
    TextInputLayout firstName;
    TextInputLayout lastName;
    TextInputLayout email;
    TextView date;
    TextInputLayout phoneNo;
    TextInputLayout address;
    TextInputLayout Editinitamount;

    //
    GetCardID cardID1;

    //
    ProgressDialog progressDialog;

    //
    TextView retry;

    //Image Button
    ImageButton imgbtnDate;

    //Calender
    Calendar c;

    //Date Picker
    DatePickerDialog dpg;

    //Radio Button
    RadioButton R_male, female, other;

    //RadioGroup
    RadioGroup radioGroup;

    //Firebase Database Reference
    DatabaseReference databaseReference;

    //UserDefine Class
    RegisterUser registerUser;

    //Boolean Value
    boolean validAllFilds = true, CheckEmail = true, CheckPhone = true,CheckCardID=true, CheckID=false;

    //String Value
    String initValue;

    //
    FancyButton button1,button2;

    public isssue_card() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //Set View
        Issue_CardFragment = inflater.inflate(R.layout.isssue_card, container, false);

        //initialize All type of object and Controls method Call
        initialize();

        //get date click event
        imgbtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to get currant date and year
                registerUser.DOB(getContext(),date);
            }
        });

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof  FancyButton)
                {
                    if(isConnected())
                    {
                        button2.collapse();
                        GETCARDID();
                    }
                    else
                    {
                        button2.expand();
                        AlertDialogBox();
                    }
                }

            }
        };
        button2.setOnClickListener(listener2);

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof  FancyButton)
                {
                    if(isConnected())
                    {
                        button1.collapse();
                        //to check All text box are valid value or not method call
                        if (ValidFilds()) {
                            //to Store All control/ form/ fragment value in Firebase Database User Define Method Call
                            storeToDataBase();
                        }
                        else
                        {
                            button1.expand();
                        }
                    }
                    else
                    {
                        button1.expand();
                        AlertDialogBox();
                    }
                }

            }
        };
        button1.setOnClickListener(listener1);

        return Issue_CardFragment;
    }

    private void GETCARDID() {
        DatabaseReference getcardid = FirebaseDatabase.getInstance().getReference("CardID");
        getcardid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    cardID1 = snapshot.getValue(GetCardID.class);
                    cardID.getEditText().setText(cardID1.getValue());
                }else {
                    Toast.makeText(getContext(), "Please Scan Card Again", Toast.LENGTH_SHORT).show();
                }
                button2.expand();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                button2.expand();
            }
        });
    }

    //initialize All type of object and Controls method
    private void initialize() {

        //
        cardID1 = new GetCardID();

        //
        button1 = (FancyButton) Issue_CardFragment.findViewById(R.id.Submit);
        button2 = (FancyButton) Issue_CardFragment.findViewById(R.id.Get_Card_ID);

        //
        progressDialog = new ProgressDialog(Issue_CardFragment.getContext());

        //Text Input Layout
        cardID = Issue_CardFragment.findViewById(R.id.Card_ID);
        firstName = Issue_CardFragment.findViewById(R.id.First_NAME);
        lastName = Issue_CardFragment.findViewById(R.id.Last_NAME);
        email = Issue_CardFragment.findViewById(R.id.Email);
        phoneNo = Issue_CardFragment.findViewById(R.id.Phone_Number);
        address = Issue_CardFragment.findViewById(R.id.Address);
        Editinitamount = Issue_CardFragment.findViewById(R.id.Initial_Amount);
        date = Issue_CardFragment.findViewById(R.id.date);

        //Image Button
        imgbtnDate = Issue_CardFragment.findViewById(R.id.imgbtn);

        //Radio Button
        R_male = Issue_CardFragment.findViewById(R.id.male);
        female = Issue_CardFragment.findViewById(R.id.female);
        other = Issue_CardFragment.findViewById(R.id.other);

        //RadioGroup
        radioGroup = Issue_CardFragment.findViewById(R.id.RBG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        registerUser.setGender("male");
                        break;
                    case R.id.female:
                        registerUser.setGender("female");
                        break;
                    case R.id.other:
                        registerUser.setGender("other");
                        break;
                }
            }
        });

        //Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer");

        //UserDefine Class
        registerUser = new RegisterUser();

        //Set Gender by default male
        registerUser.setGender("male");

    }

    //to check All text box are valid value or not
    private boolean ValidFilds() {
        validAllFilds = true;
        if (cardID.getEditText().getText().toString().isEmpty() & firstName.getEditText().getText().toString().isEmpty() & lastName.getEditText().getText().toString().isEmpty() & email.getEditText().getText().toString().isEmpty() & phoneNo.getEditText().getText().toString().isEmpty() & address.getEditText().getText().toString().isEmpty() & Editinitamount.getEditText().getText().toString().isEmpty() & date.getText().toString().equalsIgnoreCase("dd-mm-yy")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setTitle(Html.fromHtml("<font color='#ff0f0f'>Error</font>"));
            builder.setMessage("All Field are Required");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
            validAllFilds = false;
        } else {
            if (validAllFilds) {
                if (cardID.getEditText().getText().toString().isEmpty()) {
                    cardID.setErrorEnabled(true);
                    cardID.setError("Please Enter This Field");
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
                    Matcher ms = ps.matcher(cardID.getEditText().getText().toString());
                    if (!ms.matches()) {
                        cardID.setErrorEnabled(true);
                        cardID.setError("Please Enter Valid Card ID");
                        validAllFilds = false;
                    } else {
                        if (cardID.getEditText().getText().toString().length() < 8 | cardID.getEditText().getText().toString().length() > 8) {
                            cardID.setErrorEnabled(true);
                            cardID.setError("Please Enter Card ID In 8 digit");
                            validAllFilds = false;
                        }
                        else {
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (cardID.getEditText().getText().toString().equals(ds.getValue(RegisterUser.class).getCardID())) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setCancelable(false);
                                            builder.setTitle(Html.fromHtml("<font color='#ff0f0f'>Card Issued All Ready</font>"));
                                            builder.setMessage("This Card is Issue AllReady by " + ds.getValue(RegisterUser.class).getfName() + " " + ds.getValue(RegisterUser.class).getlName());
                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            builder.show();
                                            validAllFilds = false;
                                            break;
                                        } else {
                                            validAllFilds = true;
                                        }
                                    }
                                    if (validAllFilds) {
                                        validAllFilds = true;
                                        cardID.setErrorEnabled(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }
            if (validAllFilds) {
                if (firstName.getEditText().getText().toString().isEmpty()) {
                    firstName.setErrorEnabled(true);
                    firstName.setError("Please Enter This Field");
                    validAllFilds = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z]+$");
                    Matcher ms = ps.matcher(firstName.getEditText().getText().toString());
                    if (!ms.matches()) {
                        firstName.setErrorEnabled(true);
                        firstName.setError("Please Enter Valid First Name");
                        validAllFilds = false;
                    } else {
                        firstName.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }
            if (validAllFilds) {
                if (lastName.getEditText().getText().toString().isEmpty()) {
                    lastName.setErrorEnabled(true);
                    lastName.setError("Please Enter This Field");
                    validAllFilds = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z]+$");
                    Matcher ms = ps.matcher(lastName.getEditText().getText().toString());
                    if (!ms.matches()) {
                        lastName.setErrorEnabled(true);
                        lastName.setError("Please Enter Valid Last Name");
                        validAllFilds = false;
                    } else {
                        lastName.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }
            if (validAllFilds) {
                if (date.getText().toString().equalsIgnoreCase("dd-mm-yy")) {
                    Toast.makeText(getContext(), "Please Select DOB", Toast.LENGTH_LONG).show();
                    validAllFilds = false;
                }
            }
            if (validAllFilds) {
                if (phoneNo.getEditText().getText().toString().isEmpty()) {
                    phoneNo.setErrorEnabled(true);
                    phoneNo.setError("Please Enter This Field");
                    validAllFilds = false;
                } else {
                    Pattern ps = Pattern.compile("^[0-9]+$");
                    Matcher ms = ps.matcher(phoneNo.getEditText().getText().toString());
                    if (!ms.matches()) {
                        phoneNo.setErrorEnabled(true);
                        phoneNo.setError("Please Enter Phone Number In Digit Only");
                        validAllFilds = false;
                    } else {
                        if (phoneNo.getEditText().getText().toString().length() < 10 | phoneNo.getEditText().getText().toString().length() > 10) {
                            phoneNo.setErrorEnabled(true);
                            phoneNo.setError("Please Enter Phone Number In 10 Digit Only");
                            validAllFilds = false;
                        } else {
                            phoneNo.setErrorEnabled(false);
                            validAllFilds = true;
                        }
                    }
                }
            }
            if (validAllFilds) {
                if (email.getEditText().getText().toString().isEmpty()) {
                    email.setErrorEnabled(true);
                    email.setError("Please Enter This Field");
                    validAllFilds = false;
                }
            }

            if (validAllFilds) {
                if (address.getEditText().getText().toString().isEmpty()) {
                    address.setErrorEnabled(true);
                    address.setError("Please Enter This Field");
                    validAllFilds = false;
                } else {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9./, ]+$");
                    Matcher ms = ps.matcher(address.getEditText().getText().toString());
                    if (!ms.matches()) {
                        String temp1 = address.getEditText().getText().toString();
                        String temp2 = String.valueOf(temp1.charAt(temp1.indexOf("-")));

                        int i = temp2.compareTo("-");
                        if (i == 0) {
                            validAllFilds = true;
                            address.setErrorEnabled(false);
                        } else {
                            address.setErrorEnabled(true);
                            address.setError("Please Enter Address in character only");
                            validAllFilds = false;
                        }
                    } else {
                        address.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }
            if (validAllFilds) {
                if (Editinitamount.getEditText().getText().toString().isEmpty()) {
                    Editinitamount.setErrorEnabled(true);
                    Editinitamount.setError("Please Enter This Field");
                    validAllFilds = false;
                } else {
                    Pattern ps = Pattern.compile("^[0-9.]+$");
                    Matcher ms = ps.matcher(Editinitamount.getEditText().getText().toString());
                    if (!ms.matches()) {
                        Editinitamount.setErrorEnabled(true);
                        Editinitamount.setError("Please Enter Initial Amount In Digit Only");
                        validAllFilds = false;
                    } else {
                        Editinitamount.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }

        }
        return validAllFilds;
    }

    //to Store All control/ form/ fragment value in Firebase Database User Define Method
    private void storeToDataBase() {
        registerUser.setCardID(cardID.getEditText().getText().toString());
        registerUser.setfName(firstName.getEditText().getText().toString());
        registerUser.setlName(lastName.getEditText().getText().toString());
        registerUser.setDate(date.getText().toString());
        registerUser.setPhoneNo(phoneNo.getEditText().getText().toString());
        registerUser.setEmail(email.getEditText().getText().toString());
        registerUser.setAddress(address.getEditText().getText().toString());

        DatabaseReference CheckDB = FirebaseDatabase.getInstance().getReference("Customer");

        CheckDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.hasChildren()) {
                        if (registerUser.getPhoneNo().equals(ds.getValue(RegisterUser.class).getPhoneNo())) {
                            Toast.makeText(getContext(), "This Mobile Number Is allReady register", Toast.LENGTH_SHORT).show();
                            CheckPhone = false;
                            break;
                        } else {
                            CheckPhone = true;
                        }
                        if (registerUser.getEmail().equals(ds.getValue(RegisterUser.class).getEmail())) {
                            Toast.makeText(getContext(), "This E-mail is allReady register", Toast.LENGTH_SHORT).show();
                            CheckEmail = false;
                            break;
                        } else {
                            CheckEmail = true;
                        }
                        if (registerUser.getCardID().equals(ds.getValue(RegisterUser.class).getCardID())) {
                            Toast.makeText(getContext(), "This Card is allReady register", Toast.LENGTH_SHORT).show();
                            CheckCardID = false;
                            break;
                        } else {
                            CheckCardID = true;
                        }
                        if (registerUser.getCardID().equals(ds.getValue(RegisterUser.class).getCardID())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<font color='#ff0f0f'>Card Issued All Ready</font>"));
                            builder.setMessage("This Card is Issue AllReady by " + ds.getValue(RegisterUser.class).getfName() + " " + ds.getValue(RegisterUser.class).getlName());
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                            CheckCardID = false;
                            break;
                        } else {
                            CheckCardID = true;
                        }
                    }
                }
                if (CheckEmail & CheckPhone & CheckCardID) {
                    if (TextUtils.isEmpty(Editinitamount.getEditText().getText().toString())) {
                        initValue = "50";
                    } else {
                        double T1 = Double.valueOf(Editinitamount.getEditText().getText().toString()) + 50;
                        initValue = String.valueOf(T1);
                    }
                    registerUser.setBalance(initValue);

                    databaseReference.child(registerUser.getCardID()).setValue(registerUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //sendMail();
                                registerUser.SENDMAIL(getContext());

                                cardID.getEditText().setText("");
                                firstName.getEditText().setText("");
                                lastName.getEditText().setText("");
                                date.setText("");
                                phoneNo.getEditText().setText("");
                                email.getEditText().setText("");
                                address.getEditText().setText("");
                                Editinitamount.getEditText().setText("");

                                button1.expand();
                            }
                        }
                    });
                }
                else{
                    button1.expand();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void AlertDialogBox() {
        Dialog dialog = new Dialog(Issue_CardFragment.getContext(), R.style.NO_INTERNET_DIALOG);
        dialog.setContentView(R.layout.no_internet_dilog);
        dialog.setCancelable(false);
        dialog.show();
        retry = dialog.findViewById(R.id.BTN_RETRY);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Waiting For Connection..");
                progressDialog.show();
                startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnected()) {

                            progressDialog.dismiss();
                            AlertDialogBox();
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                }, 5000);
            }
        });
    }
}