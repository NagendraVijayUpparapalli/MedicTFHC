package com.example.cool.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ListView extends AppCompatActivity {

    private EditText etsearch;
    private android.widget.ListView list;

    private ListViewAdapter adapter;
    private String[] moviewList;
    public static ArrayList<MovieNames> movieNamesArrayList = new ArrayList<MovieNames>();
    public static ArrayList<MovieNames> array_sort = new ArrayList<MovieNames>();
    int textlength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cityviews);

        // Generate sample dataGenerate sample data

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        moviewList = new String[]{"Kakinada", "Tuni", "Visakapatnam",
                "Vijayawada", "Hyderabad", "Guntur", "Rajamundary", "Vijayanagaram",
                "Srikakulam","Tirupati","wrangal"};


        list = (android.widget.ListView) findViewById(R.id.listView);

        movieNamesArrayList = new ArrayList<>();

        for (int i = 0; i < moviewList.length; i++) {
            MovieNames movieNames = new MovieNames(moviewList[i]);
            // Binds all strings into an array
            movieNamesArrayList.add(movieNames);
            array_sort.add(movieNames);
        }

        adapter = new ListViewAdapter(this,movieNamesArrayList);
        list.setAdapter(adapter);


        etsearch = (EditText) findViewById(R.id.editText);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = moviewList[position].toString();
                Toast.makeText(ListView.this, s, Toast.LENGTH_SHORT).show();
                Toast.makeText(ListView.this, array_sort.get(position).getMovieName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ListView.this,BloodBank.class);
                i.putExtra("city",array_sort.get(position).getMovieName());
                startActivity(i);
            }
        });

        etsearch.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textlength = etsearch.getText().length();
                array_sort.clear();
                for (int i = 0; i < movieNamesArrayList.get(i).getMovieName().length(); i++) {
                    if (textlength <= movieNamesArrayList.get(i).getMovieName().length()) {
                        if (movieNamesArrayList.get(i).getMovieName().toLowerCase().contains(
                                etsearch.getText().toString().toLowerCase().trim())) {
                            array_sort.add(movieNamesArrayList.get(i));
                        }
                    }
                }
                adapter = new ListViewAdapter(ListView.this,array_sort);
                list.setAdapter(adapter);

            }
        });

    }
}
