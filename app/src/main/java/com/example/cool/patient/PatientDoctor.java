package com.example.cool.patient;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PatientDoctor extends AppCompatActivity {
    public static final CharSequence[] specialities  = {"Select Speciality", "Head", "Eyes","nose"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_doctor);

        CardView cardView =  (CardView) findViewById(R.id.card_view);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(PatientDoctor.this,ViewDoctor.class);
               startActivity(i);
//                final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
//                final EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);

            }
        });

        Spinner spinner1 = (Spinner) findViewById(R.id.speciality);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, specialities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner1.setAdapter(adapter); // Apply the adapter to the spinner

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitle("DoctorClass");


        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PatientDoctor.this, MainActivity.class);
        startActivity(intent);

           }
    }

    );

}



}
