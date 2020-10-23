package com.example.letshang.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends BaseAdapter {

    private Context context;
    private List<Event> events;

    public EventsAdapter (Context context, List<Event> events){
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Verificar vista
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.from(this.context).inflate(R.layout.eventslayout,viewGroup,false);
        }

        //Inflar y declarar
        TextView titulo, fecha, lugar, host;
        titulo = view.findViewById(R.id.tvTituloEventsLayout);
        fecha = view.findViewById(R.id.tvFechaEventsLayout);
        lugar = view.findViewById(R.id.tvLugarEventsLayout);
        host = view.findViewById(R.id.tvHostEventsLayout);

        //Asignar titulo
        titulo.setText(events.get(i).getTitle());

        //Asignar fecha
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String deit =  formatter.format(this.events.get(i).getStartDate())  + " - " + formatter.format(events.get(i).getEndDate());
        fecha.setText(deit);

        //Asignar lugar
        LatLng latLng = events.get(i).getLocation();
        String city = "";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            city = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lugar.setText(city);

        //Asignar host
        host.setText(EventProvider.getInsatance().getEventHost(events.get(i).getID()).getName());

        return view;
    }



}
