package com.example.letshang.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.letshang.R;

import java.util.ArrayList;

public class TagAdapter extends ArrayAdapter<String> {
    public TagAdapter(Context context, ArrayList<String> tags) {
        super(context, 0, tags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String s = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_item, parent, false);
        }
        // Lookup view for data population
        Button button = (Button) convertView.findViewById(R.id.btnTagItem);
        button.setText(s);
        // Populate the data into the template view using the data object
        // Return the completed view to render on screen
        return convertView;
    }
}
