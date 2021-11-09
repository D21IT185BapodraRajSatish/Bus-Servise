package com.example.proto_type_1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ekalips.fancybuttonproj.FancyButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_Rout extends Fragment {

    //VIew
    View Add_Rout;
    //TextInputLayout
    TextInputLayout FirstPoint, LastPoint, KiloMetre, RoutNumber;
    //
    ProgressDialog progressDialog;
    //
    TextView retry;
    //Button
    Button BtnSubmit;
    //ListView
    ListView LstMainAct, LstTitle;
    //List
    List<Routs> routsList, routsList2;
    //Adapter
    RoutsAdpater routsAdpater, routsAdpater2;
    //UserDefine Class
    GetandSetRout rout1, rout2, rout3, rout4;
    //Firebase Database Reference
    DatabaseReference dbstore, dbretrive, dbstore1, dbretrive1, dbretrieve2;
    //
    FancyButton button1;

    public Add_Rout() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set View
        Add_Rout = inflater.inflate(R.layout.add_rout, container, false);

        //init method called
        init();

        //List View Long Click Event
        LstMainAct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (isConnected()) {
                    Animation animation = new AlphaAnimation(0.3f, 0.1f);
                    animation.setDuration(400);
                    view.startAnimation(animation);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do You want to DELETE Rout" + routsAdpater.getItem(position).Customize_Rout_Number + " ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dbretrieve2 = FirebaseDatabase.getInstance().getReference("stops").child(routsAdpater.getItem(position).Customize_Rout_Number);
                            dbretrieve2.removeValue();
                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else {
                    AlertDialogBox();
                }
                return true;
            }
        });

        //Submit Button Click Event
        /*BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    if (validator()) {
                        dbretrive1.addListenerForSingleValueEvent(new ValueEventListener() {
                            boolean valid = true;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String rn = RoutNumber.getEditText().getText().toString();
                                String fp = FirstPoint.getEditText().getText().toString();
                                String lp = LastPoint.getEditText().getText().toString();
                                String km = KiloMetre.getEditText().getText().toString();

                                for (DataSnapshot iteam : dataSnapshot.getChildren()) {
                                    rout4 = iteam.getValue(GetandSetRout.class);
                                    if (TextUtils.equals(rn, String.valueOf(rout4.getRout_Number())) & TextUtils.equals(fp, rout4.getFirst_Point()) & TextUtils.equals(lp, rout4.getLast_Point())) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Oops...!");
                                        builder.setMessage("This Rout Already Registered");
                                        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                RoutNumber.setErrorEnabled(false);
                                                FirstPoint.setErrorEnabled(false);
                                                LastPoint.setErrorEnabled(false);
                                                KiloMetre.setErrorEnabled(false);
                                            }
                                        });
                                        builder.create().show();
                                        valid = false;
                                        break;
                                    } else if (TextUtils.equals(rn, String.valueOf(rout4.getRout_Number()))) {
                                        Toast.makeText(getContext(), "Rout Number Already Registered", Toast.LENGTH_SHORT).show();
                                        valid = false;
                                        break;
                                    } else if (TextUtils.equals(fp, rout4.getFirst_Point()) & TextUtils.equals(lp, rout4.getLast_Point()) | TextUtils.equals(lp, rout4.getFirst_Point()) & TextUtils.equals(fp, rout4.getLast_Point())) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Oops...!");
                                        builder.setMessage("This Rout Already Registered");
                                        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                RoutNumber.setErrorEnabled(false);
                                                FirstPoint.setErrorEnabled(false);
                                                LastPoint.setErrorEnabled(false);
                                                KiloMetre.setErrorEnabled(false);
                                            }
                                        });
                                        builder.create().show();
                                        valid = false;
                                        break;
                                    } else {
                                        valid = true;
                                    }
                                }
                                if (valid) {
                                    storeToDatabase();
                                }
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
        });*/

        //List View Click Event
        LstMainAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isConnected()) {
                    String RoutNumber = routsList.get(position).getCustomize_Rout_Number();
                    String FirstPoint = routsList.get(position).getCustomize_Source_Point();
                    String LastPoint = routsList.get(position).getCustomize_Destination_Point();

                    Animation animation = new AlphaAnimation(0.3f, 0.1f);
                    animation.setDuration(400);
                    view.startAnimation(animation);

                    Intent intent = new Intent(getContext(), Add_Sub_Rout.class);
                    intent.putExtra("Rout", RoutNumber);
                    intent.putExtra("FPoint", FirstPoint);
                    intent.putExtra("LPoint", LastPoint);

                    startActivity(intent);
                } else {
                    AlertDialogBox();
                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof FancyButton) {
                    if (isConnected()) {
                        button1.collapse();
                        if (validator()) {
                            dbretrive1.addListenerForSingleValueEvent(new ValueEventListener() {
                                boolean valid = true;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String rn = RoutNumber.getEditText().getText().toString();
                                    String fp = FirstPoint.getEditText().getText().toString();
                                    String lp = LastPoint.getEditText().getText().toString();
                                    String km = KiloMetre.getEditText().getText().toString();

                                    for (DataSnapshot iteam : dataSnapshot.getChildren()) {
                                        rout4 = iteam.getValue(GetandSetRout.class);
                                        if (TextUtils.equals(rn, String.valueOf(rout4.getRout_Number())) & TextUtils.equals(fp, rout4.getFirst_Point()) & TextUtils.equals(lp, rout4.getLast_Point())) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Oops...!");
                                            builder.setMessage("This Rout Already Registered");
                                            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    RoutNumber.setErrorEnabled(false);
                                                    FirstPoint.setErrorEnabled(false);
                                                    LastPoint.setErrorEnabled(false);
                                                    KiloMetre.setErrorEnabled(false);
                                                }
                                            });
                                            builder.create().show();
                                            valid = false;
                                            break;
                                        } else if (TextUtils.equals(rn, String.valueOf(rout4.getRout_Number()))) {
                                            Toast.makeText(getContext(), "Rout Number Already Registered", Toast.LENGTH_SHORT).show();
                                            valid = false;
                                            break;
                                        } else if (TextUtils.equals(fp, rout4.getFirst_Point()) & TextUtils.equals(lp, rout4.getLast_Point()) | TextUtils.equals(lp, rout4.getFirst_Point()) & TextUtils.equals(fp, rout4.getLast_Point())) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Oops...!");
                                            builder.setMessage("This Rout Already Registered");
                                            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    RoutNumber.setErrorEnabled(false);
                                                    FirstPoint.setErrorEnabled(false);
                                                    LastPoint.setErrorEnabled(false);
                                                    KiloMetre.setErrorEnabled(false);
                                                }
                                            });
                                            builder.create().show();
                                            valid = false;
                                            break;
                                        } else {
                                            valid = true;
                                        }
                                    }
                                    if (valid) {
                                        storeToDatabase();
                                    } else {
                                        button1.expand();
                                    }
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

            }
        };
        button1.setOnClickListener(listener);

        return Add_Rout;
    }

    //initialization of Controls And other Objects
    private void init() {

        progressDialog = new ProgressDialog(Add_Rout.getContext());

        button1 = Add_Rout.findViewById(R.id.FRG2_Btn_Submit);

        //Edit Text
        FirstPoint = Add_Rout.findViewById(R.id.First_Point);
        LastPoint = Add_Rout.findViewById(R.id.Last_Point);
        KiloMetre = Add_Rout.findViewById(R.id.Kilo_Meter);
        RoutNumber = Add_Rout.findViewById(R.id.Rout_Number);

        //Button
        //BtnSubmit = Add_Rout.findViewById(R.id.FRG2_Btn_Submit);

        //initialization ListView
        LstMainAct = Add_Rout.findViewById(R.id.FRG2_Lst_Main_Act);
        LstTitle = Add_Rout.findViewById(R.id.FRG2_Lst_Title);

        //initialize ArrayList & ArrayAdapter
        routsList = new ArrayList<>();
        routsList2 = new ArrayList<>();
        routsAdpater = new RoutsAdpater(getContext(), R.layout.customize_list_view, routsList);
        routsAdpater2 = new RoutsAdpater(getContext(), R.layout.customize_list_view, routsList2);
        routsList2.add(new Routs("Rout No.", "Source", "Destination"));

        LstTitle.setAdapter(routsAdpater2);

        //initialize Java Classes
        rout1 = new GetandSetRout();
        rout2 = new GetandSetRout();
        rout3 = new GetandSetRout();
        rout4 = new GetandSetRout();

        //initialize Database References
        dbstore = FirebaseDatabase.getInstance().getReference("stops");
        dbretrive = FirebaseDatabase.getInstance().getReference("stops");
        dbretrive1 = FirebaseDatabase.getInstance().getReference("stops");
        dbretrieve2 = FirebaseDatabase.getInstance().getReference("stops");
        //Retrieved Method call
        getFromDatabase();
    }

    //Retrieve from database
    private void getFromDatabase() {
        dbretrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                routsAdpater.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    rout3 = item.getValue(GetandSetRout.class);
                    String temp1 = rout3.getFirst_Point();
                    String temp2 = rout3.getLast_Point();
                    temp1 = temp1.replace("_", ".");
                    temp2 = temp2.replace("_", ".");
                    routsList.add(new Routs(String.valueOf(rout3.getRout_Number()), temp1, temp2));
                }
                LstMainAct.setAdapter(routsAdpater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Store to Database
    private void storeToDatabase() {
        String temp1 = FirstPoint.getEditText().getText().toString();
        String temp2 = LastPoint.getEditText().getText().toString();

        temp1 = temp1.replace(".", "_");
        temp2 = temp2.replace(".", "_");

        rout1.setFirst_Point(temp1);
        rout1.setLast_Point(temp2);
        rout1.setKilometre(Double.parseDouble(KiloMetre.getEditText().getText().toString()));
        rout1.setRout_Number(Integer.parseInt(RoutNumber.getEditText().getText().toString()));

        dbstore.child(String.valueOf(rout1.getRout_Number())).setValue(rout1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dbstore1 = FirebaseDatabase.getInstance().getReference("stops").child(String.valueOf(rout1.getRout_Number()));

                rout2.setStp_Name(rout1.getFirst_Point());
                rout2.setKilometre(0);
                dbstore1.child(rout2.getStp_Name()).setValue(rout2);

                rout2.setStp_Name(rout1.getLast_Point());
                rout2.setKilometre(rout1.getKilometre());
                dbstore1.child(rout2.getStp_Name()).setValue(rout2);
                button1.expand();
                Toast.makeText(getContext(), "Rout Register successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //to check All text box are valid value or not
    private boolean validator() {
        boolean valid = true;
        if (TextUtils.isEmpty(RoutNumber.getEditText().getText().toString()) & TextUtils.isEmpty(FirstPoint.getEditText().getText().toString()) & TextUtils.isEmpty(LastPoint.getEditText().getText().toString()) & TextUtils.isEmpty(KiloMetre.getEditText().getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("all filed are Required");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RoutNumber.setErrorEnabled(false);
                    FirstPoint.setErrorEnabled(false);
                    LastPoint.setErrorEnabled(false);
                    KiloMetre.setErrorEnabled(false);
                }
            });
            builder.create().show();
            valid = false;
            button1.expand();
        } else {
            if(valid) {
                if (TextUtils.isEmpty(RoutNumber.getEditText().getText().toString())) {
                    RoutNumber.setErrorEnabled(true);
                    RoutNumber.setError("Please Enter Rout Number");
                    valid = false;
                    button1.expand();
                } else {
                    Pattern ps = Pattern.compile("^[0-9.]+$");
                    Matcher ms = ps.matcher(RoutNumber.getEditText().getText().toString());
                    if (!ms.matches()) {
                        RoutNumber.setErrorEnabled(true);
                        RoutNumber.setError("Please Enter Rout Number In Digit Only");
                        valid = false;
                        button1.expand();
                    } else {
                        valid = true;
                        RoutNumber.setErrorEnabled(false);
                    }

                }
            }
            if(valid) {
                if (TextUtils.isEmpty(FirstPoint.getEditText().getText().toString())) {
                    FirstPoint.setErrorEnabled(true);
                    FirstPoint.setError("Please Enter First Point");
                    valid = false;
                    button1.expand();
                } else {
                    Pattern ps = Pattern.compile("^[A-Za-z.()0-9 ]+$");
                    Matcher ms = ps.matcher(FirstPoint.getEditText().getText().toString());
                    if (!ms.matches()) {
                        FirstPoint.setErrorEnabled(true);
                        FirstPoint.setError("Please Enter First Point In Character Only");
                        valid = false;
                        button1.expand();
                    } else {
                        valid = true;
                        FirstPoint.setErrorEnabled(false);
                    }
                }
            }
            if(valid){
            if (TextUtils.isEmpty(LastPoint.getEditText().getText().toString())) {
                LastPoint.setErrorEnabled(true);
                LastPoint.setError("Please Enter Last Point");
                valid = false;
                button1.expand();
            } else {
                Pattern ps = Pattern.compile("^[A-Za-z0-9 ]+$");
                Matcher ms = ps.matcher(LastPoint.getEditText().getText().toString());
                if (!ms.matches()) {
                    LastPoint.setErrorEnabled(true);
                    LastPoint.setError("Please Enter Last Point In Character Only");
                    valid = false;
                    button1.expand();
                } else {
                    valid = true;
                    LastPoint.setErrorEnabled(false);
                }
            }}
            if(valid){
            if (TextUtils.isEmpty(KiloMetre.getEditText().getText().toString())) {
                KiloMetre.setErrorEnabled(true);
                KiloMetre.setError("Please Enter Kilometre");
                valid = false;
                button1.expand();
            } else {
                if (TextUtils.equals(String.valueOf(KiloMetre.getEditText().getText().toString().charAt(0)), ".")) {
                    KiloMetre.setError("value must contain digit");
                    valid = false;
                    button1.expand();
                } else {
                    Pattern ps = Pattern.compile("^[0-9.]+$");
                    Matcher ms = ps.matcher(KiloMetre.getEditText().getText().toString());
                    if (!ms.matches()) {
                        KiloMetre.setErrorEnabled(true);
                        KiloMetre.setError("Please Enter Kilometre In Digit Only");
                        valid = false;
                        button1.expand();
                    } else {
                        valid = true;
                        KiloMetre.setErrorEnabled(false);
                    }
                }
            }}
        }
        if (valid) {
            if (TextUtils.equals(FirstPoint.getEditText().getText().toString(), LastPoint.getEditText().getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("First Point & Last Point are same");
                builder.setTitle("Oops...!");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RoutNumber.setErrorEnabled(false);
                        FirstPoint.setErrorEnabled(false);
                        LastPoint.setErrorEnabled(false);
                        KiloMetre.setErrorEnabled(false);
                    }
                });
                builder.create().show();
                valid = false;
                button1.expand();
            }
        }
        return valid;
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
        Dialog dialog = new Dialog(Add_Rout.getContext(), R.style.NO_INTERNET_DIALOG);
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