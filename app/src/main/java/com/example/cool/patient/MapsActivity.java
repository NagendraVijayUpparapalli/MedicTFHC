package com.example.cool.patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lati,longi;
    static int getUserId;
    static String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getUserId = getIntent().getIntExtra("id",getUserId);
        moduleName = getIntent().getStringExtra("doc");

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

                Toast.makeText(getApplicationContext(),latLng.latitude+","+latLng.longitude,Toast.LENGTH_SHORT).show();

                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("are you sure this is your location");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        System.out.println("yesss");
                        if(moduleName.equals("docAdd"))
                        {
                            Intent intent = new Intent(MapsActivity.this,DocAddAddress.class);
                            intent.putExtra("id",getUserId);
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
                            Intent intent = new Intent(MapsActivity.this,DocUpdateAddress.class);
                            intent.putExtra("id",getUserId);
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
                            intent.putExtra("emergencyService",getIntent().getStringExtra("emergencyService"));
                            intent.putExtra("comments",getIntent().getStringExtra("comments"));

                            intent.putExtra("lat",lati);
                            intent.putExtra("lng",longi);
                            startActivity(intent);
                        }

                        if(moduleName.equals("diagAdd"))
                        {
                            Intent intent = new Intent(MapsActivity.this,DiagAddAddress.class);
                            intent.putExtra("id",getUserId);
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

//                        if(moduleName.equals("diagUpdate"))
//                        {
//                            Intent intent = new Intent(MapsActivity.this,DiagAddAddress.class);
//                            intent.putExtra("id",getUserId);
//                            intent.putExtra("diagName",getIntent().getStringExtra("diagName"));
//                            intent.putExtra("address",getIntent().getStringExtra("address"));
//                            intent.putExtra("city",getIntent().getStringExtra("city"));
//                            intent.putExtra("state",getIntent().getStringExtra("state"));
//                            intent.putExtra("district",getIntent().getStringExtra("district"));
//                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
//                            intent.putExtra("pincode",getIntent().getStringExtra("pincode"));
//                            intent.putExtra("person",getIntent().getStringExtra("person"));
//                            intent.putExtra("landmobile",getIntent().getStringExtra("landmobile"));
//                            intent.putExtra("comments",getIntent().getStringExtra("comments"));
//
//                            intent.putExtra("lat",lati);
//                            intent.putExtra("lng",longi);
//                            startActivity(intent);
//                        }
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
