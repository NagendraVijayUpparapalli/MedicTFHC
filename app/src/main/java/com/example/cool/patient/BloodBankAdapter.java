package com.example.cool.patient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Udayasri on 22-03-2018.
 */

public class BloodBankAdapter extends ArrayAdapter<BloodBankClass> {
    ArrayList<BloodBankClass> bloodBankClassArrayList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public BloodBankAdapter(Context context, int resource, ArrayList<BloodBankClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        bloodBankClassArrayList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.itemImage = (ImageView) v.findViewById(R.id.image);
            holder.name = (TextView) v.findViewById(R.id.bloodbankname);
            holder.location = (TextView) v.findViewById(R.id.bloodbankaddress);
            holder.mobile = (TextView) v.findViewById(R.id.mobilenumber);
//            holder.availabilty = (TextView) v.findViewById(R.id.availability);
            holder.dist = (TextView) v.findViewById(R.id.between_dist);

            holder.lat = (TextView) v.findViewById(R.id.listlat);
            holder.lng = (TextView) v.findViewById(R.id.listlong);
            holder.person_name = (TextView) v.findViewById(R.id.contact_person);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.itemImage.setImageResource(R.drawable.ambulance);
        new DownloadImageTask(holder.itemImage).execute(bloodBankClassArrayList.get(position).getImage());
        holder.name.setText(bloodBankClassArrayList.get(position).getName());
        holder.lat.setText(bloodBankClassArrayList.get(position).getLati());
//        holder.mobile.setText(bloodBankClassArrayList.get(position).getMobile());
        holder.lng.setText(bloodBankClassArrayList.get(position).getLongi());
        holder.person_name.setText(bloodBankClassArrayList.get(position).getContact_person());
//        holder.availabilty.setText(bloodBankClassArrayList.get(position).getAvailability());
        double d1 = Double.parseDouble(bloodBankClassArrayList.get(position).getDistance());
        double dis = Math.round(d1*1000)/1000.0;
        String[] arr = String.valueOf(dis).split("\\.");
        int[] intarr = new int[2];
        intarr[0] = Integer.parseInt(arr[0]);
        intarr[1] = Integer.parseInt(arr[1]);

        System.out.println("dist decimal...."+intarr[0]);

//        String s = (Double.toString(dis));
//       String[] ar=  s.split(".");
//       String a = ar[0];
//        String b = ar[1];
//        System.out.println("dist decimal...."+s);

//        for(int i=0;i<ar.length;i++)
//        {
//            a= ar[i];
//        }

//        System.out.println("dist a...."+a);
//        System.out.println("dist b...."+b);
        holder.dist.setText("Distance :"+intarr[0]+"."+intarr[1]+" Km");

//        holder.dist.setText("Distance :"+dis+" Km");

        return v;

    }

    static class ViewHolder {
        public ImageView itemImage;
        public TextView name,location,mobile,availabilty,dist,lat,lng,person_name;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

}
