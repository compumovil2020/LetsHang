package com.example.letshang.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.example.letshang.ui.dialog.CircleTransform;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.squareup.picasso.Transformation;

import static android.content.ContentValues.TAG;

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
    public Event getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
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
        ImageView imagen;
        titulo = view.findViewById(R.id.tvTituloEventsLayout);
        fecha = view.findViewById(R.id.tvFechaEventsLayout);
        lugar = view.findViewById(R.id.tvLugarEventsLayout);
        host = view.findViewById(R.id.tvHostEventsLayout);
        imagen = view.findViewById(R.id.imImagenEventsLayout);


        //Asignar titulo
        titulo.setText(events.get(i).getTitle());

        //Asignar fecha
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String deit =  formatter.format(this.events.get(i).getStartDate().getTime())  + " - " + formatter.format(events.get(i).getEndDate().getTime());
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
        Log.i(TAG, "llamando al metodo el evento " + events.get(i).getTitle() + " con id " + events.get(i).getID());
        Log.i(TAG, "la lista tiene tam " + events.size());
        host.setText(EventProvider.getInsatance().getEventHostName(events.get(i).getID()));

        //Asignar foto
        String imageUri = "https://picsum.photos/200?random=" + i;
        Picasso.with(context).load(imageUri).transform(new CircleTransform()).into(imagen);

        return view;
    }







}