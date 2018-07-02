package com.example.cool.patient.patient.ViewBloodBanksList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Location;
import android.location.LocationManager;
import android.location.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.widget.ListView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.MyFamily;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.example.cool.patient.common.SelectCity;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.patient.ViewMedicalShopsList.GetCurrentMedicalShopsList;
import com.google.android.gms.maps.model.LatLng;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class BloodBank extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final CharSequence[] BloodGroups  = {"---Select Blood Group ---", "A+", "A-", "B+", "B-", "AB+", "AB-","O+","O-"};
    public static final CharSequence[] cities  = {"---Select City ---", "Tirupati", "Kakinada","Tuni","Hyderabad",};
    public static final CharSequence[] BloodBankName  = {"---Select BloodBank Name ---", "Chittoor", "East Godavari ","West Godavari "};
    public static final CharSequence[] MobileNumber  = {"---Select MobileNumber ---", "9491232233", "8764758392"};
    Spinner cityt,bloodbankname,bloodgroup;
    private static SeekBar seek_bar;
    private static TextView distance,availability;
    static int progress_value,dis = 20,availabilityCount;
    private static String label="";

    //view particular blood bank details fields
    Dialog MyDialog;
    TextView myaddress,mymobile,myname,mynavigate,myperson_name,mysms,mycancel;
    ImageView image;
    Bitmap mIcon11;


    String myPhone,myBloodbank_name,myCity,myArea,contact_name,location,uri=null,userMobile,userId;
    String arr[];
    String mainUrl=null;

    //lat,long


    static String uploadServerUrl = null;
    static String getcity=null;
    LocationManager locationManager;
    String lattitude,longitude;
    static double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;


    static double selectedCitylat;
    static double selectedCitylong;

    ArrayList<BloodBankClass> arrayList;

    BloodBankAdapter adapter1;
    ListView listview;
    CardView cardView;

    //location spinner
    private EditText etsearch;
    private android.widget.ListView list;

   
    int textlength = 0;

    static String selectedlocation=null;
    static String selectedItemText=null;
    SearchableSpinner cityname;

    String getUserId,addressline,mobile,email,pincode,city,state;
    TextView current_city;
    ApiBaseUrl baseUrl;

    // expandable list view
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile,sidenavBloodgroup;

    FloatingActionButton homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        baseUrl = new ApiBaseUrl();

        mobile = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("userId");
        System.out.print("userid in getdoctors list....."+getUserId);

        listview = (ListView)findViewById(R.id.mylist);
//        cardView = (CardView)findViewById(R.id.mylist);
        current_city = (TextView) findViewById(R.id.select_city);
        availability = (TextView) findViewById(R.id.availability);
        current_city.setText(city);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);

        current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BloodBank.this,SelectCity.class);
                i.putExtra("module","bloodbankList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance="+dis;

        current_city.setText(city);

        final List<String> list = new ArrayList<String>();
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
        list.add("Adoni ");
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

//        Collections.sort(list);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BloodBank.this,R.layout.support_simple_spinner_dropdown_item,list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
//                    View view = null ;
//                    TextView tv = (TextView) view;
//                    tv.setTextColor(Color.RED);
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.RED);
//                    tv.setPointerIcon(PointerIcon.getSystemIcon(Context,R.drawable.active_dot));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };


        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        seek_bar.setProgress(dis);
//        seek_bar.setMin(1);
        distance = (TextView) findViewById(R.id.DistanceRange);
//        viewbloodbank = (TextView) findViewById(R.id.viewdetails);
//        bw_dist = (TextView) findViewById(R.id.between_dist);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        toolbar.setTitle("Blood Bank");

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(BloodBank.this,PatientDashBoard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",mobile);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );


        //home button
        homebutton = (FloatingActionButton) findViewById(R.id.home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodBank.this,PatientDashBoard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        sidenavName = (TextView) headerLayout.findViewById(R.id.name);
        sidenavAddress = (TextView) headerLayout.findViewById(R.id.address);
        sidenavMobile = (TextView) headerLayout.findViewById(R.id.mobile);
        sidenavEmail = (TextView) headerLayout.findViewById(R.id.email);
        sidenavBloodgroup = (TextView) headerLayout.findViewById(R.id.bloodgroup);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = PatientSideNavigationExpandableSubList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new PatientSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean retVal = true;

                if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM1) {
                    retVal = false;
                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM2) {
                    retVal = false;
                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {
                    retVal = false;

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM4) {
                    // call some activity here
                    Intent editProfile = new Intent(BloodBank.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getUserId);
                    startActivity(editProfile);

                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(BloodBank.this,MyFamily.class);
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(BloodBank.this,AboutUs.class);
                    startActivity(contact);


                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(BloodBank.this,ReachUs.class);
                    startActivity(contact);

                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here

                    Intent contact = new Intent(BloodBank.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });

//        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupClickListener() {
//
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                boolean retVal = true;
//
//                if (groupPosition == CustomExpandableListAdapter.ITEM1) {
//                    retVal = false;
//                } else if (groupPosition == CustomExpandableListAdapter.ITEM2) {
//                    retVal = false;
//                } else if (groupPosition == CustomExpandableListAdapter.ITEM3) {
//
//                    // call some activity here
//                } else if (groupPosition == CustomExpandableListAdapter.ITEM4) {
//                    // call some activity here
//
//                }
//                return retVal;
//            }
//        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM1) {
                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_1) {

                        Intent i = new Intent(BloodBank.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(BloodBank.this,GetCurrentDiagnosticsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                        // call activity here

                        Intent in = new Intent(BloodBank.this,GetCurrentMedicalShopsList.class);
                        in.putExtra("userId",getUserId);
                        in.putExtra("mobile",mobile);
                        startActivity(in);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                        // call activity here
                        // call activity here
                        Intent contact = new Intent(BloodBank.this,AboutUs.class);
                        startActivity(contact);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                        // call activity here
                        Intent bloodbank = new Intent(BloodBank.this,BloodBank.class);
                        bloodbank.putExtra("userId",getUserId);
                        bloodbank.putExtra("mobile",mobile);
                        startActivity(bloodbank);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                        // call activity here
                        // call activity here
                        Intent contact = new Intent(BloodBank.this,AboutUs.class);
                        startActivity(contact);

                    }

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here
                        Intent intent = new Intent(BloodBank.this,ChangePassword.class);
                        intent.putExtra("mobile",mobile);
                        startActivity(intent);


                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                }

                else if(groupPosition == PatientSideNavigationExpandableListAdapter.ITEM2) {
                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        // call activity here

                        Intent intent = new Intent(BloodBank.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(BloodBank.this,PatientMyDiagnosticAppointments.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_3) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_4) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_5) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_6) {

                        // call activity here

                    }
                }
                return true;

            }
        });

        rangeBar();

    }


    //Get patient list based on id from api call
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getProfileDetails(result);

        }
    }

    private void getProfileDetails(String result) {

        try
        {
            JSONObject js = new JSONObject(result);

            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");
            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myAddress1 = (String) js.get("Address1");
            String myAddress2 = (String) js.get("Address2");
            String myBlood_group = (String) js.get("BloodGroup");

//                TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile;

            sidenavName.setText(mySurname+" "+myName);
            sidenavMobile.setText(myMobile);
            sidenavEmail.setText(myEmail);
            sidenavBloodgroup.setText(myBlood_group);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(BloodBank.this);
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

    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(BloodBank.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (BloodBank.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(BloodBank.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            System.out.print("helo this is else");

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+longitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

//                uploadServerUrl = "https://medictfhc.com/mapi/GetBloodBankDetails?Latitude=17.717100&Longitude=83.309200&Distance=5";
                uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance=20";
                new GetBloodBankDetails().execute(uploadServerUrl);
                arrayList = new ArrayList<BloodBankClass>();

                listview = (ListView)findViewById(R.id.mylist);

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

                        lat = Double.parseDouble(arrayList.get(position).getLati());
                        lng = Double.parseDouble(arrayList.get(position).getLongi());

                        getaddress(lat,lng);

                        MyDialog =  new Dialog(BloodBank.this);
                        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        MyDialog.setContentView(R.layout.bloodbank_view);

                        image = (ImageView) MyDialog.findViewById(R.id.imageView);
                        myname = (TextView) MyDialog.findViewById(R.id.bloodbankname);
                        mynavigate = (TextView) MyDialog.findViewById(R.id.navigate);
                        myperson_name = (TextView) MyDialog.findViewById(R.id.person_name);
                        myaddress = (TextView) MyDialog.findViewById(R.id.addressline);
                        mymobile = (TextView) MyDialog.findViewById(R.id.phone);
//                        myemail = (TextView) MyDialog.findViewById(R.id.emailid);
                        mysms = (TextView) MyDialog.findViewById(R.id.SMS);

                        mycancel = (TextView) MyDialog.findViewById(R.id.cancel_icon);
                        String addressarea = addressline;
                        arr= addressarea.split(",");

                        myname.setText(arrayList.get(position).getName());
                        myaddress.setText(addressarea);
                        mymobile.setText(arrayList.get(position).getMobile());
//                        myemail.setText("Not Available");
                        myperson_name.setText(arrayList.get(position).getContact_person());

                        myBloodbank_name = myname.getText().toString();
                        myArea = myaddress.getText().toString();
                        myPhone = mymobile.getText().toString();

                        new GetImageTask(image).execute(arrayList.get(position).getImage());

                        mysms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Mytask().execute();
                            }
                        });


                        mycancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDialog.cancel();
                            }
                        });

                        mynavigate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String lt = String.valueOf(lat);
                                String lg = String.valueOf(lng);

                                uri = String.format(Locale.ENGLISH, "geo:0,0?q="+lt+","+lg+"("+myname.getText().toString()+")");
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(intent);
                            }
                        });


                        MyDialog.setCancelable(false);
                        MyDialog.setCanceledOnTouchOutside(false);
                        MyDialog.show();
                    }
                });

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

//                uploadServerUrl = "https://medictfhc.com/mapi/GetBloodBankDetails?Latitude=17.717100&Longitude=83.309200&Distance=5";
                uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance="+dis;
                new GetBloodBankDetails().execute(uploadServerUrl);
                arrayList = new ArrayList<BloodBankClass>();

                listview = (ListView)findViewById(R.id.mylist);

//                Collections.sort(arrayList, new CustomComaprator());
//                adapter1 = new BloodBankAdapter(this,str,arrayList);

//                adapter1.notifyDataSetChanged();


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

                        lat = Double.parseDouble(arrayList.get(position).getLati());
                        lng = Double.parseDouble(arrayList.get(position).getLongi());

                        System.out.println("lattttt...."+lat);
                        System.out.println("lattttt...."+lng);
                        getaddress(lat,lng);


                        MyDialog =  new Dialog(BloodBank.this);
                        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        MyDialog.setContentView(R.layout.bloodbank_view);

                        image = (ImageView) MyDialog.findViewById(R.id.imageView);
                        myname = (TextView) MyDialog.findViewById(R.id.bloodbankname);
                        mynavigate = (TextView) MyDialog.findViewById(R.id.navigate);
                        myperson_name = (TextView) MyDialog.findViewById(R.id.person_name);
                        myaddress = (TextView) MyDialog.findViewById(R.id.addressline);
                        mymobile = (TextView) MyDialog.findViewById(R.id.phone);
//                        myemail = (TextView) MyDialog.findViewById(R.id.emailid);
                        mysms = (TextView) MyDialog.findViewById(R.id.SMS);

                        mycancel = (TextView) MyDialog.findViewById(R.id.cancel_icon);

                        String addressarea = addressline;
                        arr= addressarea.split(",");

                        myname.setText(arrayList.get(position).getName());
                        myaddress.setText(addressarea);
                        mymobile.setText(arrayList.get(position).getMobile());
//                        myemail.setText("Not Available");
                        myperson_name.setText(arrayList.get(position).getContact_person());

                        myBloodbank_name = myname.getText().toString();
                        myArea = myaddress.getText().toString();
                        myPhone = mymobile.getText().toString();
//                mycontact_name = myperson_name.getText().toString();

                        new GetImageTask(image).execute(arrayList.get(position).getImage());

                        mysms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Mytask().execute();
                            }
                        });


                        mycancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDialog.cancel();
                            }
                        });

                        mynavigate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String lt = String.valueOf(lat);
                                String lg = String.valueOf(lng);

                                uri = String.format(Locale.ENGLISH, "geo:0,0?q="+lt+","+lg+"("+myname.getText().toString()+")");
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(intent);
                            }
                        });


                        MyDialog.setCancelable(false);
                        MyDialog.setCanceledOnTouchOutside(false);
                        MyDialog.show();

                    }
                });

//                textView.setText( lattitude);
//                textView1.setText(longitude);
                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }


            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public class CustomComaprator implements Comparator<BloodBankClass>
    {
        @Override
        public int compare(BloodBankClass o1, BloodBankClass o2) {
            return o1.getDistance().compareTo(o2.getDistance());
        }
    }

    private void getlatlng()
    {
        if(Geocoder.isPresent())
        {
            try {
                selectedlocation = city;
                System.out.println("getlatlong method city....."+selectedlocation);
                Geocoder gc=new Geocoder(this);
                List<Address> addresses1=gc.getFromLocationName(selectedlocation,5);
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


                        lat = Double.parseDouble(arrayList.get(position).getLati());
                        lng = Double.parseDouble(arrayList.get(position).getLongi());

                        System.out.println("lattttt...."+lat);
                        System.out.println("lattttt...."+lng);
                        getaddress(lat,lng);


                        MyDialog =  new Dialog(BloodBank.this);
                        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        MyDialog.setContentView(R.layout.bloodbank_view);

                        image = (ImageView) MyDialog.findViewById(R.id.imageView);
                        myname = (TextView) MyDialog.findViewById(R.id.bloodbankname);
                        mynavigate = (TextView) MyDialog.findViewById(R.id.navigate);
                        myperson_name = (TextView) MyDialog.findViewById(R.id.person_name);
                        myaddress = (TextView) MyDialog.findViewById(R.id.addressline);
                        mymobile = (TextView) MyDialog.findViewById(R.id.phone);
//                        myemail = (TextView) MyDialog.findViewById(R.id.emailid);
                        mysms = (TextView) MyDialog.findViewById(R.id.SMS);

                        mycancel = (TextView) MyDialog.findViewById(R.id.cancel_icon);

                        String addressarea = addressline;
                        arr= addressarea.split(",");

                        myname.setText(arrayList.get(position).getName());
                        myaddress.setText(addressarea);
                        mymobile.setText(arrayList.get(position).getMobile());
//                        myemail.setText("Not Available");
                        myperson_name.setText(arrayList.get(position).getContact_person());

                        myBloodbank_name = myname.getText().toString();
                        myArea = myaddress.getText().toString();
                        myPhone = mymobile.getText().toString();
//                mycontact_name = myperson_name.getText().toString();

                        new GetImageTask(image).execute(arrayList.get(position).getImage());

                        mysms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Mytask().execute();
                            }
                        });


                        mycancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDialog.cancel();
                            }
                        });

                        mynavigate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String lt = String.valueOf(lat);
                                String lg = String.valueOf(lng);

                                uri = String.format(Locale.ENGLISH, "geo:0,0?q="+lt+","+lg+"("+myname.getText().toString()+")");
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(intent);
                            }
                        });


                        MyDialog.setCancelable(false);
                        MyDialog.setCanceledOnTouchOutside(false);
                        MyDialog.show();

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                {

                    addressline = fulladdress.get(0).getAddressLine(0);

                    System.out.println("address line..."+addressline);

                }
            }

        } catch (IOException e) {
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

//    @Override
//    public void onClick(View v) {
//        //get latitude and longitude based on current location
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//
//        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            getLocation();
//        }
//        //cityname.setText("");
//        Toast.makeText(getApplicationContext(), "Clicked Location", Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(BloodBank.this, com.example.cool.patient.ListView.class);
//        startActivity(i);
//       // getlatlng();
//    }

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
                    availabilityCount = 0;
                    System.out.println("bb availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));

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

            availabilityCount = jarray.length();
            System.out.println("bb availabilityCount...."+availabilityCount);

            availability.setText(Integer.toString(availabilityCount));

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject object = jarray.getJSONObject(i);
                BloodBankClass bb = new BloodBankClass();

                bb.setName(object.getString("BloodBankName").substring(5));

                bb.setMobile(object.getString("MobileNumber"));
                bb.setLocation(city);
                bb.setContact_person(object.getString("ContactPerson"));
                bb.setLat((object.getString("Latitude")));
                bb.setLng((object.getString("Longitude")));
                //bb.setAvailability("Yes");
                bb.setDistance(object.getString("distance"));
                bb.setImage(object.getString("BloodbankImage"));

                arrayList.add(bb);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();
        try{

            data.put("Latitude","17.717100");
            data.put("Longitude","83.309200");
            data.put("Distance",dis);
            return data.toString();
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
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
                distance.setText(progress+" Km") ;

//                bw_dist.setText("Distance :"+progress+"Km");

//                Toast.makeText(BloodBank.this,"SeekBar is in progress",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(BloodBank.this,"SeekBar is in StartTracking",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance.setText(progress_value+" Km");
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

        distance.setText(seek_bar.getProgress()+" Km");

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

                lat = Double.parseDouble(arrayList.get(position).getLati());
                lng = Double.parseDouble(arrayList.get(position).getLongi());

                System.out.println("lattttt...."+lat);
                System.out.println("lattttt...."+lng);
                getaddress(lat,lng);


                MyDialog =  new Dialog(BloodBank.this);
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.bloodbank_view);

                image = (ImageView) MyDialog.findViewById(R.id.imageView);
                myname = (TextView) MyDialog.findViewById(R.id.bloodbankname);
                mynavigate = (TextView) MyDialog.findViewById(R.id.navigate);
                myperson_name = (TextView) MyDialog.findViewById(R.id.person_name);
                myaddress = (TextView) MyDialog.findViewById(R.id.addressline);
                mymobile = (TextView) MyDialog.findViewById(R.id.phone);
//                myemail = (TextView) MyDialog.findViewById(R.id.emailid);
                mysms = (TextView) MyDialog.findViewById(R.id.SMS);

                mycancel = (TextView) MyDialog.findViewById(R.id.cancel_icon);

                String addressarea = addressline;
                arr= addressarea.split(",");

                myname.setText(arrayList.get(position).getName());
                myaddress.setText(addressarea);
                mymobile.setText(arrayList.get(position).getMobile());
//                myemail.setText("Not Available");
                myperson_name.setText(arrayList.get(position).getContact_person());

                myBloodbank_name = myname.getText().toString();
                myArea = myaddress.getText().toString();
                myPhone = mymobile.getText().toString();
//                mycontact_name = myperson_name.getText().toString();

                new GetImageTask(image).execute(arrayList.get(position).getImage());


                mysms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Mytask().execute();
                    }
                });


                mycancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.cancel();
                    }
                });

                mynavigate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String lt = String.valueOf(lat);
                        String lg = String.valueOf(lng);

                        uri = String.format(Locale.ENGLISH, "geo:0,0?q="+lt+","+lg+"("+myname.getText().toString()+")");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                });


                MyDialog.setCancelable(false);
                MyDialog.setCanceledOnTouchOutside(false);
                MyDialog.show();

            }
        });

    }


    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
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

                String contact_person="Admin";
                String phone1=myPhone;
                String link="http://meditfhc.com/";

                String message="Blood Bank Details: -"+myBloodbank_name+", "+"Contact Details: "+contact_person+"-"+phone1+", "+"Address: "+myArea+". Click here for navigate: "+link+". \n"+"Regards MEDIC TFHC.";
                mainUrl="http://www.mgage.solutions/SendSMS/sendmsg.php?";
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = mobile;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(mainUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                mainUrl=stringBuilder.toString();
                System.out.println("mainurl "+mainUrl);
                myURL=new URL(mainUrl);

                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            }
            catch (IOException e) {
                e.printStackTrace();
            }


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return null;
        }

        @Override
        protected void onPreExecute() {
            showMessageSuccessfullSent();
            super.onPreExecute();
        }
    }

    public void showMessageSuccessfullSent(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(BloodBank.this);
        a_builder.setMessage("The Message has sent Successfully to your registered mobile number")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Successfully Sent");
        alert.show();

    }

}