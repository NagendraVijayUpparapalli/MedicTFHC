package com.example.cool.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BloodBanksList extends AppCompatActivity {

    String range_distance;
    TextView range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        range = (TextView) findViewById(R.id.between_dist);
        String getcity = getIntent().getStringExtra("Distance");
        range.setText(getcity);
    }
}
