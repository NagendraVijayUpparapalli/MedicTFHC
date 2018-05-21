package com.example.cool.patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Udayasri on 07-05-2018.
 */

public class DoctorManageAddressAdapter extends RecyclerView.Adapter<DoctorManageAddressAdapter.ViewHolder>{

    List<DoctorManageAddressClass> doctorManageAddressClassArrayList;
    Context context;
    String lati,longi,consultationFee,comments,emergencyContact,addressId;
    boolean emergencyService;

    ProgressDialog progressDialog;

    ApiBaseUrl baseUrl = new ApiBaseUrl();

    String docId,addId,comment;
    int index = 0;

    EditText reason_Todelete;
    String reasonToDelete = null;


//    EditText reason_Todelete;

    public DoctorManageAddressAdapter(DoctorManageAddress doctorManageAddress,List<DoctorManageAddressClass> manageAddressClassList) {
        this.context = doctorManageAddress;
        this.doctorManageAddressClassArrayList = manageAddressClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView profileImage;
        public TextView hospitalName,address,city,state,pincode,district,mobile,contactPerson;


        public Button edit,inactive;


        public ViewHolder(final View itemView) {
            super(itemView);
//            itemImage = (ImageView)itemView.findViewById(R.id.image);
            hospitalName = (TextView)itemView.findViewById(R.id.Hospital_Name);
            address=(TextView)itemView.findViewById(R.id.Address) ;
            city = (TextView)itemView.findViewById(R.id.city);
            pincode=(TextView)itemView.findViewById(R.id.pincode) ;
            district = (TextView)itemView.findViewById(R.id.district);
            state = (TextView)itemView.findViewById(R.id.state);
            contactPerson = (TextView)itemView.findViewById(R.id.contact_person_name);
            mobile = (TextView)itemView.findViewById(R.id.phone_number);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);

            reason_Todelete = (EditText) itemView.findViewById(R.id.reason_delete);



            edit = (Button) itemView.findViewById(R.id.Edit);
            inactive = (Button) itemView.findViewById(R.id.InActive);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),DoctorUpdateAddress.class);
                    intent.putExtra("addressId",addressId);
                    intent.putExtra("hospitalName",hospitalName.getText().toString());
                    intent.putExtra("address",address.getText().toString());
                    intent.putExtra("city",city.getText().toString());
                    intent.putExtra("pincode",pincode.getText().toString());
                    intent.putExtra("district",district.getText().toString());
                    intent.putExtra("state",state.getText().toString());
                    intent.putExtra("contactName",contactPerson.getText().toString());
                    intent.putExtra("mobile",mobile.getText().toString());

                    intent.putExtra("fee",consultationFee);
                    intent.putExtra("emergencyContact",emergencyContact);

                    intent.putExtra("lati",lati);
                    intent.putExtra("longi",longi);
                    intent.putExtra("comments",comments);
                    intent.putExtra("emergencyService",emergencyService);

                    itemView.getContext().startActivity(intent);
                }
            });

//            inactive.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(itemView.getContext(),DoctorUpdateAddress.class);
//                    intent.putExtra("DoctorID",addressId);
//                    intent.putExtra("AddressID",addressId);
//                    intent.putExtra("Comment",address.getText().toString());
//
//                    itemView.getContext().startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_doc_manage_address, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.hospitalName.setText(doctorManageAddressClassArrayList.get(i).getHospitalName());
        viewHolder.address.setText(doctorManageAddressClassArrayList.get(i).getAddress1());
        viewHolder.mobile.setText(doctorManageAddressClassArrayList.get(i).getLandLineNo());

        viewHolder.city.setText(doctorManageAddressClassArrayList.get(i).getCityName());
        viewHolder.pincode.setText(doctorManageAddressClassArrayList.get(i).getZipcode());
        viewHolder.district.setText(doctorManageAddressClassArrayList.get(i).getDistrict());
        viewHolder.state.setText(doctorManageAddressClassArrayList.get(i).getStateName());
        viewHolder.contactPerson.setText(doctorManageAddressClassArrayList.get(i).getContactPerson());
        viewHolder.mobile.setText(doctorManageAddressClassArrayList.get(i).getLandLineNo());


        reasonToDelete = reason_Todelete.getText().toString();

        final int pos = i;

        System.out.println("size.."+doctorManageAddressClassArrayList.size());

        viewHolder.inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    docId = doctorManageAddressClassArrayList.get(pos).getDoctorId();
                    addId = doctorManageAddressClassArrayList.get(pos).getAddressId();
                    comment = doctorManageAddressClassArrayList.get(pos).getDeleteReason();

                System.out.println("size.."+doctorManageAddressClassArrayList.size());

//                    deleteItem(pos);
                    Intent intent = new Intent(context,DoctorDeleteAddress.class);
                    intent.putExtra("DoctorID",docId);
                    intent.putExtra("AddressID",addId);
                    intent.putExtra("Comment",comment);

                    context.startActivity(intent);


//                    String js = formatDataAsJson();
//                    new sendDetailsToDeleteDoctorAddress().execute(baseUrl.getUrl()+"DeleteDoctorAddress?DoctorID="+docId+"&AddressID="+addId+"&Comment="+comment);
                    index = pos;
            }
        });


//        viewHolder.mobile.setText(doctorManageAddressClassArrayList.get(i).getLandLineNo());

        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+doctorManageAddressClassArrayList.get(i).getProfileImage());


        addressId = doctorManageAddressClassArrayList.get(i).getAddressId();

        lati = doctorManageAddressClassArrayList.get(i).getLatitude();
        longi= doctorManageAddressClassArrayList.get(i).getLongitude();

        consultationFee = doctorManageAddressClassArrayList.get(i).getConsultationFee();
        emergencyContact = doctorManageAddressClassArrayList.get(i).getEmergencyContactNumber();

        comments = doctorManageAddressClassArrayList.get(i).getComment();
        emergencyService = doctorManageAddressClassArrayList.get(i).getEmergencyservice();


//        viewHolder.itemImage.setImageResource(images[i]);
    }



    void deleteItem(int index) {
        doctorManageAddressClassArrayList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return doctorManageAddressClassArrayList.size();
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
        }

    }

}
