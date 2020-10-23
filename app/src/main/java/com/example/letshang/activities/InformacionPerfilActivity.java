package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.providers.UserProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class InformacionPerfilActivity extends AppCompatActivity {

    private TextView tvDeportes, tvConciertos, tvConferencias;
    private Button btnEvento, btnEditar;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private TextView tvNombre, tvCorreo;

    private UserProvider usProv = UserProvider.getInsatance();
    private LinearLayout linearLayoutContenedor;
    private LinearLayout linearLayoutTextos;
    private TextView textViewTags;
    private ListView listViewEvents;
    EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_perfil);
        mAuth = FirebaseAuth.getInstance();


        btnEditar = findViewById(R.id.btnEditarInformacionPerfil);

        drawerLayout = findViewById(R.id.informacion_perfil_drawer_layout);
        navView = findViewById(R.id.informacion_perfil_nav_view);

        tvNombre = findViewById(R.id.tvNombreInformacionPerfil);
        tvCorreo = findViewById(R.id.tvCorreoInformacionPerfil);

        linearLayoutContenedor = findViewById(R.id.llInformacionPerfil1);

        listViewEvents = findViewById(R.id.listEventosInformacionPerfil);


        Participant parti = (Participant)usProv.getCurrentUser();

        tvNombre.setText(parti.getName());
        tvCorreo.setText(parti.getEmail());

        Preference pr = parti.getPreferences();

        String[] tags = pr.getInterests();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        int contadorTags = 0;
        for(String tag : tags){
            if(contadorTags == 0){
                linearLayoutTextos = new LinearLayout(this);
                linearLayoutTextos.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutTextos.setLayoutParams(layoutParams);
                linearLayoutContenedor.addView(linearLayoutTextos);
            }

            if(contadorTags < 3){

                TableRow.LayoutParams lp = new TableRow.LayoutParams();
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.weight = 1;
                lp.leftMargin = 10;
                lp.rightMargin = 10;
                Typeface face = ResourcesCompat.getFont(this,R.font.coves_bold);
                textViewTags = new TextView(this);
                textViewTags.setLayoutParams(lp);
                textViewTags.setGravity(Gravity.CENTER);
                textViewTags.setTextSize(16);
                textViewTags.setText(tag);

                Participant participant = (Participant)usProv.getCurrentUser();

                List<Event> listEvents = participant.getPastEvents();
                eventsAdapter = new EventsAdapter(this,listEvents);
                linearLayoutTextos.addView(textViewTags);
            }

            if(contadorTags==2){
                contadorTags = 0;
            }
            contadorTags += 1;
        }

        listViewEvents.setAdapter(eventsAdapter);

        setupMenu();


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdministrarPerfilActivity.class);
                startActivity(intent);
            }
        });

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
                    //does nothing
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
                    Intent intent = new Intent(getApplicationContext() , EventosInscritosActivity.class);
                    startActivity(intent);
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