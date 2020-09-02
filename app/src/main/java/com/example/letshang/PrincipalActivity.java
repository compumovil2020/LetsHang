package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class PrincipalActivity extends AppCompatActivity {

    ImageView filterImage;
    ConstraintLayout eventLayout;
    Switch listMapSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        filterImage = findViewById(R.id.principalFilterIcon);
        eventLayout = findViewById(R.id.eventLayout);
        listMapSwitch = findViewById(R.id.switch1);

        listMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(buttonView.getContext() , MapActivity.class);
                startActivity(intent);
                listMapSwitch.setChecked(false);
            }


        });

        filterImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , FiltersActivity.class);
                startActivity(intent);
            }
        });

        eventLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , DescripcionEvento.class);
                startActivity(intent);
            }
        });


    }
}