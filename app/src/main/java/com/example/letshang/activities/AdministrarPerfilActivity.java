package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.User;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.util.Log;

public class AdministrarPerfilActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etTelefono, etLocation, etFecha;
    private Button btnGuardar;

    private UserProvider usrProvider = UserProvider.getInstance();
    private Participant part;

    private LinearLayout linearLayoutContenedor;
    private LinearLayout linearLayoutTextos;
    private TextView textViewTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_perfil);

        etNombre = findViewById(R.id.etNombreAdministrarPerfil);
        etEmail = findViewById(R.id.etEmailAdministrarPerfil);
        etTelefono = findViewById(R.id.etTelefonoAdministrarPerfil);
        etLocation = findViewById(R.id.etLocationAdministrarPerfil);
        etFecha = findViewById(R.id.etCalendarAdministrarPerfil);

        btnGuardar = findViewById(R.id.btnGuardarAdministrarPerfil);

        linearLayoutContenedor = findViewById(R.id.llAdministrarPerfil3);

        getSupportActionBar().setTitle("Mi perfil");


        part = (Participant)usrProvider.getCurrentUser();
        etNombre.setText(part.getName());
        etEmail.setText(part.getEmail());
        etTelefono.setText(part.getPhone());
        etLocation.setText("part.getLocation()");
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String today = formatter.format(part.getBirthDate().getTime());
        etFecha.setText(today);

        Preference pr = part.getPreferences();

        List<String> tags = pr.getInterests();

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
                Typeface face = ResourcesCompat.getFont(this,R.font.coves_bold);
                textViewTags = new TextView(this);
                textViewTags.setLayoutParams(lp);
                textViewTags.setGravity(Gravity.CENTER);
                textViewTags.setTextSize(16);
                textViewTags.setText(tag);

                linearLayoutTextos.addView(textViewTags);
            }

            if(contadorTags==2){
                contadorTags = 0;
            }
            contadorTags++;
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


}