package com.example.cool.patient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Headparts extends AppCompatActivity {

    private ListView listView;
    private SwipeListAdapter adapter;
    private List<Doctor> movieList;

    Spinner dropdown;
    String[] items=new String[]{"Kakinada","Tuni","Vizag"};

    static String label="";
    String strJson="";
    String city1[]=new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headparts);
        listView=(ListView) findViewById(R.id.listView1) ;
        dropdown=(Spinner) findViewById(R.id.spinner) ;

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter1);

        movieList = new ArrayList<>();
        adapter = new SwipeListAdapter(this, movieList);
         strJson = "{\"Doctors\":[{\"id\": \"01\",\"name\": \"Gayathri\",\"city\": \"Kakinada\"},{\"id\": \"02\",\"name\": \"Udayasri\",\"city\": \"Tuni\"},{\"id\": \"03\",\"name\": \"Sudarsan\",\"city\": \"Vizag\"},{\"id\": \"04\",\"name\": \"Sudarsan\",\"city\": \"Vizag\"}]}";

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                label = dropdown.getItemAtPosition(dropdown.getSelectedItemPosition()).toString();
                System.out.println("label"+label);
                movieList.clear();
                getlist(label);

                listView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    public void getlist(String label1)
    {
        System.out.println("label1./...."+label1);
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(strJson);
            JSONArray jsonArray=jsonObject.optJSONArray("Doctors");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                int id1 = Integer.parseInt(jsonObject1.optString("id").toString());
                String name=jsonObject1.get("name").toString();
                String city = jsonObject1.optString("city").toString();
                System.out.println(name);
                System.out.println("city is"+city);
                Doctor d=new Doctor(id1,name,city);
               // movieList.add(0, d);
                if(city.equals(label1)) {

                    System.out.println("hiikdgfhg");
                    movieList.add(0, d);

                }
                else
                {
                    System.out.println("this is else");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
