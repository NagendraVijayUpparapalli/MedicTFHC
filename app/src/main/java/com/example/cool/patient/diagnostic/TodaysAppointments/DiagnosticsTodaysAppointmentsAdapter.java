package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cool.patient.R;

import java.util.List;

/**
 * Created by Udayasri on 29-05-2018.
 */

class DiagnosticsTodaysAppointmentsAdapter extends RecyclerView.Adapter<DiagnosticsTodaysAppointmentsAdapter.ViewHolder> {
    private Context context;
    private List<PatientDatainDiagnosticsTodaysAppointmentsClass> my_data;
    String email,aadharnumber,comments,status,image,diagnosticId,diagMobile;

    public DiagnosticsTodaysAppointmentsAdapter(Context context, List<PatientDatainDiagnosticsTodaysAppointmentsClass> data_list) {

        this.context=context;
        this.my_data=data_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_in_diagnostic_today_appointments,parent,false);
        return new DiagnosticsTodaysAppointmentsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        holder.patientid.setText(my_data.get(position).getPa());
        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.mobileNumber.setText(my_data.get(position).getMobileNo());
        holder.appiontmentid.setText(String.valueOf(my_data.get(position).getAppointmentID()));

        holder.email.setText(my_data.get(position).getEmailID());
        holder.Dstatus.setText(String.valueOf(my_data.get(position).getDstatus()));
        holder.aadhar.setText(my_data.get(position).getAadharnumebr());

        String myStatus = "";
        if(my_data.get(position).getDstatus() == 0)
        {
            myStatus = "Pending";
            holder.Status.setText(myStatus);
        }
        else if(my_data.get(position).getDstatus() == 1)
        {
            myStatus = "Initiated";
            holder.Status.setText(myStatus);
        }
        else if(my_data.get(position).getDstatus() == 2)
        {
            myStatus = "In Progress";
            holder.Status.setText(myStatus);
        }
        else if(my_data.get(position).getDstatus() == 3)
        {
            myStatus = "Finished";
            holder.Status.setText(myStatus);
        }



        diagnosticId = my_data.get(position).getDiagnosticId();
        diagMobile = my_data.get(position).getDiagMobile();

        //  email=my_data.get(position).getEmailID();
//        comments=my_data.get(position).getComments();
//        status=my_data.get(position).getStatus();
//        image=my_data.get(position).getPrescription();
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView PatientName,mobileNumber,aadhar,Status,comment,email,imageurl,appiontmentid,Dstatus,patientid;

        public ViewHolder(final View itemView) {
            super(itemView);

//            patientid = (TextView) itemView.findViewById(R.id.patientid);
            PatientName = (TextView) itemView.findViewById(R.id.patientname);
            mobileNumber=(TextView)itemView.findViewById(R.id.phonenumber);
            email=(TextView)itemView.findViewById(R.id.email);
            appiontmentid=(TextView)itemView.findViewById(R.id.appointmentid);
            Dstatus=(TextView)itemView.findViewById(R.id.Dstatus);
            aadhar=(TextView)itemView.findViewById(R.id.aadharno);
            Status=(TextView)itemView.findViewById(R.id.status);
//            comment=(TextView)itemView.findViewById(R.id.comments);

//            imageurl=(TextView)itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i=new Intent(itemView.getContext(), DiagnosticsViewTodaysApppointment.class);
                    i.putExtra("patientname",PatientName.getText().toString());
                    i.putExtra("mobilenumber",mobileNumber.getText().toString());
                    i.putExtra("diagId",diagnosticId);
                    i.putExtra("diagMobile",diagMobile);
                    i.putExtra("emailid",email.getText().toString());
                    i.putExtra("appointmentid",appiontmentid.getText().toString());
                    i.putExtra("Dstatus",Dstatus.getText().toString());
                    i.putExtra("Aadharnum",aadhar.getText().toString());
                    itemView.getContext().startActivity(i);
                }
            });


        }
    }
}