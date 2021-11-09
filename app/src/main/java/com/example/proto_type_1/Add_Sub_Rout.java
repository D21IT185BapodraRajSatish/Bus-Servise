package com.example.proto_type_1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_Sub_Rout extends AppCompatActivity {

    //Text View
    TextView TXT_VIEW_kilometre;
    //TextInputLayout
    TextInputLayout stpname, kilometre;
    //
    ProgressDialog progressDialog;
    //
    TextView retry;
    //
    Toolbar toolbar;
    //Button
    Button btnadd;
    //List View
    ListView lstview, lstTitle;
    //Array List
    ArrayList<Routs> arrayList, arrayList1;
    //Adapter
    RoutsAdpater routsAdpater, routsAdpater1;
    //Firebase Database Reference
    DatabaseReference dbstore, dbretrieve, dbretrieve1, dbretrieve2, dbretrieve3, dbretrive4;
    //UserDefine Class
    GetandSetRout store, retrieve, retrieve1, retrieve2, retrieve3;
    //
    FancyButton button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sub_rout);

        //init method call
        init();

        //Set kilometer value in TextView method Call
        setTitleKilometer();

        //List View Long Click Event
        lstview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(isConnected())
                {
                    final int i = position;

                    Animation animation = new AlphaAnimation(0.3f, 0.1f);
                    animation.setDuration(400);
                    view.startAnimation(animation);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Add_Sub_Rout.this);
                    builder.setMessage("Do You want to DELETE " + routsAdpater.getItem(position).getCustomize_Source_Point() + " ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbretrieve3 = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout")).child(routsAdpater.getItem(i).getCustomize_Source_Point().replace("/", "--").replace(".", "_"));
                            dbretrieve3.removeValue();
                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();

                }
                else
                {
                    AlertDialogBox();
                }
                return true;
            }
        });

        //Button Click Event
        /*btnadd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(final View v) {

                if (validator()) {

                    dbretrieve2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Double Kilo_M = dataSnapshot.getValue(GetandSetRout.class).getKilometre();
                            Double TxtK_M = Double.parseDouble(kilometre.getEditText().getText().toString());
                            if (TxtK_M >= Kilo_M) {
                                Toast.makeText(Add_Sub_Rout.this, "Kilometre's value is more than or equal last point value", Toast.LENGTH_SHORT).show();
                            } else if (TxtK_M <= 0) {
                                Toast.makeText(Add_Sub_Rout.this, "Kilometre's value Can't be negative or zero", Toast.LENGTH_SHORT).show();
                            } else {
                                dbretrive4 = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout"));
                                dbretrive4.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        boolean vaild = false;
                                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                                            if (item.hasChildren()) {
                                                retrieve3 = item.getValue(GetandSetRout.class);
                                                String CPD = retrieve3.getStp_Name();
                                                String CPT = stpname.getEditText().getText().toString();
                                                //int CPint=CPD.compareToIgnoreCase(CPT);
                                                if (CPD.equalsIgnoreCase(CPT) & TextUtils.equals(kilometre.getEditText().getText().toString(), String.valueOf(retrieve3.getKilometre()))) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Add_Sub_Rout.this);
                                                    builder.setCancelable(false);
                                                    builder.setMessage("This Rout Details allready register ");
                                                    builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                                    builder.create().show();
                                                    vaild = false;
                                                    break;
                                                } else if (TextUtils.equals(String.valueOf(Double.parseDouble(kilometre.getEditText().getText().toString())), String.valueOf(retrieve3.getKilometre()))) {
                                                    Toast.makeText(Add_Sub_Rout.this, "Stop is already registered at this kilometre", Toast.LENGTH_SHORT).show();
                                                    vaild = false;
                                                    break;
                                                } else if (CPD.equalsIgnoreCase(CPT)) {
                                                    Toast.makeText(Add_Sub_Rout.this, "Stop is already registered ", Toast.LENGTH_SHORT).show();
                                                    vaild = false;
                                                    break;
                                                } else {
                                                    vaild = true;
                                                }
                                            }
                                        }
                                        if (vaild) {
                                            storeToDatabase();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });*/

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof  FancyButton)
                {
                    button1.collapse();
                    if (validator()) {

                        dbretrieve2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Double Kilo_M = dataSnapshot.getValue(GetandSetRout.class).getKilometre();
                                Double TxtK_M = Double.parseDouble(kilometre.getEditText().getText().toString());
                                if (TxtK_M >= Kilo_M) {
                                    Toast.makeText(Add_Sub_Rout.this, "Kilometre's value is more than or equal last point value", Toast.LENGTH_SHORT).show();
                                } else if (TxtK_M <= 0) {
                                    Toast.makeText(Add_Sub_Rout.this, "Kilometre's value Can't be negative or zero", Toast.LENGTH_SHORT).show();
                                } else {
                                    dbretrive4 = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout"));
                                    dbretrive4.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            boolean vaild = false;
                                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                                if (item.hasChildren()) {
                                                    retrieve3 = item.getValue(GetandSetRout.class);
                                                    String CPD = retrieve3.getStp_Name();
                                                    String CPT = stpname.getEditText().getText().toString();
                                                    //int CPint=CPD.compareToIgnoreCase(CPT);
                                                    if (CPD.equalsIgnoreCase(CPT) & TextUtils.equals(kilometre.getEditText().getText().toString(), String.valueOf(retrieve3.getKilometre()))) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Sub_Rout.this);
                                                        builder.setCancelable(false);
                                                        builder.setMessage("This Rout Details allready register ");
                                                        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                                        builder.create().show();
                                                        vaild = false;
                                                        break;
                                                    } else if (TextUtils.equals(String.valueOf(Double.parseDouble(kilometre.getEditText().getText().toString())), String.valueOf(retrieve3.getKilometre()))) {
                                                        Toast.makeText(Add_Sub_Rout.this, "Stop is already registered at this kilometre", Toast.LENGTH_SHORT).show();
                                                        vaild = false;
                                                        break;
                                                    } else if (CPD.equalsIgnoreCase(CPT)) {
                                                        Toast.makeText(Add_Sub_Rout.this, "Stop is already registered ", Toast.LENGTH_SHORT).show();
                                                        vaild = false;
                                                        break;
                                                    } else {
                                                        vaild = true;
                                                    }
                                                }
                                            }
                                            if (vaild) {
                                                storeToDatabase();
                                            }
                                            else
                                            {
                                                button1.expand();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }
        };
        button1.setOnClickListener(listener);

    }

    //Set Kilometer in Text View method
    private void setTitleKilometer() {
        dbretrieve.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.hasChildren()) {
                        retrieve3 = ds.getValue(GetandSetRout.class);
                        if (TextUtils.equals(retrieve3.getStp_Name(), getIntent().getExtras().getString("LPoint"))) {
                            TXT_VIEW_kilometre.setText(String.valueOf(retrieve3.getKilometre()));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Add_Sub_Rout.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //validate controls
    private boolean validator() {
        boolean valid;
        if (TextUtils.isEmpty(stpname.getEditText().getText().toString()) & TextUtils.isEmpty(kilometre.getEditText().getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("all filed are Required");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stpname.setErrorEnabled(false);
                    kilometre.setErrorEnabled(false);
                }
            });
            builder.create().show();
            button1.expand();
            valid = false;
            button1.expand();
        } else {
            if (stpname.getEditText().getText().toString().isEmpty()) {
                stpname.setErrorEnabled(true);
                stpname.setError("Please Enter SubStop name");
                button1.expand();
                valid = false;
                button1.expand();
            } else {
                Pattern ps = Pattern.compile("^[A-Za-z/()0-9., ]+$");
                Matcher ms = ps.matcher(stpname.getEditText().getText().toString());
                if (!ms.matches()) {
                    String temp1 = stpname.getEditText().getText().toString();
                    String temp2 = String.valueOf(temp1.charAt(temp1.indexOf("-")));

                    int i = temp2.compareTo("-");
                    if (i == 0) {
                        valid = true;
                        stpname.setErrorEnabled(false);

                    } else {
                        stpname.setErrorEnabled(true);
                        stpname.setError("Please Enter Stop Name in character only");
                        valid = false;
                        button1.expand();
                    }
                } else {
                    valid = true;
                    stpname.setErrorEnabled(false);
                }
            }
            if (valid) {
                if (kilometre.getEditText().getText().toString().isEmpty()) {
                    kilometre.setError("value must contain digit");
                    valid = false;
                    button1.expand();
                } else {
                    if (TextUtils.isEmpty(kilometre.getEditText().getText().toString())) {
                        kilometre.setErrorEnabled(true);
                        kilometre.setError("Please Enter Kilometre");
                        valid = false;
                        button1.expand();
                    } else {
                        Pattern ps = Pattern.compile("^[0-9.]+$");
                        Matcher ms = ps.matcher(kilometre.getEditText().getText().toString());
                        if (!ms.matches()) {
                            kilometre.setErrorEnabled(true);
                            kilometre.setError("Please Enter Kilometre In Digit Only");
                            valid = false;
                            button1.expand();
                        } else {
                            valid = true;
                            kilometre.setErrorEnabled(false);
                        }
                    }

                }
            }
        }
        if (valid) {
            String FrPOint = getIntent().getExtras().getString("FPoint");
            String LaPOint = getIntent().getExtras().getString("LPoint");
            if (TextUtils.equals(FrPOint, stpname.getEditText().getText().toString()) | TextUtils.equals(LaPOint, stpname.getEditText().getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_Sub_Rout.this);
                builder.setTitle("Oops...!");
                builder.setMessage("This Rout Already Registered");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                valid = false;
                button1.expand();
            }
        }
        return valid;
    }

    //Store Value To Database
    private void storeToDatabase() {
        String temp = stpname.getEditText().getText().toString();
        String temp1 = stpname.getEditText().getText().toString();

        temp = temp.replace("/", "--");
        temp = temp.replace(".", "_");
        store.setStp_Name(temp);
        store.setKilometre(Double.parseDouble(kilometre.getEditText().getText().toString()));

        dbstore.child(store.getStp_Name()).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    button1.expand();
                    Toast.makeText(Add_Sub_Rout.this, "Rout Register successfully", Toast.LENGTH_SHORT).show();
                } else {
                    button1.expand();
                    Toast.makeText(Add_Sub_Rout.this, "ERROR : " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Initialize controls & object
    private void init() {
        //textView
        TXT_VIEW_kilometre = findViewById(R.id.Register_Sub_rout_Kilometre1);

        //
        button1 = (FancyButton) findViewById(R.id.RSR_ADD);

        //TextInputLayout
        stpname = findViewById(R.id.Sub_Rout_Name);
        kilometre = findViewById(R.id.Sub_Rout_Kilo_Meter);

        //Set User define Toolbar
        toolbar = findViewById(R.id.toolbar_RSR);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Register Sub Rout");

        //
        progressDialog=new ProgressDialog(Add_Sub_Rout.this);

        //Button
        //btnadd = findViewById(R.id.RSR_ADD);

        //ListView
        lstview = findViewById(R.id.RSR_LST);
        lstTitle = findViewById(R.id.RSR_Lst_Title);

        //Java Class
        store = new GetandSetRout();
        retrieve = new GetandSetRout();
        retrieve1 = new GetandSetRout();
        retrieve2 = new GetandSetRout();
        retrieve3 = new GetandSetRout();

        //DatabaseReference

        dbretrieve2 = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout")).child(getIntent().getExtras().getString("LPoint"));
        dbstore = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout"));
        dbretrieve = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout"));
        dbretrieve1 = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout"));
        dbretrieve3 = FirebaseDatabase.getInstance().getReference("stops").child(getIntent().getExtras().getString("Rout"));


        //ArrayList & RoutAdapter
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        routsAdpater = new RoutsAdpater(this, R.layout.customize_list_view, arrayList);
        routsAdpater1 = new RoutsAdpater(this, R.layout.customize_list_view, arrayList1);
        arrayList1.add(new Routs("Sr.No.", "Stop Name", "Kilometre From Source"));
        lstTitle.setAdapter(routsAdpater1);

        //RetrieveFromDatabase
        retrieveFromDatabase();
    }

    //Retrieve Value From Database
    private void retrieveFromDatabase() {

        dbretrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
                String temp;
                String temp1;
                String FiPoint = getIntent().getExtras().getString("FPoint");
                String LaPoint = getIntent().getExtras().getString("LPoint");
                routsAdpater.clear();
                for (DataSnapshot iteam : dataSnapshot.getChildren()) {
                    if (iteam.hasChildren()) {
                        for (DataSnapshot sub : iteam.getChildren()) {
                            if (sub.hasChildren()) {
                                retrieve = sub.getValue(GetandSetRout.class);
                                temp = retrieve.getStp_Name();
                                temp = temp.replace("_", ".");
                                temp = temp.replace("--", "/");

                                temp1 = String.valueOf(retrieve.getKilometre());
                                temp1 = temp1.replace("_", ".");
                                if (!TextUtils.equals(FiPoint, temp) & !TextUtils.equals(LaPoint, temp)) {
                                    arrayList.add(new Routs(String.valueOf(i), temp, temp1));
                                    i += 1;
                                    break;
                                }
                            } else {
                                retrieve = iteam.getValue(GetandSetRout.class);
                                temp = retrieve.getStp_Name();
                                temp1 = String.valueOf(retrieve.getKilometre());
                                temp = temp.replace("_", ".");
                                temp = temp.replace("--", "/");
                                temp1 = temp1.replace("_", ".");
                                if (!TextUtils.equals(FiPoint, temp) & !TextUtils.equals(LaPoint, temp)) {
                                    arrayList.add(new Routs(String.valueOf(i), temp, temp1));
                                    i += 1;
                                }
                                break;
                            }
                        }
                    }

                }

                lstview.setAdapter(routsAdpater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Add_Sub_Rout.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void AlertDialogBox() {
        Dialog dialog = new Dialog(Add_Sub_Rout.this, R.style.NO_INTERNET_DIALOG);
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