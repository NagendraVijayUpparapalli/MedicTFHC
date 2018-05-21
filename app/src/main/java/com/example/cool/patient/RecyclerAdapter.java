package com.example.cool.patient;

/**
 * Created by KIIT SEZ on 10-03-2018.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import android.support.design.widget.Snackbar;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] titles = {"EYE","NOSE","Ear"};

   // private String[] details = {"BHMS"};
 //   private String[] experience = {"Your health is good. Take one paracetmal after meal, benzoyl after dinner"};


    private int[] images = { R.drawable.eyeimage,
            R.drawable.noseimage,
            R.drawable.earimage};

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public TextView textView;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
//            textView=(TextView)itemView.findViewById(R.id.item_detai2) ;
//            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemView.getContext().startActivity(new Intent(v.getContext(),Headparts.class));


//                    Snackbar.make(v, "Click detected on item " + position,
//                            Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        //viewHolder.itemDetail.setText(details[i]);
        //viewHolder.textView.setText(experience[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }


    @Override
    public int getItemCount() {
        return titles.length;
    }
}