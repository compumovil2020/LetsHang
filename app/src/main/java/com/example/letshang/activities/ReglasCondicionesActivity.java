package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.model.User;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.firebase.auth.FirebaseAuth;

public class ReglasCondicionesActivity extends AppCompatActivity {

    private CheckBox cbConfirmar;
    private Event evento;
    private EventProvider evProv = EventProvider.getInsatance();
    private UserProvider usProv = UserProvider.getInstance();
    private Button btnRegistrar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglas_condiciones);

        evento = evProv.getEventByID(getIntent().getStringExtra("idevento"));

        cbConfirmar = findViewById(R.id.cbConfirmarReglas);

        btnRegistrar = findViewById(R.id.btnRegistrarReglas);


        btnRegistrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PrincipalActivity.class);

                evProv.inscribirEvento(mAuth.getUid(), evento.getID());
                ((Participant) usProv.getCurrentUser()).getPastEvents().add(evento);
                startActivity(intent);
            }
        });
    }
}
