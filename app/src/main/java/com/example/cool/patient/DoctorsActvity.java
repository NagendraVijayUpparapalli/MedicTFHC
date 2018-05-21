package com.example.cool.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DoctorsActvity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorsactvity);
          btn1=(Button) findViewById(R.id.male);
          btn2=(Button) findViewById(R.id.female);
         // btn3=(Button) findViewById(R.id.mchild);
         // btn4=(Button) findViewById(R.id.fchild);

          btn1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i=new Intent(DoctorsActvity.this, maleactivity.class);
                  startActivity(i);

              }
          });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DoctorsActvity.this, Female.class);
                startActivity(i);

            }
        });



    }
}
