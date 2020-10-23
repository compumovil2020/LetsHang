package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.model.User;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class EventosInscritosActivity extends AppCompatActivity{

    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Button btnEvento, btnAgregar;
    private FirebaseAuth mAuth;

    private UserProvider userProvider = UserProvider.getInsatance();
    private ListView listViewEvents;
    EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_inscrito);

        getSupportActionBar().setTitle("Eventos inscritos");
        mAuth = FirebaseAuth.getInstance();

        //inflate
        drawerLayout = findViewById(R.id.inscritos_drawer_layout);
        navView = findViewById(R.id.inscritos_nav_view);
        //btnEvento = findViewById(R.id.btnEventoEventosInscritos);
        btnAgregar = findViewById(R.id.btnAgregarEventosInscritos);
        listViewEvents = findViewById(R.id.listEventosAdapter);

        Participant participant = (Participant)userProvider.getCurrentUser();

        List<Event> listEvents = participant.getPastEvents();

        eventsAdapter = new EventsAdapter(this,listEvents);
        listViewEvents.setAdapter(eventsAdapter);

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EventosInscritosActivity.this, "Toast por defecto", Toast.LENGTH_LONG);
            }
        });

        setupMenu();

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
                    Intent intent = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    // does nothing
                }
                if(item.getItemId() ==  R.id.item_menu_logout){
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext() , StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_pago){
                    Toast.makeText(getApplicationContext(), item.getTitle() ,Toast.LENGTH_SHORT ).show();

                }
                return true;
            }
        });

        /*btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DescripcionEventoActivity.class);
                startActivity(intent);
            }
        });*/

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PrincipalActivity.class);
                startActivity(intent);
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