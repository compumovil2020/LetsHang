package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FiltersActivity extends AppCompatActivity {


    private Button applyFiltersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        applyFiltersButton = findViewById(R.id.applyFiltersButton);

        getSupportActionBar().setTitle("");




        applyFiltersButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}