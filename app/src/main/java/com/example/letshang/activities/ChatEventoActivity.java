package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.letshang.R;
import com.example.letshang.model.EventChat;
import com.example.letshang.ui.adapter.EventChatAdapter;
import com.example.letshang.ui.adapter.EventsAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatEventoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ListView listViewChatEventos;
    private List<EventChat> listEventChat = new ArrayList<EventChat>();
    private EventChatAdapter eventChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_evento);

        getSupportActionBar().setTitle("Chat");
        mAuth = FirebaseAuth.getInstance();

        listViewChatEventos = findViewById(R.id.lvChatsEventoChat);
        EventChat eC = new EventChat("Tu","MENSAJE NUMERO 1 PARA INTENTO DE TU","2020-09-18");
        listEventChat.add(eC);

        EventChat eC2 = new EventChat("Otro1","MENSAJE NUMERO 1 PARA INTENTO DE OTRO1","2020-09-19");
        listEventChat.add(eC2);

        EventChat eC3 = new EventChat("Otro2","MENSAJE NUMERO 1 PARA INTENTO DE OTRO2","2020-09-18");
        listEventChat.add(eC3);

        eventChatAdapter = new EventChatAdapter(this, listEventChat);
        listViewChatEventos.setAdapter(eventChatAdapter);
    }
}