package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.letshang.R;
import com.example.letshang.model.Chat;
import com.example.letshang.providers.UserProvider;
import com.example.letshang.ui.adapter.EventChatAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private List<Chat> listChat = new ArrayList<Chat>();
    private EventChatAdapter eventChatAdapter;
    private Map<String, Bitmap> foticos;
    private UserProvider userProvider;
    //Database
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceNombre;
    private DatabaseReference databaseReferenceUsuario;
    private DatabaseReference databaseReferenceMensajes;
    private DatabaseReference databaseReferenceInfoMensajes;

    private StorageReference mStorageRef;

    //Layout Elements
    private Button btEnviarMensaje;
    private EditText mensajeAEnviar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_evento);
        getSupportActionBar().setTitle("Chat");
        userProvider = UserProvider.getInstance();
        //Inflate Elements
        btEnviarMensaje = findViewById(R.id.btEnviarChatEvento);
        mensajeAEnviar = findViewById(R.id.etEscribirMensajeChatEvento);
        listViewChatEventos = findViewById(R.id.lvChatsEventoChat);

        //Instances
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Save context
        context = this;

        //Get intent extras
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //TODO: CAMBIAR AL EXTRA QUE ES
        idEvento = extras.getString("idevento");
        Log.i("DATABASESTATUS",idEvento);
        //idEvento = "IDEvento1";
        foticos = new HashMap<String, Bitmap>();
        //Verificar si el evento ya tiene mensajes
        verifyChat(idEvento);

        btEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crearMensaje(idEvento, mensajeAEnviar.getText().toString(), mAuth.getUid());
                mensajeAEnviar.setText("");
            }
        });
    }

    public void crearMensaje(String idEvento, String mensaje, String idUsuario){
        //Today date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");


        long unixTime = System.currentTimeMillis() / 1000L;


        Map<String, Object> dato = new HashMap<>();
        dato.put("IDUsuario", idUsuario);
        dato.put("Nombre",userProvider.getCurrentUser().getName());
        dato.put("cuerpo", mensaje);
        dato.put("fecha", unixTime);


        //Crear en firebase
        String key = databaseReferenceUsuario.push().getKey();
        databaseReference = database.getReference("chats-evento");
        databaseReference.child(idEvento).child(key).setValue(dato);
    }

    public void verifyChat(final String idEvento){
        databaseReferenceUsuario = database.getReference("chats-evento").child(idEvento);
        databaseReferenceUsuario.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat eCInfoMensajes = new Chat();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Log.i("DATABASESTATUS",ds.getKey());
                    if(ds.getKey().equals("IDUsuario")){
                        eCInfoMensajes.setIdUsuario(ds.getValue().toString());
                    }
                    if(ds.getKey().equals("Nombre")){
                        eCInfoMensajes.setNombre(ds.getValue().toString());
                    }
                    if(ds.getKey().equals("cuerpo")){
                        eCInfoMensajes.setCuerpo(ds.getValue().toString());
                    }
                    if(ds.getKey().equals("fecha")){
                        eCInfoMensajes.setFecha((long)ds.getValue());
                    }
                }
                getProfilePhoto(eCInfoMensajes);
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




    public void getProfilePhoto(final Chat eCInfoMensajes){
        if(!foticos.containsKey(eCInfoMensajes.getIdUsuario())){
            try {
                final File localFile = File.createTempFile("images","jpg");
                StorageReference imageRef = mStorageRef.child("images/profile/"+eCInfoMensajes.getIdUsuario()+"/profilePic.jpg");
                imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap selectedImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        eCInfoMensajes.setFoto(selectedImage);
                        foticos.put(eCInfoMensajes.getIdUsuario(),selectedImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ERROR", "NO SE CARGO IMAGEN");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            eCInfoMensajes.setFoto(foticos.get(eCInfoMensajes.getIdUsuario()));
        }
        listChat.add(eCInfoMensajes);
        setAdapter();
    }

    public void setAdapter(){
        eventChatAdapter = new EventChatAdapter(mAuth.getUid(),context, listChat);
        listViewChatEventos.setAdapter(eventChatAdapter);
    }
}