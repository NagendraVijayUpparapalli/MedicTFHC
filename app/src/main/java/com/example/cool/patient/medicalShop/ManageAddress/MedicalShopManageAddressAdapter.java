package com.example.cool.patient.medicalShop.ManageAddress;

/**
 * Created by Udayasri on 01-06-2018.
 */

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
import com.example.cool.patient.medicalShop.MedicalShopDashboard;

import java.io.InputStream;
import java.util.List;


public class MedicalShopManageAddressAdapter extends RecyclerView.Adapter<MedicalShopManageAddressAdapter.ViewHolder>{

    List<MedicalShopManageAddressClass> medicalManageAddressClassArrayList;
    Context context;
    String lati,longi,consultationFee,comments,emergencyContact,addressId,pharmacy,FromTime,ToTime,Experience,pharmacyId;
    boolean emergencyService;

    ProgressDialog progressDialog;

    ApiBaseUrl baseUrl = new ApiBaseUrl();

    String docId,addId,comment;
    int index = 0;
    EditText reason_Todelete;
    String reasonToDelete = null,phone;

    ProgressDialog mProgressDialog;


//    EditText reason_Todelete;

    public MedicalShopManageAddressAdapter(MedicalShopManageAddress medicalManageAddress, List<MedicalShopManageAddressClass> manageAddressClassList) {
        this.context = medicalManageAddress;
        this.medicalManageAddressClassArrayList = manageAddressClassList;
    }

    public MedicalShopManageAddressAdapter(MedicalShopDashboard medicalManageAddress, List<MedicalShopManageAddressClass> manageAddressClassList) {
        this.context = medicalManageAddress;
        this.medicalManageAddressClassArrayList = manageAddressClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView profileImage;
        public TextView medicalshopId,landlineNo,Emergency_contect,Experience,FromTime,ToTime,AddressID,hospitalName,
                address,city,state,pincode,district,mobile,contactPerson,Pharmacytype,latitude,longitude,regMobile;


        public Button edit,inactive;


        public ViewHolder(final View itemView) {
            super(itemView);
//            itemImage = (ImageView)itemView.findViewById(R.id.image);
            AddressID = (TextView)itemView.findViewById(R.id.AddressID);
            medicalshopId = (TextView)itemView.findViewById(R.id.medicalshopId);
            latitude = (TextView)itemView.findViewById(R.id.lati);
            longitude =(TextView)itemView.findViewById(R.id.longi);
            Experience = (TextView)itemView.findViewById(R.id.Experience);
            hospitalName = (TextView)itemView.findViewById(R.id.Hospital_Name);
            address=(TextView)itemView.findViewById(R.id.Address) ;
            city = (TextView)itemView.findViewById(R.id.city);
            landlineNo = (TextView)itemView.findViewById(R.id.landlineNo);
            Emergency_contect = (TextView)itemView.findViewById(R.id.emergencyContact);
            pincode=(TextView)itemView.findViewById(R.id.pincode) ;
            district = (TextView)itemView.findViewById(R.id.district);
            FromTime = (TextView)itemView.findViewById(R.id.FromTime);
            ToTime = (TextView)itemView.findViewById(R.id.ToTime);
            state = (TextView)itemView.findViewById(R.id.state);
            Pharmacytype =(TextView)itemView.findViewById(R.id.Pharmacytype);
            contactPerson = (TextView)itemView.findViewById(R.id.contact_person_name);
            mobile = (TextView)itemView.findViewById(R.id.phone_number);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            reason_Todelete = (EditText) itemView.findViewById(R.id.reason_delete);
            edit = (Button) itemView.findViewById(R.id.Edit);
            inactive = (Button) itemView.findViewById(R.id.InActive);
            regMobile = (TextView) itemView.findViewById(R.id.regMobile);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),MedicalShopUpdateAddress.class);

                    intent.putExtra("medicalId",medicalshopId.getText().toString());
                    intent.putExtra("addressId",AddressID.getText().toString());
                    intent.putExtra("pharmacyId",Pharmacytype.getText().toString());

                    intent.putExtra("diagName",hospitalName.getText().toString());
                    intent.putExtra("address",address.getText().toString());
                    intent.putExtra("Lindlineno",landlineNo.getText().toString());
                    intent.putExtra("city",city.getText().toString());
                    intent.putExtra("pincode",pincode.getText().toString());
                    intent.putExtra("district",district.getText().toString());
                    intent.putExtra("state",state.getText().toString());
                    intent.putExtra("emergencyContact",Emergency_contect.getText().toString());
                    //intent.putExtra("pharamncy",)
                    intent.putExtra("contactName",contactPerson.getText().toString());
                    intent.putExtra("mobile",mobile.getText().toString());
                    //   intent.putExtra("fee",consultationFee);
                    //intent.putExtra("emergencyContact",emergencyContact);
                    intent.putExtra("lati",latitude.getText().toString());
                    intent.putExtra("longi",longitude.getText().toString());
                    intent.putExtra("Experience",Experience.getText().toString());
                    intent.putExtra("comments",comments);
                    intent.putExtra("FromTime",FromTime.getText().toString());
                    intent.putExtra("ToTime",ToTime.getText().toString());
                    intent.putExtra("emergencyService",emergencyService);
                    intent.putExtra("regMobile",regMobile.getText().toString());

                    System.out.println("emergency service.."+emergencyService);

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
                .inflate(R.layout.row_medical_shop_manage_address, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.hospitalName.setText(medicalManageAddressClassArrayList.get(i).getHospitalName());
        viewHolder.address.setText(medicalManageAddressClassArrayList.get(i).getAddress1());
        viewHolder.mobile.setText(medicalManageAddressClassArrayList.get(i).getLandLineNo());
        viewHolder.city.setText(medicalManageAddressClassArrayList.get(i).getCityName());
        viewHolder.pincode.setText(medicalManageAddressClassArrayList.get(i).getZipcode());
        viewHolder.district.setText(medicalManageAddressClassArrayList.get(i).getDistrict());
        viewHolder.Emergency_contect.setText(medicalManageAddressClassArrayList.get(i).getEmergencyContactNumber());
        viewHolder.state.setText(medicalManageAddressClassArrayList.get(i).getStateName());
        viewHolder.contactPerson.setText(medicalManageAddressClassArrayList.get(i).getContactPerson());
        viewHolder.mobile.setText(medicalManageAddressClassArrayList.get(i).getLandLineNo());
        viewHolder.AddressID.setText(medicalManageAddressClassArrayList.get(i).getAddressId());
        viewHolder.Pharmacytype.setText(medicalManageAddressClassArrayList.get(i).getPharmacyKey());
        viewHolder.longitude.setText(medicalManageAddressClassArrayList.get(i).getLongitude());
        viewHolder.latitude.setText(medicalManageAddressClassArrayList.get(i).getLatitude());
        viewHolder.FromTime.setText(medicalManageAddressClassArrayList.get(i).getFrom());
        viewHolder.ToTime.setText(medicalManageAddressClassArrayList.get(i).getTo());
        viewHolder.Experience.setText(medicalManageAddressClassArrayList.get(i).getExperience());
        viewHolder.landlineNo.setText(medicalManageAddressClassArrayList.get(i).getLandLineNo());
        viewHolder.medicalshopId.setText(medicalManageAddressClassArrayList.get(i).getMedicalshopID());


        reasonToDelete = reason_Todelete.getText().toString();
        phone = viewHolder.regMobile.getText().toString();



        final int pos = i;
        System.out.println("size.."+medicalManageAddressClassArrayList.size());

        viewHolder.inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docId = medicalManageAddressClassArrayList.get(pos).getMedicalshopID();
                addId = medicalManageAddressClassArrayList.get(pos).getAddressId();
                comment = medicalManageAddressClassArrayList.get(pos).getDeleteReason();

                System.out.println("size.."+medicalManageAddressClassArrayList.size());

                    deleteItem(pos);
                Intent intent = new Intent(context,MedicalShopDeleteAddress.class);
                intent.putExtra("MedicalShopID",docId);
                intent.putExtra("AddressID",addId);
                intent.putExtra("Comment",comment);
                intent.putExtra("mobile",phone);

                context.startActivity(intent);

//                    String js = formatDataAsJson();
//                    new sendDetailsToDeleteDoctorAddress().execute(baseUrl.getUrl()+"DeleteDoctorAddress?DoctorID="+docId+"&AddressID="+addId+"&Comment="+comment);
                index = pos;
            }
        });

//        viewHolder.mobile.setText(medicalManageAddressClassArrayList.get(i).getLandLineNo());

        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+medicalManageAddressClassArrayList.get(i).getProfileImage());

        comments = medicalManageAddressClassArrayList.get(i).getComment();
        emergencyService = medicalManageAddressClassArrayList.get(i).getEmergencyservice();

        System.out.println("emergency service in medical manage.."+emergencyService);

    }

    void deleteItem(int index) {
        medicalManageAddressClassArrayList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return medicalManageAddressClassArrayList.size();
    }

    private class GetProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
//            mProgressDialog.setTitle("Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");

            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

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
            mProgressDialog.dismiss();
            bmImage.setImageBitmap(result);
        }
    }
}
