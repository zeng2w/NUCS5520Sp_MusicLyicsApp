package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class AtYouService extends AppCompatActivity {

    String[] list = {"CNH","USD","CAD","EUR"};
    String from = "";
    String to = "";
    String Amount = "";

    AutoCompleteTextView currencyFrom;
    AutoCompleteTextView currencyTo;
    Button convert;
    TextView output;
    EditText amount;
    ArrayAdapter<String> currencyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_you_service);

        currencyFrom = findViewById(R.id.currencyFrom);
        currencyTo = findViewById(R.id.currencyTo);
        convert = findViewById(R.id.convert);
        output = findViewById(R.id.output);
        amount = findViewById(R.id.amount);

        currencyList = new ArrayAdapter<String>(this, R.layout.list_item,list);

        currencyFrom.setAdapter(currencyList);
        currencyTo.setAdapter(currencyList);


        currencyFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                from = adapterView.getItemAtPosition(position).toString();
            }

        });

        currencyTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                to = adapterView.getItemAtPosition(position).toString();
            }

        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amount = (String) amount.getText().toString();
                String url_str = String.format("https://api.exchangerate.host/convert?from=%s&to=%s&amount=%s",from,to,Amount);
                URL url = null;
                try {
                    url = new URL(url_str);
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

                String req_result = jsonobj.get("result").getAsString();
                output.setText(req_result);
            }
        });
    }


}