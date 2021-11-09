package com.example.proto_type_1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ekalips.fancybuttonproj.FancyButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class recharge_crad extends Fragment {

    //TextInputLayout
    @SuppressLint("StaticFieldLeak")
    static TextInputLayout cardId;

    //View
    View Recharge_Card_Fragment;
    TextInputLayout amount;
    //
    ProgressDialog progressDialog;

    //Button
    //Button iconbutton, rechargebutton;
    //
    TextView retry;
    //UserDefine Class
    RegisterUser UserInfo;
    //
    GetCardID cardID;
    //firebase Database Reference
    DatabaseReference RechageCard;
    String UserEmail;

    //Double
    double UserBalance;

    //Boolean
    boolean CMPCRADID = false, validAllFilds = true, CheckID = false;

    //
    FancyButton button1, button2;

    public recharge_crad() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Recharge_Card_Fragment = inflater.inflate(R.layout.recharge_crad, container, false);

        //initialize All type of object and Controls method Call
        init();

        /*Get Card Id Btn Click Event
        iconbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isConnected())
                new doit().execute();
               else
                   AlertDialogBox();
            }
        });

        Recharge button click event
       rechargebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected())
                {
                    if(ValidFilds())
                    {
                        DatabaseReference getValue = FirebaseDatabase.getInstance().getReference("Customer").child(cardId.getEditText().getText().toString());
                        getValue.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserInfo = dataSnapshot.getValue(RegisterUser.class);
                                UserEmail = UserInfo.getEmail();
                                UserBalance = Double.parseDouble(UserInfo.getBalance());
                                RechargeCard(cardId.getEditText().getText().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else
                {
                    AlertDialogBox();
                }
            }
        });*/

        View.OnClickListener listener = view -> {
            if (view instanceof FancyButton) {
                button2.collapse();
                if (isConnected()) {
                    GETCARDID();
                    //new doit().execute();
                } else {
                    button2.expand();
                    AlertDialogBox();
                }
            }

        };
        button2.setOnClickListener(listener);

        View.OnClickListener listener2 = view -> {
            if (view instanceof FancyButton) {
                button1.collapse();
                if (isConnected()) {
                    if (ValidFilds()) {
                        DatabaseReference GetCardInfoDBCheck = FirebaseDatabase.getInstance().getReference("Customer");
                        GetCardInfoDBCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot cardidDB : dataSnapshot.getChildren()) {
                                    if (cardId.getEditText().getText().toString().equals(cardidDB.getValue(RegisterUser.class).getCardID())) {
                                        UserInfo = cardidDB.getValue(RegisterUser.class);
                                        UserEmail = UserInfo.getEmail();
                                        UserBalance = Double.parseDouble(UserInfo.getBalance());
                                        cardId.setErrorEnabled(false);
                                        button1.setVisibility(View.VISIBLE);
                                        CheckID = true;
                                        break;
                                    } else {
                                        CheckID = false;
                                        button1.setVisibility(View.GONE);
                                        cardId.setErrorEnabled(true);
                                        cardId.setError("Invalid Card ID");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        if (CheckID) {
                            DatabaseReference getValue = FirebaseDatabase.getInstance().getReference("Customer").child(cardId.getEditText().getText().toString());
                            getValue.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserInfo = dataSnapshot.getValue(RegisterUser.class);
                                    UserEmail = UserInfo.getEmail();
                                    UserBalance = Double.parseDouble(UserInfo.getBalance());
                                    RechargeCard(cardId.getEditText().getText().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        DatabaseReference getValue = FirebaseDatabase.getInstance().getReference("Customer").child(cardId.getEditText().getText().toString());
                        getValue.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserInfo = dataSnapshot.getValue(RegisterUser.class);
                                UserEmail = UserInfo.getEmail();
                                UserBalance = Double.parseDouble(UserInfo.getBalance());
                                RechargeCard(cardId.getEditText().getText().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    AlertDialogBox();
                }
            }

        };
        button1.setOnClickListener(listener2);

        return Recharge_Card_Fragment;
    }

    private void GETCARDID() {
        DatabaseReference getcardid = FirebaseDatabase.getInstance().getReference("CardID");
        getcardid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cardID = snapshot.getValue(GetCardID.class);
                    cardId.getEditText().setText(cardID.getValue());

                } else {
                    Toast.makeText(getContext(), "Please Scan Card Again", Toast.LENGTH_SHORT).show();
                }
                button2.expand();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                button2.expand();
            }
        });

        DatabaseReference GetCardInfoDBCheck = FirebaseDatabase.getInstance().getReference("Customer");
        GetCardInfoDBCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot cardidDB : dataSnapshot.getChildren()) {
                    if (cardId.getEditText().getText().toString().equals(cardidDB.getValue(RegisterUser.class).getCardID())) {
                        UserInfo = cardidDB.getValue(RegisterUser.class);
                        UserEmail = UserInfo.getEmail();
                        UserBalance = Double.parseDouble(UserInfo.getBalance());
                        cardId.setErrorEnabled(false);
                        button1.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        button1.setVisibility(View.GONE);
                        cardId.setErrorEnabled(true);
                        cardId.setError("Invalid Card ID");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //recharge cad  method
    public void RechargeCard(final String ID) {
        if (ID.isEmpty() | amount.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Value in card ID or amount", Toast.LENGTH_SHORT).show();
        } else {
            if (Double.parseDouble(amount.getEditText().getText().toString()) <= 1) {
                Toast.makeText(getContext(), "Please enter amount more than 0", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference CmpIDdb = FirebaseDatabase.getInstance().getReference("Customer");
                CmpIDdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ID.equals(ds.getValue(RegisterUser.class).getCardID())) {
                                CMPCRADID = true;
                                break;
                            } else {
                                CMPCRADID = false;
                            }
                        }
                        if (CMPCRADID) {
                            double amountvael = Double.parseDouble(amount.getEditText().getText().toString());
                            RechageCard = FirebaseDatabase.getInstance().getReference("Customer").child(cardId.getEditText().getText().toString());
                            RegisterUser bel = new RegisterUser();
                            UserBalance = UserBalance + amountvael;
                            bel.setBalance(String.valueOf(UserBalance));
                            RechageCard.child("balance").setValue(bel.getBalance());
                            button1.expand();
                            sendMail();
                        } else {
                            button1.expand();
                            Toast.makeText(getContext(), "Invalid Card ID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        button1.expand();
                    }
                });
            }
        }
    }


    //Send Recipient Java Mail
    private void sendMail() {
        String mail = UserEmail;

        String message = "Dear Passenger,\n" +
                "\t\t\t\tYour recharge of ₹ " + Double.parseDouble(amount.getEditText().getText().toString()) + " is successfully completed. ₹ " + Double.parseDouble(amount.getEditText().getText().toString()) + " is add to the current balance. Now your current balance is rs ₹ " + UserBalance + ".\n" +
                "Thank You,\n" +
                "Bus Services";

        String subject = "RECHARGE CARD";

        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), mail, subject, message);

        javaMailAPI.SetToastMessage("CARD RECHARGE SUCCESSFULLY");

        javaMailAPI.execute();
        button1.expand();
    }


    //initialize All type of object and Controls method
    private void init() {

        //
        cardID = new GetCardID();

        //
        button1 = (FancyButton) Recharge_Card_Fragment.findViewById(R.id.FRAGMENT_RECHARGE_BTN);
        button2 = (FancyButton) Recharge_Card_Fragment.findViewById(R.id.FRAGMENT_RECHARGE_GET_ID_BTN);

        //
        progressDialog = new ProgressDialog(Recharge_Card_Fragment.getContext());

        //Text Input Layout
        cardId = Recharge_Card_Fragment.findViewById(R.id.FRAGMENT_CARD_ID);
        amount = Recharge_Card_Fragment.findViewById(R.id.FRAGMENT_AMOUNT);

        /*Button
        iconbutton = Recharge_Card_Fragment.findViewById(R.id.FRAGMENT_RECHARGE_GET_ID_BTN);
        rechargebutton = Recharge_Card_Fragment.findViewById(R.id.FRAGMENT_RECHARGE_BTN);*/

        //UserDefine class
        UserInfo = new RegisterUser();
    }

    //
    private boolean ValidFilds() {
        if (cardId.getEditText().getText().toString().isEmpty() & amount.getEditText().getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setTitle(Html.fromHtml("<font color='#ff0f0f'>Error</font>"));
            builder.setMessage("All Field are Required");
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                dialogInterface.cancel();
                button1.expand();
            });
            builder.create();
            builder.show();
            validAllFilds = false;
            button1.expand();
        } else {
            validAllFilds = true;
            if (validAllFilds) {
                if (amount.getEditText().getText().toString().isEmpty()) {
                    amount.setErrorEnabled(true);
                    amount.setError("Please Enter This Field");
                    validAllFilds = false;
                    button1.expand();
                } else {
                    Pattern ps = Pattern.compile("^[0-9]+$");
                    Matcher ms = ps.matcher(amount.getEditText().getText().toString());
                    if (!ms.matches()) {
                        amount.setErrorEnabled(true);
                        amount.setError("Please Enter Valid Amount");
                        validAllFilds = false;
                        button1.expand();
                    } else {
                        amount.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }
            if (validAllFilds) {
                cardId.setErrorEnabled(false);
                if (cardId.getEditText().getText().toString().isEmpty()) {
                    cardId.setErrorEnabled(true);
                    cardId.setError("Please Enter This Field");
                    validAllFilds = false;
                    button1.expand();
                } else {
                    Pattern ps = Pattern.compile("^[A-Z0-9]+$");
                    Matcher ms = ps.matcher(cardId.getEditText().getText().toString());
                    if (!ms.matches()) {
                        cardId.setErrorEnabled(true);
                        cardId.setError("Please Enter Valid Card ID");
                        validAllFilds = false;
                        button1.expand();
                    } else {
                        cardId.setErrorEnabled(false);
                        validAllFilds = true;
                    }
                }
            }
        }
        return validAllFilds;
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
        Dialog dialog = new Dialog(Recharge_Card_Fragment.getContext(), R.style.NO_INTERNET_DIALOG);
        dialog.setContentView(R.layout.no_internet_dilog);
        dialog.setCancelable(false);
        dialog.show();
        retry = dialog.findViewById(R.id.BTN_RETRY);
        retry.setOnClickListener(v -> {
            dialog.dismiss();
            button1.expand();
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Waiting For Connection..");
            progressDialog.show();
            startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
            new Handler().postDelayed(() -> {
                if (!isConnected()) {
                    progressDialog.dismiss();
                    AlertDialogBox();
                } else {
                    progressDialog.dismiss();
                }
            }, 5000);
        });
    }

}