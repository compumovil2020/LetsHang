package com.example.letshang.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.EventChat;

import java.util.List;

public class EventChatAdapter extends BaseAdapter {

    private Context context;
    private List<EventChat> eventChats;

    public EventChatAdapter (Context context, List<EventChat> eventChats){
        this.context = context;
        this.eventChats = eventChats;
    }

    @Override
    public int getCount() {
        return eventChats.size();
    }

    @Override
    public EventChat getItem(int i) {
        return eventChats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Verificar vista

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            view = LayoutInflater.from(this.context).inflate(R.layout.eventchatslayout,viewGroup,false);

        }

        //Inflar
        TextView nombreRemitente, fechaRemitente, cuerpoRemitente, nombreTu, fechaTu, cuerpoTu;
        LinearLayout llRemitente, llTu;
        nombreRemitente = view.findViewById(R.id.tvNombreRemitenteEventsChat);
        fechaRemitente = view.findViewById(R.id.tvFechaRemitenteEventsChat);
        cuerpoRemitente = view.findViewById(R.id.tvCuerpoRemitenteEventsChat);
        nombreTu = view.findViewById(R.id.tvNombreTuEventsChat);
        fechaTu = view.findViewById(R.id.tvFechaTuEventsChat);
        cuerpoTu = view.findViewById(R.id.tvCuerpoTuEventsChat);
        llRemitente = view.findViewById(R.id.llRemitenteEventsChat);
        llTu = view.findViewById(R.id.llTuEventsChat);


        //Si es tu enviado o si es otro
        if(eventChats.get(i).getIdUsuario().equals("Tu")){
            nombreRemitente.setVisibility(View.INVISIBLE);
            fechaRemitente.setVisibility(View.INVISIBLE);
            cuerpoRemitente.setVisibility(View.INVISIBLE);

            nombreTu.setText(eventChats.get(i).getIdUsuario());
            fechaTu.setText(eventChats.get(i).getFecha());
            cuerpoTu.setText(eventChats.get(i).getCuerpo());

            //llRemitente.setVisibility(View.INVISIBLE);
            llTu.setBackgroundColor(R.color.tuColor);
            llRemitente.removeAllViews();
            llTu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));
        } else {

            nombreTu.setVisibility(View.INVISIBLE);
            fechaTu.setVisibility(View.INVISIBLE);
            cuerpoTu.setVisibility(View.INVISIBLE);

            nombreRemitente.setText(eventChats.get(i).getIdUsuario());
            fechaRemitente.setText(eventChats.get(i).getFecha());
            cuerpoRemitente.setText(eventChats.get(i).getCuerpo());

            llRemitente.setBackgroundColor(R.color.colorPrimary);
            //llTu.setVisibility(View.INVISIBLE);
            llTu.removeAllViews();
            llRemitente.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
