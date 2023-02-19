package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class AtYouService extends AppCompatActivity {

    TextView output;
    EditText amount;
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
    }

//    public void callWebserviceButtonHandler(View view){
//        PingWebServiceTask task = new PingWebServiceTask();
//        task.execute(url.getText().toString());
//    }
//
//    private class PingWebServiceTask extends AsyncTask<String, Integer, String[]> {
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            String[] result = new String[2];
//            URL url = null;
//            try {
//                url = new URL(params[0]);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.connect();
//
//                InputStream inputStream = conn.getInputStream();
//                final String response = convertStreamToString(inputStream);
//
//                JSONObject jObject = new JSONObject(response);
//                String jTitle = jObject.getString("title");
//                String jBody = jObject.getString("body");
//                result[0] = jTitle;
//                result[1] = jBody;
//                return result;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            result[0] = "Something went wrong";
//            return(result);
//        }
//
//        @Override
//        protected void onPostExecute(String... s){
//            super.onPostExecute(s);
//            TextView output = (TextView) findViewById(R.id.textViewAmountExchanged);
//            output.setText(s[0]);
//        }
//    }
//
//    private String convertStreamToString(InputStream is){
//        Scanner s = new Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next().replace(",", ",\n") : "";
//    }
}