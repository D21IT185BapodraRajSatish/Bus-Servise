package com.example.proto_type_1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ekalips.fancybuttonproj.FancyButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Add_Conductor extends Fragment {

    View Add_Conductor;
    String TEMP1;

    TextView retry, TXT_ID, date;

    ProgressDialog progressDialog;

    TextInputLayout firstName, lastName, email, phoneNo, address, Password;

    //Image Button
    ImageButton imgbtnDate;

    //Calender
    Calendar c;

    //Date Picker
    DatePickerDialog dpg;

    //Radio Button
    RadioButton male, female, other;

    //RadioGroup
    RadioGroup radioGroup;

    //Firebase Database Reference
    DatabaseReference databaseReference;

    //UserDefine Class
    RegisterConductor registerUser;

    //Boolean Value
    boolean validAllFilds = true, CheckEmail = true, CheckPhone = true;

    //String Value
    FirebaseAuth auth;

    //FancyButton
    FancyButton button1;

    //
    StorageReference reference;

    public Add_Conductor() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Add_Conductor = inflater.inflate(R.layout.add_conductor, container, false);
        //initialize All type of object and Controls method Call
        initialize();

        //get date click event
        imgbtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to get currant date and year
                registerUser.DOB(getContext(), date);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof FancyButton) {
                    if (isConnected()) {
                        //to check All text box are valid value or not method call
                        if (ValidFilds()) {
                            //to Store All control/ form/ fragment value in Firebase Database User Define Method Call
                            ((FancyButton) view).collapse();
                            //storeToDataBase();
                            APPLIATION();
                        }
                    } else {
                        AlertDialogBox();
                    }
                }

            }
        };
        button1.setOnClickListener(listener);

        return Add_Conductor;
    }

    //initialize All type of object and Controls method
    private void initialize() {

        TXT_ID = Add_Conductor.findViewById(R.id.TXT_ID_NUM_AC);

        button1 = Add_Conductor.findViewById(R.id.btn1);

        progressDialog = new ProgressDialog(Add_Conductor.getContext());

        //Text Input Layout
        firstName = Add_Conductor.findViewById(R.id.First_Name_AC);
        lastName = Add_Conductor.findViewById(R.id.Last_Name_AC);
        email = Add_Conductor.findViewById(R.id.EMAIL_AC);
        phoneNo = Add_Conductor.findViewById(R.id.Phone_Number_AC);
        address = Add_Conductor.findViewById(R.id.Address_AC);
        Password = Add_Conductor.findViewById(R.id.Password_AC);

        //Image Button
        imgbtnDate = Add_Conductor.findViewById(R.id.imgbtn_AC);

        //Radio Button
        male = Add_Conductor.findViewById(R.id.male_AC);
        female = Add_Conductor.findViewById(R.id.female_AC);
        other = Add_Conductor.findViewById(R.id.other_AC);

        //RadioGroup
        radioGroup = Add_Conductor.findViewById(R.id.RBG_AC);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male_AC:
                        registerUser.setGender("male");
                        break;
                    case R.id.female_AC:
                        registerUser.setGender("female");
                        break;
                    case R.id.other_AC:
                        registerUser.setGender("other");
                        break;
                }
            }
        });

        //Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Conductor");

        //UserDefine Class
        registerUser = new RegisterConductor();

        //Set Gender by default male
        registerUser.setGender("male");

        date = Add_Conductor.findViewById(R.id.date_AC);

        auth = FirebaseAuth.getInstance();
    }


    //to check All text box are valid value or not
    private boolean ValidFilds() {
        validAllFilds = true;
        if (firstName.getEditText().getText().toString().isEmpty() & lastName.getEditText().getText().toString().isEmpty() & email.getEditText().getText().toString().isEmpty() & phoneNo.getEditText().getText().toString().isEmpty() & address.getEditText().getText().toString().isEmpty() & date.getText().toString().equalsIgnoreCase("dd-mm-yy")) {
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
            if (email.getEditText().getText().toString().isEmpty()) {
                email.setErrorEnabled(true);
                email.setError("Please Enter This Field");
                validAllFilds = false;
            } else {
                email.setErrorEnabled(false);
                validAllFilds = true;
            }
        }
        if (validAllFilds) {
            if (Password.getEditText().getText().toString().isEmpty()) {
                Password.setErrorEnabled(true);
                Password.setError("Please Enter This Field");
                validAllFilds = false;
            } else {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
                Matcher ms = ps.matcher(Password.getEditText().getText().toString());
                if (!ms.matches()) {
                    Password.setErrorEnabled(true);
                    Password.setError("Please Enter Password In Digit & NUmber  Only");
                    validAllFilds = false;
                } else {
                    if (Password.getEditText().getText().toString().length() < 8 | Password.getEditText().getText().toString().length() > 8) {
                        Password.setErrorEnabled(true);
                        Password.setError("Please Enter Password In 8 Digit Only");
                        validAllFilds = false;
                    } else {
                        Password.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }
        }
    }
        return validAllFilds;
}

    //to Store All control/ form/ fragment value in Firebase Database User Define Method
    private void storeToDataBase() {
        registerUser.setId(TXT_ID.getText().toString());
        registerUser.setfName(firstName.getEditText().getText().toString());
        registerUser.setlName(lastName.getEditText().getText().toString());
        registerUser.setDate(date.getText().toString());
        registerUser.setAddress(address.getEditText().getText().toString());
        registerUser.setEmail(email.getEditText().getText().toString());
        registerUser.setPassword(Password.getEditText().getText().toString());
        registerUser.setPhoneNo(phoneNo.getEditText().getText().toString());
        DatabaseReference CheckDB = FirebaseDatabase.getInstance().getReference("Conductor");

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
                    }
                }
                if (CheckEmail & CheckPhone) {

                    databaseReference.child(registerUser.getId()).setValue(registerUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                auth.createUserWithEmailAndPassword(registerUser.getEmail(), registerUser.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        //APPLIATION();
                                        registerUser.SENDMAIL(getContext(), TEMP1);
                                        button1.expand();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void APPLIATION() {
        final String[] TEMP001 = new String[1];
        reference = FirebaseStorage.getInstance().getReference("APPLICATION/Conductor.apk");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                TEMP001[0] = url;
                LINK(url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                button1.expand();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LINK(String url) {
        TEMP1 = url;
        storeToDataBase();
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Fetching details");
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.hasChildren()) {
                            int temp = Integer.valueOf(ds.getValue(RegisterConductor.class).getId());
                            temp++;
                            TXT_ID.setText(String.valueOf(temp));
                            progressDialog.dismiss();
                        }
                    }
                } else {
                    int temp = 1;
                    TXT_ID.setText(String.valueOf(temp));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        Dialog dialog = new Dialog(Add_Conductor.getContext(), R.style.NO_INTERNET_DIALOG);
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