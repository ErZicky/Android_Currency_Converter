package com.exam.mycurrency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Spinner StartCurrency;
    Spinner EndCurrency;
    Button HButton;
    TextView Hview;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
            
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.history);

        StartCurrency = (Spinner) findViewById(R.id.start_cur);
        EndCurrency = (Spinner) findViewById(R.id.end_cur);
        HButton = (Button) findViewById(R.id.button);
        Hview = (TextView) findViewById(R.id.Htext);
        Hview.setMovementMethod(new ScrollingMovementMethod());

        LoadSymbols(); //carico le abbreviazione delle valute


        HButton.setOnClickListener(new View.OnClickListener() { //il bottone per mostrare la storia del conversion rate viene cliccato
            public void onClick(View v) {
                Hview.setText("");
                loadConRateHistory(); //carico il rateo di conversione degli ultimi 7 giorni per le valute selezionate


            }
        });



    }





    public void loadConRateHistory() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                OkHttpClient client = new OkHttpClient();


                // creo i calendar per la data odierna e per 7 giorni prima
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -7);

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());



                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.DAY_OF_YEAR, -1);

                String date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar2.getTime());
                Log.d("responsemsg", date2);

                //creo la richiesta http
                Request request = new Request.Builder()
                        .url("https://api.freecurrencyapi.com/v1/historical?apikey=ImLoHfOLkWEiYm3MnUhary5wlxaMZHSqQ7YKCZMD&currencies="+EndCurrency.getSelectedItem().toString()+"&base_currency="+ StartCurrency.getSelectedItem().toString() +"&date_from="+date+"&date_to" +date2)
                   //     .addHeader("apikey", "YraibCBIybmjk59VIH2J05DJpkULhQ24")
                        .get()
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    Log.d("responsemsg", response.toString());
                     return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) { //elaboro il risultato
                if (result != null) {
                    Log.d("resultSymH", result);
                    try {
                        //provo ad estrarre i dati dal Json ritornato dalla chiamata
                        JSONObject dataJson = new JSONObject(result);
                        JSONObject data = dataJson.getJSONObject("data");

                        StringBuilder resultBuilder = new StringBuilder();

                        Iterator<String> keys = data.keys();
                        while (keys.hasNext()) {
                            String date = keys.next();
                            JSONObject currencyObject = data.getJSONObject(date);
                            String currencyCode = currencyObject.keys().next();
                            double exchangeRate = currencyObject.getDouble(currencyCode);

                            String resultString = "Data: " + date + "\n" + "   Tasso di cambio: " + exchangeRate + "\n"; //creo la stringa da printare per ogni riga/data
                            resultBuilder.append(resultString);
                            Log.d("resultSym", resultString);
                        }

                        String resultText = resultBuilder.toString();

                        Hview.setText(resultText); //printo la stringa formata da tutte le stringhe create per gli utlimi 7 giorni

                    } catch (Exception e) {
                        Log.d("errore in risposta", e.toString());
                    }
                }


            }
        }.execute();
    }






    //navigazione tramite bottom nav menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.history:
                return true;


            case R.id.home:
                    Intent home = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(home);
                    return true;
          case R.id.favorite:
              Intent fav = new Intent(HistoryActivity.this, Favorites.class);
              HistoryActivity.this.startActivity(fav);


              return true;
        }
        return false;
    }


    public void LoadSymbols() { //carico i simboli delle valute 
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                //creo richiesta http
                Request request = new Request.Builder()
                        .url("https://api.freecurrencyapi.com/v1/currencies?apikey=ImLoHfOLkWEiYm3MnUhary5wlxaMZHSqQ7YKCZMD&currencies=")
                        // .addHeader("apikey", "YraibCBIybmjk59VIH2J05DJpkULhQ24")
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) { //elaboro il json ricevuto se non è nullo per estrarre i simboli delle valute
                if (result != null) {
                    Log.d("resultSym", result);
                    try {
                        JSONObject dataJson = new JSONObject(result);
                        JSONObject data = dataJson.getJSONObject("data");

                        List<String> currencyList = new ArrayList<>(); //lista che conterrà le valute

                        Iterator<String> keys = data.keys();
                        while (keys.hasNext()) {
                            String currencyCode = keys.next();
                            JSONObject currency = data.getJSONObject(currencyCode);
                            String currencySymbol = currency.getString("code");
                            currencyList.add(currencySymbol);
                            Log.d("resultSym", currencySymbol);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>( //creo un adapter per i due spinner che conterrà la lista di valute (currencyList)
                                HistoryActivity.this,
                                android.R.layout.simple_spinner_item,
                                currencyList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //definisco il tipo di spinner

                        //associo i due spinner all'adapter creato contenente i simboli delle valute
                        StartCurrency.setAdapter(adapter);
                        EndCurrency.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.d("errore in risposta", e.toString());
                    }
                }

            }
        }.execute();
    }


}
