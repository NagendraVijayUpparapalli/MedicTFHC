package com.example.cool.patient.patient.ViewBloodBanksList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class ViewBloodBank extends AppCompatActivity {

    TextView address,mobile,email,name,navigate,person_name,sms;
    ImageView image;
    Bitmap mIcon11;
    ArrayList<BloodBankClass> bloodBankClassArrayList;
    String latitude,longitude;
    String phone,bloodbank_name,city,area,contact_name,location,uri=null,userMobile,userId;
    String arr[];
    String mainUrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_bank);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewBloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewBloodBank.this,BloodBank.class);
                        intent.putExtra("userId",getIntent().getStringExtra("id"));
                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                        startActivity(intent);

                    }
                }

        );


        image = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.bloodbankname);
        navigate = (TextView) findViewById(R.id.navigate);
        person_name = (TextView) findViewById(R.id.person_name);
        address = (TextView) findViewById(R.id.addressline);
        mobile = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.emailid);
        sms = (TextView) findViewById(R.id.SMS);
//        location = (TextView) findViewById(R.id.cityname);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mytask().execute();
            }
        });

        String image1 = getIntent().getStringExtra("image");

        System.out.println("image url.."+image1);
        final String bloodbankname = getIntent().getStringExtra("name");
        String contact_person = getIntent().getStringExtra("person_name");

        String addressarea = getIntent().getStringExtra("addressline");
        arr= addressarea.split(",");
        location = getIntent().getStringExtra("city");

//        if(arr[0])

        String mobilenum = getIntent().getStringExtra("phone");
        String emailid = getIntent().getStringExtra("email");
        System.out.println("area....."+addressarea+"mobile.."+mobilenum+"email..."+emailid);

        latitude = getIntent().getStringExtra("lati");
        longitude = getIntent().getStringExtra("longi");

        System.out.println("latii......"+latitude+"longi......"+longitude);

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = String.format(Locale.ENGLISH, "geo:0,0?q="+latitude+","+longitude+"("+bloodbankname+")");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


//        Toast.makeText(getApplicationContext(),image1,Toast.LENGTH_SHORT).show();


        new GetImageTask(image).execute(image1);
//        image.setImageResource(p);

//        new DownloadImageTask(R.drawable.ambuance).execute(bloodBankClassArrayList.get(position).getImage());
        name.setText(bloodbankname);
        address.setText(addressarea);
        mobile.setText(mobilenum);
        email.setText(emailid);
        person_name.setText(contact_person);

        bloodbank_name = name.getText().toString();
        area = address.getText().toString();
//        city = location.getText().toString();
        phone = mobile.getText().toString();
        contact_name = person_name.getText().toString();

    }

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }

    }

    private class Mytask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();
                //HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();

//                String bloodbank_name="Apollo";
                String contact_person="Admin";
//                String city="Visakhapatnam";
//                String area="Ramnagar";
                String phone1=phone;
                String link="http://meditfhc.com/";

//                String message="Blood Bank Details: -"+bloodbank_name+", "+"Contact Details: "+contact_person+"-"+phone1+", "+"Address: "+area+". Click here for navigate: "+link+". \n"+"Regards MEDIC TFHC.";

                String message="Blood Bank Details: -"+bloodbank_name+", "+"Contact Details: "+contact_person+"-"+phone1+", "+"Address: "+area+". Click here for navigate: "+link+". \n"+"Regards MEDIC TFHC.";
                mainUrl="http://www.mgage.solutions/SendSMS/sendmsg.php?";
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination="9676856122";

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(mainUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                mainUrl=stringBuilder.toString();
                System.out.println("mainurl "+mainUrl);
                myURL=new URL(mainUrl);

                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            }
            catch (IOException e) {
                e.printStackTrace();
            }


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return null;
        }

        @Override
        protected void onPreExecute() {
            showMessage();
            super.onPreExecute();
        }
    }

    public void showMessage(){



        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(ViewBloodBank.this);
        a_builder.setMessage("The Message has sent Successfully to your registered mobile number")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Successfully Sent");
        alert.show();

    }

}
