package com.example.cool.patient;

/**
 * Created by Udayasri on 21-03-2018.
 */
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AboutUsSwipeAdapter extends PagerAdapter {
    private int[] resource_images={R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

    private LayoutInflater layoutInflater;
    private Context ctx;
    public AboutUsSwipeAdapter(Context ctx)
    {
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return resource_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.about_us_swipe, container, false);
        ImageView imageView=(ImageView) item_view.findViewById(R.id.image_view);
        imageView.setImageResource(resource_images[position]);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
//        ViewPager vp = (ViewPager) container;
//        View view = (View) object;
//        vp.removeView(view);

    }
}
