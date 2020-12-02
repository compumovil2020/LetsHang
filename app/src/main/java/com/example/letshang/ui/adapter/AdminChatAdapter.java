package com.example.letshang.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdminChatAdapter extends BaseAdapter {
    private String userAuthid;
    private Context context;
    private List<Chat> chats;

    //Database for de name of the user
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public AdminChatAdapter(String userAuthid, Context context, List<Chat> chats){
        this.userAuthid = userAuthid;
        this.context = context;
        this.chats = chats;
    }

    public String getUserAuthid(){return this.userAuthid;}

    public void setUserAuthId(String userAuthId) {
        this.userAuthid = userAuthId;
    }

    public int getCount() {
        return chats.size();
    }

    public Chat getItem(int i) {
        return chats.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Verificar vista

        database = FirebaseDatabase.getInstance();

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            view = LayoutInflater.from(this.context).inflate(R.layout.adminchatlayout,viewGroup,false);
        }

        //Inflar
        TextView nombreRemitente, fechaRemitente, nombreTu, fechaTu, cuerpoRemitente, cuerpoTu;
        LinearLayout llRemitente, llTu;
        nombreRemitente = view.findViewById(R.id.tvAdminNombre);
        fechaRemitente = view.findViewById(R.id.tvFechaChatAdmin);
        cuerpoRemitente = view.findViewById(R.id.tvCuerpoAdminChat);
        nombreTu = view.findViewById(R.id.tvNombreTuAdminChat);
        fechaTu = view.findViewById(R.id.tvFechaTuAdminChat);
        cuerpoTu = view.findViewById(R.id.tvCuerpoTuAdminChat);
        llRemitente = view.findViewById(R.id.llAdminChat);
        llTu = view.findViewById(R.id.llTuAdminChat);

        //Log.i("Nuevo mensaje",chats.get(i).getIdUsuario());

        //Si es tu enviado o si es otro
        if(chats.get(i).getIdUsuario().equals(this.getUserAuthid())){

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
                nombreTu.setText(chats.get(i).getNombre());

                Date c = new Date(chats.get(i).getFecha());
                SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy, h:mm:ss aaa");

                String myDate = dtf.format(c);
                fechaTu.setText(myDate);
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
                nombreRemitente.setText("Admin");


                Date c = new Date(chats.get(i).getFecha());
                SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy, h:mm:ss aaa");

                String myDate = dtf.format(c);
                fechaRemitente.setText(myDate);
                cuerpoRemitente.setText(chats.get(i).getCuerpo());
            }

            llTu.removeAllViews();

            llRemitente.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
