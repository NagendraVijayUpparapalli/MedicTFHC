package com.example.cool.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends AppCompatActivity {

    TextView current_location,cancel_button;
    private ListView selected_location;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;
    SearchView searchView;

    static String getModuleName=null;

    final ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        current_location = (TextView) findViewById(R.id.current_city);
        cancel_button = (TextView) findViewById(R.id.cancel_icon);
//        searchView = (SearchView) findViewById(R.id.citysearch);

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        getModuleName = getIntent().getStringExtra("docList");

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                System.out.println("Text ["+cs+"]");
                System.out.println("filtered selected city text...."+cs.toString());
                adapter.getFilter().filter(cs);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectCity.this,BloodBank.class);
                startActivity(i);
            }
        });

        selected_location = (ListView) findViewById(R.id.list_view);

        list.add("Addateegala");
        list.add("Ainavilli");
        list.add("Alamuru");
        list.add("Allavaram");
        list.add("Amalapuram");
        list.add("Ambajipeta");
        list.add("Anakapalle");
        list.add("Anandapuram");
        list.add("Ananthagiri");
        list.add("Anaparthy");
        list.add("Araku Valley");
        list.add("Atchutapuram");
        list.add("Atreyapuram");
        list.add("Bheemunipatnam");
        list.add("Biccavolu");
        list.add("Butchayyapeta");
        list.add("Cheedikada");
        list.add("Chintapalle");
        list.add("Chodavaram");
        list.add("Devarapalle");
        list.add("Devipatnam");
        list.add("Dumbriguda");
        list.add("G.Madugula");
        list.add("Gajuwaka");
        list.add("Gandepalle");
        list.add("Gangavaram");
        list.add("Gokavaram");
        list.add("Gollaprolu");
        list.add("Golugonda");
        list.add("Gudem Kotha Veedhi");
        list.add("Hukumpeta");
        list.add("I.Polavaram");
        list.add("Jaggampeta");
        list.add("K.Kotapadu");
        list.add("Kadiam");
        list.add("Kajuluru");
        list.add("Kakinada Urban");
        list.add("Kapileswarapuram");
        list.add("Karapa");
        list.add("Kasimkota");
        list.add("Katrenikona");
        list.add("Kirlampudi");
        list.add("Korukonda");
        list.add("Kotananduru");
        list.add("Kotauratla");
        list.add("Kothapalle");
        list.add("Kothapeta");
        list.add("Koyyuru");
        list.add("Madugula");
        list.add("Makavarapalem");
        list.add("Malikipuram");
        list.add("Mamidikuduru");
        list.add("Mandapeta");
        list.add("Maredumilli");
        list.add("Mummidivaram");
        list.add("Munagapaka");
        list.add("Munchingi Puttu");
        list.add("Nakkapalle");
        list.add("Narsipatnam");
        list.add("Nathavaram");
        list.add("P Gannavaram");
        list.add("Paderu");
        list.add("Padmanabham");
        list.add("Pamarru");
        list.add("Paravada");
        list.add("Payakaraopeta");
        list.add("Peda Bayalu");
        list.add("Pedagantyada");
        list.add("Pedapudi");
        list.add("Peddapuram");
        list.add("Pendurthi");
        list.add("Pithapuram");
        list.add("Prathipadu");
        list.add("Rajahmundry Rural");
        list.add("Rajahmundry Urban");
        list.add("Rajanagaram");
        list.add("Rajavommangi");
        list.add("Ramachandrapuram");
        list.add("Rambilli");
        list.add("Rampachodavaram");
        list.add("Rangampeta");
        list.add("Ravikamatham");
        list.add("Ravulapalem");
        list.add("Rayavaram");
        list.add("Razole");
        list.add("Rolugunta");
        list.add("Rowthulapudi");
        list.add("S Rayavaram");
        list.add("Sabbavaram");
        list.add("Sakhinetipalle");
        list.add("Samalkota");
        list.add("Sankhavaram");
        list.add("Seethanagaram");
        list.add("Thallarevu");
        list.add("Thondangi");
        list.add("Tuni");
        list.add("Uppalaguptam");
        list.add("Y Ramavaram");
        list.add("Yelamanchili");
        list.add("Yeleswaram");
        list.add("Palakollu");
        list.add("Rajamahendravaram");
        list.add("Kakinada");
        list.add("Anantapur");
        list.add("Bathalapalli");
        list.add("Guntakal");
        list.add("Hindupur");
        list.add("Kadiri");
        list.add("Puttaparthy");
        list.add("Chittoor");
        list.add("Kuppam");
        list.add("Madanapalli");
        list.add("Tirumala");
        list.add("Tirupati");
        list.add("Guntur");
        list.add("Narasaraopeta");
        list.add("Piduguralla");
        list.add("Repalle");
        list.add("Tenali");
        list.add("Gudivada");
        list.add("Machilipatnam");
        list.add("Vijayawada");
        list.add("Adoni ");
        list.add("Kurnool ");
        list.add("Nandyal ");
        list.add("Chirala ");
        list.add("Markapur ");
        list.add("Ongole ");
        list.add("Gudur");
        list.add("Kavali");
        list.add("Nellore");
        list.add("Ragolu");
        list.add("Rajam");
        list.add("Sangivalasa");
        list.add("Parvathipuram");
        list.add("Eluru");
        list.add("Jangareddygudem");
        list.add("Narasapuram");
        list.add("Tanuku");
        list.add("PRODDATUR");
        list.add("Pulivendula");
        list.add("srikakulam");
        list.add("vijayanagaram");
        list.add("Bhimavaram");


        // Adding items to listview
      adapter = new ArrayAdapter<String>(this, R.layout.city_itemslist,  R.id.city_name, list);
      selected_location.setAdapter(adapter);

//      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//          @Override
//          public boolean onQueryTextSubmit(String query) {
//              return false;
//          }
//
//          @Override
//          public boolean onQueryTextChange(String newText) {
//
//              adapter.getFilter().filter(newText);
//              return false;
//          }
//      });

      current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectCity.this,MainActivity.class);
                startActivity(i);
            }
        });

        selected_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),adapter.getItem(position), Toast.LENGTH_LONG).show();

                Intent i = new Intent(SelectCity.this,MainActivity1.class);
                i.putExtra("city",adapter.getItem(position));
                startActivity(i);

//                if(getModuleName.equals("doclist"))
//                {
//                    Intent i1 = new Intent(SelectCity.this,GetDoctorsList.class);
//                    i1.putExtra("city",adapter.getItem(position));
//                    startActivity(i);
//                }
            }
        });
    }

}
