package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorReferDiagnostic extends AppCompatActivity {

    private Context context;
    private ReferDiagRecyclerViewAdapter adapter;
    private ArrayList<String> arrayList;
    private Button selectButton;
    SearchableSpinner spinner;

    HashMap<Long, String> myDiagSpecialityList = new HashMap<Long, String>();
    List<String> specialityList;
    ArrayAdapter<String> spinnerAdapter;

    List selectedSpecialityListItems =null;

    HashMap<Long, String> myDiagRangeList = new HashMap<Long, String>();
    List<String> diagCenterList;

    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;

    String appointmentId = null;

    ApiBaseUrl baseUrl;

    String referStatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_refer_diagnostic);

        baseUrl = new ApiBaseUrl();

        appointmentId = getIntent().getStringExtra("aid");

        referStatus = getIntent().getStringExtra("refer");

        if(referStatus.equals("Yes"))
        {
            new GetAllDiagSpeciality().execute(baseUrl.getUrl()+"GetDiagSpeciality");

            new GetDiagnosticsRange().execute(baseUrl.getUrl()+"GetDiagnosticRange?AppointmentID="+appointmentId);
        }
        else
        {
//            new GetDiagnosticsRange().execute(baseUrl.getUrl()+"GetDiagnosticRange?AppointmentID="+appointmentId);
            String js = formatDoctorTimingsDataAsJson();
            new sendDoctorReferDiagnosticDetails().execute(baseUrl.getUrl()+"DoctotUpdateTodaysAppointment",js.toString());
        }

        selectButton = (Button) findViewById(R.id.select_button);
//        populateRecyclerView();
        onClickEvent();



        spinner = (SearchableSpinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "Black birch",
                "Bolean birch",
                "Canoe birch",
                "Cherry birch",
                "European weeping birch"
        };

    }


    //Get all diagnostic specialities from api call
    public class GetAllDiagSpeciality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorReferDiagnostic.this);
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

//                httpURLConnection.setDoOutput(true);
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

            progressDialog.dismiss();
            Log.e("TAG result prev timings", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getAllDiagSpecialities(result);


        }
    }


    private void getAllDiagSpecialities(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            specialityList = new ArrayList<>();


            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myDiagSpecialityList.put(cityKey,cityValue);
                specialityList.add(jsonObj.getString("Value"));



            }

            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);


            arrayList = new ArrayList<>();
            for (int i = 0; i <specialityList.size(); i++)
                arrayList.add(specialityList.get(i));//Adding items to recycler view
            adapter = new ReferDiagRecyclerViewAdapter(context, arrayList);
            recyclerView.setAdapter(adapter);

        }
        catch (JSONException e)
        {}
    }


    //Get all diagnostic range from api call
    public class GetDiagnosticsRange extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog1 = new ProgressDialog(DoctorReferDiagnostic.this);
            // Set progressdialog title
            progressDialog1.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog1.setMessage("Loading...");

            progressDialog1.setIndeterminate(false);
            // Show progressdialog
            progressDialog1.show();
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

//                httpURLConnection.setDoOutput(true);
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
            progressDialog1.dismiss();

            Log.e("TAG result diag centers", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDiagRange(result);


        }
    }

    private void getDiagRange(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            diagCenterList = new ArrayList<>();


            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myDiagRangeList.put(cityKey,cityValue);
                diagCenterList.add(jsonObj.getString("Value"));

            }

            // Initializing an ArrayAdapter
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    this,R.layout.refer_diag_spinner_item,diagCenterList
            );
            spinnerArrayAdapter.setDropDownViewResource(R.layout.refer_diag_spinner_item);
            spinner.setAdapter(spinnerArrayAdapter);

//            System.out.println("sun prev.."+prevSunTimeSlotsList);

        }
        catch (JSONException e)
        {}
    }

    public void showMessage(String s){

        final AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(s)
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(DoctorReferDiagnostic.this,DoctorReferDiagnostic.class);
////                        intent.putExtra("id",getUserId);
//                        startActivity(intent);
                        dialog.cancel();
                    }
                })

                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String js = formatDoctorTimingsDataAsJson();
                        System.out.println("js.."+js.toString());
                        new sendDoctorReferDiagnosticDetails().execute(baseUrl.getUrl()+"InsertDoctorReferDiagnostics",js.toString());

                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("You selected specialities");
        alert.show();

    }


    private void onClickEvent() {
        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedSpecialityListItems = new ArrayList();

                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));
                            System.out.println("item please"+selectedRowLabel);

                            //append the row label text
                            selectedSpecialityListItems.add(selectedRowLabel);
                            stringBuilder.append(selectedRowLabel + "\n");

                            System.out.println("item please...."+stringBuilder.toString());
                        }
                    }
//                    Toast.makeText(getApplicationContext(),  stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    showMessage(stringBuilder.toString());
                }



            }
        });

//        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
//                //Check if item is selected or not via size
//                if (selectedRows.size() > 0) {
//                    //Loop to all the selected rows array
//                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
//
//                        //Check if selected rows have value i.e. checked item
//                        if (selectedRows.valueAt(i)) {
//
//                            //remove the checked item
//                            arrayList.remove(selectedRows.keyAt(i));
//                        }
//                    }
//
//                    //notify the adapter and remove all checked selection
//                    adapter.removeSelection();
//                }
//            }
//        });


        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the current text of Select Button
                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < arrayList.size(); i++)
                        adapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.deselect_all));
                } else {
                    //If button text is Deselect All remove check from all items
                    adapter.removeSelection();

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.select_all));
                }
            }
        });
    }


    private String formatDoctorTimingsDataAsJson() {

        // iterate and display values
//        System.out.println("Fetching Keys and corresponding [Multiple] Values n");

        JSONObject data = new JSONObject();

        try{

            if(referStatus.equals("Yes"))
            {

                org.json.simple.JSONArray allDataArray = new org.json.simple.JSONArray();

                String a[] = new String[selectedSpecialityListItems.size()];
                //Loop index size()
                for(int index = 0; index < a.length; index++) {

                    JSONObject eachData = new JSONObject();

                    eachData.put("SpecialityID", getSpecialityKeyFromValue(myDiagSpecialityList,selectedSpecialityListItems.get(index)));
                    allDataArray.add(eachData);

                }

                data.put("PatientID",getIntent().getStringExtra("pid"));
                data.put("DiagnosticCenterID",getDiagnsticCenterKeyFromValue(myDiagRangeList,spinner.getSelectedItem().toString()));
                data.put("Prescription",getIntent().getStringExtra("prescription"));

                data.put("DComment",getIntent().getStringExtra("reason"));
                data.put("AppointmentID",getIntent().getStringExtra("aid"));

                data.put("PatientName",getIntent().getStringExtra("name"));
                data.put("SpecialityLst",allDataArray.toString());



                System.out.println("final array = " + allDataArray.toString());

                System.out.println("final array size= " + allDataArray.size());

                return data.toString();
            }

            else
            {

                data.put("AppointmentID",getIntent().getStringExtra("aid"));
                data.put("Comments",getIntent().getStringExtra("reason"));
                data.put("ReferDiagnostic",0);

                data.put("ReferMedicalShop",0);
                data.put("Prescription",getIntent().getStringExtra("prescription"));
                return data.toString();
            }

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    public static Object getSpecialityKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static Object getDiagnsticCenterKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    //send doctor refer diagnostic center details
    private class sendDoctorReferDiagnosticDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service","Started");
                httpURLConnection.connect();

                //write
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params refer add....."+params[1]);
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
            Log.e("TAG result docreferdiag", result); // this is expecting a response code to be sent from your server upon receiving the POST data
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
                        Intent intent = new Intent(DoctorReferDiagnostic.this,DoctorDashboard.class);
                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                        intent.putExtra("id",getIntent().getStringExtra("id"));
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
        alert.setTitle("Your Appointment");
        alert.show();

    }


}
