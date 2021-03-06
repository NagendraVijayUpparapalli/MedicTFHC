package com.example.cool.patient.patient.ViewDoctorsList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.patient.BookAppointment.PatientBookAppointmentToDoctor;
import com.example.cool.patient.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Udayasri on 12-05-2018.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder>{

        List<DoctorClass> doctorClassList;
        Context context;
        AlertDialog alertDialog1;
        TextView closeIcon;
        Dialog MyDialog;
        String selectedCity,lati,longi,myClass,consultationFee,comments,emergencyContact;
        int selectedRange;
        String patientId;
        boolean emergencyService;

        ImageView  newUser,oldUser;

    String doctorname,hospitalname,doornum,city,state,payment,mobile,navigaton;

        ApiBaseUrl baseUrl = new ApiBaseUrl();


//public DoctorListAdapter(AdapterView.OnItemSelectedListener getCurrentDoctorsList, List<DoctorClass> doctorClassList) {
//        this.context = getCurrentDoctorsList;
//        this.doctorClassList = doctorClassList;
//        }

    public DoctorListAdapter(GetCurrentDoctorsList getCurrentDoctorsList,List<DoctorClass> doctorClassList) {
        this.context = getCurrentDoctorsList;
        this.doctorClassList = doctorClassList;
        myClass = "list";
    }

    public DoctorListAdapter(GetCurrentDoctorsList11 getCurrentDoctorsList,List<DoctorClass> doctorClassList) {
        this.context = getCurrentDoctorsList;
        this.doctorClassList = doctorClassList;
        myClass = "list11";
    }

class ViewHolder extends RecyclerView.ViewHolder{

    public int currentItem;
    public ImageView profileImage;
    public TextView docName,doctorName,qualification,speciality,fee,doctorphonenum,addressId,doctorId,userId,distance;



    RecyclerView recyclerView;

    public Button edit,inactive;


    public ViewHolder(final View itemView) {
        super(itemView);
//            itemImage = (ImageView)itemView.findViewById(R.id.image);

        doctorName = (TextView)itemView.findViewById(R.id.doctorName);
        docName = (TextView)itemView.findViewById(R.id.docName);
        qualification=(TextView)itemView.findViewById(R.id.qualification) ;
        speciality = (TextView)itemView.findViewById(R.id.speciality);
        fee=(TextView)itemView.findViewById(R.id.consultationFee) ;
        doctorphonenum=(TextView) itemView.findViewById(R.id.phonenum);
        addressId=(TextView) itemView.findViewById(R.id.addressId);
        doctorId=(TextView) itemView.findViewById(R.id.doctorId);
        userId=(TextView) itemView.findViewById(R.id.userid);
        distance  = (TextView) itemView.findViewById(R.id.distance);

        profileImage = (ImageView) itemView.findViewById(R.id.docImage);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDialog  = new Dialog(context);
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.custom_popup);

                closeIcon = (TextView) MyDialog.findViewById(R.id.closeIcon);
                oldUser = (ImageView)MyDialog.findViewById(R.id.oldUser);
                newUser = (ImageView)MyDialog.findViewById(R.id.newUser);

                oldUser.setEnabled(true);
                newUser.setEnabled(true);

                closeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MyDialog.cancel();
//                        Intent intent = new Intent(context,GetCurrentDoctorsList.class);
//                        intent.putExtra("userId",userId.getText().toString());
//                        intent.putExtra("mobile",doctorphonenum.getText().toString());// change to patient mobile
//                        context.startActivity(intent);
                    }
                });

                oldUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myClass.equals("list"))
                        {
                            Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
                            intent.putExtra("user","Yes");
                            intent.putExtra("userId",userId.getText().toString());
                            intent.putExtra("doctorName",docName.getText().toString());
                            intent.putExtra("addressId",addressId.getText().toString());
                            intent.putExtra("doctorId",doctorId.getText().toString());
                            intent.putExtra("lat",lati);
                            intent.putExtra("long",longi);
                            intent.putExtra("myClass","list");
                            intent.putExtra("selectedCity",selectedCity);
                            intent.putExtra("range",selectedRange);
                            intent.putExtra("mobile",doctorphonenum.getText().toString());
                            intent.putExtra("fee",fee.getText().toString());
                            context.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
                            intent.putExtra("user","Yes");
                            intent.putExtra("userId",userId.getText().toString());
                            intent.putExtra("doctorName",docName.getText().toString());
                            intent.putExtra("addressId",addressId.getText().toString());
                            intent.putExtra("doctorId",doctorId.getText().toString());
                            intent.putExtra("lat",lati);
                            intent.putExtra("long",longi);
                            intent.putExtra("myClass","list11");
                            intent.putExtra("selectedCity",selectedCity);
                            intent.putExtra("range",selectedRange);
                            intent.putExtra("mobile",doctorphonenum.getText().toString());
                            intent.putExtra("fee",fee.getText().toString());
                            context.startActivity(intent);
                        }
                    }
                });

                newUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myClass.equals("list"))
                        {
                            Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
                            intent.putExtra("user","No");
                            intent.putExtra("userId",userId.getText().toString());
                            intent.putExtra("doctorName",docName.getText().toString());
                            intent.putExtra("addressId",addressId.getText().toString());
                            intent.putExtra("doctorId",doctorId.getText().toString());
                            intent.putExtra("lat",lati);
                            intent.putExtra("long",longi);
                            intent.putExtra("myClass","list");
                            intent.putExtra("selectedCity",selectedCity);
                            intent.putExtra("range",selectedRange);
                            intent.putExtra("mobile",doctorphonenum.getText().toString());
                            intent.putExtra("fee",fee.getText().toString());
                            context.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
                            intent.putExtra("user","No");
                            intent.putExtra("userId",userId.getText().toString());
                            intent.putExtra("doctorName",docName.getText().toString());
                            intent.putExtra("addressId",addressId.getText().toString());
                            intent.putExtra("doctorId",doctorId.getText().toString());
                            intent.putExtra("lat",lati);
                            intent.putExtra("long",longi);
                            intent.putExtra("myClass","list11");
                            intent.putExtra("selectedCity",selectedCity);
                            intent.putExtra("range",selectedRange);
                            intent.putExtra("mobile",doctorphonenum.getText().toString());
                            intent.putExtra("fee",fee.getText().toString());
                            context.startActivity(intent);
                        }
                    }
                });

                MyDialog.show();

//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setTitle("Do you want to take Appointment for Register user?");
//                //  alert.show();
//                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
//                        intent.putExtra("user","Yes");
//                        intent.putExtra("userId",userId.getText().toString());
//                        intent.putExtra("doctorName",doctorName.getText().toString());
//                        intent.putExtra("addressId",addressId.getText().toString());
//                        intent.putExtra("doctorId",doctorId.getText().toString());
//                        intent.putExtra("lat",lati);
//                        intent.putExtra("long",longi);
//                        intent.putExtra("mobile",doctorphonenum.getText().toString());
//                        intent.putExtra("fee",fee.getText().toString());
//                        context.startActivity(intent);
//                    }
//                });
//                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context,PatientBookAppointmentToDoctor.class);
//                        intent.putExtra("user","No");
//                        intent.putExtra("userId",userId.getText().toString());
//                        intent.putExtra("doctorName",doctorName.getText().toString());
//                        intent.putExtra("addressId",addressId.getText().toString());
//                        intent.putExtra("doctorId",doctorId.getText().toString());
//                        intent.putExtra("lat",lati);
//                        intent.putExtra("long",longi);
//                        intent.putExtra("mobile",doctorphonenum.getText().toString());
//                        intent.putExtra("fee",fee.getText().toString());
//                        context.startActivity(intent);
//                    }
//                });
//                alert.setCancelable(false);
//                alertDialog1 = alert.create();
//                alertDialog1.setCanceledOnTouchOutside(false);
//                alert.show();
            }
        });

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
//        viewHolder.doctorName.setText("Dr. "+doctorClassList.get(i).getName());
        String docName = "Dr. "+doctorClassList.get(i).getName();
        int docNameLength = docName.length();

        String name_qualification = "Dr. "+doctorClassList.get(i).getName()+" "+doctorClassList.get(i).getQualification();
//        String arr[] = name_qualification.split(" ");
        SpannableString spannableString = new SpannableString(name_qualification);
        spannableString.setSpan(new RelativeSizeSpan(1.35f),0,docNameLength,0);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.docName)),0,docNameLength,0);
        viewHolder.doctorName.setText(spannableString);

//        viewHolder.doctorName.setText(Html.fromHtml("Dr. "+doctorClassList.get(i).getName()+"<sub>"+doctorClassList.get(i).getQualification()+"</sup>"));
//        text.setText(Html.fromHtml("5x<sup>2</sup>"));
        viewHolder.qualification.setText(doctorClassList.get(i).getQualification());
        viewHolder.speciality.setText(doctorClassList.get(i).getSpecialityName());
        viewHolder.fee.setText(doctorClassList.get(i).getConsultationFee());
        viewHolder.doctorphonenum.setText(doctorClassList.get(i).getMobile());
        viewHolder.addressId.setText(doctorClassList.get(i).getAddressId());
        viewHolder.doctorId.setText(doctorClassList.get(i).getDoctorId());
        viewHolder.userId.setText(doctorClassList.get(i).getPatientId());
        viewHolder.distance.setText(doctorClassList.get(i).getDistance());
        viewHolder.docName.setText("Dr. "+doctorClassList.get(i).getName());


        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+doctorClassList.get(i).getDoctorImage());


        lati = doctorClassList.get(i).getLatitude();
        longi= doctorClassList.get(i).getLongitude();
        selectedCity = doctorClassList.get(i).getSelectedCity();
        selectedRange = doctorClassList.get(i).getMyRangeDistance();

//        patientId = doctorClassList.get(i).getPatientId();

    }

    @Override
    public int getItemCount() {
        return doctorClassList.size();
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