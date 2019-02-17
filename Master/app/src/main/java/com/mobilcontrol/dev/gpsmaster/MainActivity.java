package com.mobilcontrol.dev.gpsmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton gpsgoster ;
    ImageButton resimgoster ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsgoster = (ImageButton) findViewById(R.id.gpsgoster);
        resimgoster = (ImageButton) findViewById(R.id.resimgoster);

        gpsgoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this.getApplicationContext(), MapCommandWaitingActivity.class);

                startActivity(it);
            }
        });

        resimgoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ita = new Intent(MainActivity.this.getApplicationContext(), CameraCommandWaitingActivity.class);

                startActivity(ita);
            }
        });


    }












}
