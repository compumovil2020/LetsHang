package com.example.letshang.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Chat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EventChatAdapter extends BaseAdapter {

    private String userAuthId;
    private Context context;
    private List<Chat> chats;
    private View v;

    //Database for de name of the user
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public EventChatAdapter (String userAuthId, Context context, List<Chat> chats){
        this.userAuthId = userAuthId;
        this.context = context;
        this.chats = chats;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Chat getItem(int i) {
        return chats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Verificar vista

        database = FirebaseDatabase.getInstance();

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            view = LayoutInflater.from(this.context).inflate(R.layout.eventchatslayout,viewGroup,false);
        }

        //Inflar
        TextView nombreRemitente, fechaRemitente, nombreTu, fechaTu, cuerpoRemitente, cuerpoTu;
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
        if(chats.get(i).getIdUsuario().equals(this.getUserAuthId())){

            if(nombreRemitente != null){
                nombreRemitente.setVisibility(View.INVISIBLE);
            }
            if(fechaRemitente != null){
                fechaRemitente.setVisibility(View.INVISIBLE);
            }
            if(cuerpoRemitente != null){
                cuerpoRemitente.setVisibility(View.INVISIBLE);
            }

            if(nombreTu != null && fechaTu != null && cuerpoTu!= null){
                nombreTu.setText("Tu");
                fechaTu.setText(chats.get(i).getFecha());
                cuerpoTu.setText(chats.get(i).getCuerpo());
            }
            llRemitente.removeAllViews();

            llTu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));

        } else {

            if(nombreTu  != null){
                nombreTu.setVisibility(View.INVISIBLE);
            }
            if(fechaTu != null){
                fechaTu.setVisibility(View.INVISIBLE);
            }
            if(cuerpoTu != null){
                cuerpoTu.setVisibility(View.INVISIBLE);
            }

            if(nombreRemitente != null && fechaRemitente != null && cuerpoRemitente != null){
                nombreRemitente.setText(chats.get(i).getNombre());
                fechaRemitente.setText(chats.get(i).getFecha());
                cuerpoRemitente.setText(chats.get(i).getCuerpo());
            }
            llTu.removeAllViews();

            llRemitente.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
