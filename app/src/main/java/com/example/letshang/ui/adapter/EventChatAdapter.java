package com.example.letshang.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.letshang.R;
import com.example.letshang.model.EventChat;
import com.example.letshang.ui.dialog.CircleTransform;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EventChatAdapter extends BaseAdapter {

    private String userAuthId;
    private Context context;
    private List<EventChat> eventChats;
    private View v;
    private ImageView cardRemitente, cardTu;

    //Database for de name of the user
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public EventChatAdapter (String userAuthId, Context context, List<EventChat> eventChats){
        this.userAuthId = userAuthId;
        this.context = context;
        this.eventChats = eventChats;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
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
        if(eventChats.get(i).getIdUsuario().equals(this.getUserAuthId())){

            nombreRemitente.setVisibility(View.INVISIBLE);
            fechaRemitente.setVisibility(View.INVISIBLE);
            cuerpoRemitente.setVisibility(View.INVISIBLE);

            nombreTu.setText("Tu");
            fechaTu.setText(eventChats.get(i).getFecha());
            cuerpoTu.setText(eventChats.get(i).getCuerpo());

            if(eventChats.get(i).getFoto() != null){
                cardTu.setImageBitmap(eventChats.get(i).getFoto());
            } else
            {
                cardTu.setImageResource(R.drawable.icn_profile);
            }

            //llRemitente.setVisibility(View.INVISIBLE);
            //llTu.setBackgroundColor(R.color.tuColor);
            llRemitente.removeAllViews();

            llTu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));

        } else {

            nombreTu.setVisibility(View.INVISIBLE);
            fechaTu.setVisibility(View.INVISIBLE);
            cuerpoTu.setVisibility(View.INVISIBLE);

            nombreRemitente.setText(eventChats.get(i).getNombre());
            fechaRemitente.setText(eventChats.get(i).getFecha());
            cuerpoRemitente.setText(eventChats.get(i).getCuerpo());

            if(eventChats.get(i).getFoto() != null){
                cardRemitente.setImageBitmap(eventChats.get(i).getFoto());
            } else
            {
                cardRemitente.setImageResource(R.drawable.icn_profile);
            }

            //llRemitente.setBackgroundColor(R.color.colorPrimary);
            //llTu.setVisibility(View.INVISIBLE);
            llTu.removeAllViews();

            llRemitente.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
