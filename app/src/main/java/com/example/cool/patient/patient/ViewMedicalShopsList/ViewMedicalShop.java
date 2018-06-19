package com.example.cool.patient.patient.ViewMedicalShopsList;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewMedicalShop extends AppCompatActivity {

    TextView shopname,hospitalname,doornum,city,state,fee,payment,shopphonenum,navigation,ContactPersonname,SMS,cancel;

    Button button;

    ImageView doctorimage;

    AlertDialog alertDialog1;
    ProgressDialog progressDialog;

    int year1,month,day;


    static String newName,mySurname,myName,myEmail,myMobile,myGender,myMaritalStatus,myAadhar_num;
    Long myAge;
    boolean enableHistory;


    String myshopname,navigate,myContactPersonname,user,cur_addressId,medicalID,mydocName,myShopName,myaddress,
            mycity,mystate,myfee,mypaymentMode,myphone,myLati,myLongi,myImage;

    Bitmap mIcon11;
    ApiBaseUrl baseUrl;

    ImageView centerImage;

    List<String> allItemsList, prevSunTimeSlotsList,prevMonTimeSlotsList,prevTueTimeSlotsList,prevWedTimeSlotsList,prevThurTimeSlotsList,prevFriTimeSlotsList,prevSatTimeSlotsList;
    ArrayAdapter<String > sunAdapter,monAdapter,tueAdapter,wedAdapter,thurAdapter,friAdapter,satAdapter;
    String myDayName;
    String patientMobileNumber,patientId;

    HashMap<String, String> AllTimeSlotsList = new HashMap<String, String>();
    String uri=null;
    static String mainUrl = null;

    Dialog MyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_shop);
        baseUrl = new ApiBaseUrl();

        myImage = getIntent().getStringExtra("image");
        myshopname = getIntent().getStringExtra("shopName");
        user = getIntent().getStringExtra("user");
        patientId = getIntent().getStringExtra("userId");
        patientMobileNumber = getIntent().getStringExtra("userMobile");
        cur_addressId = getIntent().getStringExtra("addressId");
        medicalID = getIntent().getStringExtra("medicalID");
        myLati = getIntent().getStringExtra("lat");
        myContactPersonname= getIntent().getStringExtra("ContactPerson");
        myLongi = getIntent().getStringExtra("long");
        myphone = getIntent().getStringExtra("mobile");

        new GetMeidcalAllAddressDetails().execute(baseUrl.getUrl()+"MSGetAddress?ID="+medicalID);

        MyDialog =  new Dialog(ViewMedicalShop.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.medicalshopview);

        shopname=(TextView) MyDialog.findViewById(R.id.Shop_name);
        ContactPersonname = (TextView)MyDialog.findViewById(R.id.ContactPersonname);
        doornum=(TextView) MyDialog.findViewById(R.id.dr_no);
        city=(TextView) MyDialog.findViewById(R.id.city);
        state=(TextView) MyDialog.findViewById(R.id.state);
        shopphonenum=(TextView) MyDialog.findViewById(R.id.Phononumber);
        navigation =(TextView) MyDialog.findViewById(R.id.navigate);
        SMS = (TextView) MyDialog.findViewById(R.id.sms);
        centerImage = (ImageView) MyDialog.findViewById(R.id.centerImage);

        cancel = (TextView) MyDialog.findViewById(R.id.cancel_icon);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.setCancelable(false);
        MyDialog.setCanceledOnTouchOutside(false);
        MyDialog.show();

        new GetProfileImageTask(centerImage).execute(baseUrl.getImageUrl()+myImage);

        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mytask().execute();
            }
        });
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = String.format(Locale.ENGLISH, "geo:0,0?q="+myLati+","+myLongi+"("+myshopname+")");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


        ContactPersonname.setText(myContactPersonname);
        shopname.setText(myshopname);
        shopphonenum.setText(myphone);


        System.out.println("docname.."+myshopname);
        System.out.println("addres id.."+cur_addressId);

        System.out.println("fee.."+myfee);
        System.out.println("phone.."+myphone);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Medical List");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewMedicalShop.this,PatientDashBoard.class);
                        intent.putExtra("id",patientId);
                        intent.putExtra("mobile",patientMobileNumber);
                        startActivity(intent);
                    }
                }
        );

    }

    private class GetProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetProfileImageTask(ImageView bmImage) {
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
            centerImage.setImageBitmap(result);
        }

    }

    public void showSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();

//                        new Mytask().execute();
                        Intent intent = new Intent(ViewMedicalShop.this,PatientDashBoard.class);
                        intent.putExtra("id",patientId);
                        intent.putExtra("mobile",patientMobileNumber);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Doctor Appointment");
        alert.show();

    }

    public void showErrorMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();

    }

    //Get all addresses for doctor list from api call
    private class GetMeidcalAllAddressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            // Create a progressdialog
            progressDialog = new ProgressDialog(ViewMedicalShop.this);
            // Set progressdialog title
            progressDialog.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog.setMessage("Loading...");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service", "Started");
                httpURLConnection.setRequestMethod("GET");
                System.out.println("u...."+params[0]);
                System.out.println("dsfafssss....");
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Api response.....", result);

            getAllAddress(result);
            progressDialog.dismiss();
        }
    }

    private void getAllAddress(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String myaddressId = object.getString("AddressID");
                System.out.println("addrid.."+myaddressId);

                if(myaddressId.equals(cur_addressId))
                {
                    System.out.println("if cond");
                    myaddress = object.getString("Address1");
                    myShopName = object.getString("ShopName");
                    mystate = object.getString("StateName");

                    mycity = object.getString("CityName");

                    //  hospitalname.setText(myShopName);
                    doornum.setText(myaddress);
                    city.setText(mycity);
                    state.setText(mystate);

                    break;
                }
//                else {
//                    continue;
//                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    ////Patient booking appointment successfull sms////

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
                String shopname = myshopname;
                String ContactPersonname = myContactPersonname;
                String shopphonenum = myphone;
                String area = myaddress;
//                String date = appointmentdate.getText().toString();
//                String address = myaddress+","+mycity;
                String link="https://www.medictfhc.com/UserLogin.aspx";

                String message="Medical store details: -"+shopname+", "+"Contact Details: "+ContactPersonname+"-"+shopphonenum+", "+"Address: "+area+". Click here for navigate: "+link+". \n"+"Regards MEDIC TFHC.";
                mainUrl="http://www.mgage.solutions/SendSMS/sendmsg.php?";
                //   smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination=myMobile;
                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(mainUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);
                mainUrl=stringBuilder.toString();
                System.out.println("smsUrl..."+mainUrl);
                myURL=new URL(mainUrl);
                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return null;
        }
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }
    }

}