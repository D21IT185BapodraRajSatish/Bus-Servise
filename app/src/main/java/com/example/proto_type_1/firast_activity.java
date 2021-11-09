package com.example.proto_type_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class firast_activity extends AppCompatActivity {

    TextView retry;

    ProgressDialog progressDialog;

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private DrawerLayout drawer;
    public Fragment selectedFregment = new isssue_card();

    //Bottom navigation item Click Listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            if (!isConnected())
            {
                AlertDialogBox();
            }
            else
            {
                switch (menuItem.getItemId()) {
                    case R.id.ISSUE_CARD:
                        selectedFregment = new isssue_card();
                        toolbar.setSubtitle("Issue Card");
                        break;
                    case R.id.RECHARGE_CARD:
                        selectedFregment = new recharge_crad();
                        toolbar.setSubtitle("Recharge Card");
                        break;
                    case R.id.ADD_ROUT:
                        selectedFregment = new Add_Rout();
                        toolbar.setSubtitle("Add Rout");
                        break;
                    case R.id.ADD_CONDUCTOR:
                        selectedFregment = new Add_Conductor();
                        toolbar.setSubtitle("Add Conductor");
                        break;
                }
                if (selectedFregment == null) {
                    selectedFregment = new Add_Conductor();
                    toolbar.setSubtitle("Add Conductor");
                    bottomNavigationView.setSelectedItemId(R.id.ADD_CONDUCTOR);
                    Toast.makeText(firast_activity.this, String.valueOf(bottomNavigationView.getSelectedItemId()), Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
                }
                else {
                    //bottomNavigationView.setSelectedItemId(R.id.ISSUE_CARD);
                    //Toast.makeText(firast_activity.this, String.valueOf(bottomNavigationView.getSelectedItemId()), Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
                }
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firast_activity);

        progressDialog = new ProgressDialog(firast_activity.this);

        //Set User define Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Navigation Drawer in Activity
        drawer = findViewById(R.id.drawer_layout);

        //Set Bottom Navigation View In Activity
        bottomNavigationView = findViewById(R.id.boottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

            //Set Fragment view if Application Was Started First Time
            if (savedInstanceState == null) {
                bottomNavigationView.setSelectedItemId(R.id.ADD_CONDUCTOR);
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
            }
            else {
                selectedFregment = new isssue_card();
                toolbar.setSubtitle("Issue Card");
                bottomNavigationView.setSelectedItemId(R.id.ISSUE_CARD);
                bottomNavigationView.setSelected(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
            }

    }

    private boolean isConnected ()
    {
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
        Dialog dialog = new Dialog(firast_activity.this,R.style.NO_INTERNET_DIALOG);
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
                        if(!isConnected())
                        {
                            progressDialog.dismiss();
                            AlertDialogBox();
                        }
                        else
                        {
                            progressDialog.dismiss();
                        }
                    }
                },5000);
            }
        });
    }
}