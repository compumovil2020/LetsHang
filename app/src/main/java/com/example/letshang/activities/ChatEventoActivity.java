package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.letshang.R;
import com.example.letshang.model.EventChat;
import com.example.letshang.ui.adapter.EventChatAdapter;
import com.example.letshang.ui.adapter.EventsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatEventoActivity extends AppCompatActivity {

    //Constante
    private static final String PATH_CHAT_EVENT = "chats-evento";

    //Variables extras
    private String idEvento;

    //Auth
    private FirebaseAuth mAuth;

    //Adapter
    private ListView listViewChatEventos;
    private List<EventChat> listEventChat = new ArrayList<EventChat>();
    private EventChatAdapter eventChatAdapter;

    //Database
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceUsuario;
    private DatabaseReference databaseReferenceMensajes;
    private DatabaseReference databaseReferenceInfoMensajes;

    //Layout Elements
    private Button btEnviarMensaje;
    private EditText mensajeAEnviar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_evento);
        getSupportActionBar().setTitle("Chat");

        //Inflate Elements
        btEnviarMensaje = findViewById(R.id.btEnviarChatEvento);
        mensajeAEnviar = findViewById(R.id.etEscribirMensajeChatEvento);
        listViewChatEventos = findViewById(R.id.lvChatsEventoChat);

        //Instances
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //Save context
        context = this;

        //Get intent extras
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //TODO: CAMBIAR AL EXTRA QUE ES
        //idEvento = extras.getString("idevento");
        idEvento = "IDEvento1";

        //Verificar si el evento ya tiene mensajes
        verifyChat(idEvento);

        btEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crearMensaje("PRUEBAIDEVENTO", mensajeAEnviar.getText().toString(), mAuth.getUid());
                mensajeAEnviar.setText("");
            }
        });
    }

    public void crearMensaje(String idEvento, String mensaje, String idUsuario){
        //Today date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, String> dato = new HashMap<>();
        dato.put("IDUsuario", idUsuario);
        dato.put("cuerpo", mensaje);
        dato.put("fecha", dtf.format(c).toString());

        //Crear en firebase
        String key = databaseReferenceUsuario.push().getKey();
        databaseReferenceUsuario.child("chats-evento").child(idEvento).child(key).setValue(dato);
    }

    public void verifyChat(final String idEvento){
        databaseReferenceUsuario = database.getReference("chats-evento");
        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().equals(idEvento)){
                        Log.i("DATABASESTATUS",ds.getKey());
                        listEventChat.clear();

                        getMensajes(idEvento);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DATABASESTATUS","PROBLEMAS", error.toException());
            }
        });
    }

    public void getMensajes(final String idEvento){
        databaseReferenceMensajes = database.getReference("chats-evento/"+idEvento);
        databaseReferenceMensajes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Log.i("DATABASESTATUS",ds.getKey());
                    listEventChat.clear();
                    getInfoMensajes(idEvento , ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DATABASESTATUS","PROBLEMAS", error.toException());
            }
        });
    }

    public void getInfoMensajes(String idEvento, String idMensaje){
        databaseReferenceInfoMensajes = database.getReference("chats-evento/"+idEvento+"/"+idMensaje);
        listEventChat.clear();
        databaseReferenceInfoMensajes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EventChat eCInfoMensajes = new EventChat();

                int cont = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Log.i("DATABASESTATUS",ds.getKey());
                    if(ds.getKey().equals("IDUsuario")){
                        eCInfoMensajes.setIdUsuario(ds.getValue().toString());
                        cont++;
                    }

                    if(ds.getKey().equals("cuerpo")){
                        eCInfoMensajes.setCuerpo(ds.getValue().toString());
                        cont++;
                    }

                    if(ds.getKey().equals("fecha")){
                        eCInfoMensajes.setFecha(ds.getValue().toString());
                        cont++;
                    }

                }

                listEventChat.add(eCInfoMensajes);

                if(cont == 3){
                    cont = 0;
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DATABASESTATUS","PROBLEMAS", error.toException());
            }
        });
    }

    public void setAdapter(){
        eventChatAdapter = new EventChatAdapter(context, listEventChat);
        listViewChatEventos.setAdapter(eventChatAdapter);
    }
}