package com.example.e145322j.find_me_toilet;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e145322j.find_me_toilet.adapter.ToiletAdapter;
import com.example.e145322j.find_me_toilet.data.Toilet;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public static int PLACE_PICKER_REQUEST = 1;
    public static int NUMBER_TUPLE = 1;

    private ArrayList<Toilet> toilets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toilets = Toilet.loadData(this);
        Button btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPlace();
            }
        });

        //spinner pour le choix de nombre de tuple
        Spinner spin = (Spinner)findViewById(R.id.spinner);
        ArrayList nbDisplay = new ArrayList();
        for(int i = 1; i < 11; i++){
            nbDisplay.add(i);
        }
        ArrayAdapter<Integer> aa = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, nbDisplay);
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NUMBER_TUPLE = Integer.parseInt(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //initialisation de listview
        ListView lv = (ListView) findViewById(R.id.list_toilet);
        lv.setAdapter(new ToiletAdapter(this, toilets));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toilet toilet = (Toilet) parent.getItemAtPosition(position);
                goDetailToilet(toilet);
            }
        });

       

    }


    public void goDetailToilet(Toilet toilet){
        Gson gson = new Gson();
        String toiletJson = gson.toJson(toilet, Toilet.class);
        Intent in = new Intent(this, ToiletActivity.class);
        in.putExtra(Intent.EXTRA_TEXT, toiletJson);
        startActivity(in);
    }


    public void goPlace(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }catch(GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                ArrayList<Toilet> closest = Toilet.getClosestToilet(toilets, place.getLatLng(), NUMBER_TUPLE);
                fillListView(closest);
            }
        }
    }

    public void fillListView(ArrayList<Toilet> closest){
        ListView lv = (ListView)findViewById(R.id.list_toilet);
        lv.setVisibility(View.VISIBLE);
        lv.setAdapter(new ToiletAdapter(this, closest));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
