package com.example.cool.patient.diagnostic.ManageAddress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;

import java.util.List;



/**
 * Created by Udayasri on 23-05-2018.
 */

public class DiagnosticsManageAddressAdapter extends RecyclerView.Adapter<DiagnosticsManageAddressAdapter.ViewHolder>{

    List<DiagnosticsManageAddressClass> diagnosticsManageAddressClassList;
    Context context;
//    String diagId,address1,comment,emergencyContact,city,state,pincode,district,landlineNo,contactPerson,lati,longi,centerImage,fromTime,toTime,addressId;
    boolean emergencyService;

    ProgressDialog progressDialog;

    ApiBaseUrl baseUrl = new ApiBaseUrl();

    String diagId,addId,comment;
    int index = 0;

    EditText reason_Todelete;
    String reasonToDelete = null,mobile;

//    EditText reason_Todelete;

    public DiagnosticsManageAddressAdapter(DiagnosticManageAddress diagnosticManageAddress,List<DiagnosticsManageAddressClass> manageAddressClassList) {
        this.context = diagnosticManageAddress;
        this.diagnosticsManageAddressClassList = manageAddressClassList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView profileImage;
        public TextView diagnosticName,address,mobile,diagId,address1,comment,emergencyContact,city,state,pincode,
                district,landlineNo,contactPerson,lati,longi,centerImage,fromTime,toTime,addressId,regMobile;


        public Button edit,inactive;


        public ViewHolder(final View itemView) {
            super(itemView);
//            diagId,address1,comment,emergencyContact,city,state,pincode,district,
// landlineNo,contactPerson,lati,longi,centerImage,fromTime,toTime,addressId;
            diagnosticName = (TextView)itemView.findViewById(R.id.diagnostic_name);
            address=(TextView)itemView.findViewById(R.id.address) ;
            mobile = (TextView)itemView.findViewById(R.id.phone_number);
            diagId = (TextView)itemView.findViewById(R.id.diagid);

            address1 = (TextView)itemView.findViewById(R.id.address1);
            comment = (TextView)itemView.findViewById(R.id.comment);
            emergencyContact = (TextView)itemView.findViewById(R.id.emergencyContact);
            city = (TextView)itemView.findViewById(R.id.city);
            state = (TextView)itemView.findViewById(R.id.state);
            pincode = (TextView)itemView.findViewById(R.id.pincode);
            district = (TextView)itemView.findViewById(R.id.district);
            landlineNo = (TextView)itemView.findViewById(R.id.landline);
            contactPerson = (TextView)itemView.findViewById(R.id.contact_person);
            lati = (TextView)itemView.findViewById(R.id.latitude);
            longi = (TextView)itemView.findViewById(R.id.longitude);
            centerImage = (TextView)itemView.findViewById(R.id.centerImage);
            fromTime = (TextView)itemView.findViewById(R.id.fromtime);
            toTime = (TextView)itemView.findViewById(R.id.totime);
            addressId = (TextView)itemView.findViewById(R.id.addressId);
            regMobile = (TextView) itemView.findViewById(R.id.regMobile);

            reason_Todelete = (EditText) itemView.findViewById(R.id.reason_delete);
            regMobile = (TextView) itemView.findViewById(R.id.regMobile);

            edit = (Button) itemView.findViewById(R.id.Edit);
            inactive = (Button) itemView.findViewById(R.id.InActive);
//

//        address1,comment,emergencyContact,city,state,pincode,
//                district,landlineNo,contactPerson,lati,longi,centerImage,fromTime,toTime,addressId;

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),DiagnosticsUpdateAddress.class);
                    intent.putExtra("diagid",diagId.getText().toString());
                    intent.putExtra("addressId",addressId.getText().toString());
                    intent.putExtra("mobile",mobile.getText().toString());
                    intent.putExtra("centerName",diagnosticName.getText().toString());
                    intent.putExtra("address1",address1.getText().toString());
                    intent.putExtra("comment",comment.getText().toString());
                    intent.putExtra("emergencyContact",emergencyContact.getText().toString());
                    intent.putExtra("city",city.getText().toString());
                    intent.putExtra("pincode",pincode.getText().toString());
                    intent.putExtra("district",district.getText().toString());
                    intent.putExtra("state",state.getText().toString());
                    intent.putExtra("landline",landlineNo.getText().toString());
                    intent.putExtra("contactName",contactPerson.getText().toString());
                    intent.putExtra("lati",lati.getText().toString());
                    intent.putExtra("longi",longi.getText().toString());

                    intent.putExtra("fromtime",fromTime.getText().toString());
                    intent.putExtra("totime",toTime.getText().toString());
                    intent.putExtra("emergencyService",emergencyService);
                    intent.putExtra("regMobile",regMobile.getText().toString());

                    itemView.getContext().startActivity(intent);
                }
            });

//            inactive.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(itemView.getContext(),DoctorUpdateAddress.class);
//                    intent.putExtra("DoctorID",addressId);
//                    intent.putExtra("AddressID",addressId);
//                    intent.putExtra("Comment",address.getText().toString());
//
//                    itemView.getContext().startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_diagnostics_manage_address, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.diagnosticName.setText(diagnosticsManageAddressClassList.get(i).getCenterName());
        viewHolder.address.setText(diagnosticsManageAddressClassList.get(i).getAddress1()+","+diagnosticsManageAddressClassList.get(i).getCityName());
        viewHolder.mobile.setText(diagnosticsManageAddressClassList.get(i).getMobileNumeber());


//            diagId,address1,comment,emergencyContact,city,state,pincode,district,
// landlineNo,contactPerson,lati,longi,centerImage,fromTime,toTime,addressId,regMobile;

        viewHolder.diagId.setText(diagnosticsManageAddressClassList.get(i).getDiagnosticId());
        viewHolder.address1.setText(diagnosticsManageAddressClassList.get(i).getAddress1());
        viewHolder.comment.setText(diagnosticsManageAddressClassList.get(i).getComment());
        viewHolder.emergencyContact.setText(diagnosticsManageAddressClassList.get(i).getEmergencyContactNumber());
        viewHolder.city.setText(diagnosticsManageAddressClassList.get(i).getCityName());
        viewHolder.state.setText(diagnosticsManageAddressClassList.get(i).getStateName());
        viewHolder.pincode.setText(diagnosticsManageAddressClassList.get(i).getPincode());
        viewHolder.district.setText(diagnosticsManageAddressClassList.get(i).getDistrict());
        viewHolder.landlineNo.setText(diagnosticsManageAddressClassList.get(i).getLandLineNo());
        viewHolder.contactPerson.setText(diagnosticsManageAddressClassList.get(i).getContactPerson());
        viewHolder.lati.setText(diagnosticsManageAddressClassList.get(i).getLatitude());
        viewHolder.longi.setText(diagnosticsManageAddressClassList.get(i).getLongitude());
//        viewHolder.centerImage.setText(diagnosticsManageAddressClassList.get(i).getI());
        viewHolder.fromTime.setText(diagnosticsManageAddressClassList.get(i).getFromTime());
        viewHolder.toTime.setText(diagnosticsManageAddressClassList.get(i).getToTime());
        viewHolder.addressId.setText(diagnosticsManageAddressClassList.get(i).getAddressId());
        viewHolder.regMobile.setText(diagnosticsManageAddressClassList.get(i).getRegisterdMobileNumber());

        emergencyService = diagnosticsManageAddressClassList.get(i).isEmergencyservice();
        final int pos = i;

        reasonToDelete = reason_Todelete.getText().toString();
        mobile = viewHolder.regMobile.getText().toString();
        diagId = diagnosticsManageAddressClassList.get(pos).getDiagnosticId();
        addId = diagnosticsManageAddressClassList.get(pos).getAddressId();
        comment = diagnosticsManageAddressClassList.get(pos).getDeleteReason();

        System.out.println("diag id in manage adapter.."+diagId);


        System.out.println("size.."+diagnosticsManageAddressClassList.size());

        viewHolder.inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("size.."+diagnosticsManageAddressClassList.size());

                    deleteItem(pos);
                Intent intent = new Intent(context,DiagnosticDeleteAddress.class);
                intent.putExtra("DiagID",diagId);
                intent.putExtra("AddressID",addId);
                intent.putExtra("Comment",comment);
                intent.putExtra("mobile",mobile);

                context.startActivity(intent);

                index = pos;
            }
        });

    }

    void deleteItem(int index) {
        diagnosticsManageAddressClassList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return diagnosticsManageAddressClassList.size();
    }

}
