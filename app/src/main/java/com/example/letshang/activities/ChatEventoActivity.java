package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.letshang.R;
import com.example.letshang.model.EventChat;
import com.example.letshang.ui.adapter.EventChatAdapter;
import com.example.letshang.ui.adapter.EventsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatEventoActivity extends AppCompatActivity {

    //Auth
    private FirebaseAuth mAuth;

    //Adapter
    private ListView listViewChatEventos;
    private List<EventChat> listEventChat = new ArrayList<EventChat>();
    private EventChatAdapter eventChatAdapter;

    //Database
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

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

        //Instances
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //Save context
        context = this;

        //Asignar adapter
        listViewChatEventos = findViewById(R.id.lvChatsEventoChat);
        EventChat eC = new EventChat("Tu","MENSAJE NUMERO 1 PARA INTENTO DE TU","2020-09-18");
        listEventChat.add(eC);

        EventChat eC2 = new EventChat("Otro1","MENSAJE NUMERO 1 PARA INTENTO DE OTRO1 MENSAJE NUMERO 1 PARA INTENTO DE OTRO1 MENSAJE NUMERO 1 PARA INTENTO DE OTRO1 MENSAJE NUMERO 1 PARA INTENTO DE OTRO1 MENSAJE NUMERO 1 PARA INTENTO DE OTRO1","2020-09-19");
        listEventChat.add(eC2);

        EventChat eC3 = new EventChat("Otro2","MENSAJE NUMERO 1 PARA INTENTO DE OTRO2","2020-09-18");
        listEventChat.add(eC3);

        eventChatAdapter = new EventChatAdapter(this, listEventChat);

        listViewChatEventos.setAdapter(eventChatAdapter);

        btEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventChat enviar = new EventChat("Tu", mensajeAEnviar.getText().toString(), "2020-11-28");
                listEventChat.add(enviar);
                eventChatAdapter = new EventChatAdapter(context, listEventChat);
                listViewChatEventos.setAdapter(eventChatAdapter);
            }
        });
    }
}