package com.example.cool.patient.subscriptionPlan;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Subscription_for_Rural extends AppCompatActivity {

    Dialog MyDialog;
    Button pay;
    Button cancel;
    String doctorName,diagName,subscriptionType,moduleName,moduleId,paymentMode,planType,amount;
    ApiBaseUrl baseUrl;

    RadioGroup paymentRadioGroup;
    RadioButton paymentradioButton;
    int radiobuttonid;
    Button silverButton,goldButton,platiniumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_for__rural);


        baseUrl = new ApiBaseUrl();
        silverButton = (Button) findViewById(R.id.silver);
        goldButton = (Button) findViewById(R.id.gold);
        platiniumButton = (Button) findViewById(R.id.platinium);

        silverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "SILVER";
                amount = "9000";
            }
        });

        goldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "GOLD";
                amount = "12000";
            }
        });

        platiniumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "PLATINUM";
                amount = "15000";
            }
        });

        moduleName = getIntent().getStringExtra("module");

        if(moduleName.equals("doc"))
        {

            subscriptionType = getIntent().getStringExtra("subscriptionType");
            moduleName = getIntent().getStringExtra("docName");
            moduleId = getIntent().getStringExtra("id");

            System.out.println("module id from doc...."+moduleId);
            System.out.println("module type from doc...."+moduleName);
            System.out.println("subscriptionType in urban....."+subscriptionType);
        }

        else if(moduleName.equals("diag"))
        {
            subscriptionType = getIntent().getStringExtra("subscriptionType");
            moduleName = getIntent().getStringExtra("diagName");
            moduleId = getIntent().getStringExtra("id");

            System.out.println("module id from diag...."+moduleId);
            System.out.println("module type from diag....."+moduleName);
            System.out.print("subscriptionType in urban diag....."+subscriptionType);
        }

        Button button = (Button) findViewById(R.id.sliver);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });

        Button gold = (Button) findViewById(R.id.Gold);

        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });

        Button platinum = (Button) findViewById(R.id.platinium);

        platinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("For RURAL");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Subscription_for_Rural.this,DoctorDashboard.class);
//                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                }

        );

    }


    public void MyCustomAlertDialog(){
        MyDialog =  new Dialog(Subscription_for_Rural.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.subscription_paymentmode_alert);


        // find the radioButton by returned id
        paymentRadioGroup = (RadioGroup) MyDialog.findViewById(R.id.paymentModeType_radioGroup);

        radiobuttonid = paymentRadioGroup.getCheckedRadioButtonId();

        System.out.println("radio...."+radiobuttonid);

        paymentradioButton = (RadioButton) MyDialog.findViewById(radiobuttonid);

        pay.setEnabled(true);
        cancel.setEnabled(true);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String js = formatDataAsJson();
                System.out.println("subscript json data...."+js.toString());

//                new sendSubscriptionDetails().execute(baseUrl.getUrl()+"Subscription",js.toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.setCancelable(false);
        MyDialog.setCanceledOnTouchOutside(false);
        MyDialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qricon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_EditProfile) {
//            Intent i = new Intent(PatientDashBoard.this,EditProfile.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }
//        if(id == R.id.Language_options) {
//            Intent i1 = new Intent(PatientDashBoard.this,Language_Optional.class);


//            startActivity(i1);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }
//        if(id == R.id.Logout) {
//           Intent i1 = new Intent(PatientDashBoard.this,Login.class);
//           startActivity(i1);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

        if(paymentradioButton.getText().equals(" Cash On Hand"))
        {
            paymentMode = "Cash On Hand";
        }
        if(paymentradioButton.getText().equals(" Pay Using Paytm"))
        {
            paymentMode = "Pay Using Paytm";
        }
        if(paymentradioButton.getText().equals(" Debit Card"))
        {
            paymentMode = "Debit Card";
        }
        if(paymentradioButton.getText().equals(" Net Banking"))
        {
            paymentMode = "Net Banking";
        }

        try{

            if(moduleName.equals("doc") )
            {
                data.put("PaymentMode",paymentMode);
                data.put("SubscriptionType",subscriptionType);
                data.put("PlanType",planType);
                data.put("UserID",moduleId);
                data.put("Amount",amount);
                data.put("DocName",doctorName);
                return data.toString();
            }
            else if(moduleName.equals("diag"))
            {
                data.put("PaymentMode",paymentMode);
                data.put("SubscriptionType",subscriptionType);
                data.put("PlanType",planType);
                data.put("UserID",moduleId);
                data.put("Amount",amount);
                data.put("CenterName",diagName);
                return data.toString();
            }




        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    //send subscription details to api
    private class sendSubscriptionDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setChunkedStreamingMode(1024);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                Log.d("Service","Started");
//                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                //write
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
                else if(statuscode == 500){
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
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
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
//
            Log.e("TAG result diag profile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
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


        }
    }
    public void showSuccessMessage(String message){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        Intent intent = new Intent(Subscription_for_Rural.this,DiagnosticDashboard.class);
                        intent.putExtra("id",moduleId);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();
    }

    public void showErrorMessage(String message){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();

    }


}
