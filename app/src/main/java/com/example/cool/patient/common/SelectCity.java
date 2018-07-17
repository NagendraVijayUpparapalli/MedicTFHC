package com.example.cool.patient.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.patient.PatientDashBoard11;
import com.example.cool.patient.patient.ViewBloodBanksList.BloodBank;
import com.example.cool.patient.patient.ViewBloodBanksList.BloodBank1;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList11;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList11;
import com.example.cool.patient.patient.ViewMedicalShopsList.GetCurrentMedicalShopsList;
import com.example.cool.patient.patient.ViewMedicalShopsList.GetCurrentMedicalShopsList11;
import com.example.cool.patient.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectCity extends AppCompatActivity {

    TextView current_location,cancel_button;
    private ListView selected_location;

    // Search EditText
    EditText inputSearch;
    SearchView searchView;

    static String getModuleName = null,userId = null;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    List<String> citiesList;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();

    final ArrayList<String> list = new ArrayList<String>();

    ApiBaseUrl baseUrl;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        baseUrl = new ApiBaseUrl();

        current_location = (TextView) findViewById(R.id.current_city);
        cancel_button = (TextView) findViewById(R.id.cancel_icon);

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        getModuleName = getIntent().getStringExtra("module");
        userId = getIntent().getStringExtra("userId");

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        citiesList = new ArrayList<>();
//        citiesList.remove(0);

        list.add("Addateegala");
        list.add("Ainavilli");
        list.add("Alamuru");
        list.add("Allavaram");
        list.add("Amalapuram");
        list.add("Ambajipeta");
        list.add("Anakapalle");
        list.add("Anandapuram");
        list.add("Ananthagiri");
        list.add("Anaparthy");
        list.add("Araku Valley");
        list.add("Atchutapuram");
        list.add("Atreyapuram");
        list.add("Bheemunipatnam");
        list.add("Biccavolu");
        list.add("Butchayyapeta");
        list.add("Cheedikada");
        list.add("Chintapalle");
        list.add("Chodavaram");
        list.add("Devarapalle");
        list.add("Devipatnam");
        list.add("Dumbriguda");
        list.add("G.Madugula");
        list.add("Gajuwaka");
        list.add("Gandepalle");
        list.add("Gangavaram");
        list.add("Gokavaram");
        list.add("Gollaprolu");
        list.add("Golugonda");
        list.add("Gudem Kotha Veedhi");
        list.add("Hukumpeta");
        list.add("I.Polavaram");
        list.add("Jaggampeta");
        list.add("K.Kotapadu");
        list.add("Kadiam");
        list.add("Kajuluru");
        list.add("Kakinada Urban");
        list.add("Kapileswarapuram");
        list.add("Karapa");
        list.add("Kasimkota");
        list.add("Katrenikona");
        list.add("Kirlampudi");
        list.add("Korukonda");
        list.add("Kotananduru");
        list.add("Kotauratla");
        list.add("Kothapalle");
        list.add("Kothapeta");
        list.add("Koyyuru");
        list.add("Madugula");
        list.add("Makavarapalem");
        list.add("Malikipuram");
        list.add("Mamidikuduru");
        list.add("Mandapeta");
        list.add("Maredumilli");
        list.add("Mummidivaram");
        list.add("Munagapaka");
        list.add("Munchingi Puttu");
        list.add("Nakkapalle");
        list.add("Narsipatnam");
        list.add("Nathavaram");
        list.add("P Gannavaram");
        list.add("Paderu");
        list.add("Padmanabham");
        list.add("Pamarru");
        list.add("Paravada");
        list.add("Payakaraopeta");
        list.add("Peda Bayalu");
        list.add("Pedagantyada");
        list.add("Pedapudi");
        list.add("Peddapuram");
        list.add("Pendurthi");
        list.add("Pithapuram");
        list.add("Prathipadu");
        list.add("Rajahmundry Rural");
        list.add("Rajahmundry Urban");
        list.add("Rajanagaram");
        list.add("Rajavommangi");
        list.add("Ramachandrapuram");
        list.add("Rambilli");
        list.add("Rampachodavaram");
        list.add("Rangampeta");
        list.add("Ravikamatham");
        list.add("Ravulapalem");
        list.add("Rayavaram");
        list.add("Razole");
        list.add("Rolugunta");
        list.add("Rowthulapudi");
        list.add("S Rayavaram");
        list.add("Sabbavaram");
        list.add("Sakhinetipalle");
        list.add("Samalkota");
        list.add("Sankhavaram");
        list.add("Seethanagaram");
        list.add("Thallarevu");
        list.add("Thondangi");
        list.add("Tuni");
        list.add("Uppalaguptam");
        list.add("Y Ramavaram");
        list.add("Yelamanchili");
        list.add("Yeleswaram");
        list.add("Palakollu");
        list.add("Rajamahendravaram");
        list.add("Kakinada");
        list.add("Anantapur");
        list.add("Bathalapalli");
        list.add("Guntakal");
        list.add("Hindupur");
        list.add("Kadiri");
        list.add("Puttaparthy");
        list.add("Chittoor");
        list.add("Kuppam");
        list.add("Madanapalli");
        list.add("Tirumala");
        list.add("Tirupati");
        list.add("Guntur");
        list.add("Narasaraopeta");
        list.add("Piduguralla");
        list.add("Repalle");
        list.add("Tenali");
        list.add("Gudivada");
        list.add("Machilipatnam");
        list.add("Vijayawada");
        list.add("Adoni");
        list.add("Kurnool ");
        list.add("Nandyal ");
        list.add("Chirala ");
        list.add("Markapur ");
        list.add("Ongole ");
        list.add("Gudur");
        list.add("Kavali");
        list.add("Nellore");
        list.add("Ragolu");
        list.add("Rajam");
        list.add("Sangivalasa");
        list.add("Parvathipuram");
        list.add("Eluru");
        list.add("Jangareddygudem");
        list.add("Narasapuram");
        list.add("Tanuku");
        list.add("PRODDATUR");
        list.add("Pulivendula");
        list.add("srikakulam");
        list.add("vijayanagaram");
        list.add("Bhimavaram");

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                System.out.println("Text ["+cs+"]");
                System.out.println("filtered selected city text...."+cs.toString());
                adapter.getFilter().filter(cs);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getModuleName.equals("bloodbankList"))
                {
                    Intent i1 = new Intent(SelectCity.this,BloodBank1.class);
                    i1.putExtra("userId",userId);
                    i1.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i1);
                }

                else if(getModuleName.equals("docList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentDoctorsList.class);
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

                else if(getModuleName.equals("diagList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentDiagnosticsList.class);
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }
                else if(getModuleName.equals("patient"))
                {
                    Intent i = new Intent(SelectCity.this,PatientDashBoard.class);
                    i.putExtra("id",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }
            }
        });

        selected_location = (ListView) findViewById(R.id.list_view);


//        adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, citiesList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
//        selected_location.setAdapter(adapter); // Apply the adapter to the spinner


        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getModuleName.equals("bloodbankList"))
                {
                    Intent i1 = new Intent(SelectCity.this,BloodBank.class);
                    i1.putExtra("userId",userId);
                    i1.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i1);
                }

                else if(getModuleName.equals("docList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentDoctorsList.class);
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

                else if(getModuleName.equals("diagList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentDiagnosticsList.class);
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

                else if(getModuleName.equals("patient"))
                {
                    Intent i = new Intent(SelectCity.this,PatientDashBoard.class);
                    i.putExtra("id",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }
                else if(getModuleName.equals("medicalList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentMedicalShopsList.class);
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

            }
        });

        selected_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),adapter.getItem(position), Toast.LENGTH_LONG).show();


                if(getModuleName.equals("bloodbankList"))
                {
                    Intent i1 = new Intent(SelectCity.this,BloodBank1.class);
                    i1.putExtra("city",adapter.getItem(position));
                    i1.putExtra("userId",userId);
                    i1.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i1);
                }

                else if(getModuleName.equals("docList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentDoctorsList11.class);
                    i.putExtra("city",adapter.getItem(position));
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

                else if(getModuleName.equals("diagList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentDiagnosticsList11.class);
                    i.putExtra("city",adapter.getItem(position));
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

                else if(getModuleName.equals("patient"))
                {
                    Intent i = new Intent(SelectCity.this,PatientDashBoard11.class);
                    i.putExtra("id",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }

                else if(getModuleName.equals("medicalList"))
                {
                    Intent i = new Intent(SelectCity.this,GetCurrentMedicalShopsList11.class);
                    i.putExtra("city",adapter.getItem(position));
                    i.putExtra("userId",userId);
                    i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                    startActivity(i);
                }
            }
        });
    }

    //Get cities list from api call
    private class GetAllCities extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog = new ProgressDialog(SelectCity.this);
//            // Set progressdialog title
////            progressDialog.setTitle("You are logging");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading..");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(SelectCity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.setContentView(R.layout.myprogress);
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss. select city...");

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

            Log.e("TAG result  cities ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            getCities(result);

        }
    }

    private void getCities(String result) {
        try
        {

            JSONArray jsonArr = new JSONArray(result);

            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                if(cityKey == 0)
                {
                    System.out.print("mycity list.."+myCitiesList);
                    System.out.print("city list.."+citiesList);
                }

                else
                {
                    myCitiesList.put(cityKey,cityValue);
                    citiesList.add(jsonObj.getString("Value"));
                    System.out.print("mycity list.."+myCitiesList);
                    System.out.print("city list.."+citiesList);

                    // Adding items to listview
                    adapter = new ArrayAdapter<String>(this, R.layout.city_itemslist,  R.id.city_name, citiesList);
                    selected_location.setAdapter(adapter);
                }


            }

        }
        catch (JSONException e)
        {}
    }
}
