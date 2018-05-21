package com.example.cool.patient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Udayasri on 16-05-2018.
 */

public class MyPatientDataAdapter extends RecyclerView.Adapter<MyPatientDataAdapter.ViewHolder> {


private Context context;
private List<MyPatientData> my_data;

        String aadharnumber,status1;
        int AppoinmentID;
        public MyPatientDataAdapter(GetPatientDetailsList getpatient_details, List<MyPatientData> data_list) {
        this.context=getpatient_details;
        this.my_data=data_list;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_patientdetails,parent,false);
        return new ViewHolder(itemView);

        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {

        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.mobileNumber.setText(my_data.get(position).getMobileNo());
        holder.Timeslot.setText(my_data.get(position).getTimeSlots());
        holder.Status.setText(my_data.get(position).getStatus1());
        holder.aadhar.setText(my_data.get(position).getAadharNumber());

//        aadharnumber = my_data.get(position).getAadharNumber();
        AppoinmentID = my_data.get(position).getAppointmentID();

        System.out.println("adapaterappin"+AppoinmentID);

        status1=my_data.get(position).getStatus1().toString();

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

        }

@Override
public int getItemCount() {
        return my_data.size();
        }

public  class ViewHolder extends  RecyclerView.ViewHolder{

    public TextView PatientName,mobileNumber,Timeslot,Status,aadhar;


    public ViewHolder(final View itemView) {
        super(itemView);
        PatientName = (TextView) itemView.findViewById(R.id.Patient_name);
        mobileNumber = (TextView)itemView.findViewById(R.id.phone_number);
        Timeslot = (TextView)itemView.findViewById(R.id.time_slot);
        Status = (TextView)itemView.findViewById(R.id.Status);
        aadhar = (TextView)itemView.findViewById(R.id.aadharNumber);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("this is adapter");
                Intent intent=new Intent(itemView.getContext(),GetPatientDetailsTotalData.class);
                intent.putExtra("patientname",PatientName.getText().toString());
                intent.putExtra("mobilenumber",mobileNumber.getText().toString());
                intent.putExtra("timeslot",Timeslot.getText().toString());
                intent.putExtra("status",Status.getText().toString());
                intent.putExtra("aadharnumber",aadhar.getText().toString());
                intent.putExtra("AppointmentID",AppoinmentID);
                itemView.getContext().startActivity(intent);
                //itemView.getContext().startActivity(new Intent(itemView.getContext(),Getpatientdetailstotaldata.class));
            }
        });
    }
}
}

