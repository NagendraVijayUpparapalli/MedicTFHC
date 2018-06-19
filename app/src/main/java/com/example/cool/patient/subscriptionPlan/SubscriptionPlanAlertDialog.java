package com.example.cool.patient.subscriptionPlan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SubscriptionPlanAlertDialog extends AppCompatActivity {

    TextView textview;
    AlertDialog alertDialog1;
    CharSequence[] values = {"Urban", "Rural"};
    String doctorName,diagnosticName,moduleName,moduleId;
    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan_alert_dialog);

        baseUrl = new ApiBaseUrl();

        moduleId = getIntent().getStringExtra("id");
        moduleName = getIntent().getStringExtra("module");

        if(moduleName.equals("doc"))
        {
            new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+moduleId);
        }
        else if(moduleName.equals("diag"))
        {
            new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+moduleId);
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Subscription For ?");
        alert.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        if(moduleName.equals("doc"))
                        {
                            Toast.makeText(getApplicationContext(), "your doc", Toast.LENGTH_LONG).show();
                            Intent urban  = new Intent(SubscriptionPlanAlertDialog.this,Subscription_for_Urban.class);
                            urban.putExtra("subscriptionType","URBAN");
                            urban.putExtra("docName",doctorName);
                            urban.putExtra("id",moduleId);
                            urban.putExtra("module","doc");
                            startActivity(urban);
                        }

                        else if(moduleName.equals("diag"))
                        {
                            Toast.makeText(getApplicationContext(), "your diag", Toast.LENGTH_LONG).show();
                            Intent urban  = new Intent(SubscriptionPlanAlertDialog.this,Subscription_for_Urban.class);
                            urban.putExtra("subscriptionType","URBAN");
                            urban.putExtra("diagName",diagnosticName);
                            urban.putExtra("id",moduleId);
                            urban.putExtra("module","diag");
                            startActivity(urban);
                        }
                        break;
                    case 1:
                        if(moduleName.equals("doc"))
                    {
                        Toast.makeText(getApplicationContext(), "your doc", Toast.LENGTH_LONG).show();
                        Intent urban  = new Intent(SubscriptionPlanAlertDialog.this,Subscription_for_Rural.class);
                        urban.putExtra("subscriptionType","RURAL");
                        urban.putExtra("docName",doctorName);
                        urban.putExtra("id",moduleId);
                        urban.putExtra("module","doc");
                        startActivity(urban);
                    }

                        else if(moduleName.equals("diag"))
                    {
                        Toast.makeText(getApplicationContext(), "your diag", Toast.LENGTH_LONG).show();
                        Intent urban  = new Intent(SubscriptionPlanAlertDialog.this,Subscription_for_Rural.class);
                        urban.putExtra("subscriptionType","RURAL");
                        urban.putExtra("diagName",diagnosticName);
                        urban.putExtra("id",moduleId);
                        urban.putExtra("module","diag");
                        startActivity(urban);
                    }
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

    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result docprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

            String myFirstName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");
            doctorName = myFirstName+" "+mySurname;

            System.out.println("doc name in plan.."+doctorName);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    //get diagnostic details based on id from api call

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

            Log.e("TAG result diagprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDiagnosticDetails(result);
        }

    }

    private void getDiagnosticDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);
            String  firstName= (String) js.get("FirstName");
            String lastName = (String) js.get("LastName");
            diagnosticName = firstName+" "+lastName;


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

}
