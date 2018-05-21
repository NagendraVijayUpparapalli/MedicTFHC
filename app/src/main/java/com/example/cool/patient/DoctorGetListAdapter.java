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
 * Created by Udayasri on 14-05-2018.
 */

public class DoctorGetListAdapter extends ArrayAdapter<DoctorClass> {
    ArrayList<DoctorClass> doctorClassArrayList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public DoctorGetListAdapter(Context context, int resource, ArrayList<DoctorClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        doctorClassArrayList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);

            holder.doctorName = (TextView)v.findViewById(R.id.doctorName);
            holder.qualification=(TextView)v.findViewById(R.id.qualification) ;
            holder.speciality = (TextView)v.findViewById(R.id.speciality);
            holder.fee=(TextView)v.findViewById(R.id.consultationFee) ;

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.doctorName.setText(doctorClassArrayList.get(position).getName());
        holder.qualification.setText(doctorClassArrayList.get(position).getQualification());
        holder.speciality.setText(doctorClassArrayList.get(position).getSpecialityName());
        holder.fee.setText(doctorClassArrayList.get(position).getConsultationFee());

        return v;

    }

    static class ViewHolder {
        public int currentItem;
        public ImageView profileImage;
        public TextView doctorName,qualification,speciality,fee;

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
