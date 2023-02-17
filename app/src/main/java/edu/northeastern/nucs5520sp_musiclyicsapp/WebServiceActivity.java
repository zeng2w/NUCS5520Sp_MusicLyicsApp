package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WebServiceActivity extends AppCompatActivity {
    static String REQUEST_API = "https://api.exchangerate.host/convert?from=%s&to=%s&amount=%s";
    private Button btn_exchagne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service2);
        btn_exchagne = findViewById(R.id.button);

        btn_exchagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}