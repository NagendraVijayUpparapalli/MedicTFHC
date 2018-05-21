package com.example.cool.patient;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubscriptionPlanAlertDialog extends AppCompatActivity {

    TextView textview;
    AlertDialog alertDialog1;
    CharSequence[] values = {"For urban", "For Rural"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan_alert_dialog);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Subscription For ??");
        alert.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
//                        Toast.makeText(alert_dilog.this, "For urban", Toast.LENGTH_LONG).show();
                        Intent i  = new Intent(SubscriptionPlanAlertDialog.this,Subscription.class);
                        startActivity(i);
                        break;
                    case 1:
//                        Toast.makeText(alert_dilog.this, "For Rural", Toast.LENGTH_LONG).show();
                        Intent rural  = new Intent(SubscriptionPlanAlertDialog.this,Subscription_for_Rural.class);
                        startActivity(rural);
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alert.setCancelable(false);
        alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(false);

        //  alert.show();


//        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//
//                Toast.makeText(alert_dilog.this, "ok", Toast.LENGTH_SHORT).show();
//            }
//        });

        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(alert_dilog.this, "cancle", Toast.LENGTH_SHORT).show();
                Intent cancel =  new Intent(SubscriptionPlanAlertDialog.this,DoctorDashboard.class);
                startActivity(cancel);
            }
        });
        alert.show();

    }
}
