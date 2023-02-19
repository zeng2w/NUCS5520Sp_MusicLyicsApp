package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    String Amount = "";
    String from = "";
    String to = "";

    TextView output;
    EditText amount;
    Button convert;
    Spinner fromCurrency;
    Spinner toCurrency;
    ArrayAdapter adapter;

    private String currencyFrom;
    private String currencyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_you_service);

        amount = findViewById(R.id.editTextAmount);
        output = findViewById(R.id.textViewAmountExchanged);
        convert = findViewById(R.id.buttonTest);
        fromCurrency = findViewById(R.id.spinnerFrom);
        toCurrency = findViewById(R.id.spinnerTo);

        adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);

        fromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyFrom = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyTo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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