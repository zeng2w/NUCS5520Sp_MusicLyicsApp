package edu.northeastern.nucs5520sp_musiclyicsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.os.StrictMode;

public class example<policy> extends AppCompatActivity {

    static String REQUEST_API1 = "https://api.exchangerate.host/convert?from=CNY&to=USD&amount=";
    static String REQUEST_API2 = "https://api.exchangerate.host/convert?from=USD&to=CNY&amount=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Button btn = findViewById(R.id.button);
        Button btn2 = findViewById(R.id.button2);

        // usd to cn
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_usd = findViewById(R.id.tv_us);
                TextView tv_cn = findViewById(R.id.tv_cn);
                TextView tv_rate = findViewById(R.id.tv_rate);

                String usdS = tv_usd.getText().toString();
                double usdD;
                String rateS = "";
                String cnS = "";

                if ("".equals(usdS) || "0".equals(usdS)) {
                    tv_cn.setText("0");
                    tv_usd.setText("0");
                    return;
                } else {
                    usdD = Double.valueOf(usdS);
                    cnS = getInfoFromServer(usdD,REQUEST_API2)[0];
                }

                rateS = getInfoFromServer(usdD,REQUEST_API2)[1];

                tv_cn.setText(cnS);
                tv_rate.setText(rateS);
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_usd = findViewById(R.id.tv_us);
                TextView tv_cn = findViewById(R.id.tv_cn);
                TextView tv_rate = findViewById(R.id.tv_rate);

                String usdS;
                String rateS;
                String cnS = tv_cn.getText().toString();
                double cnD;

                if ("".equals(cnS) || "0".equals(cnS)) {
                    tv_cn.setText("0");
                    tv_usd.setText("0");
                    return;
                } else {
                    cnD = Double.valueOf(cnS);
                }

                rateS = getInfoFromServer(cnD,REQUEST_API1)[1];
                usdS = getInfoFromServer(cnD,REQUEST_API1)[0];
                tv_usd.setText(usdS);
                tv_rate.setText(rateS);
            }
        });
    }








    private String[] getInfoFromServer(double amount, String RE) {
        URL url = null;
        try {
            url = new URL(RE + String.valueOf(amount));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            request.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonParser jp = new JsonParser();
        JsonElement root = null;
        try {
            root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject jsonobj = root.getAsJsonObject();

        String res = jsonobj.get("result").getAsString();
        String rate = jsonobj.get("info").getAsJsonObject().get("rate").getAsString();
//        Toast.makeText(example.this,"data:" + rate,Toast.LENGTH_SHORT).show();
        return new String[]{res, rate};
    }
}