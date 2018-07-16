package com.example.cool.patient.doctor.TodaysAppointments;



import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cool.patient.R;
import com.example.cool.patient.patient.MyDoctorAppointments.ViewPatientMyDoctorAppointment;

import java.util.List;

public class DoctorPatientHistoryAdapter  extends RecyclerView.Adapter<DoctorPatientHistoryAdapter.ViewHolder> {

    Context context;
    List<DoctorPatientHistoryClass> my_data;
    String date,docId,docMobile;
    public DoctorPatientHistoryAdapter(Context applicationContext, List<DoctorPatientHistoryClass> data_list, String d) {

        this.context=applicationContext;
        this.my_data=data_list;
        this.date=d;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_patient_history_in_doctor,parent,false);
        return new DoctorPatientHistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoctorPatientHistoryAdapter.ViewHolder holder, int position) {

        holder.patientid.setText(my_data.get(position).getPatientId());
        holder.PatientName.setText(my_data.get(position).getPatientname());
        holder.mobileNumber.setText(my_data.get(position).getMobilenumber());
        holder.appdate.setText(my_data.get(position).getDate());
        holder.aadhaarNumber.setText(my_data.get(position).getAaadharnumber());
        holder.reason.setText(my_data.get(position).getReason());
        holder.doctor_comment.setText(my_data.get(position).getDoctorcomment());
        holder.doctorName.setText(my_data.get(position).getDoctorcomment());
        holder.doctorImageUrl.setText(my_data.get(position).getDoctorImageUrl());
        holder.prescriptUrl.setText(my_data.get(position).getPrescription());

        docId = my_data.get(position).getDoctorId();
        docMobile = my_data.get(position).getDoctorMobile();

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView PatientName,mobileNumber,appdate,aadhaarNumber,patientid,reason,speciality,doctor_comment,doctorName,
                doctorImageUrl,prescriptUrl;
        public ViewHolder(final View itemView) {
            super(itemView);

            PatientName = (TextView) itemView.findViewById(R.id.patientname);
            mobileNumber=(TextView)itemView.findViewById(R.id.mobilenumber);
            appdate=(TextView)itemView.findViewById(R.id.date);
            aadhaarNumber = (TextView) itemView.findViewById(R.id.aadhaarNumber);
            patientid=(TextView)itemView.findViewById(R.id.mobilenumber);
            reason=(TextView)itemView.findViewById(R.id.date);
            speciality = (TextView) itemView.findViewById(R.id.aadhaarNumber);
            doctor_comment=(TextView)itemView.findViewById(R.id.mobilenumber);
            doctorName=(TextView)itemView.findViewById(R.id.date);
            prescriptUrl=(TextView)itemView.findViewById(R.id.prescriptUrl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),ViewPatientHistoryInDoctor.class);
                    intent.putExtra("id",docId);
                    intent.putExtra("doctorMobile",docMobile);
                    intent.putExtra("patientId",patientid.getText().toString());
                    intent.putExtra("patientname",PatientName.getText().toString());
                    intent.putExtra("mobilenum",mobileNumber.getText().toString());
                    intent.putExtra("appdate",appdate.getText().toString());
                    intent.putExtra("aadharnumber",aadhaarNumber.getText().toString());
                    intent.putExtra("reason",reason.getText().toString());
                    intent.putExtra("speciality",speciality.getText().toString());
                    intent.putExtra("doctorcomment",doctor_comment.getText().toString());
                    intent.putExtra("doctorName",doctorName.getText().toString());
                    intent.putExtra("doctorImageUrl",doctorImageUrl.getText().toString());
                    intent.putExtra("prescriptUrl",prescriptUrl.getText().toString());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
