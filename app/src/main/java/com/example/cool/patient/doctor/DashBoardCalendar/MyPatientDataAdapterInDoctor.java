package com.example.cool.patient.doctor.DashBoardCalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cool.patient.R;

import java.util.List;


/**
 * Created by Udayasri on 16-05-2018.
 */

public class MyPatientDataAdapterInDoctor extends RecyclerView.Adapter<MyPatientDataAdapterInDoctor.ViewHolder> {


private Context context;
private List<MyPatientDataClassInDoctor> my_data;

//        String aadharnumber,status1,doctorId,DoctorAddressId,doctorMobile,appointmentDate,AppoinmentID,patientId;
//        String mypatientId;

    String status1;

        public MyPatientDataAdapterInDoctor(GetPatientDetailsListInDoctor getpatient_details,
                                            List<MyPatientDataClassInDoctor> data_list) {
        this.context=getpatient_details;
        this.my_data=data_list;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_patientdetails_in_doctor,parent,false);
        return new ViewHolder(itemView);

        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {

        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.mobileNumber.setText(my_data.get(position).getMobileNo());
        holder.Timeslot.setText(my_data.get(position).getTimeSlots());
        holder.Status.setText(my_data.get(position).getStatus1());
        holder.aadhar.setText(my_data.get(position).getAadharNumber());

//    doctorId,DoctorAddressId,doctorMobile,appointmentDate,AppoinmentID,patientId;

        holder.DoctorAddressId.setText(my_data.get(position).getDoctorAddressId());
        holder.doctorId.setText(my_data.get(position).getDoctorId());
        holder.doctorMobile.setText(my_data.get(position).getDoctorMobile());
        holder.patientId.setText(my_data.get(position).getPatientID());
        holder.AppoinmentID.setText(my_data.get(position).getAppointmentID());
        holder.appointmentDate.setText(my_data.get(position).getAppointmentDate());

//        mypatientId = my_data.get(position).getPatientID();

        status1= my_data.get(position).getStatus1();

        if(status1.equals("Accept"))
        {

        holder.Status.setTextColor(Color.GREEN);

        }
        else if(status1.equals("Reject"))
        {

        holder.Status.setTextColor(Color.RED);
        }

        else if(status1.equals("Pending"))
        {

            holder.Status.setTextColor(Color.CYAN);
        }
        else if(status1.equals("Reschedule"))
        {

            holder.Status.setTextColor(Color.BLUE);

        }

        }

@Override
public int getItemCount() {
        return my_data.size();
        }

public  class ViewHolder extends  RecyclerView.ViewHolder{

    public TextView PatientName,mobileNumber,Timeslot,Status,aadhar,
            doctorId,DoctorAddressId,doctorMobile,appointmentDate,AppoinmentID,patientId;

    public ViewHolder(final View itemView) {
        super(itemView);
        PatientName = (TextView) itemView.findViewById(R.id.Patient_name);
        mobileNumber = (TextView)itemView.findViewById(R.id.phone_number);
        Timeslot = (TextView)itemView.findViewById(R.id.time_slot);
        Status = (TextView)itemView.findViewById(R.id.Status);
        aadhar = (TextView)itemView.findViewById(R.id.aadharNumber);

        doctorId = (TextView) itemView.findViewById(R.id.doctorId);
        DoctorAddressId = (TextView)itemView.findViewById(R.id.DoctorAddressId);
        doctorMobile = (TextView)itemView.findViewById(R.id.doctorMobile);
        appointmentDate = (TextView)itemView.findViewById(R.id.appointmentDate);
        AppoinmentID = (TextView)itemView.findViewById(R.id.AppoinmentID);
        patientId = (TextView)itemView.findViewById(R.id.patientId);

        System.out.println("this is size..."+my_data.size());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("this is adapter");
                Intent intent=new Intent(itemView.getContext(),GetPatientDetailsTotalDataInDoctor.class);
                intent.putExtra("patientname",PatientName.getText().toString());
                intent.putExtra("mobilenumber",mobileNumber.getText().toString());
                intent.putExtra("timeslot",Timeslot.getText().toString());
                intent.putExtra("status",Status.getText().toString());
                intent.putExtra("aadharnumber",aadhar.getText().toString());
                intent.putExtra("AppointmentID",AppoinmentID.getText().toString());
                intent.putExtra("doctorId",doctorId.getText().toString());
                intent.putExtra("doctorMobile",doctorMobile.getText().toString());
                intent.putExtra("doctorAddressID",DoctorAddressId.getText().toString());
                intent.putExtra("patientID",patientId.getText().toString());
                intent.putExtra("appointmentDate",appointmentDate.getText().toString());
                itemView.getContext().startActivity(intent);
                //itemView.getContext().startActivity(new Intent(itemView.getContext(),Getpatientdetailstotaldata.class));
            }
        });
    }
}
}

