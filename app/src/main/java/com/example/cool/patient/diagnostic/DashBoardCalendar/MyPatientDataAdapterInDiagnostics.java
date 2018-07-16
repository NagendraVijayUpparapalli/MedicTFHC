package com.example.cool.patient.diagnostic.DashBoardCalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cool.patient.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Udayasri on 29-05-2018.
 */

public class MyPatientDataAdapterInDiagnostics extends RecyclerView.Adapter<MyPatientDataAdapterInDiagnostics.ViewHolder> {
    private List<MyPatientDataClassInDiagnostics> my_data;
    Context context;
    List<String> speciality=new ArrayList<>();
    StringBuilder builder;
    String test;

    String testname;
    public MyPatientDataAdapterInDiagnostics(Context context, List<MyPatientDataClassInDiagnostics> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_patientdetails_in_diagnostics,parent,false);
        return new MyPatientDataAdapterInDiagnostics.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.Patientname.setText(my_data.get(position).getPatientName());
        holder.mobilenumber.setText(my_data.get(position).getMobileNo());
        holder.status.setText(my_data.get(position).getStatus());
        holder.Aadharnumber.setText(my_data.get(position).getAadharnumber());
        holder.comments.setText(my_data.get(position).getComments());
        holder.Amount.setText(my_data.get(position).getAmount());
        holder.Prescription.setText(my_data.get(position).getPrescription());
        holder.paymode.setText(my_data.get(position).getPayment());
        holder.RDID.setText(String.valueOf(my_data.get(position).getRdTestID()));
        holder.addressId.setText(my_data.get(position).getAddressId());
        holder.centerName.setText(my_data.get(position).getCenterName());
        holder.diagnosticId.setText(my_data.get(position).getDiagnosticId());
        holder.diagmobile.setText(my_data.get(position).getDiagmobile());
        holder.appointmentDate.setText(my_data.get(position).getAppointmentDate());

        builder=new StringBuilder();

        speciality.add(my_data.get(position).getSpeciality().toString());

        for(int i=0;i<speciality.size();i++)
        {
            System.out.println("testname"+speciality.get(i));
            test=speciality.get(i);
            builder.append(test+"\n");

        }

        holder.myspeciality.setText(builder.toString());

        System.out.println("test name.."+holder.myspeciality.getText().toString());

//        Finished, Initiated, In Progress

        if(my_data.get(position).getStatus().equals("Initiated"))
        {

            holder.status.setTextColor(Color.BLACK);

        }
        else if(my_data.get(position).getStatus().equals("Finished"))
        {

            holder.status.setTextColor(Color.BLACK);
        }

        else if(my_data.get(position).getStatus().equals("Pending"))
        {

            holder.status.setTextColor(Color.BLACK);
        }
        else if(my_data.get(position).getStatus().equals("In Progress"))
        {

            holder.status.setTextColor(Color.BLACK);

        }

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Patientname,mobilenumber,status,Aadharnumber,Amount,Prescription,comments,paymode,RDID,
                addressId,centerName,diagnosticId,diagmobile,appointmentDate,myspeciality;
        public ViewHolder(final View itemView) {
            super(itemView);

            Patientname=(TextView)itemView.findViewById(R.id.Patient_name);
            mobilenumber=(TextView)itemView.findViewById(R.id.phone_number);
            status=(TextView)itemView.findViewById(R.id.Status);
            Aadharnumber=(TextView)itemView.findViewById(R.id.Aadharnumber);
            comments=(TextView)itemView.findViewById(R.id.comments);
            Prescription=(TextView)itemView.findViewById(R.id.prescription);
            Amount=(TextView)itemView.findViewById(R.id.amount);
            paymode=(TextView)itemView.findViewById(R.id.paymentmode);
            RDID=(TextView) itemView.findViewById(R.id.rdid);
            addressId = (TextView) itemView.findViewById(R.id.addressId);
            centerName = (TextView) itemView.findViewById(R.id.centerName);
            diagnosticId = (TextView) itemView.findViewById(R.id.diagnosticId);
            diagmobile = (TextView) itemView.findViewById(R.id.diagmobile);
            appointmentDate = (TextView) itemView.findViewById(R.id.appointmentDate);
            myspeciality = (TextView) itemView.findViewById(R.id.speciality);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i=new Intent(itemView.getContext(),GetPatientDetailsTotalDataInDiagnostics.class);

                    i.putExtra("Patientname",Patientname.getText().toString());
                    i.putExtra("mobilenumber",mobilenumber.getText().toString());
                    i.putExtra("status",status.getText().toString());
                    i.putExtra("Aadharnumber",Aadharnumber.getText().toString());
                    i.putExtra("comments",comments.getText().toString());
                    i.putExtra("Prescription",Prescription.getText().toString());
                    i.putExtra("Amount",Amount.getText().toString());
                    i.putExtra("paymode",paymode.getText().toString());
                    i.putExtra("addressId",addressId.getText().toString());
                    i.putExtra("centerName",centerName.getText().toString());
                    i.putExtra("diagnosticId",diagnosticId.getText().toString());
                    i.putExtra("diagmobile",diagmobile.getText().toString());
                    i.putExtra("date",appointmentDate.getText().toString());
                    i.putExtra("rdid",Integer.parseInt(RDID.getText().toString()));
                    i.putStringArrayListExtra("testname", (ArrayList<String>) speciality);
                    itemView.getContext().startActivity(i);

                }
            });

        }
    }
}

