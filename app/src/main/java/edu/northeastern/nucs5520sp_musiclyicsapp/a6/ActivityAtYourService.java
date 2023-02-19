package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

public class ActivityAtYourService extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editTextOriginalAmount;
    TextView textViewConvertedAmount;
    private final Handler handler = new Handler();
    Spinner spinnerFrom;
    Spinner spinnerTo;
    String fromCurrencyStr;
    String toCurrencyStr;

    ArrayList<Conversion> conversionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_you_service);

        // Initialize the conversionList that stores all Conversion objects
        conversionList = new ArrayList<>();

        // credit: https://www.youtube.com/watch?v=on_OrrX7Nw4
        // Create the Spinner objects for selecting from currency and to currency
        spinnerFrom = findViewById(R.id.spinner_from_currency);
        spinnerTo = findViewById(R.id.spinner_to_currency);
        // Setup the adapter used by the spinners (how the spinners look)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        // Set onItemSelectedListener for the spinners
        spinnerFrom.setOnItemSelectedListener(this);
        spinnerTo.setOnItemSelectedListener(this);

        // Link the EditText and TextView objects with respective views by id
        editTextOriginalAmount = findViewById(R.id.editText_original_amount);
        textViewConvertedAmount = findViewById(R.id.textView_conversion_result);

        // Limit the number of digits for the amount to be converted to 2 digits after the decimal place
        editTextOriginalAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFiler()});
    }

    /**
     * Convert a certain amount of original currency into foreign currency.
     * API: https://exchangerate.host/#/#articles
     * @param originalAmount  the amount of original currency
     * @param fromCurrency    the original currency to be converted
     * @param toCurrency      the target currency we want as the result
     * @return  (Conversion)  the Conversion object with updated convertedAmount value
     */
    public Conversion convert(String originalAmount, String fromCurrency, String toCurrency) throws IOException, JSONException {

        // Set up the Conversion object and fill in known properties
        Conversion conversion = new Conversion(originalAmount, fromCurrency, toCurrency);

        // Set up the url which includes the information needed for the conversion
        @SuppressLint("DefaultLocale") String url_str
                = String.format("https://api.exchangerate.host/convert?from=%s&to=%s&amount=%s",
                fromCurrency,
                toCurrency,
                originalAmount);

        // Create url, establish HTTP request, and connect the request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        int status = request.getResponseCode();


        if (status == HttpURLConnection.HTTP_OK) {
            // Build JSON object
            // Credit: https://www.digitalocean.com/community/tutorials/java-httpurlconnection-example-java-http-request-get-post
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String inputLine;

            // Read web content into the StringBuilder
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

            // Turn result string into JSON object
            String resultStr = sb.toString();
            JSONObject resultJson = new JSONObject(resultStr);

            // Update Conversion object with results
            String convertedAmount = resultJson.get("result").toString();
            conversion.setConvertedAmount(convertedAmount);

            conversionList.add(conversion);
            return conversion;
        }
        else {
            Log.e(TAG, "Unable to parse result from: " + url_str);
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_convert) {
            CurrencyConvertRunnable currencyConvertRunnable = new CurrencyConvertRunnable();
            new Thread(currencyConvertRunnable).start();
        }
        else if (viewId == R.id.button_history) {
            Intent intent = new Intent(ActivityAtYourService.this, ActivityConversionHistory.class);
            intent.putParcelableArrayListExtra("conversion list", conversionList);
        }
    }

    /**
     * Class to deal with selections in spinners: dropdowns for "from currency" and "to currency".
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int viewId = view.getId();
        if (viewId == R.id.spinner_from_currency) {
            fromCurrencyStr = adapterView.getItemAtPosition(i).toString();
        }
        else if (viewId == R.id.spinner_to_currency) {
            toCurrencyStr = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class CurrencyConvertRunnable implements Runnable {

        @Override
        public void run() {
            String originalAmountStr = editTextOriginalAmount.getText().toString();
            String finalFromCurrencyStr = spinnerFrom.getSelectedItem().toString();
            String finalToCurrencyStr = spinnerTo.getSelectedItem().toString();
            try {
                Conversion newConversion = convert(originalAmountStr, finalFromCurrencyStr, finalToCurrencyStr);
                handler.post(() -> textViewConvertedAmount.setText(newConversion.getConvertedAmount()));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("conversion list", conversionList);
        outState.putString("from amount", textViewConvertedAmount.getText().toString());
        outState.putString("to amount", editTextOriginalAmount.getText().toString());
        // Save spinner selection state for screen rotation
        // Credit: https://stackoverflow.com/questions/48601676/how-to-handle-spinners-when-screen-orientation-changes
        outState.putInt("from currency selection", spinnerFrom.getSelectedItemPosition());
        outState.putInt("to currency selection", spinnerTo.getSelectedItemPosition());
    }
}