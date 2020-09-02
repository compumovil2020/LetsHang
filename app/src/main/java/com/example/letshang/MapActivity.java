package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class MapActivity extends AppCompatActivity {


    ImageView filterIcon;
    Switch listMapSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        filterIcon = findViewById(R.id.imageFilterMap);
        listMapSwitch = findViewById(R.id.switch2);


        filterIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , FiltersActivity.class);
                startActivity(intent);
            }
        });

        listMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                finish();
            }
        });

    }
}