package com.example.letshang.activities;

import androidx.annotation.NonNull;
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
import com.example.letshang.ui.adapter.AdminChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
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

        Map<String, String> dato = new HashMap<>();
        dato.put("IDUsuario", idUsuario);
        dato.put("cuerpo", mensaje);
        dato.put("fecha", dtf.format(c).toString());

        //Crear en firebase
        String key = databaseReferenceUsuario.push().getKey();
        databaseReference = database.getReference("chats-admin");
        databaseReference.child(idUsuario).child(key).setValue(dato);
    }

    public void verifyChat(final String idUsuario){
        databaseReferenceUsuario = database.getReference("chats-admin");
        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().equals(idUsuario)){
                        Log.i("DATABASESTATUSVeri",ds.getKey());
                        listChat.clear();
                        getMensajes(idUsuario);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DATABASESTATUS","PROBLEMAS", error.toException());
            }
        });
    }
    public void getMensajes(final String idUsuario){
        databaseReferenceMensajes = database.getReference("chats-admin/"+idUsuario);
        databaseReferenceMensajes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    listChat.clear();
                    getInfoMensajes(idUsuario , ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DATABASESTATUS","PROBLEMAS", error.toException());
            }
        });
    }
    public void getInfoMensajes(String idUsuario, String idMensaje){
        databaseReferenceInfoMensajes = database.getReference("chats-admin/"+idUsuario+"/"+idMensaje);
        listChat.clear();
        databaseReferenceInfoMensajes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chat eCInfoMensajes = new Chat();

                int cont = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Log.i("DATABASESInfoMensajes",ds.getKey()+" "+ds.getValue());
                    if(ds.getKey().equals("IDUsuario")){
                        eCInfoMensajes.setIdUsuario(ds.getValue().toString());
                        cont++;
                    }
                    if(ds.getKey().equals("admin")){
                        eCInfoMensajes.setIdUsuario("admin");
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

                listChat.add(eCInfoMensajes);

                if(cont == 3){
                    cont = 0;
                    getNameOfUser(eCInfoMensajes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DATABASESTATUS","PROBLEMAS", error.toException());
            }
        });
    }
    public void getNameOfUser(final Chat eCInfoMensajes){
        Log.i("DATABASESTATUSName",eCInfoMensajes.getIdUsuario());
        databaseReferenceNombre = database.getReference("users/"+eCInfoMensajes.getIdUsuario());
        databaseReferenceNombre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int c = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().equals("name")){
                        Log.i("DATABASESTATUSName",ds.getValue()+" ");
                        eCInfoMensajes.setNombre(ds.getValue().toString());
                        c++;
                    }
                }
                if(c == 1){
                    c = 0;
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
        adminChatAdapter = new AdminChatAdapter(mAuth.getUid(), this.context, listChat);
        listViewChatAdmin.setAdapter(adminChatAdapter);
    }
}