package com.example.e145322j.find_me_toilet;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.e145322j.find_me_toilet.data.Toilet;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


public class ToiletActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private Toilet toilet;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet);
        Bundle bund = getIntent().getExtras();
        String toiletJson = bund.getString(Intent.EXTRA_TEXT);
        if(toiletJson != null){
            Gson gson = new Gson();
            toilet = gson.fromJson(toiletJson, Toilet.class);
        }
        this.setTitle(toilet.addressToString());
        TextView type = (TextView)findViewById(R.id.t_type);
        type.setText("Type: "+toilet.getType());
        TextView handiAcces = (TextView)findViewById(R.id.t_access_handi);
        if(toilet.isHandiAccess()) {
            handiAcces.setText("Accès handicapé: oui");
        }else{
            handiAcces.setText("Accès handicapé: non");
        }
        TextView automatique = (TextView)findViewById(R.id.t_auto);
        if(toilet.isAutomatique()){
            automatique.setText("Automatique: oui");
        }else{
            automatique.setText("Automatique: non");
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
       /* StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);*/




    }

    @Override
    public void onMapReady(GoogleMap maps) {
        map = maps;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(toilet != null) {
            double lat = toilet.getLatitude();
            double lng = toilet.getLongitude();
            maps.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(toilet.getAdresse()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 18));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toilet, menu);
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

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        double lat = toilet.getLatitude();
        double lng = toilet.getLongitude();
        streetViewPanorama.setPosition(new LatLng(lat, lng));

    }
}
