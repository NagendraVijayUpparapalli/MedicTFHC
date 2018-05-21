package com.example.cool.patient;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Udayasri on 20-03-2018.
 */

public class MyBloodBankAdapter extends RecyclerView.Adapter<MyBloodBankAdapter.ViewHolder> {

    private String[] names = {"Akshaya","Akshara","Trust"};
    private String[] addresss = {"Tirupati","Vizaq","Kakinada"};
    private String[] phone = {"9323232772","87717181818","98382981891"};

   static String strJson=null;


    // private String[] details = {"BHMS"};
    //   private String[] experience = {"Your health is good. Take one paracetmal after meal, benzoyl after dinner"};


    private int[] images = { R.drawable.ambulance,
            R.drawable.ambulance,
            R.drawable.ambulance};

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView name,address,mobile,dist;


        public ViewHolder(final View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.bloodbankname);
            address=(TextView)itemView.findViewById(R.id.bloodbankaddress) ;
            mobile = (TextView)itemView.findViewById(R.id.mobilenumber);
//            dist = (TextView)itemView.findViewById(R.id.between_dist);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    int position = getAdapterPosition();
                    itemView.getContext().startActivity(new Intent(v.getContext(),Registration.class));

//                    Snackbar.make(v, "Click detected on item " + position,
//                            Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                }
            });
        }
    }

    @Override
    public MyBloodBankAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_blood_bank, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyBloodBankAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(names[i]);
        viewHolder.address.setText(addresss[i]);
        viewHolder.mobile.setText(phone[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return names.length;
}
}