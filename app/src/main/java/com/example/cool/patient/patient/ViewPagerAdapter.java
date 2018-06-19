package com.example.cool.patient.patient;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cool.patient.R;

/**
 * Created by Sanket on 27-Feb-17.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.appslider, R.drawable.doctorslider,R.drawable.diagonsticslider,R.drawable.medicalslider,R.drawable.hospitalslider,R.drawable.bloodbankslider,R.drawable.ambulanceslider};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Toast.makeText(context, "app clicked", Toast.LENGTH_SHORT).show();
//                    view.getContext().startActivity(new Intent(view.getContext(),Recongination.class));

                } else if(position == 1){
                    Toast.makeText(context, "DoctorClass clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 2){
                    Toast.makeText(context, "Diagnostic clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 3){
                    Toast.makeText(context, "medical clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 4){
                    Toast.makeText(context, "hospital clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 5){
                    Toast.makeText(context, "blood bank clicked", Toast.LENGTH_SHORT).show();
                }else if(position == 6){
                    Toast.makeText(context, "ambulance clicked", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Slide 7 Clicked", Toast.LENGTH_SHORT).show();

                }


            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
