package com.exam.mycurrency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    EditText Inputcurrency;
    TextView OutputCurrency;
    List<String> keysList;
    Spinner StartCurrency;
    Spinner EndCurrency;
    ImageView FlagStart;
    ImageView FlagEnd;
    BottomNavigationView bottomNavigationView;

    ImageView FavoriteIcon;

    List<String> CurrencySymbolOrder = new ArrayList<>();
    int selectionS = -1;
    int selectionE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        Inputcurrency = (EditText) findViewById(R.id.inputcurrency);
        OutputCurrency = (TextView) findViewById(R.id.textViewResult);
        StartCurrency = (Spinner) findViewById(R.id.start_cur);
        EndCurrency = (Spinner) findViewById(R.id.end_cur);
        FlagStart = (ImageView) findViewById(R.id.Flag_Start);
        FlagEnd = (ImageView) findViewById(R.id.Flag_End);
        FavoriteIcon = (ImageView) findViewById(R.id.SetFavorite);


        LoadSymbols(); //carico le tre lettere delle valute


        Inputcurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) { //quando il testo viene modificato effettuo automaticamente il cambio
                if(Inputcurrency.getText().length() != 0)
                    loadConRate(Inputcurrency.getText().toString()); //effetuo il cambio
            }
        });

        StartCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SavedList SymbolsHolder = SavedList.getInstance();

                if(SymbolsHolder.getSymbolsSize() == 0) //creo una lista di valute necessaria per quando viene aperta l'attività tramite preferiti
                {
                    for (int k = 0; k < StartCurrency.getCount(); k++) {
                        String item = StartCurrency.getItemAtPosition(k).toString();
                        SymbolsHolder.AddSymbols(item);
                    }
                }
                
                //codice per cambiare la bandiera in base alla valute 
                try {
                    int flagResourceId = getResources().getIdentifier((StartCurrency.getSelectedItem().toString()).toLowerCase(), "drawable", getPackageName()); //ottengo l'id della bandiera con il nome corrispondente alla valuta
                    Drawable image = getResources().getDrawable(flagResourceId);
                    FlagStart.setImageDrawable(image);
                }
                catch (Exception e)
                {
                    Log.d("bandiere", e.toString());
                }
                loadConRate(Inputcurrency.getText().toString());


                FavoriteIcon.setImageResource(R.drawable.star_empty);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //niente
            }
        });
        EndCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                     //codice per cambiare la bandiera il base alla valute 
                try {
                    int flagResourceId = getResources().getIdentifier((EndCurrency.getSelectedItem().toString()).toLowerCase(), "drawable", getPackageName()); //ottengo l'id della bandiera con il nome corrispondente alla valuta
                    Drawable image = getResources().getDrawable(flagResourceId);
                    FlagEnd.setImageDrawable(image);

                }
                catch (Exception e)
                {
                    Log.d("bandiere", e.toString());
                }

                loadConRate(Inputcurrency.getText().toString());
                FavoriteIcon.setImageResource(R.drawable.star_empty);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //niente
            }
        });



        FavoriteIcon.setOnClickListener(new View.OnClickListener() { //click sulla stella
            @Override
            public void onClick(View v)
            {

                if(FavoriteIcon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.star_empty).getConstantState())
                {
                    SavedCurrencies t =   new SavedCurrencies(StartCurrency.getSelectedItem().toString(), EndCurrency.getSelectedItem().toString()); //new SavedCurrencies("ITL", "TUL");

                    SavedList favoritesholder = SavedList.getInstance();

                    if(!favoritesholder.FindDuplicate(t)) //se la coppia di valute non è gia stata salvata la salvo
                    {
                        favoritesholder.addToDataList(t);
                        FavoriteIcon.setImageResource(R.drawable.star_full);


                    }
                    else //altrimenti mostro un toast per informare che esiste già
                    {
                        CharSequence text = "Coppia di valute già presente";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(MainActivity.this, text, duration);
                        toast.show();

                    }


                }
                else
                {
                    FavoriteIcon.setImageResource(R.drawable.star_empty);


                }

            }
        });

        //controllo se questa attività è stata avviata con infomrazioni extra (usate per quando si clicca su una coppia nei preferiti) in modo da caricare la coppia
        Intent intent = getIntent();
        String Sitem = intent.getStringExtra("selectedItem");
        if(Sitem != "" && Sitem!=null)
        {
            //ottengo le due valute preferite
            String[] elements = Sitem.split("/");
            String firstString = elements[0];
            String secondString = elements[1];

            SelectSpinnerExtra(firstString, secondString); //carico le valute 
        }


    }


        //navigazione tramite bottom nav
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
                finish(); //chiudo questa attività
                return true;


            case R.id.home:

                return true;

            case R.id.favorite:
                Intent fav = new Intent(MainActivity.this, Favorites.class);
                MainActivity.this.startActivity(fav);
                finish(); //chiudo questa attività
                return true;


        }
        return false;
    }


    void SelectSpinnerExtra(String from, String to) //ottengo gli indici per gli spinner corrispondenti alle valute preferite
    {



        SavedList SymbolsHolder = SavedList.getInstance();

        int indexS = SymbolsHolder.returnSymbolPosition(from);
        //creo gli index di selezione che userò quando verranno caricati i simboli per selezionare le valute preferite
        if (indexS != -1) {
           // StartCurrency.setSelection(indexS);
            selectionS=indexS;
        }

        int indexE = SymbolsHolder.returnSymbolPosition(to);
        if (indexE != -1) {
            //StartCurrency.setSelection(indexE);
            selectionE = indexE;
        }
    }


    public void loadConRate(String amountEdited) { //effettuo la conversioni fra valute
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                //preparo la richiesta all'api
                OkHttpClient client = new OkHttpClient();
                
                Request request = new Request.Builder()
                        .url("https://api.freecurrencyapi.com/v1/latest?apikey=ImLoHfOLkWEiYm3MnUhary5wlxaMZHSqQ7YKCZMD&currencies="+EndCurrency.getSelectedItem()+"&base_currency="+StartCurrency.getSelectedItem())
             //          .addHeader("apikey", "YraibCBIybmjk59VIH2J05DJpkULhQ24")
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
            protected void onPostExecute(String result) { //se il risultato non è nullo elaboro il json
                if (result != null)
                {
                    Log.d("result", result);
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
                    JsonObject dataObject = jsonObject.getAsJsonObject("data");

                    String currencyCode = dataObject.keySet().iterator().next();

                    // Ottiengo il rateo di cambio
                    double currencyValue = dataObject.get(currencyCode).getAsDouble();

                    // Moltiplico il rateo per la quantità immessa
                    double multipliedValue = currencyValue * Double.parseDouble(Inputcurrency.getText().toString());

                    // Formatto il risultato a due cifre decimali
                    String formattedResult = String.format("%.2f", multipliedValue);

                    //mostro il risultato nella textview
                    OutputCurrency.setText(formattedResult);
                }
            }
        }.execute();
    }


    public void LoadSymbols() { //carico i simboli delle valute 
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                //preparo richiesta all'api
                OkHttpClient client = new OkHttpClient();

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
                protected void onPostExecute(String result) { //se il risultato non è nullo elaboro il JSON
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
                                    MainActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    currencyList
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //definisco il tipo di spinner

                             //associo i due spinner all'adapter creato contenente i simboli delle valute
                            StartCurrency.setAdapter(adapter);
                            EndCurrency.setAdapter(adapter);


                            //se selectionE e selectionS non sono -1 vuol dire che questa attività è stata aperta con Extra e quindi è
                            //necessario selezionare delle valute preferite quindi imposto i due spinner agli index delle valute della coppia preferita
                            if(selectionE != -1  && selectionS != -1)
                            {
                                StartCurrency.setSelection(selectionS);
                                EndCurrency.setSelection(selectionE);


                            }
                        } catch (Exception e) {
                            Log.d("errore in risposta", e.toString());
                        }
                    }

                }
            }.execute();
    }







}



