package com.example.cool.patient.patient.ViewDiagnosticsList;

import android.content.Context;
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

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.BookAppointment.PatientBookAppointmentToDiagnostics;
import com.example.cool.patient.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Udayasri on 05-06-2018.
 */


public class DiagnosticsListAdapter11 extends RecyclerView.Adapter<DiagnosticsListAdapter11.ViewHolder>{

    List<DiagnosticsClass> diagnosticsClassList;
    Context context;
    AlertDialog alertDialog1;
    String address,lati,longi,consultationFee,comments,emergencyContact;
    int patientId;
    boolean emergencyService;

    String doctorname,hospitalname,doornum,city,state,payment,mobile,navigaton;

    ApiBaseUrl baseUrl = new ApiBaseUrl();


    public DiagnosticsListAdapter11(GetCurrentDiagnosticsList11 getCurrentDiagnosticsList,List<DiagnosticsClass> diagnosticsClassList) {
        this.context = getCurrentDiagnosticsList;
        this.diagnosticsClassList = diagnosticsClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView profileImage;
        public TextView diagName,qualification,speciality,fee,addressId,diagId,userId,centerImage,contactPerson,mobileNumber;

        RecyclerView recyclerView;

        public Button edit,inactive;


        public ViewHolder(final View itemView) {
            super(itemView);
//            itemImage = (ImageView)itemView.findViewById(R.id.image);

            diagName = (TextView)itemView.findViewById(R.id.diagName);
            addressId=(TextView) itemView.findViewById(R.id.addressId);
            diagId=(TextView) itemView.findViewById(R.id.diagid);
            userId=(TextView) itemView.findViewById(R.id.userid);

            contactPerson = (TextView) itemView.findViewById(R.id.contactPerson);
            mobileNumber = (TextView) itemView.findViewById(R.id.mobileNumber);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,PatientBookAppointmentToDiagnostics.class);
                    intent.putExtra("patientId",userId.getText().toString());
                    intent.putExtra("diagid",diagId.getText().toString());
                    intent.putExtra("centerName",diagName.getText().toString());
                    intent.putExtra("addressId",addressId.getText().toString());
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_get_diagnostics_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.diagName.setText(diagnosticsClassList.get(i).getCenterName());
        viewHolder.addressId.setText(diagnosticsClassList.get(i).getAddressId());
        viewHolder.diagId.setText(diagnosticsClassList.get(i).getDiagId());
        viewHolder.userId.setText(diagnosticsClassList.get(i).getUserId());
        viewHolder.contactPerson.setText(diagnosticsClassList.get(i).getContactPerson());
        viewHolder.mobileNumber.setText(diagnosticsClassList.get(i).getMobileNumber());


        new GetProfileImageTask(viewHolder.profileImage).execute(baseUrl.getImageUrl()+diagnosticsClassList.get(i).getCenterImage());


        lati = diagnosticsClassList.get(i).getLatitude();
        longi= diagnosticsClassList.get(i).getLongitude();

//        patientId = diagnosticsClassList.get(i).getPatientId();

    }

    @Override
    public int getItemCount() {
        return diagnosticsClassList.size();
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
