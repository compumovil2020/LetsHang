package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.letshang.R;
import com.example.letshang.model.Chat;
import com.example.letshang.providers.UserProvider;
import com.example.letshang.ui.adapter.AdminChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdminActivity extends AppCompatActivity {

    //Auth
    private FirebaseAuth mAuth;

    //Adapter
    private ListView listViewChatAdmin;
    private List<Chat> listChat = new ArrayList<Chat>();
    private AdminChatAdapter adminChatAdapter;
    private UserProvider userProvider;
    private String nameUser;
    //Database
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceNombre;
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
        setContentView(R.layout.activity_chat_admin);
        getSupportActionBar().setTitle("Chat");

        //Inflate Elements
        btEnviarMensaje = findViewById(R.id.btEnviarChatAdmin);
        mensajeAEnviar = findViewById(R.id.etEscribirMensajeChatAdmin);
        listViewChatAdmin = findViewById(R.id.lvChatAdmin);

        //Instances
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userProvider = UserProvider.getInstance();
        nameUser = userProvider.getCurrentUser().getName();
        //Save context
        context = this;

        verifyChat(mAuth.getUid());

        btEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crearMensaje(mensajeAEnviar.getText().toString(), mAuth.getUid());
                mensajeAEnviar.setText("");
            }
        });
    }

    public void crearMensaje(String mensaje, String idUsuario){
        //Today date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy, h:mm:ss aaa");

        long unixTime = System.currentTimeMillis() / 1000L;

        Map<String, Object> dato = new HashMap<>();
        dato.put("IDUsuario", idUsuario);
        dato.put("admin", false);
        dato.put("cuerpo", mensaje);
        dato.put("fecha", unixTime);


        Chat dat = new Chat(idUsuario, userProvider.getCurrentUser().getName(), mensaje, unixTime, null);

        //Crear en firebase
        String key = databaseReferenceUsuario.push().getKey();
        databaseReference = database.getReference("chats-admin");
        databaseReference.child(idUsuario).child(key).setValue(dato);
    }

    public void verifyChat(final String idUsuario){
        databaseReferenceUsuario = database.getReference("chats-admin").child(idUsuario);
        databaseReferenceUsuario.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat nuevoChat = new Chat();
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(ds.getKey().equals("IDUsuario")){
                        Log.i("CHATADMINUsuario",ds.getValue()+" ");
                        nuevoChat.setIdUsuario(ds.getValue().toString());
                        nuevoChat.setNombre(nameUser);
                    }
                    if(ds.getKey().equals("cuerpo")){
                        Log.i("CHATADMIN",ds.getValue()+" ");
                        nuevoChat.setCuerpo(ds.getValue().toString());
                    }
                    if(ds.getKey().equals("fecha")){
                        Log.i("CHATADMIN",ds.getValue()+" ");
                        nuevoChat.setFecha((long)ds.getValue());
                    }
                }
                listChat.add(nuevoChat);
                setAdapter();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setAdapter(){
        adminChatAdapter = new AdminChatAdapter(mAuth.getUid(), this.context, listChat);
        listViewChatAdmin.setAdapter(adminChatAdapter);
    }

}