package com.example.kantarellen.ShoppingList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.R;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<String> items;
    ArrayList<String> amounts;


    public ShoppingListAdapter(Context c, ArrayList<String> items, ArrayList<String> amounts) {
        this.c = c;
        this.items = items;
        this.amounts = amounts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.main_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTxt.setText(items.get(position));
        holder.amountTxt.setText(amounts.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
