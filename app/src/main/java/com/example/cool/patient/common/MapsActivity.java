package com.example.cool.patient.common;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddressFromMaps;
import com.example.cool.patient.diagnostic.ManageAddress.DiagnosticUpdateAddressFromMaps;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddressFromMaps;
import com.example.cool.patient.doctor.ManageAddress.DoctorUpdateAddressFromMaps;
import com.example.cool.patient.medicalShop.AddAddress.MedicalShopAddAddressFromMaps;
import com.example.cool.patient.medicalShop.ManageAddress.MedicalShopUpdateAddressFromMaps;
import com.example.cool.patient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lati,longi;
    static String getUserId,regMobile;
    static String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getUserId = getIntent().getStringExtra("id");
        regMobile = getIntent().getStringExtra("regMobile");

        moduleName = getIntent().getStringExtra("doc");

        System.out.println("my reg mobile...."+regMobile+"....userid.."+getUserId+"\n"+"module.."+moduleName);

//        setUpMapIfNeeded();

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            // mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).gte;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {


                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    }
                });

            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(15.8651212, 78.5321165);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Andhra Pradesh"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                final double latitude=latLng.latitude;
                final double longitude=latLng.longitude;

                LatLng postion=new LatLng(latitude,longitude);
                mMap.addMarker(new MarkerOptions().position(postion));

                System.out.println("latitude"+latitude+"\n"+"Longitude"+longitude);


                lati = String.valueOf(latLng.latitude);
                longi = String.valueOf(latLng.longitude);

//                Toast.makeText(getApplicationContext(),latLng.latitude+","+latLng.longitude,Toast.LENGTH_SHORT).show();

                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("are you sure this is your location");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        System.out.println("yesss");
                        if(moduleName.equals("docAdd"))
                        {
                            Intent intent = new Intent(MapsActivity.this,DoctorAddAddressFromMaps.class);
                            intent.putExtra("id",getUserId);
                            intent.putExtra("regMobile",regMobile);
                            intent.putExtra("hospitalName",getIntent().getStringExtra("hospitalName"));
                            intent.putExtra("address",getIntent().getStringExtra("address"));
                            intent.putExtra("city",getIntent().getStringExtra("city"));
                            intent.putExtra("state",getIntent().getStringExtra("state"));
                            intent.putExtra("district",getIntent().getStringExtra("district"));
                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
                            intent.putExtra("person",getIntent().getStringExtra("person"));
                            intent.putExtra("fee",getIntent().getStringExtra("fee"));

                            intent.putExtra("lat",lati);
                            intent.putExtra("lng",longi);
                            startActivity(intent);
                        }

                        if(moduleName.equals("docUpdate"))
                        {
                            Intent intent = new Intent(MapsActivity.this,DoctorUpdateAddressFromMaps.class);
                            intent.putExtra("id",getUserId);
                            intent.putExtra("regMobile",regMobile);
                            intent.putExtra("addressId",getIntent().getStringExtra("addressId"));
                            intent.putExtra("hospitalName",getIntent().getStringExtra("hospitalName"));
                            intent.putExtra("address",getIntent().getStringExtra("address"));
                            intent.putExtra("city",getIntent().getStringExtra("city"));
                            intent.putExtra("state",getIntent().getStringExtra("state"));
                            intent.putExtra("district",getIntent().getStringExtra("district"));
                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
                            intent.putExtra("person",getIntent().getStringExtra("person"));
                            intent.putExtra("fee",getIntent().getStringExtra("fee"));

                            intent.putExtra("contactName",getIntent().getStringExtra("contactName"));
                            intent.putExtra("emergencyContact",getIntent().getStringExtra("emergencyContact"));
                            intent.putExtra("emergencyService",getIntent().getBooleanExtra("emergencyService",true));
                            intent.putExtra("comments",getIntent().getStringExtra("comments"));

                            intent.putExtra("lat",lati);
                            intent.putExtra("lng",longi);
                            startActivity(intent);
                        }

                        if(moduleName.equals("diagAdd"))
                        {
                            Intent intent = new Intent(MapsActivity.this,DiagnosticAddAddressFromMaps.class);
                            intent.putExtra("id",getUserId);
                            intent.putExtra("regMobile",regMobile);
                            intent.putExtra("diagName",getIntent().getStringExtra("diagName"));
                            intent.putExtra("address",getIntent().getStringExtra("address"));
                            intent.putExtra("city",getIntent().getStringExtra("city"));
                            intent.putExtra("state",getIntent().getStringExtra("state"));
                            intent.putExtra("district",getIntent().getStringExtra("district"));
                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
                            intent.putExtra("person",getIntent().getStringExtra("person"));
                            intent.putExtra("landmobile",getIntent().getStringExtra("landmobile"));
                            intent.putExtra("comments",getIntent().getStringExtra("comments"));

                            intent.putExtra("lat",lati);
                            intent.putExtra("lng",longi);
                            startActivity(intent);
                        }

                        if(moduleName.equals("diagUpdate"))
                        {
                            Intent intent = new Intent(MapsActivity.this,DiagnosticUpdateAddressFromMaps.class);
                            intent.putExtra("diagid",getIntent().getStringExtra("diagid"));
                            intent.putExtra("regMobile",regMobile);
                            intent.putExtra("addressId",getIntent().getStringExtra("addressId"));
                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                            intent.putExtra("centerName",getIntent().getStringExtra("centerName"));
                            intent.putExtra("address1",getIntent().getStringExtra("address1"));
                            intent.putExtra("comment",getIntent().getStringExtra("comment"));
                            intent.putExtra("emergencyContact",getIntent().getStringExtra("emergencyContact"));
                            intent.putExtra("city",getIntent().getStringExtra("city"));
                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
                            intent.putExtra("district",getIntent().getStringExtra("district"));
                            intent.putExtra("state",getIntent().getStringExtra("state"));
                            intent.putExtra("landline",getIntent().getStringExtra("landline"));
                            intent.putExtra("contactName",getIntent().getStringExtra("contactName"));
                            intent.putExtra("emergencyService",getIntent().getBooleanExtra("emergencyService",true));
                            intent.putExtra("lati",lati);
                            intent.putExtra("longi",longi);

                            intent.putExtra("fromtime",getIntent().getStringExtra("fromtime"));
                            intent.putExtra("totime",getIntent().getStringExtra("totime"));
                            startActivity(intent);
                        }

                        if(moduleName.equals("medicAdd"))
                        {
                            Intent intent = new Intent(MapsActivity.this,MedicalShopAddAddressFromMaps.class);
                            intent.putExtra("id",getUserId);
                            intent.putExtra("regMobile",regMobile);
                            intent.putExtra("diagName",getIntent().getStringExtra("diagName"));
                            intent.putExtra("address",getIntent().getStringExtra("address"));
                            intent.putExtra("Experience",getIntent().getStringExtra("Experience"));
                            intent.putExtra("city",getIntent().getStringExtra("city"));
                            intent.putExtra("state",getIntent().getStringExtra("state"));
                            intent.putExtra("district",getIntent().getStringExtra("district"));
                            intent.putExtra("PharmacyType",getIntent().getStringExtra("PharmacyType"));
                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
                            intent.putExtra("person",getIntent().getStringExtra("person"));
                            intent.putExtra("landmobile",getIntent().getStringExtra("landmobile"));
                            intent.putExtra("comments",getIntent().getStringExtra("comments"));

                            intent.putExtra("lat",lati);
                            intent.putExtra("lng",longi);
                            startActivity(intent);
                        }
                        if(moduleName.equals("medicUpdate"))
                        {
                            Intent intent = new Intent(MapsActivity.this,MedicalShopUpdateAddressFromMaps.class);
                            intent.putExtra("medicalId",getIntent().getStringExtra("medicalId"));
                            intent.putExtra("regMobile",regMobile);
                            intent.putExtra("addressId",getIntent().getStringExtra("addressId"));
                            intent.putExtra("diagName",getIntent().getStringExtra("diagName"));
                            intent.putExtra("address",getIntent().getStringExtra("address"));
                            intent.putExtra("Experience",getIntent().getStringExtra("Experience"));
                            intent.putExtra("city",getIntent().getStringExtra("city"));
                            intent.putExtra("state",getIntent().getStringExtra("state"));
                            intent.putExtra("district",getIntent().getStringExtra("district"));
                            intent.putExtra("PharmacyType",getIntent().getStringExtra("PharmacyType"));
                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
                            intent.putExtra("person",getIntent().getStringExtra("person"));
                            intent.putExtra("landmobile",getIntent().getStringExtra("landmobile"));
                            intent.putExtra("comments",getIntent().getStringExtra("comments"));
                            intent.putExtra("fromTime",getIntent().getStringExtra("fromTime"));
                            intent.putExtra("toTime",getIntent().getStringExtra("toTime"));
                            intent.putExtra("Emeregency_contact",getIntent().getStringExtra("Emeregency_contact"));
                            intent.putExtra("lat",lati);
                            intent.putExtra("lng",longi);
                            startActivity(intent);
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });



    }
}
