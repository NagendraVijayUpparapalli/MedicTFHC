package com.example.cool.patient;

/**
 * Created by Udayasri on 10-06-2018.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

public class MedicalShopListAdapter11 extends RecyclerView.Adapter<MedicalShopListAdapter11.ViewHolder>{

        List<MedicalShopClass> medicalClassList;
        Context context;
        AlertDialog alertDialog1;
        String address,consultationFee,comments,emergencyContact;
        String patientId;
        boolean emergencyService;

        String doctorname,hospitalname,doornum,city,state,payment,mobile,navigaton,patientMobileNumber;

        ApiBaseUrl baseUrl = new ApiBaseUrl();


public MedicalShopListAdapter11(GetCurrentMedicalShopsList11 getCurrentMedicalList,List<MedicalShopClass> medicalClassList) {
        this.context = getCurrentMedicalList;
        this.medicalClassList = medicalClassList;
        }

class ViewHolder extends RecyclerView.ViewHolder{

    public int currentItem;
    public ImageView profileImage;
    public TextView ContactPerson,Shopname,qualification,speciality,fee,medicalphonenum,addressId,medicalShopeID,userId,centerImage,lati,longi;



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
        centerImage=(TextView) itemView.findViewById(R.id.image);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewMedicalShop.class);
                intent.putExtra("userId",patientId);
                intent.putExtra("userMobile",patientMobileNumber);
                intent.putExtra("shopName",Shopname.getText().toString());
                intent.putExtra("addressId",addressId.getText().toString());
                intent.putExtra("medicalID",medicalShopeID.getText().toString());
                intent.putExtra("lat",lati.getText().toString());
                intent.putExtra("long",longi.getText().toString());
                intent.putExtra("image",centerImage.getText().toString());
                intent.putExtra("mobile",medicalphonenum.getText().toString());
                intent.putExtra("ContactPerson",ContactPerson.getText().toString());
                context.startActivity(intent);
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
        viewHolder.Shopname.setText(medicalClassList.get(i).getShopName());
        viewHolder.ContactPerson.setText(medicalClassList.get(i).getContactPerson());

        viewHolder.medicalphonenum.setText(medicalClassList.get(i).getMobile());
        viewHolder.addressId.setText(medicalClassList.get(i).getAddressId());
        viewHolder.medicalShopeID.setText(medicalClassList.get(i).getDoctorId());
        viewHolder.lati.setText(medicalClassList.get(i).getLatitude());
        viewHolder.longi.setText(medicalClassList.get(i).getLongitude());
        viewHolder.centerImage.setText(medicalClassList.get(i).getMedicImage());


        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+medicalClassList.get(i).getMedicImage());



        patientMobileNumber = medicalClassList.get(i).getUsermobileNumber();

        patientId = medicalClassList.get(i).getPatientId();

    }

    @Override
    public int getItemCount() {
        return medicalClassList.size();
    }



//    public void checkRegisteredUserOrNotAlert()
//    {
//        AlertDialog.Builder alert = new AlertDialog.Builder(context);
//        alert.setTitle("Do you want to take Appointment for Register user?");
//        //  alert.show();
//        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
//                intent.putExtra("doctorName",);
//                context.startActivity(intent);
//            }
//        });
//        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
//                context.startActivity(intent);
//            }
//        });
//        alert.setCancelable(false);
//        alertDialog1 = alert.create();
//        alertDialog1.setCanceledOnTouchOutside(false);
//        alert.show();
//    }

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
    }

}
}
