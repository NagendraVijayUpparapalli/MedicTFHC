package com.example.cool.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class maleactivity extends AppCompatActivity {

    ImageView img1,img2,img3,img4,img5,img6,img7,img8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male);
        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);
        img4=(ImageView) findViewById(R.id.img4);
        img5=(ImageView) findViewById(R.id.img5);
        img6=(ImageView) findViewById(R.id.img6);
        img7=(ImageView) findViewById(R.id.img7);
        img8=(ImageView) findViewById(R.id.img8);



        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on head do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // finish();
                                Intent i=new Intent(maleactivity.this, HeadSpecials.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();


            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on hands do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Hands.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();

            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on hands do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Hands.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();

            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on lungs do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Lungs.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();


            }
        });


        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on Heart do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Heart.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();


            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on Stomach do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Stomach.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();



            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on Legs do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Legs.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();

            }
        });
        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(maleactivity.this);
                a_builder.setMessage("You are clicked on Legs do you want continue")
                        .setCancelable(false)
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                Intent i=new Intent(maleactivity.this, Legs.class);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Status");
                alert.show();

            }
        });
    }
}
