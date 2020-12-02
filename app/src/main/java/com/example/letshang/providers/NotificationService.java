package com.example.letshang.providers;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.letshang.DTO.AcademicEventDTO;
import com.example.letshang.DTO.GameEventDTO;
import com.example.letshang.DTO.MusicEventDTO;
import com.example.letshang.DTO.SocialEventDTO;
import com.example.letshang.DTO.SportEventDTO;
import com.example.letshang.DTO.Transformer;
import com.example.letshang.R;
import com.example.letshang.activities.ChatAdminActivity;
import com.example.letshang.activities.DescripcionEventoActivity;
import com.example.letshang.model.Chat;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.model.SocialEvent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationService extends IntentService {


    private Participant user;
    private UserProvider up;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference eventRef;
    private DatabaseReference chatRef;
    private long unixTime;
    private static final String CHANNEL_ID = "servicio_notificaciones_letshang" ;
    private int notificationId = 1;


    private static String TAG = "NotificationService";


    public NotificationService() {
        super("servicio de notificaciones");
        Log.i(TAG, "NotificationService: Starting...");
        up = UserProvider.getInstance();
        this.user = (Participant) up.getCurrentUser();
        Log.i(TAG, "Preferences: " + ((Participant)this.user).getPreferences().toString());
        unixTime = System.currentTimeMillis() / 1000L;
        Log.i(TAG, "unix time: " + unixTime);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        createNotificationChannel();

        listenForEvents();
        listenForMessages();

    }

    private void listenForMessages() {
        chatRef = database.getReference("chats-admin").child(user.getId());
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // ignora los mensajes viejos


                Chat nuevoChat = new Chat();
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(ds.getKey().equals("IDUsuario")){
                        Log.i("CHATADMINUsuario",ds.getValue()+" ");
                        nuevoChat.setIdUsuario(ds.getValue().toString());
                        nuevoChat.setNombre(user.getName());
                    }
                    if(ds.getKey().equals("admin")){
                        Log.i("CHATADMIN",ds.getValue()+" ");
                        if((Boolean)ds.getValue() == true)
                            nuevoChat.setIdUsuario("admin");
                        nuevoChat.setNombre("Admin");
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

                Log.i(TAG, "fecha " + nuevoChat.getFecha());
                Log.i(TAG, "nuevochat: " + nuevoChat.getIdUsuario());
                Log.i(TAG, "user: " + user.getId());
                if(unixTime < nuevoChat.getFecha() && !nuevoChat.getIdUsuario().equals(user.getId())){
                    Log.i(TAG, "onChildAdded:  mensaje nuevo!" );
                    Log.i(TAG, "onChildAdded: " + snapshot.getValue());
                    notificarChatAdmin(nuevoChat);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "onChildChanged: ");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "onChildRemoved: ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "onChildMoved: ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "onCancelled: ");
            }
        });


    }

    private void notificarChatAdmin(Chat nuevoChat) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.map);
        mBuilder.setContentTitle("El admin te está hablando");
        mBuilder.setContentText(nuevoChat.getCuerpo());
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent intent = new Intent(getApplicationContext(), ChatAdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true); //Remueve la notificación cuando se toca

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // notificationId es un entero unico definido para cada notificacion que se lanza
        notificationManager.notify(notificationId, mBuilder.build());
        notificationId++;
    }

    private void listenForEvents(){
        DatabaseReference sportEventRef = database.getReference("events").child("sport-event");
        DatabaseReference academicEventRef = database.getReference("events").child("academic-event");
        DatabaseReference musicEventRef = database.getReference("events").child("music-event");
        DatabaseReference socialEventRef = database.getReference("events").child("social-event");
        DatabaseReference gameEventRef = database.getReference("events").child("game-event");



        musicEventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // la re chambonada, si han pasado menos de 5 segundos desde que se instancio la clase, se ignora
                // esto es porque el onChildAdded trae todos los hijos de la BD, no solo los se meten nuevos
                // y aca solo importan los nuevos

                Log.i(TAG, "onChildAdded: path" + snapshot.getRef());

                if( (System.currentTimeMillis() / 1000L) - 5 < unixTime){

                    return;
                }

                Log.i(TAG, "onChildAdded evento: " + snapshot.getValue());

                MusicEventDTO dto = snapshot.getValue(MusicEventDTO.class);
                Event e = Transformer.transform(dto);
                e.setID(snapshot.getKey());

                if(esInteresante(e) && !dto.getHostName().equals(user.getName())){
                    notifyEvent(e);
                }

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

        sportEventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // la re chambonada, si han pasado menos de 5 segundos desde que se instancio la clase, se ignora
                // esto es porque el onChildAdded trae todos los hijos de la BD, no solo los se meten nuevos
                // y aca solo importan los nuevos

                Log.i(TAG, "onChildAdded: path" + snapshot.getRef());

                if( (System.currentTimeMillis() / 1000L) - 5 < unixTime){

                    return;
                }

                Log.i(TAG, "onChildAdded evento: " + snapshot.getValue());
                SportEventDTO dto = snapshot.getValue(SportEventDTO.class);
                Event e = Transformer.transform(dto);
                e.setID(snapshot.getKey());

                if(esInteresante(e) && !dto.getHostName().equals(user.getName())){
                    notifyEvent(e);
                }

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

        socialEventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // la re chambonada, si han pasado menos de 5 segundos desde que se instancio la clase, se ignora
                // esto es porque el onChildAdded trae todos los hijos de la BD, no solo los se meten nuevos
                // y aca solo importan los nuevos

                Log.i(TAG, "onChildAdded: path" + snapshot.getRef());

                if( (System.currentTimeMillis() / 1000L) - 5 < unixTime){
                    return;
                }

                Log.i(TAG, "onChildAdded evento: " + snapshot.getValue());

                SocialEventDTO dto = snapshot.getValue(SocialEventDTO.class);
                Event e = Transformer.transform(dto);
                e.setID(snapshot.getKey());

                if(esInteresante(e) && !dto.getHostName().equals(user.getName())){
                    notifyEvent(e);
                }

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

        gameEventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // la re chambonada, si han pasado menos de 5 segundos desde que se instancio la clase, se ignora
                // esto es porque el onChildAdded trae todos los hijos de la BD, no solo los se meten nuevos
                // y aca solo importan los nuevos

                Log.i(TAG, "onChildAdded: path" + snapshot.getRef());

                if( (System.currentTimeMillis() / 1000L) - 5 < unixTime){

                    return;
                }

                Log.i(TAG, "onChildAdded evento: " + snapshot.getValue());

                GameEventDTO dto = snapshot.getValue(GameEventDTO.class);
                Event e = Transformer.transform(dto);
                e.setID(snapshot.getKey());

                if(esInteresante(e) && !dto.getHostName().equals(user.getName())){
                    notifyEvent(e);
                }

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

        academicEventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // la re chambonada, si han pasado menos de 5 segundos desde que se instancio la clase, se ignora
                // esto es porque el onChildAdded trae todos los hijos de la BD, no solo los se meten nuevos
                // y aca solo importan los nuevos

                Log.i(TAG, "onChildAdded: path" + snapshot.getRef());

                if( (System.currentTimeMillis() / 1000L) - 5 < unixTime){

                    return;
                }

                Log.i(TAG, "onChildAdded evento: " + snapshot.getValue());

                AcademicEventDTO dto = snapshot.getValue(AcademicEventDTO.class);
                Event e = Transformer.transform(dto);
                e.setID(snapshot.getKey());

                if(esInteresante(e) && !dto.getHostName().equals(user.getName())){
                    notifyEvent(e);
                }

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

    private boolean esInteresante(Event e){

        return true;
    }

    private void notifyEvent(Event e){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.map);
        mBuilder.setContentTitle("Este evento te puede interessar!");
        mBuilder.setContentText(e.getTitle());
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent intent = new Intent(getApplicationContext(), DescripcionEventoActivity.class);
        intent.putExtra("idevento", e.getID());
        intent.putExtra("from", "principal");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true); //Remueve la notificación cuando se toca

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // notificationId es un entero unico definido para cada notificacion que se lanza
        notificationManager.notify(notificationId, mBuilder.build());
        notificationId++;

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //IMPORTANCE_MAX MUESTRA LA NOTIFICACIÓN ANIMADA
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
