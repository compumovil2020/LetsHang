package com.example.letshang.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Chat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventChatAdapter extends BaseAdapter {

    private String userAuthId;
    private Context context;
    private List<Chat> chats;
    private View v;
    private ImageView cardRemitente, cardTu;

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
        cardRemitente = view.findViewById(R.id.cvImagenRemitente);
        cardTu = view.findViewById(R.id.cvTu);

        //Si es tu enviado o si es otro
        Log.i("CagadaSerrano",chats.get(i).getIdUsuario());
        if(chats.get(i).getIdUsuario().equals(this.getUserAuthId())){

            if(nombreRemitente != null && fechaRemitente != null && cuerpoRemitente != null){
                nombreRemitente.setVisibility(View.INVISIBLE);
                fechaRemitente.setVisibility(View.INVISIBLE);
                cuerpoRemitente.setVisibility(View.INVISIBLE);
            }

            if(nombreTu!= null && fechaTu != null && cuerpoTu!= null && cardTu != null){
                nombreTu.setText("Tu");

                Date c = new Date(chats.get(i).getFecha());
                SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy, h:mm:ss aaa");

                String myDate = dtf.format(c);
                fechaTu.setText(myDate);
                cuerpoTu.setText(chats.get(i).getCuerpo());

                if(chats.get(i).getFoto() != null){
                    cardTu.setImageBitmap(chats.get(i).getFoto());
                } else
                {
                    cardTu.setImageResource(R.drawable.icn_profile);
                }
            }


            llRemitente.removeAllViews();

            llTu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));

        } else {

            if(nombreTu!= null && fechaTu != null && cuerpoTu!= null){
                nombreTu.setVisibility(View.INVISIBLE);
                fechaTu.setVisibility(View.INVISIBLE);
                cuerpoTu.setVisibility(View.INVISIBLE);
            }

            if(nombreRemitente != null && fechaRemitente != null && cuerpoRemitente != null && cardRemitente != null) {
                nombreRemitente.setText(chats.get(i).getNombre());

                Date c = new Date(chats.get(i).getFecha());
                SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy, h:mm:ss aaa");

                String myDate = dtf.format(c);
                fechaRemitente.setText(myDate);
                cuerpoRemitente.setText(chats.get(i).getCuerpo());
                if(chats.get(i).getFoto() != null){
                    cardRemitente.setImageBitmap(chats.get(i).getFoto());
                } else
                {
                    cardRemitente.setImageResource(R.drawable.icn_profile);
                }
            }

            llTu.removeAllViews();

            llRemitente.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
