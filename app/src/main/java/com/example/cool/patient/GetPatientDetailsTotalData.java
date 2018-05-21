package com.example.cool.patient;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
public class GetPatientDetailsTotalData extends AppCompatActivity {

    TextView aadharnumber,mobilenumber,timeslot,patientname;
    Spinner spinner;

    String patientnme,aadhar,mobilenum,timeslt,str,url,status1;
    static String AppointmentID,DoctorComment,Approved,Amount,Prescrition,Payment=null;
    int AppointmentID1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static String[] status = {"---Status---", "Pending", "Accept", "Reshudule", "Reject"};
    FloatingActionButton camaraicon;
    ImageView image;
    //MultiAutoCompleteTextView comment;
    CheckBox paytm,netbanking,cashonhand,creditcard;
    EditText amount,comment;
    Button button;

    ArrayAdapter<String> statusAdapter;

    ApiBaseUrl baseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_patient_details_total_data);

        baseUrl = new ApiBaseUrl();

        camaraicon = (FloatingActionButton) findViewById(R.id.prescription_camaraIcon);
        image = (ImageView) findViewById(R.id.prescription);
        comment=(EditText) findViewById(R.id.Comments_Others);
        button=(Button) findViewById(R.id.button);
        paytm=(CheckBox)findViewById(R.id.pay_paytm);
        netbanking=(CheckBox)findViewById(R.id.net_banking);
        cashonhand=(CheckBox)findViewById(R.id.cash_on_hand);
        creditcard=(CheckBox)findViewById(R.id.debit_card);
        amount=(EditText)findViewById(R.id.amount);

        spinner = (Spinner) findViewById(R.id.status);

        aadharnumber=(TextView)findViewById(R.id.aadhaarNumber);
        mobilenumber=(TextView)findViewById(R.id.mobilenumber);
        timeslot=(TextView)findViewById(R.id.time_slot);
        patientname=(TextView)findViewById(R.id.Patient_name);

        patientnme=getIntent().getStringExtra("patientname");
        aadhar=getIntent().getStringExtra("aadharnumber");
        mobilenum=getIntent().getStringExtra("mobilenumber");
        timeslt=getIntent().getStringExtra("timeslot");
        status1=getIntent().getStringExtra("status");
        AppointmentID1=getIntent().getIntExtra("AppointmentID",1);

        System.out.println("aadhar in patient total data..."+aadhar);

        System.out.println("Appintmentid..."+AppointmentID1);
        AppointmentID=Integer.toString(AppointmentID1);
        //AppointmentID1=Integer.parseInt(AppointmentID);

        patientname.setText(patientnme);
        mobilenumber.setText(mobilenum);
        aadharnumber.setText(aadhar);
        timeslot.setText(timeslt);


        statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner.setAdapter(statusAdapter); // Apply the adapter to the spinner


        DoctorComment = comment.getText().toString().trim();
        Amount = amount.getText().toString();

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Payment = "Pay with Paytm";

                netbanking.setEnabled(false);
                cashonhand.setEnabled(false);
                creditcard.setEnabled(false);
            }
        });

        netbanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment="Net Banking";
                cashonhand.setEnabled(false);
                creditcard.setEnabled(false);
                paytm.setEnabled(false);
            }
        });
        cashonhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment="Cash On Hand";
                creditcard.setEnabled(false);
                paytm.setEnabled(false);
                netbanking.setEnabled(false);
            }
        });


        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment="Credit/Debit Card";
                paytm.setEnabled(false);
                netbanking.setEnabled(false);
                cashonhand.setEnabled(false);

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("doctor comment..."+comment.getText().toString().trim());

                System.out.println("Amount..."+amount.getText().toString());

                System.out.println("status btn..."+spinner.getSelectedItem().toString());


                String json=formatDataAsJson();
                new SendDetails().execute(baseUrl.getUrl()+"DoctotUpdateAppointment",json.toString());
            }
        });

        camaraicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
                }
            }
        });

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetPatientDetailsTotalData.this,GetPatientDetailsList.class);
                        startActivity(intent);

                    }
                }

        );



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_CAMERA_REQUEST_CODE)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
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
                if(s == 1017)
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

    public void showSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        Intent intent = new Intent(GetPatientDetailsTotalData.this,DoctorDashboard.class);
//                        intent.putExtra("id",getUserId);
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


    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        if(paytm.isChecked())
        {
            Payment = "Pay with Paytm";
        }


        if(netbanking.isChecked())
        {
            Payment = "Net Banking";
        }

        if(cashonhand.isChecked())
        {
            Payment="Cash On Hand";
        }

        if(creditcard.isChecked())
        {
            Payment="Credit/Debit Card";
        }

        System.out.println("payment mode..."+Payment);

        try{

            data.put("AppointmentID", AppointmentID);
            data.put("DoctorComment",comment.getText().toString().trim());
            data.put("Approved",spinner.getSelectedItem().toString());
            data.put("Payment",Payment);
            data.put("Amount",amount.getText().toString());
            data.put("Prescription","");

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private void showalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetPatientDetailsTotalData.this);
        builder.setMessage("Appointment updated successfully.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showalert1() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetPatientDetailsTotalData.this);
        builder.setMessage("Appointment updated not successfull.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}