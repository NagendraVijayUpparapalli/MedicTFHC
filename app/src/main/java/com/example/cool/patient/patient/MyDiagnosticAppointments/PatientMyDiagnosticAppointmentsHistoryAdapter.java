package com.example.cool.patient.patient.MyDiagnosticAppointments;

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
 * Created by Udayasri on 03-06-2018.
 */

class PatientMyDiagnosticAppointmentsHistoryAdapter extends RecyclerView.Adapter<PatientMyDiagnosticAppointmentsHistoryAdapter.ViewHolder> {


    private Context context;
    private List<PatientMyDiagnosticAppointmentDetailsClass> my_data;
    String date, comments, amount, modeofpayment, doctorname, reason, prescription,mobileNumber,DiagAddressId;

    String aadharnumber, status1;
    int AppoinmentID;

    public PatientMyDiagnosticAppointmentsHistoryAdapter(Context context, List<PatientMyDiagnosticAppointmentDetailsClass> data_list, String d) {
        this.context = context;
        this.my_data = data_list;
        this.date = d;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_patient_my_diagnostic_appointments, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.Diagnosticstatus.setText(my_data.get(position).getDiagnosticsStatus());

        holder.Centername.setText(my_data.get(position).getCenterName());
        holder.Testname.setText(my_data.get(position).getTestName());

        holder.DiagnosticReport.setText(my_data.get(position).getDiagnosticReport());
        holder.PaymentMode.setText(my_data.get(position).getPaymentmode());

        holder.Amount.setText(my_data.get(position).getAmount());
        holder.Comment.setText(my_data.get(position).getComment());
        holder.appointmentId.setText(my_data.get(position).getAppointmentID());
        holder.userId.setText(my_data.get(position).getUserId());
        holder.appointmentdate.setText(my_data.get(position).getRequestDate());
        mobileNumber = my_data.get(position).getMobileNumber();
        DiagAddressId = my_data.get(position).getDiagAddressId();


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userId,appointmentId,PatientName,Timeslot,Centername,Testname,Diagnosticstatus,
                DiagnosticReport,PaymentMode,Amount,Comment,appointmentdate;

        public ViewHolder(final View itemView) {
            super(itemView);
            PatientName = (TextView) itemView.findViewById(R.id.patientname);
            Centername=(TextView) itemView.findViewById(R.id.Center_name);
            Testname = (TextView) itemView.findViewById(R.id.testname);
            DiagnosticReport=(TextView) itemView.findViewById(R.id.DiagnosticReport);
            Diagnosticstatus = (TextView) itemView.findViewById(R.id.status);
            PaymentMode = (TextView) itemView.findViewById(R.id.paymentmode);
            Amount=(TextView) itemView.findViewById(R.id.amount);
            Comment = (TextView) itemView.findViewById(R.id.comment);
            appointmentId = (TextView) itemView.findViewById(R.id.appointmentid);
            userId = (TextView) itemView.findViewById(R.id.userid);
            appointmentdate = (TextView) itemView.findViewById(R.id.appointmentdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("this is adapter");
                    Intent intent = new Intent(itemView.getContext(), ViewPatientMyDiagnosticAppointments.class);
                    intent.putExtra("DiagAddressId",DiagAddressId);
                    intent.putExtra("mobile",mobileNumber);
                    intent.putExtra("userId",userId.getText().toString());
                    intent.putExtra("appointmentId",appointmentId.getText().toString());
                    intent.putExtra("patientname", PatientName.getText().toString());
                    intent.putExtra("status", Diagnosticstatus.getText().toString());
                    intent.putExtra("appointmentdate", appointmentdate.getText().toString());

                    intent.putExtra("centername", Centername.getText().toString());
                    intent.putExtra("testname", Testname.getText().toString());

                    intent.putExtra("diagnostcreport", DiagnosticReport.getText().toString());
                    intent.putExtra("paymentmode", PaymentMode.getText().toString());

                    intent.putExtra("amount", Amount.getText().toString());
                    intent.putExtra("comment", Comment.getText().toString());

                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
