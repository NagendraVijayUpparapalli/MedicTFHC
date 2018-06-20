package com.example.cool.patient.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.andexert.library.RippleView;
import com.example.cool.patient.R;

import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.bloder.magic.view.MagicButton;

public class ChangePassword extends AppCompatActivity {

    EditText mobile,curr_pswd,new_pswd,confirm_pswd;
    String phone_number,current_password,new_password,confirm_password;
    MagicButton btn_change_password;

    static String uploadServerUrl = null,mobile_number;
    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        baseUrl = new ApiBaseUrl();

        mobile_number = getIntent().getStringExtra("mobile");

        mobile = (EditText) findViewById(R.id.mobile_number);
        curr_pswd = (EditText) findViewById(R.id.currentpassword);
        new_pswd = (EditText) findViewById(R.id.newpassword);
        confirm_pswd = (EditText) findViewById(R.id.confirmpassword);

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

//        button = (MagicButton) findViewById(R.id.btn_login);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadServerUrl = baseUrl.getUrl()+"ChangePassword";
                String js = formatDataAsJson();
                new SendChangePasswordDetails().execute(uploadServerUrl,js.toString());
            }
        });

    }

    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();
        try{
            data.put("MobileNumber",mobile_number);
            data.put("Password",curr_pswd.getText().toString());
            data.put("NewPassword",new_pswd.getText().toString());
            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private class SendChangePasswordDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
//            JSONObject js = (JSONObject) JSONValue.parse(result);
            try
            {
                org.json.JSONObject jsono = new org.json.JSONObject(result);
                String ss = (String) jsono.get("Message");
                showMessage(ss);
                Log.e("Api response if.....", result);

            }
            catch (Exception e)
            {}


        }
    }

    public void showMessage(String responsemessage){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(responsemessage)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
//        alert.setTitle("Login Credentials");
        alert.show();

    }
}
