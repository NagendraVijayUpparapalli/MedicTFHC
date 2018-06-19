package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DiagnosticsViewTodaysApppointment extends AppCompatActivity {

    EditText Patientname,emailid,mobilenumer,aadharnumber,address;
    TextView testname,status;
    MultiAutoCompleteTextView comments;

    String Pname,eid,mnumer,aanumber,addres,ttname,statuss,cmt,prescrption,centername;
    int appointmentID,Dstatus,RDID;
    String PatientID,diagnosticId,diagMobile;
    String str,commnt;
    Button submit,history;
    FloatingActionButton camera,gallery;
    final int REQUEST_CODE_GALLERY1 = 999,REQUEST_CODE_GALLERY2 = 1;

    Uri selectedImageUri ;
    Bitmap selectedImageBitmap = null;
    ImageView prescription;
    static String encodedImage = null;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_view_todays_apppointment);

        baseUrl = new ApiBaseUrl();

        Patientname=(EditText)findViewById(R.id.name);
        emailid=(EditText)findViewById(R.id.emailid);
        mobilenumer=(EditText)findViewById(R.id.mobilenumber);
        aadharnumber=(EditText)findViewById(R.id.aadharnumber);
        address=(EditText)findViewById(R.id.address);
        testname=(TextView)findViewById(R.id.testname);
        status=(TextView)findViewById(R.id.status);
        comments=(MultiAutoCompleteTextView)findViewById(R.id.Comments);

        prescription=(ImageView) findViewById(R.id.prescription);

        camera=(FloatingActionButton) findViewById(R.id.camera_icon);
        gallery=(FloatingActionButton)findViewById(R.id.gallery_icon);

        submit=(Button)findViewById(R.id.submit);
        history=(Button)findViewById(R.id.click);

        aanumber=getIntent().getStringExtra("Aadharnum");
        appointmentID=Integer.parseInt(getIntent().getStringExtra("appointmentid"));
        diagnosticId = getIntent().getStringExtra("diagId");
        diagMobile = getIntent().getStringExtra("diagMobile");

        new GetPatientDetails().execute(baseUrl.getUrl()+"DiagnosticViewTodayAppDetails"+"?id="+appointmentID);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DiagnosticsViewTodaysApppointment.this,MainPatientHistoryInDiagnostics.class);
                i.putExtra("patientid",PatientID);
                i.putExtra("diagId",diagnosticId);
                i.putExtra("diagMobile",diagMobile);
                System.out.println("patient id in view diag todays.."+PatientID+".."+diagnosticId+"..."+diagMobile);
                startActivity(i);
            }
        });

        gallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                DiagnosticsViewTodaysApppointment.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json=formatDataAsJson();
                new SendDetails().execute(baseUrl.getUrl()+"DiagnosticUpdateTodayAppointment",json.toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("View Todays Appointments");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticsTodaysAppointments.class);
                        intent.putExtra("userId",diagnosticId);
                        startActivity(intent);

                    }
                }

        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY1);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY1) {
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    final InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodedImage = myEncodeImage(selectedImage);
                    formatDataAsJson();

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
//
//
////                System.out.print("text.."+encodedImage);
                prescription.setImageBitmap(selectedImageBitmap);
//
////                editText.setText(encodedImage);

                Log.d("hello","I'm in.");

            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private String myEncodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

//        decode base64 string to image
//        b = Base64.decode(encImage, Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(b, 0, b.length);
//        decodeimg.setImageBitmap(decodedImage);

        return encImage;
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
            str = result;
//            if(str.equals("Data updated successfully."))
//            {
//                showalert();
//
//            }
//            else
//            {
//                showalert1();
//            }

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

        }
    }

    private String formatDataAsJson()
    {
        commnt=comments.getText().toString();
        System.out.println("comments"+commnt);
        JSONObject data = new JSONObject();
        try{

            data.put("DiagnosticsAppID", appointmentID);
            data.put("DStatus",Dstatus);
            data.put("Comment",commnt);
            //data.put("Prescription","");
            data.put("Prescription",encodedImage.replaceAll("\n",""));


            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }
    private void showalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DiagnosticsViewTodaysApppointment.this);
        builder.setMessage("Data updated successfully.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showalert1() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DiagnosticsViewTodaysApppointment.this);
        builder.setMessage("Data updated not successfull.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    //Get patient details  based on doctor id and appointment date
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

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

                Dstatus = js.getInt("DStatus");
                RDID=js.getInt("RDID");
                Pname=(String)js.get("PatientName");
                ttname=(String)js.get("TestName");
                addres=(String)js.get("Address1");
                centername = (String) js.get("CenterName");
                eid=(String) js.get("EmailID");
                mnumer=(String) js.get("MobileNo");
                PatientID=js.getString("PatientID");

            }
            Patientname.setText(Pname);
            emailid.setText(eid);
            mobilenumer.setText(mnumer);
            aadharnumber.setText(aanumber);
            address.setText(addres);
            testname.setText(ttname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
