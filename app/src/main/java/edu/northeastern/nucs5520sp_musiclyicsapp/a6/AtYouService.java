package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class AtYouService extends AppCompatActivity {
    private static final String TAG = "_________At your service";

    private Handler textHandler = new Handler();
    TextView output;
    EditText amountInput;
    Spinner fromCurrency;
    Spinner toCurrency;


    String[] currency = { "USD", "EUR",
            "JPY", "CZK",
            "HKD", "CHF", "CNY" };
    private String currencyFrom;
    private String currencyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_you_service);

        amountInput = findViewById(R.id.editTextAmount);
        output = findViewById(R.id.textViewAmountExchanged);
        fromCurrency = findViewById(R.id.spinnerFrom);
        //fromCurrency.setOnItemSelectedListener(this);
        toCurrency = findViewById(R.id.spinnerTo);
        //toCurrency.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currency);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromCurrency.setAdapter(ad);
        toCurrency.setAdapter(ad);

        fromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyFrom = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(),currencyFrom, Toast.LENGTH_LONG).show();

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


    }

    class webServiceThread extends Thread{
        @Override
        public void run(){
            Button covertButton = findViewById(R.id.convertButton);
            covertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = amountInput.getText().toString();
                    try{
//                        String url_str = "https://api.exchangerate.host/convert?from=USD&to=EUR";
                        String url_str = "https://api.exchangerate.host/convert?from=" + currencyFrom +"&to=" + currencyTo +"&amount=" + input ;

                        URL url = new URL(url_str);
                        HttpURLConnection request = (HttpURLConnection) url.openConnection();

                        request.connect();
                        JsonParser jp = new JsonParser();
                        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                        JsonObject jsonobj = root.getAsJsonObject();

                        String req_result = jsonobj.get("result").getAsString();


//                        Toast.makeText(getApplicationContext(),currencyFrom, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),req_result, Toast.LENGTH_LONG).show();
                        textHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                output.setText(req_result);
                            }
                        });
                        Log.d(TAG, req_result);

//                        output.setText(req_result);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

    }


}
