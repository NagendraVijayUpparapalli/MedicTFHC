package com.example.cool.patient.patient.ViewDoctorsList;

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

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.BookAppointment.PatientBookAppointmentToDoctor;
import com.example.cool.patient.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Udayasri on 12-05-2018.
 */

public class DoctorListAdapter11 extends RecyclerView.Adapter<DoctorListAdapter11.ViewHolder>{

    List<DoctorClass> doctorClassList;
    Context context;
    AlertDialog alertDialog1;
    String address,lati,longi,consultationFee,comments,emergencyContact;
    String patientId;
    boolean emergencyService;

    String doctorname,hospitalname,doornum,city,state,payment,mobile,navigaton;

    ApiBaseUrl baseUrl = new ApiBaseUrl();


    public DoctorListAdapter11(GetCurrentDoctorsList11 getCurrentDoctorsList,List<DoctorClass> doctorClassList) {
        this.context = getCurrentDoctorsList;
        this.doctorClassList = doctorClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView profileImage;
        public TextView doctorName,qualification,speciality,fee,doctorphonenum,addressId,doctorId,userId;



        RecyclerView recyclerView;

        public Button edit,inactive;


        public ViewHolder(final View itemView) {
            super(itemView);
//            itemImage = (ImageView)itemView.findViewById(R.id.image);

            doctorName = (TextView)itemView.findViewById(R.id.doctorName);
            qualification=(TextView)itemView.findViewById(R.id.qualification) ;
            speciality = (TextView)itemView.findViewById(R.id.speciality);
            fee=(TextView)itemView.findViewById(R.id.consultationFee) ;
            doctorphonenum=(TextView) itemView.findViewById(R.id.phonenum);
            addressId=(TextView) itemView.findViewById(R.id.addressId);
            doctorId=(TextView) itemView.findViewById(R.id.doctorId);
            userId=(TextView) itemView.findViewById(R.id.userid);

            profileImage = (ImageView) itemView.findViewById(R.id.docImage);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Do you want to take Appointment for Register user?");
                    //  alert.show();
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
                            intent.putExtra("user","Yes");
                            intent.putExtra("userId",patientId);
                            intent.putExtra("doctorName",doctorName.getText().toString());
                            intent.putExtra("addressId",addressId.getText().toString());
                            intent.putExtra("doctorId",doctorId.getText().toString());
                            intent.putExtra("lat",lati);
                            intent.putExtra("long",longi);
                            intent.putExtra("mobile",doctorphonenum.getText().toString());
                            intent.putExtra("fee",fee.getText().toString());
                            context.startActivity(intent);
                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
                            intent.putExtra("user","No");
                            intent.putExtra("doctorName",doctorName.getText().toString());
                            intent.putExtra("addressId",addressId.getText().toString());
                            intent.putExtra("doctorId",doctorId.getText().toString());
                            intent.putExtra("lat",lati);
                            intent.putExtra("long",longi);
                            intent.putExtra("mobile",doctorphonenum.getText().toString());
                            intent.putExtra("fee",fee.getText().toString());
                            context.startActivity(intent);
                        }
                    });
                    alert.setCancelable(false);
                    alertDialog1 = alert.create();
                    alertDialog1.setCanceledOnTouchOutside(false);
                    alert.show();
                }
            });



//        district = (TextView)itemView.findViewById(R.id.district);
//        state = (TextView)itemView.findViewById(R.id.state);
//        contactPerson = (TextView)itemView.findViewById(R.id.contact_person_name);
//        mobile = (TextView)itemView.findViewById(R.id.phone_number);
//        profileImage = (ImageView) itemView.findViewById(R.id.profile_image);


//        edit = (Button) itemView.findViewById(R.id.Edit);

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(itemView.getContext(),DoctorUpdateAddress.class);
//
//                intent.putExtra("addressId",addressId);
//                intent.putExtra("hospitalName",hospitalName.getText().toString());
//                intent.putExtra("address",address.getText().toString());
//                intent.putExtra("city",city.getText().toString());
//                intent.putExtra("pincode",pincode.getText().toString());
//                intent.putExtra("district",district.getText().toString());
//                intent.putExtra("state",state.getText().toString());
//                intent.putExtra("contactName",contactPerson.getText().toString());
//                intent.putExtra("mobile",mobile.getText().toString());
//
//                intent.putExtra("fee",consultationFee);
//                intent.putExtra("emergencyContact",emergencyContact);
//
//                intent.putExtra("lati",lati);
//                intent.putExtra("longi",longi);
//                intent.putExtra("comments",comments);
//                intent.putExtra("emergencyService",emergencyService);
//
//                itemView.getContext().startActivity(intent);
//            }
//        });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//
//                    int position = getAdapterPosition();
//                    itemView.getContext().startActivity(new Intent(v.getContext(),Registration.class));
//
////                    Snackbar.make(v, "Click detected on item " + position,
////                            Snackbar.LENGTH_LONG)
////                            .setAction("Action", null).show();
//
//                }
//            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_get_doctors_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.doctorName.setText(doctorClassList.get(i).getName());
        viewHolder.qualification.setText(doctorClassList.get(i).getQualification());
        viewHolder.speciality.setText(doctorClassList.get(i).getSpecialityName());

        viewHolder.fee.setText(doctorClassList.get(i).getConsultationFee());
        viewHolder.doctorphonenum.setText(doctorClassList.get(i).getMobile());
        viewHolder.addressId.setText(doctorClassList.get(i).getAddressId());
        viewHolder.doctorId.setText(doctorClassList.get(i).getDoctorId());
//        viewHolder.userId.setText(doctorClassList.get(i).getPatientId());


        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+doctorClassList.get(i).getDoctorImage());


        lati = doctorClassList.get(i).getLatitude();
        longi= doctorClassList.get(i).getLongitude();

        patientId = doctorClassList.get(i).getPatientId();

    }

    @Override
    public int getItemCount() {
        return doctorClassList.size();
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