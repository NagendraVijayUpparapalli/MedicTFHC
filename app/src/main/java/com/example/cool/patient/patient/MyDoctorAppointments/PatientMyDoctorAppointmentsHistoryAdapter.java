package com.example.cool.patient.patient.MyDoctorAppointments;

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
 * Created by Udayasri on 23-05-2018.
 */

public class PatientMyDoctorAppointmentsHistoryAdapter extends RecyclerView.Adapter<PatientMyDoctorAppointmentsHistoryAdapter.ViewHolder> {


    private Context context;
    private List<PatientMyDoctorAppointmentDetailsClass> my_data;
//    String date;
//    comments,amount,modeofpayment,doctorname,reason,prescription,appointmentID,doctorId,UserId,mobileNumber;

    String aadharnumber,status1;
//    int AppoinmentID;
    public PatientMyDoctorAppointmentsHistoryAdapter(PatientMyDoctorAppointments patientMyDoctorAppointments, List<PatientMyDoctorAppointmentDetailsClass> data_list) {
        this.context=patientMyDoctorAppointments;
        this.my_data=data_list;
//        this.date=d;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_patient_my_doctor_appointments,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.Timeslot.setText(my_data.get(position).getTimeslot());
        holder.Status.setText(my_data.get(position).getAppointmentStatus());
        holder.doctorId.setText(my_data.get(position).getDoctorId());

        holder.doctorcomments.setText(my_data.get(position).getDoctorComment());
        holder.amount.setText(my_data.get(position).getAmount());
        holder.modeofpayment.setText(my_data.get(position).getPaymentmode());
        holder.doctorname.setText(my_data.get(position).getDoctorName());
        holder.reason.setText(my_data.get(position).getReason());
        holder.prescription.setText(my_data.get(position).getPrescription());

        holder.UserId.setText(my_data.get(position).getUserId());
        holder.mobileNumber.setText(my_data.get(position).getMobileNumber());
        holder.appointmentID.setText(my_data.get(position).getAppointmentID());
        holder.appointmentdate.setText(my_data.get(position).getDate());

        System.out.println("user app date in my doc.."+my_data.get(position).getAppointmentDate());

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView UserId,appointmentID,mobileNumber,PatientName,Timeslot,Status,doctorId,doctorcomments,
                amount,modeofpayment,doctorname,reason,prescription, appointmentdate;


        public ViewHolder(final View itemView) {
            super(itemView);
            PatientName = (TextView) itemView.findViewById(R.id.patientname);
            Timeslot=(TextView)itemView.findViewById(R.id.time_slot);
            Status=(TextView)itemView.findViewById(R.id.status);
            doctorId=(TextView)itemView.findViewById(R.id.doctorId);

            doctorcomments=(TextView)itemView.findViewById(R.id.comments);
            amount=(TextView)itemView.findViewById(R.id.amount);
            modeofpayment=(TextView)itemView.findViewById(R.id.modeofpayment);
            doctorname=(TextView)itemView.findViewById(R.id.doctorname);
            reason=(TextView)itemView.findViewById(R.id.reason);
            prescription=(TextView)itemView.findViewById(R.id.prescription);

            UserId =(TextView)itemView.findViewById(R.id.userId);
            appointmentID = (TextView)itemView.findViewById(R.id.appointmentId);
            mobileNumber = (TextView)itemView.findViewById(R.id.mobileNumber);
            appointmentdate = (TextView)itemView.findViewById(R.id.appointmentdate);

            System.out.println("user id my doc.."+UserId.getText().toString()+"...."+mobileNumber.getText().toString()+"..app date.."+appointmentdate.getText().toString().trim());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("this is adapter");
                    Intent intent=new Intent(itemView.getContext(),ViewPatientMyDoctorAppointment.class);
                    intent.putExtra("userId",UserId.getText().toString());
                    intent.putExtra("mobile",mobileNumber.getText().toString());
                    intent.putExtra("appointmentId",appointmentID.getText().toString());
                    intent.putExtra("doctorId",doctorId.getText().toString());
                    intent.putExtra("appointmentdate",appointmentdate.getText().toString());
                    intent.putExtra("patientname",PatientName.getText().toString());
                    intent.putExtra("timeslot",Timeslot.getText().toString());
                    intent.putExtra("status",Status.getText().toString());
                    intent.putExtra("comments",doctorcomments.getText().toString());
                    intent.putExtra("amount",amount.getText().toString());
                    intent.putExtra("paymentmode",modeofpayment.getText().toString());
                    intent.putExtra("doctorname",doctorname.getText().toString());
                    intent.putExtra("reason",reason.getText().toString());
                    intent.putExtra("prescription",prescription.getText().toString());
                    itemView.getContext().startActivity(intent);
                    //itemView.getContext().startActivity(new Intent(itemView.getContext(),Getpatientdetailstotaldata.class));
                }
            });
        }
    }
}
