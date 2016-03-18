package com.example.e145322j.find_me_toilet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.example.e145322j.find_me_toilet.R;
import com.example.e145322j.find_me_toilet.data.Toilet;

import java.util.ArrayList;

/**
 * Created by E145322J on 18/03/16.
 */
public class ToiletAdapter extends ArrayAdapter<Toilet> {

    public ToiletAdapter(Activity context, ArrayList<Toilet> toilets){
        super(context, R.layout.activity_main, toilets);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

    }


}
