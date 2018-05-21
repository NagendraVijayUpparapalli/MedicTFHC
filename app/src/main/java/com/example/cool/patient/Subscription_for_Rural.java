package com.example.cool.patient;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Subscription_for_Rural extends AppCompatActivity {
    Dialog MyDialog;
    Button pay;
    Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Button button = (Button) findViewById(R.id.sliver);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });

        Button gold = (Button) findViewById(R.id.Gold);

        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });

        Button platinum = (Button) findViewById(R.id.platinium);

        platinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("For RURAL");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Subscription_for_Rural.this,DoctorDashboard.class);
//                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                }

        );

    }


    public void MyCustomAlertDialog(){
        MyDialog =  new Dialog(Subscription_for_Rural.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.subscription_paymentmode_alert);


        pay = (Button)MyDialog.findViewById(R.id.Pay);
        cancel = (Button)MyDialog.findViewById(R.id.Cancel);

        pay.setEnabled(true);
        cancel.setEnabled(true);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pay now", Toast.LENGTH_LONG).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.setCancelable(false);
        MyDialog.setCanceledOnTouchOutside(false);
        MyDialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qricon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_EditProfile) {
//            Intent i = new Intent(MainActivity.this,EditProfile.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }
//        if(id == R.id.Language_options) {
//            Intent i1 = new Intent(MainActivity.this,Language_Optional.class);


//            startActivity(i1);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }
//        if(id == R.id.Logout) {
//           Intent i1 = new Intent(MainActivity.this,Login.class);
//           startActivity(i1);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
