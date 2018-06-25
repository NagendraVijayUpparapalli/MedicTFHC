package com.example.cool.patient.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SplashScreen extends Activity {


    private static int SPLASH_TIME_OUT = 4000;
    ProgressDialog progressDialog;
    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        baseUrl = new ApiBaseUrl();


//        new SendAuthDetailsToGetToken().execute(baseUrl.getTokenUrl());

        Typeface mytapeface = Typeface.createFromAsset(getAssets(),"Rosewood.ttf");
        TextView mytextview = (TextView) findViewById(R.id.title);
        mytextview.setTypeface(mytapeface);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
                finish();
            }

        },SPLASH_TIME_OUT);

    }

    //send authentication details to get permission token value
    private class SendAuthDetailsToGetToken extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(SplashScreen.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading...");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            InputStream stream = null;

            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();

                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                Log.d("Service","Started");
                httpURLConnection.setDoOutput(true);

                String authData = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode("admin", "UTF-8");

                authData += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode("admin", "UTF-8");

                authData += "&" + URLEncoder.encode("grant_type", "UTF-8") + "="
                        + URLEncoder.encode("password", "UTF-8");

                httpURLConnection.connect();

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params....."+params[0]);
                wr.writeBytes(authData);
                wr.flush();

                stream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                String result = reader.readLine();

                int statuscode = httpURLConnection.getResponseCode();

                System.out.println("status code....."+statuscode);

//                InputStream in = null;
                if (statuscode == 200) {

                    stream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(stream);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }

                }
                else if(statuscode == 404){
//                    showMessage();
                    stream = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(stream);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }

                return result;

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("TAG token result   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            JSONObject js;
            String token = null;

            try
            {
                js = new JSONObject(result);

                if(js.has("token_type"))
                {
                    token = js.getString("access_token");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Sorry",Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("access_token",token);
            editor.commit();

            System.out.println("my token value..."+token);
        }
    }
}
