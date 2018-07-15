package com.example.cool.patient.patient.ViewMedicalShopsList;

/**
 * Created by Udayasri on 01-06-2018.
 */

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;
import com.example.cool.patient.patient.BookAppointment.PatientBookAppointmentToDoctor;

import org.json.JSONArray;
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
import java.util.List;
import java.util.Locale;


class MedicalShopListAdapter extends RecyclerView.Adapter<MedicalShopListAdapter.ViewHolder>{

    List<MedicalShopClass> medicalClassList;
    Context context;
    AlertDialog alertDialog1;
    String address,consultationFee,comments,emergencyContact;
    String patientId;
    boolean emergencyService;

    String doctorname,hospitalname,doornum,city,state,payment,mobile,navigaton,patientMobileNumber,myaddressId;

    ApiBaseUrl baseUrl = new ApiBaseUrl();

    Dialog MyDialog;
    TextView myshopname,mydoornum,mycity,mystate,myfee,mypayment,myshopphonenum,
            myContactPersonname,mycancel;
    LinearLayout phoneLayout,mySMS,mynavigation;

    String uri=null;

    Button button;

    ImageView mycenterImage;
    ProgressDialog progressDialog;

    Dialog MyDialog1;
    TextView message;
    LinearLayout oklink;


    public MedicalShopListAdapter(GetCurrentMedicalShopsList getCurrentMedicalList,List<MedicalShopClass> medicalClassList) {
        this.context = getCurrentMedicalList;
        this.medicalClassList = medicalClassList;
    }

    public MedicalShopListAdapter(GetCurrentMedicalShopsList11 getCurrentMedicalList,List<MedicalShopClass> medicalClassList) {
        this.context = getCurrentMedicalList;
        this.medicalClassList = medicalClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView profileImage;
        public TextView ContactPerson,Shopname,qualification,speciality,fee,medicalphonenum,addressId,medicalShopeID,
                userId,centerImage,lati,longi,distance,usermobile;



        RecyclerView recyclerView;

        public Button edit,inactive;


        public ViewHolder(final View itemView) {
            super(itemView);
//            itemImage = (ImageView)itemView.findViewById(R.id.image);

            Shopname = (TextView)itemView.findViewById(R.id.doctorName);
            medicalphonenum=(TextView) itemView.findViewById(R.id.phonenum);
            addressId=(TextView) itemView.findViewById(R.id.addressId);
            medicalShopeID=(TextView) itemView.findViewById(R.id.doctorId);
            userId=(TextView) itemView.findViewById(R.id.userid);
            ContactPerson =(TextView)itemView.findViewById(R.id.ContactPerson);
            profileImage = (ImageView) itemView.findViewById(R.id.docImage);
            lati=(TextView) itemView.findViewById(R.id.lati);
            longi=(TextView) itemView.findViewById(R.id.lngi);
            distance  = (TextView) itemView.findViewById(R.id.distance);
            centerImage=(TextView) itemView.findViewById(R.id.image);
            usermobile = (TextView) itemView.findViewById(R.id.usermobile);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new GetMeidcalAllAddressDetails().execute(baseUrl.getUrl()+"MSGetAddress?ID="+medicalShopeID.getText().toString());

                    MyDialog =  new Dialog(context);
                    MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    MyDialog.setContentView(R.layout.medicalshopview);

                    myshopname=(TextView) MyDialog.findViewById(R.id.Shop_name);
                    myContactPersonname = (TextView)MyDialog.findViewById(R.id.ContactPersonname);
                    mydoornum=(TextView) MyDialog.findViewById(R.id.dr_no);
                    mycity=(TextView) MyDialog.findViewById(R.id.city);
                    mystate=(TextView) MyDialog.findViewById(R.id.state);
                    myshopphonenum=(TextView) MyDialog.findViewById(R.id.phone);
                    mynavigation =(LinearLayout) MyDialog.findViewById(R.id.navigate);
                    mySMS = (LinearLayout) MyDialog.findViewById(R.id.sms);
                    phoneLayout = (LinearLayout) MyDialog.findViewById(R.id.phoneLayout);
                    mycenterImage = (ImageView) MyDialog.findViewById(R.id.centerImage);

                    mycancel = (TextView) MyDialog.findViewById(R.id.cancel_icon);

                    phoneLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_CALL);
                            callintent.setData(Uri.parse("tel:"+myshopphonenum.getText().toString().trim()));
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            context.startActivity(callintent);
                        }
                    });


                    mycancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyDialog.cancel();
                        }
                    });

                    MyDialog.setCancelable(false);
                    MyDialog.setCanceledOnTouchOutside(false);
                    MyDialog.show();

                    new GetProfileImageTask(mycenterImage).execute(baseUrl.getImageUrl()+centerImage.getText().toString());

                    mySMS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Mytask().execute();
                        }
                    });
                    mynavigation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String lt = lati.getText().toString();
                            String lg = longi.getText().toString();
                            String medicalShopName = myshopname.getText().toString().trim();

                            uri = String.format(Locale.ENGLISH, "geo:0,0?q="+lt+","+lg+"("+medicalShopName+")");
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            context.startActivity(intent);
                        }
                    });


                    myContactPersonname.setText(ContactPerson.getText().toString());
                    myshopname.setText(Shopname.getText().toString());
                    myshopphonenum.setText(medicalphonenum.getText().toString());
                    myshopphonenum.setTextColor(context.getResources().getColor(R.color.sidenavtext));

                    myaddressId = addressId.getText().toString();

                    System.out.println("shop name.."+Shopname.getText().toString());
                    System.out.println("addres id.."+addressId.getText().toString());

                    System.out.println("phone.."+medicalphonenum.getText().toString());
//                    Intent intent = new Intent(context,ViewMedicalShop.class);
//                    intent.putExtra("userId",patientId);
//                    intent.putExtra("userMobile",patientMobileNumber);
//                    intent.putExtra("shopName",Shopname.getText().toString());
//                    intent.putExtra("addressId",addressId.getText().toString());
//                    intent.putExtra("medicalID",medicalShopeID.getText().toString());
//                    intent.putExtra("lat",lati.getText().toString());
//                    intent.putExtra("long",longi.getText().toString());
//                    intent.putExtra("image",centerImage.getText().toString());
//                    intent.putExtra("mobile",medicalphonenum.getText().toString());
//                    intent.putExtra("ContactPerson",ContactPerson.getText().toString());
//                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_get_medicalshops_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.usermobile.setText(medicalClassList.get(i).getUsermobileNumber());
        viewHolder.Shopname.setText(medicalClassList.get(i).getShopName());
        viewHolder.ContactPerson.setText(medicalClassList.get(i).getContactPerson());

        viewHolder.medicalphonenum.setText(medicalClassList.get(i).getMobile());
        viewHolder.addressId.setText(medicalClassList.get(i).getAddressId());
        viewHolder.medicalShopeID.setText(medicalClassList.get(i).getDoctorId());
        viewHolder.lati.setText(medicalClassList.get(i).getLatitude());
        viewHolder.longi.setText(medicalClassList.get(i).getLongitude());
        viewHolder.centerImage.setText(medicalClassList.get(i).getMedicImage());
        viewHolder.distance.setText(medicalClassList.get(i).getDistance());

        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+medicalClassList.get(i).getMedicImage());

        patientMobileNumber = medicalClassList.get(i).getUsermobileNumber();

        patientId = medicalClassList.get(i).getPatientId();

    }

    @Override
    public int getItemCount() {
        return medicalClassList.size();
    }


    private class GetProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetProfileImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
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
            bmImage.setImageBitmap(result);
//            progressDialog.dismiss();
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
                String shopname = myshopname.getText().toString();
                String ContactPersonname = myContactPersonname.getText().toString();
                String shopphonenum = myshopphonenum.getText().toString();
                String area = mydoornum.getText().toString();
                String link="https://www.medictfhc.com";

                String message="Medical store details: -"+shopname+", "+"Contact Details: "+ContactPersonname+"-"+shopphonenum+", "+"Address: "+area+". Click here for navigate: "+link+". \n"+"Regards MEDIC TFHC.";
                String mainUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = patientMobileNumber;
                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(mainUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);
                mainUrl=stringBuilder.toString();
                System.out.println("smsUrl..."+mainUrl);
                myURL=new URL(mainUrl);
                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            } catch (IOException e) {
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
        MyDialog1  = new Dialog(context);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.sms_alertdialog);

        message = (TextView) MyDialog1.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog1.findViewById(R.id.ok);

//        MyDialog1.setTitle("Your Diagnostic Appointment");

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText("Successfully sent to your registered mobile number");

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Mytask().execute();
                MyDialog1.cancel();
            }
        });
        MyDialog1.show();

    }


    //Get all addresses for doctor list from api call
    private class GetMeidcalAllAddressDetails extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            progressDialog = new ProgressDialog(context);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();
//        }

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
            Log.e("Api response.....", result);

            getAllAddress(result);
//            progressDialog.dismiss();
        }
    }

    private void getAllAddress(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String myaddressId = object.getString("AddressID");
                System.out.println("addrid.."+myaddressId);

                if(myaddressId.equals(myaddressId))
                {
                    System.out.println("if cond");
                    String myaddress = object.getString("Address1");
                    String  myShopName = object.getString("ShopName");
                    String mystateName = object.getString("StateName");

                    String mycityName = object.getString("CityName");

                    //  hospitalname.setText(myShopName);
                    mydoornum.setText(myaddress);
                    mycity.setText(mycityName);
                    mystate.setText(mystateName);

                    break;
                }
//                else {
//                    continue;
//                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
