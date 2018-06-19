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

class DiagnosticsPatientHistoryAdapter extends RecyclerView.Adapter<DiagnosticsPatientHistoryAdapter.ViewHolder>  {

    Context context;
    List<DiagnosticsPatientHistoryDataClass> my_data;
    String date,diagId,diagMobile;

    public DiagnosticsPatientHistoryAdapter(Context applicationContext, List<DiagnosticsPatientHistoryDataClass> data_list, String d) {
        this.context=applicationContext;
        this.my_data=data_list;
        this.date=d;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_patient_history_in_diagnostics,parent,false);
        return new DiagnosticsPatientHistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.patientId.setText(my_data.get(position).getPatientid());
        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.mobileNumber.setText(my_data.get(position).getMobileno());
        holder.Testname.setText(my_data.get(position).getTestname());
        holder.Aadharnumber.setText(my_data.get(position).getAadharNumber());
        holder.centername.setText(my_data.get(position).getCentername());
        holder.comments.setText(my_data.get(position).getComments());
        diagId = my_data.get(position).getDiagId();
        diagMobile = my_data.get(position).getDiagMobile();
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView PatientName,mobileNumber,Testname,Aadharnumber,centername,comments,patientId;


        public ViewHolder(final View itemView) {
            super(itemView);
            patientId = (TextView) itemView.findViewById(R.id.patientid);
            PatientName = (TextView) itemView.findViewById(R.id.patientname);
            mobileNumber=(TextView)itemView.findViewById(R.id.mobilenumber);
            Testname=(TextView)itemView.findViewById(R.id.testname);
            Aadharnumber=(TextView)itemView.findViewById(R.id.aadhaarNumber);
            centername=(TextView)itemView.findViewById(R.id.Center_name);
            comments=(TextView)itemView.findViewById(R.id.comment);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("this is adapter");
                    System.out.println("patient id in his adapter.."+patientId.getText().toString());
                    Intent intent=new Intent(itemView.getContext(),ViewPatientHistoryInDiagnosticsTodaysAppointments.class);
                    intent.putExtra("diagId",diagId);
                    intent.putExtra("diagMobile",diagMobile);
                    intent.putExtra("patientid",patientId.getText().toString());
                    intent.putExtra("patientname",PatientName.getText().toString());
                    intent.putExtra("mobilenum",mobileNumber.getText().toString());
                    intent.putExtra("testname",Testname.getText().toString());
                    intent.putExtra("aadharnumber",Aadharnumber.getText().toString());
                    intent.putExtra("centername",centername.getText().toString());
                    intent.putExtra("comments",comments.getText().toString());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}

