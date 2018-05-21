package com.example.cool.patient;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactUs extends AppCompatActivity {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        button=(Button) (findViewById(R.id.button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactUs.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_alert, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
//                final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
//                final EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);

            }
        });
    }
}