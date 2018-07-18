package com.example.cool.patient.doctor.ManageAddress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Udayasri on 07-05-2018.
 */

public class DoctorManageAddressAdapter extends RecyclerView.Adapter<DoctorManageAddressAdapter.ViewHolder>{

    List<DoctorManageAddressClass> doctorManageAddressClassArrayList;
    Context context;
//    String lati,longi,consultationFee,comments,emergencyContact,addressId,district;
    boolean myemergencyService = false;

    ProgressDialog progressDialog;

    ApiBaseUrl baseUrl = new ApiBaseUrl();

    String docId,addId,comment;
    int index = 0;

    EditText reason_Todelete;
    String reasonToDelete = null,phone;

    ProgressDialog mProgressDialog;


//    EditText reason_Todelete;

    public DoctorManageAddressAdapter(DoctorManageAddress doctorManageAddress,List<DoctorManageAddressClass> manageAddressClassList) {
        this.context = doctorManageAddress;
        this.doctorManageAddressClassArrayList = manageAddressClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;

        public ImageView profileImage;
        public TextView hospitalName,address,city,state,pincode,district,mobile,contactPerson,regMobile,
                consultationFee,emergencyContact,lati,longi,comments,emergencyService,addressId;

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
            regMobile = (TextView) itemView.findViewById(R.id.regMobile);

            edit = (Button) itemView.findViewById(R.id.Edit);
            inactive = (Button) itemView.findViewById(R.id.InActive);

//            consultationFee,emergencyContact,lati,longi,comments,emergencyService,addressId;

            consultationFee = (TextView)itemView.findViewById(R.id.consultationFee);
            emergencyContact = (TextView)itemView.findViewById(R.id.emergencyContact);
            lati = (TextView)itemView.findViewById(R.id.lati);
            longi = (TextView)itemView.findViewById(R.id.longi);
            comments = (TextView)itemView.findViewById(R.id.comments);
            emergencyService = (TextView)itemView.findViewById(R.id.emergencyService);
            addressId = (TextView)itemView.findViewById(R.id.addressId);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),DoctorUpdateAddress.class);
                    intent.putExtra("addressId",addressId.getText().toString());
                    intent.putExtra("hospitalName",hospitalName.getText().toString());
                    intent.putExtra("address",address.getText().toString());
                    intent.putExtra("city",city.getText().toString());
                    intent.putExtra("pincode",pincode.getText().toString());
                    intent.putExtra("district",district.getText().toString());
                    intent.putExtra("state",state.getText().toString());
                    intent.putExtra("contactName",contactPerson.getText().toString());
                    intent.putExtra("mobile",mobile.getText().toString());

//                    consultationFee,emergencyContact,lati,longi,comments,emergencyService,addressId

                    intent.putExtra("fee",consultationFee.getText().toString());
                    intent.putExtra("emergencyContact",emergencyContact.getText().toString());

                    intent.putExtra("id",docId);
                    intent.putExtra("lati",lati.getText().toString());
                    intent.putExtra("longi",longi.getText().toString());
                    intent.putExtra("comments",comments.getText().toString());
                    intent.putExtra("emergencyService",myemergencyService);
                    intent.putExtra("regMobile",regMobile.getText().toString());

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
        viewHolder.regMobile.setText(doctorManageAddressClassArrayList.get(i).getRegisteredMobileNumber());

    //                    consultationFee,emergencyContact,lati,longi,comments,emergencyService,addressId

        viewHolder.consultationFee.setText(doctorManageAddressClassArrayList.get(i).getConsultationFee());
        viewHolder.emergencyContact.setText(doctorManageAddressClassArrayList.get(i).getEmergencyContactNumber());
        viewHolder.lati.setText(doctorManageAddressClassArrayList.get(i).getLatitude());
        viewHolder.longi.setText(doctorManageAddressClassArrayList.get(i).getLongitude());
        viewHolder.comments.setText(doctorManageAddressClassArrayList.get(i).getComment());
//        viewHolder.emergencyService.setText(doctorManageAddressClassArrayList.get(i).getEmergencyservice());
        viewHolder.addressId.setText(doctorManageAddressClassArrayList.get(i).getAddressId());

        myemergencyService = doctorManageAddressClassArrayList.get(i).getEmergencyservice();

        final int pos = i;

        reasonToDelete = reason_Todelete.getText().toString();
        phone = viewHolder.regMobile.getText().toString();
        docId = doctorManageAddressClassArrayList.get(pos).getDoctorId();
        addId = doctorManageAddressClassArrayList.get(pos).getAddressId();
        comment = doctorManageAddressClassArrayList.get(pos).getDeleteReason();



        System.out.println("doc id in manage adapter.."+docId);


        System.out.println("size.."+doctorManageAddressClassArrayList.size());

        viewHolder.inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("size.."+doctorManageAddressClassArrayList.size());

                    deleteItem(pos);
                    Intent intent = new Intent(context,DoctorDeleteAddress.class);
                    intent.putExtra("DoctorID",docId);
                    intent.putExtra("AddressID",addId);
                    intent.putExtra("Comment",comment);
                    intent.putExtra("mobile",phone);

                    context.startActivity(intent);

                 index = pos;
            }
        });


//        viewHolder.mobile.setText(doctorManageAddressClassArrayList.get(i).getLandLineNo());

        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+doctorManageAddressClassArrayList.get(i).getProfileImage());


//        district = doctorManageAddressClassArrayList.get(i).getDistrict();
//        addressId = doctorManageAddressClassArrayList.get(i).getAddressId();
//
//        lati = doctorManageAddressClassArrayList.get(i).getLatitude();
//        longi= doctorManageAddressClassArrayList.get(i).getLongitude();
//
//        consultationFee = doctorManageAddressClassArrayList.get(i).getConsultationFee();
//        emergencyContact = doctorManageAddressClassArrayList.get(i).getEmergencyContactNumber();
//
//        comments = doctorManageAddressClassArrayList.get(i).getComment();
//        emergencyService = doctorManageAddressClassArrayList.get(i).getEmergencyservice();


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

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(context);
//            // Set progressdialog title
////            mProgressDialog.setTitle("Image");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Loading.hello..");
//
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog
//            mProgressDialog.show();
//        }

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

//            mProgressDialog.dismiss();
            bmImage.setImageBitmap(result);
        }

    }

}
