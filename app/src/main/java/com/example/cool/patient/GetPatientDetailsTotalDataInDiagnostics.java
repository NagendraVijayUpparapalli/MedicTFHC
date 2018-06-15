package com.example.cool.patient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GetPatientDetailsTotalDataInDiagnostics extends AppCompatActivity {

    String diagmobile,diagnosticId,centerName,addressId,patientname,mobilenumber,Aadharnumber,statuss,comments1,
            prescription,amount,pamode,test,appointmentDate;
    int rdid,Dstatus;
    List<String> speciality;
    private String fullScreenInd;
    TextView patintname,aadhar,mobile,testname;
    Spinner status;
    String selectedItemText,payment,comment,ammnt;
    EditText comments;
    CheckBox cashonhand,netbanking,swipe_card;
    public static final CharSequence[] states = {"---Status---", "Initiated", "In Progress", "Finished"};
    EditText amnt;
    ImageView prescrption;
    Bitmap mIcon11;
    ProgressDialog mProgressDialog;
    StringBuilder builder;
    Button submit;
    ApiBaseUrl baseUrl;

    String diagmobilenumber, diagaddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_patient_details_total_data_in_diagnostics);

        baseUrl = new ApiBaseUrl();

        speciality=new ArrayList<>();
        patintname=(TextView)findViewById(R.id.Patient_name);
        aadhar=(TextView)findViewById(R.id.aadhaarNumber);
        mobile=(TextView)findViewById(R.id.mobilenumber);
        testname=(TextView)findViewById(R.id.testname);
        comments=(EditText)findViewById(R.id.Comments);
        cashonhand=(CheckBox)findViewById(R.id.cash_on_hand);
//        paytm=(CheckBox)findViewById(R.id.pay_paytm);
        amnt=(EditText)findViewById(R.id.amount);
        netbanking=(CheckBox)findViewById(R.id.net_banking);
        swipe_card=(CheckBox)findViewById(R.id.swipe_card);
        status=(Spinner)findViewById(R.id.status);
        prescrption=(ImageView)findViewById(R.id.prescription);
        submit=(Button) findViewById(R.id.submit);
        builder=new StringBuilder();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        status.setAdapter(adapter);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedItemText = (String) adapterView.getItemAtPosition(i);

                System.out.println("status"+selectedItemText);

                if(selectedItemText.equals("Initiated"))
                {
                    Dstatus=1;
                }
                else if(selectedItemText.equals("In Progress"))
                {
                    Dstatus=2;
                }
                else if(selectedItemText.equals("Finished"))
                {
                    Dstatus=3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        diagmobile = getIntent().getStringExtra("diagmobile");
        diagnosticId = getIntent().getStringExtra("diagnosticId");
        centerName = getIntent().getStringExtra("centerName");
        addressId = getIntent().getStringExtra("addressId");
        patientname=getIntent().getStringExtra("Patientname");
        mobilenumber=getIntent().getStringExtra("mobilenumber");
        Aadharnumber=getIntent().getStringExtra("Aadharnumber");
        statuss=getIntent().getStringExtra("status");
        comments1=getIntent().getStringExtra("comments");
        prescription=getIntent().getStringExtra("Prescription");
        amount=getIntent().getStringExtra("Amount");
        pamode=getIntent().getStringExtra("paymode");
        rdid=getIntent().getIntExtra("rdid",rdid);
        appointmentDate = getIntent().getStringExtra("date");

        System.out.println("appointmentDate....in view data.."+appointmentDate);

        cashonhand.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if(cashonhand.isChecked())
                {
                    payment="Cash on Hand";
                }
            }
        });

//        paytm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(paytm.isChecked()){
//
//                    payment="Pay with Paytm";
//                }
//            }
//        });

        netbanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(netbanking.isChecked())
                {
                    payment="Online Banking";
                }

            }
        });

        swipe_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(swipe_card.isChecked())
                {
                    payment="Debit/Credit card Swipe";
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json=formatDataAsJson();
                System.out.println("js data.getpatient details in diag."+json.toString());
                new SendDetails().execute(baseUrl.getUrl()+"DiagnosticUpdateAppointment",json.toString());
            }
        });

        System.out.println("prescription..."+prescription);

        new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticCenterbyAdressByID"+"?AddressID"+addressId);

        speciality=getIntent().getStringArrayListExtra("testname");

        for(int i=0;i<speciality.size();i++)
        {
            System.out.println("testname"+speciality.get(i));
            test=speciality.get(i);
            builder.append(test+"\n");

        }

        testname.setText(builder.toString());
        patintname.setText(patientname);
        aadhar.setText(Aadharnumber);
        mobile.setText(mobilenumber);
        comments.setText(comments1);
        amnt.setText(amount);
        new DownloadImage().execute(baseUrl.getImageUrl()+prescription);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Patients List");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetPatientDetailsTotalDataInDiagnostics.this,GetPatientDetailsListInDiagnostics.class);
                        intent.putExtra("id",diagnosticId);
                        intent.putExtra("mobile",diagmobile);
                        intent.putExtra("date",appointmentDate);
                        startActivity(intent);

                    }
                }

        );



    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(GetPatientDetailsTotalDataInDiagnostics.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");

            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            prescrption.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }


    private class SendDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setUseCaches(false);
                //connection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service","Started");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params....."+params[1]);
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                int statuscode = httpURLConnection.getResponseCode();

                System.out.println("status code....."+statuscode);

                InputStream in = null;
                if (statuscode == 200) {

                    in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }

                }
                else if(statuscode == 404){
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject js;
            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {
                    showSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

        }
    }

    private String formatDataAsJson()
    {

        comment=comments.getText().toString();
        ammnt=amnt.getText().toString();

        if(netbanking.isChecked())
        {
            payment = "Online Banking";
        }

        if(cashonhand.isChecked())
        {
            payment="Cash on Hand";
        }

        if(swipe_card.isChecked())
        {
            payment="Debit/Credit card Swipe";
        }

        System.out.println("status"+selectedItemText);
        System.out.println("comments"+comments.getText().toString());
        System.out.println("amount"+ammnt);
        System.out.println("payment mode"+payment);

        JSONObject data = new JSONObject();
        try{

            data.put("DiagAppID", rdid);
            data.put("DStatus",Dstatus);
            data.put("Comment",comment);
            data.put("PaymentMode",payment);
            data.put("amount",ammnt);

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return data.toString();
    }

    public void showSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        new Mytask().execute();
                        Intent intent = new Intent(GetPatientDetailsTotalDataInDiagnostics.this,DiagnosticDashboard.class);
                        intent.putExtra("id",diagnosticId);
                        intent.putExtra("mobile",diagmobilenumber);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
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
        alert.setTitle("Updating Your Appointment");
        alert.show();

    }

    //Get patient details  based on doctor id and appointment date
    private class GetDiagnosticDetails extends AsyncTask<String, Void, String> {

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

//                httpURLConnection.setDoOutput(true);
                System.out.println("u...." + params[0]);
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDetails(result);


        }
    }

    private void getDetails(String result) {
        try {

            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js = jsonArray.getJSONObject(i);

                diagmobilenumber=(String) js.getString("MobileNumber");
                diagaddress=(String) js.getString("Address1");

            }

        } catch (JSONException e) {
            e.printStackTrace();
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


                String address = centerName+"-"+diagaddress;

                String message="Hi "+patientname+",the required samples are collected for tests and it will be initiated at "+address+" Your billable amount is: "+amnt.getText().toString()+" , "+"Please call "+diagmobilenumber+" for any assistance."+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                //mainUrl="https://www.mgage.solutions/SendSMS/sendmsg.php?";
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = mobilenumber;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(baseUrl.getSmsUrl());
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);


                System.out.println("mainurl "+baseUrl.getSmsUrl());
                myURL=new URL(baseUrl.getSmsUrl());

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
//            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }
    }

}
