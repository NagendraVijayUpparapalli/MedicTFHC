package com.example.cool.patient.patient.ViewBloodBanksList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ListView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.example.cool.patient.common.SelectCity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BloodBank1 extends AppCompatActivity {

    //lat,long
    static String uploadServerUrl = null;
    LocationManager locationManager;
    String lattitude,longitude;
    static double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;

    private static SeekBar seek_bar;
    private static TextView distance,bw_dist;
    int progress_value,dis=20,str;


    static double selectedCitylat;
    static double selectedCitylong;

    TextView selected_city;
    ListView listview;

    static String selected_location=null;
    static String getUserId,getcity=null,mobile;
    String addressline;

    ArrayList<BloodBankClass> arrayList;

    BloodBankAdapter adapter1;
    ApiBaseUrl baseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank1);

        baseUrl = new ApiBaseUrl();

        selected_city = (TextView) findViewById(R.id.select_city);
        listview = (ListView)findViewById(R.id.mylist);

        getUserId = getIntent().getStringExtra("userId");
        getcity = getIntent().getStringExtra("city");
        mobile = getIntent().getStringExtra("mobile");
        System.out.print("city....."+getcity);
        selected_city.setText(getcity);

//        selected_location = selected_city.getText().toString();

        selected_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BloodBank1.this,SelectCity.class);
                i.putExtra("module","bloodbankList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        });

        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        distance = (TextView) findViewById(R.id.DistanceRange);
        bw_dist = (TextView) findViewById(R.id.between_dist);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
//        else
//        {
        getlatlng();
//        }


        uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance="+dis;
        new GetBloodBankDetails().execute(uploadServerUrl);

        arrayList = new ArrayList<BloodBankClass>();

        listview = (ListView)findViewById(R.id.mylist);

        Collections.sort(arrayList, new Comparator<BloodBankClass>(){
            public int compare(BloodBankClass obj1, BloodBankClass obj2) {
                return obj1.getDistance().compareTo(obj2.getDistance());
            }
        });
//        adapter1.notifyDataSetChanged();


        adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);

        listview.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(BloodBank1.this,ViewBloodBank.class);

                lat = Double.parseDouble(arrayList.get(position).getLati());
                lng = Double.parseDouble(arrayList.get(position).getLongi());

                System.out.println("lattttt...."+lat);
                System.out.println("lattttt...."+lng);
                getaddress(lat,lng);
                String lt = String.valueOf(lat);
                String lg = String.valueOf(lng);

                i.putExtra("image",arrayList.get(position).getImage());
                i.putExtra("name",arrayList.get(position).getName());
                i.putExtra("lati",lt);
                i.putExtra("longi",lg);
                i.putExtra("city",arrayList.get(position).getLocation());
                i.putExtra("person_name",arrayList.get(position).getContact_person());
                i.putExtra("phone",arrayList.get(position).getMobile());
                i.putExtra("email","NA");
                i.putExtra("addressline",addressline);
                i.putExtra("mobile",mobile);
                i.putExtra("id",getUserId);
                startActivity(i);

            }
        });

//        rangeBar();


        arrayList = new ArrayList<BloodBankClass>();
        listview = (ListView)findViewById(R.id.mylist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Blood Bank");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BloodBank1.this,PatientDashBoard.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",mobile);
                        startActivity(intent);

                    }
                }

        );

        rangeBar();

    }

    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(BloodBank1.this);
        a_builder.setMessage("Blood Banks are not available for selected city")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("BloodBanks Records");
        alert.show();

    }

    public void getaddress(double lat,double lng)
    {
        geocoder=new Geocoder(getApplicationContext());

        try {
            fulladdress = geocoder.getFromLocation(lat, lng,1);
            System.out.println("full address"+fulladdress);

            if (fulladdress.isEmpty())
            {
//                        cityname.setTitle("waiting");
            }
            else
            {
                if(fulladdress.size()>0)
                    if(fulladdress.size()>0)
                    {

                        addressline = fulladdress.get(0).getAddressLine(0);

                        System.out.println("address line..."+addressline);
                    }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getlatlng()
    {
        if(Geocoder.isPresent())
        {
            try {
                selected_location = getcity;
                System.out.println("getlatlong method city....."+selected_location);
                Geocoder gc=new Geocoder(this);
                List<Address> addresses1=gc.getFromLocationName(selected_location,5);
                List<LatLng> l1=new ArrayList<>(addresses1.size());
                System.out.println("adresses1"+addresses1);
                for(Address a:addresses1){
                    if(a.hasLatitude() && a.hasLongitude()){
                        l1.add(new LatLng(a.getLatitude(),a.getLongitude()));
                    }
                }
                selectedCitylat = l1.get(0).latitude;
                selectedCitylong = l1.get(0).longitude;

//                getaddress(selectedCitylat,selectedCitylong);

//                uploadServerUrl = "https://medictfhc.com/mapi/GetBloodBankDetails?Latitude=17.717100&Longitude=83.309200&Distance=5";
                uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+selectedCitylat+"&Longitude="+selectedCitylong+"&Distance="+dis;
                new GetBloodBankDetails().execute(uploadServerUrl);
                arrayList = new ArrayList<BloodBankClass>();
                listview = (ListView)findViewById(R.id.mylist);

//                Collections.sort(arrayList, new CustomComaprator());
//                adapter1 = new BloodBankAdapter(this,str,arrayList);


                Collections.sort(arrayList, new Comparator<BloodBankClass>(){
                    public int compare(BloodBankClass obj1, BloodBankClass obj2) {
                        return obj1.getDistance().compareTo(obj2.getDistance());
                    }
                });
//                adapter1.notifyDataSetChanged();


                adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);

                listview.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long id) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(BloodBank1.this,ViewBloodBank.class);
                        Bundle bundle = new Bundle();

                        lat = Double.parseDouble(arrayList.get(position).getLati());
                        lng = Double.parseDouble(arrayList.get(position).getLongi());

                        System.out.println("lattttt...."+lat);
                        System.out.println("longiii...."+lng);

//                        Toast.makeText(getApplicationContext(), String.valueOf(lat), Toast.LENGTH_LONG).show();
                        getaddress(lat,lng);

                        String lt = String.valueOf(lat);
                        String lg = String.valueOf(lng);

//                        bundle.putInt("image", arrayList.get(position).getImage());
                        i.putExtra("image",arrayList.get(position).getImage());
                        i.putExtra("name",arrayList.get(position).getName());
                        i.putExtra("person_name",arrayList.get(position).getContact_person());
                        i.putExtra("lati",lt);
                        i.putExtra("longi",lg);
                        i.putExtra("phone",arrayList.get(position).getMobile());
                        i.putExtra("city",arrayList.get(position).getLocation());
                        i.putExtra("email","medic@gmail.com");
                        i.putExtra("mobile",mobile);
                        i.putExtra("id",getUserId);
                        i.putExtra("addressline",addressline);
//                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //Get bloodbanks list from api call
    private class GetBloodBankDetails extends AsyncTask<String, Void, String> {

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
                System.out.println("params bb...."+params[0]);
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
            try
            {
                JSONObject jsono = new JSONObject(result);
                String ss = (String) jsono.get("Message");
                if(ss.equals("No data found."))
                {
                    showMessage();
                    Log.e("Api response if.....", result);
                }
                else
                {
                    getData(result);
                    adapter1.notifyDataSetChanged();
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {}
            getData(result);
            adapter1.notifyDataSetChanged();
//            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    private void getData(String result) {
        try {
            JSONObject jsono = new JSONObject(result);
//            if(jsono.get("Message").equals("No data found."))
//            {
//                showMessage();
//            }
//            else
//            {
            JSONArray jarray = jsono.getJSONArray("BloodbankList");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                BloodBankClass bb = new BloodBankClass();

                bb.setName(object.getString("BloodBankName"));
                bb.setMobile(object.getString("MobileNumber"));
                bb.setLocation(getcity);
                bb.setContact_person(object.getString("ContactPerson"));
                bb.setLat((object.getString("Latitude")));
                bb.setLng((object.getString("Longitude")));
                //bb.setAvailability("Yes");
                bb.setDistance(object.getString("distance"));
                bb.setImage(object.getString("BloodbankImage"));

                arrayList.add(bb);
            }

//            adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);
//                 listview.setAdapter(adapter1);
//                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                            long id) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
//                    }
//                });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void rangeBar()
    {

//        bw_dist.setText("Distance :"+dis+"Km");
//        bw_dist.setText("Distance :"+seek_bar.getProgress()+"Km");

        seek_bar.setProgress(dis);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                System.out.println("progress...."+progress);
                distance.setText(progress+"Km") ;

//                bw_dist.setText("Distance :"+progress+"Km");

//                Toast.makeText(BloodBank.this,"SeekBar is in progress",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(BloodBank.this,"SeekBar is in StartTracking",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance.setText(progress_value+"Km");
//                bw_dist.setText("Distance stop value :"+progress_value+"Km");
                dis = progress_value;
                System.out.println("dis.."+dis);
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    buildAlertMessageNoGps();
//
//                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    getLocation();
//                }

                getlatlng();
//                Toast.makeText(BloodBank.this,dis,Toast.LENGTH_LONG).show();
            }
        });

        distance.setText(seek_bar.getProgress()+"Km");

        uploadServerUrl = "http://meditfhc.com/mapi/GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance="+dis;
        new GetBloodBankDetails().execute(uploadServerUrl);

        arrayList = new ArrayList<BloodBankClass>();

        listview = (ListView)findViewById(R.id.mylist);

        Collections.sort(arrayList, new Comparator<BloodBankClass>(){
            public int compare(BloodBankClass obj1, BloodBankClass obj2) {
                return obj1.getDistance().compareTo(obj2.getDistance());
            }
        });
//        adapter1.notifyDataSetChanged();


        adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);

        listview.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(BloodBank1.this,ViewBloodBank.class);

                lat = Double.parseDouble(arrayList.get(position).getLati());
                lng = Double.parseDouble(arrayList.get(position).getLongi());

                System.out.println("lattttt...."+lat);
                System.out.println("lattttt...."+lng);
                getaddress(lat,lng);
                String lt = String.valueOf(lat);
                String lg = String.valueOf(lng);

                i.putExtra("image",arrayList.get(position).getImage());
                i.putExtra("name",arrayList.get(position).getName());
                i.putExtra("lati",lt);
                i.putExtra("longi",lg);
                i.putExtra("city",arrayList.get(position).getLocation());
                i.putExtra("person_name",arrayList.get(position).getContact_person());
                i.putExtra("phone",arrayList.get(position).getMobile());
                i.putExtra("email","NA");
                i.putExtra("addressline",addressline);
                i.putExtra("mobile",mobile);
                i.putExtra("id",getUserId);
                startActivity(i);

            }
        });

    }
}
