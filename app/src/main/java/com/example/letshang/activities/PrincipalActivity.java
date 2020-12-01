package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.Event;
import com.example.letshang.model.GameEvent;
import com.example.letshang.model.MusicEvent;
import com.example.letshang.model.SocialEvent;
import com.example.letshang.model.SportEvent;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.example.letshang.ui.adapter.EventsAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity{

    private ImageView ivFiltrar;
    private ConstraintLayout eventLayout;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private TextView btnMap;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean cbMusical, cbAcademico, cbJuego, cbSocial, cbDeporte;
    // should go in utils
    private FirebaseAuth mAuth;

    private EventProvider eventProvider;
    private ListView listViewEvents;
    private List<Event> listEvents;
    EventsAdapter eventsAdapter;
    private UserProvider up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Initialize Firebase Auth
        // should go in utils
        mAuth = FirebaseAuth.getInstance();
        up.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // set action bar title
        getSupportActionBar().setTitle("Feed");

        eventProvider = EventProvider.getInsatance();

        //inflate elements
        ivFiltrar = findViewById(R.id.ivFiltrarPrincipal);
        //eventLayout = findViewById(R.id.eventLayout);
        drawerLayout = findViewById(R.id.principal_drawer_layout);
        navView = findViewById(R.id.principal_nav_view);
        btnMap = findViewById(R.id.btnMapaPrincipal);

        listViewEvents = findViewById(R.id.listEventosPrincipal);

        cbAcademico = getIntent().getBooleanExtra("cbAcademico", true);
        cbDeporte = getIntent().getBooleanExtra("cbDeporte", true);
        cbMusical = getIntent().getBooleanExtra("cbMusical", true);
        cbJuego = getIntent().getBooleanExtra("cbJuego", true);
        cbSocial = getIntent().getBooleanExtra("cbSocial", true);

        //setup side menu
        setupMenu();

        //setup listeners
        ivFiltrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , FiltersActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        listEvents = eventProvider.getAllEventsFromDBB();

        Log.i("TAM", "" + listEvents.size());

        eventsAdapter = new EventsAdapter(this, listEvents);
        listViewEvents.setAdapter(eventsAdapter);

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event e = (Event) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), DescripcionEventoActivity.class);
                intent.putExtra("idevento", "" + e.getID());
                intent.putExtra("from", "Principal");
                startActivity(intent);
            }
        });

    }

    private List<Event> filterEvents(){

        List<Event> filter = new ArrayList<>();

        for(Event actual: listEvents){
            if(cbSocial && actual instanceof SocialEvent){
                filter.add(actual);
            }
            if(cbJuego && actual instanceof GameEvent){
                filter.add(actual);
            }
            if(cbDeporte && actual instanceof SportEvent){
                filter.add(actual);
            }
            if(cbAcademico && actual instanceof AcademicEvent){
                filter.add(actual);
            }
            if(cbMusical && actual instanceof MusicEvent){
                filter.add(actual);
            }
        }
        return filter;
    }

    void setupMenu(){

        menuToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.menu_open_reader,
                R.string.menu_close_reader);

        drawerLayout.setDrawerListener(menuToggle);
        menuToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() ==  R.id.item_menu_administrar_perfil){
                    Intent intent = new Intent(getApplicationContext() , InformacionPerfilActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_sugerir){
                    Intent intent = new Intent(getApplicationContext() , CrearSugerenciaActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos){
                    //does nothing
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    Intent intent = new Intent(getApplicationContext() , EventosInscritosActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_logout){
                    mAuth.signOut();
                    mGoogleSignInClient.signOut();
                    Intent intent = new Intent(getApplicationContext() , StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_pago){
                    Toast.makeText(getApplicationContext(), item.getTitle() ,Toast.LENGTH_SHORT ).show();

                }
                if(item.getItemId() ==  R.id.item_menu_crear_evento){
                    Intent intent = new Intent(getApplicationContext() , CrearEventoActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.item_menu_chatAdmin){
                    Intent intent = new Intent(getApplicationContext() , ChatAdminActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }


    // needed to setup menu toggle button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (menuToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}