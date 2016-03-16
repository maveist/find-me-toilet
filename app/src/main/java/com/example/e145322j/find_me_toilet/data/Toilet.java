package com.example.e145322j.find_me_toilet.data;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e145322j.find_me_toilet.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by E145322J on 16/03/16.
 */
public class Toilet {
    public static String URL_JSON = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00170/Toilettes_publiques_nm_STBL/content";

    private String id; //correspondant à name dans le json
    private String adresse;
    private String commune;
    private String pole;
    private String type;
    private boolean automatique;
    private boolean handiAccess;
    private String horaires;
    private String location;

    public Toilet(String id, String adresse, String commune, String pole, String type, boolean automatique, boolean handiAccess, String horaires, String location) {
        this.id = id;
        this.adresse = adresse;
        this.commune = commune;
        this.pole = pole;
        this.type = type;
        this.automatique = automatique;
        this.handiAccess = handiAccess;
        this.horaires = horaires;
        this.location = location;
    }

    public Toilet(JSONObject json){
        try{
            id = json.getJSONObject("geo").getString("name");
            adresse = json.getString("ADRESSE");
            commune = json.getString("COMMUNE");
            pole = json.getString("POLE");
            type = json.getString("TYPE");
            automatique = json.getString("AUTOMATIQUE").equals("oui");
            handiAccess = json.getString("ACCESSIBILITE_PMR").equals("oui");
            location = json.getString("_l");
        }catch (JSONException e){
            e.printStackTrace();
        }


    }

    public static ArrayList<Toilet> getClosestToilet(ArrayList<Toilet> toilets, LatLng location){
            ArrayList<Toilet> closest = null;
            double minDist = 1000000;
            double lat = location.latitude;
            double lng = location.longitude;
            for(Toilet t : toilets){
                double dist = Math.sqrt(Math.pow((t.getLatitude() - lat), 2)+Math.pow((t.getLongitude() - lng),2));
                if(dist < minDist){
                    minDist = dist;
                    closest.add(t);
                }
            }
        return closest;
    }

    public double getLatitude(){
        String locT = location;
        locT = locT.replace("[", "");
        locT = locT.replace("]", "");
        String[] latlng = locT.split(",");
        return Double.parseDouble(latlng[0]);
    }

    public double getLongitude(){
        String locT = location;
        locT = locT.replace("[", "");
        locT = locT.replace("]", "");
        String[] latlng = locT.split(",");
        return Double.parseDouble(latlng[1]);
    }

    public static ArrayList<Toilet> loadData(Context context){
       final ArrayList<Toilet> toilet = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_JSON, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("data");
                            int len = array.length();
                            for (int i = 0; i < len; i++) {
                                Toilet t = new Toilet(array.getJSONObject(i));
                                toilet.add(t);
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },  new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });
        queue.add(jsObjRequest);
        return toilet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutomatique() {
        return automatique;
    }

    public void setAutomatique(boolean automatique) {
        this.automatique = automatique;
    }

    public boolean isHandiAccess() {
        return handiAccess;
    }

    public void setHandiAccess(boolean handiAccess) {
        this.handiAccess = handiAccess;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
