package com.exam.mycurrency;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SavedList extends RecyclerView.Adapter<SavedList.MyViewHolder> { //estendiamo RecyclerView per permettermi di definire il comportamento delle "celle" nella recycleView 
    private static SavedList instance; //instanza del singleton

    private static List<SavedCurrencies> curlist; //la lista delle coppie di valute salvate
    private static List<String> symbols; //lista di simboli necessaria per i preferiti

    private SavedList()
    {
        curlist = new ArrayList<>();
        symbols = new ArrayList<>();

    }

    public static synchronized SavedList getInstance() //ottengo istanza del singleton
    {
        if(instance == null)
        {
            instance = new SavedList();
        }

        return instance;

    }

    public void addToDataList(SavedCurrencies item) {
        curlist.add(item);
    }


    public List<SavedCurrencies> GetCurList() { //ottengo la lista di coppie di valute salvate 
        return curlist;
    }


    public static SavedCurrencies GetSingleItem(int index) //ottengo una coppia sepicifica
    {
        return curlist.get(index);
    }

    public static int GetCount()
    {
        return curlist.size();
    }

    public List<String> returnSymbols() //ritono la lista di simboli
    {
        return symbols;
    }

    public int getSymbolsSize()
    {
        return symbols.size();
    }

    public int returnSymbolPosition(String t) //ottengo la posizione di una valuta nell'elenco di valute (utilizzato per capire che elemento gli spinner devono selezionare quando si vuole caricare una coppia salvata)
    {
        return symbols.indexOf(t);
    }
    public void AddSymbols(String f)
    {
        symbols.add(f);
    }
    public boolean FindDuplicate(SavedCurrencies couple) //ritorno true se la coppia di valute passata è già presente nella lista curlist in modo da evitare di salvare la stessa coppia più volte
    {
        for(int i = 0; i<curlist.size(); i++)
        {


            if(curlist.get(i).getTargetCur().equals(couple.getTargetCur()) && curlist.get(i).GetSource().equals(couple.GetSource()) )
            {
                return  true;
            }
        }
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //da qui in poi viene definito come si comporta la singola cella del RecycleView nell'attività Favorites
        //il layout della cella è mostrato nel file saved_currrencies_item.xml in /res/layout
        TextView fromcurText;
        TextView tocurText;
        public MyViewHolder(View itemView) {
            super(itemView);
            fromcurText = itemView.findViewById(R.id.FromCur);
            tocurText = itemView.findViewById(R.id.ToCur);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { //cosa fare quando la cella viene cliccata
            int position = getAdapterPosition();
            SavedCurrencies selectedItem = curlist.get(position); //ottengo la posizione di questa coppia nella lista di coppie salvate

            // apro main activity passando come informazione extra una stringa contentente le due valute separate da '/' (valuta1/valuta2)
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("selectedItem", (selectedItem.GetSource().toString() + "/" +selectedItem.getTargetCur().toString()));
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //popola la RecycleView con celle di questo tipo 
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_currrencies_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) { //quando la cella è stata creata e associata al RecycleView ottieni la Coppia di valute corrispondente alla posizione della cella e scrivi le due valute nelle due TextView
        SavedCurrencies item = SavedList.GetSingleItem(position);
        holder.fromcurText.setText(item.GetSource().toString());
        holder.tocurText.setText(item.getTargetCur().toString());

    }

    @Override
    public int getItemCount() {
        return SavedList.GetCount();
    }
}






