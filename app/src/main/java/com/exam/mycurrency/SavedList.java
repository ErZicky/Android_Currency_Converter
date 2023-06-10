package com.exam.mycurrency;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SavedList extends RecyclerView.Adapter<SavedList.MyViewHolder> {
    private static SavedList instance;

    private static List<SavedCurrencies> curlist;
    private static List<String> symbols;

    private SavedList()
    {
        curlist = new ArrayList<>();
        symbols = new ArrayList<>();

    }

    public static synchronized SavedList getInstance()
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


    public List<SavedCurrencies> GetCurList() {
        return curlist;
    }


    public static SavedCurrencies GetSingleItem(int index)
    {
        return curlist.get(index);
    }

    public static int GetCount()
    {
        return curlist.size();
    }

    public List<String> returnSymbols()
    {
        return symbols;
    }

    public int getSymbolsSize()
    {
        return symbols.size();
    }

    public int returnSymbolPosition(String t)
    {
        return symbols.indexOf(t);
    }
    public void AddSymbols(String f)
    {
        symbols.add(f);
    }
    public boolean FindDuplicate(SavedCurrencies couple)
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fromcurText;
        TextView tocurText;
        public MyViewHolder(View itemView) {
            super(itemView);
            fromcurText = itemView.findViewById(R.id.FromCur);
            tocurText = itemView.findViewById(R.id.ToCur);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            SavedCurrencies selectedItem = curlist.get(position);

            // Open MainActivity and pass the selected item
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("selectedItem", (selectedItem.GetSource().toString() + "/" +selectedItem.getTargetCur().toString()));
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_currrencies_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SavedCurrencies item = SavedList.GetSingleItem(position);
        holder.fromcurText.setText(item.GetSource().toString());
        holder.tocurText.setText(item.getTargetCur().toString());

    }

    @Override
    public int getItemCount() {
        return SavedList.GetCount();
    }
}






