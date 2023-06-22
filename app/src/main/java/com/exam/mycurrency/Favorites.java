package com.exam.mycurrency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Favorites extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private SavedList SL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
            
        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView
                .setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) Favorites.this);
        bottomNavigationView.setSelectedItemId(R.id.favorite);

        //inizializzo il layout del recyclerview    
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager LM = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LM);

        //riempo il recycler con il contenuto di SavedList    
        SL = SavedList.getInstance();
        recyclerView.setAdapter(SL);

        //aggiungo un divider fra le voci per essere visivamente più chiaro
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LM.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);






    }



    //switch per la navigazione con il bottom nav menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                /*Log.d("menu", "history");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, historyfragment)
                        .commit();*/
                Intent historyIntent = new Intent(Favorites.this, HistoryActivity.class);
                startActivity(historyIntent);
                finish(); //chiudo questa attività
                return true;


            case R.id.home:

                    Intent home = new Intent(Favorites.this, MainActivity.class);
                    Favorites.this.startActivity(home);
                    finish(); //chiudo questa attività
                    return true;

            case R.id.favorite:
                return true;


        }
        return false;
    }
}
