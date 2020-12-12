package com.example.kantarellen.ShoppingList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.MainActivity;
import com.example.kantarellen.R;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<MyViewHolder>{

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
        //v.setOnClickListener(new ShoppingListOnClickListener());
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTxt.setText(items.get(position));
        holder.amountTxt.setText(amounts.get(position));

        //holder.nameTxt.setOnTouchListener(this);
        //holder.amountTxt.setOnTouchListener(this);

        /*
        holder.nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Hello");
                holder.nameTxt.setPaintFlags(holder.nameTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.amountTxt.setPaintFlags(holder.amountTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        holder.amountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.nameTxt.setPaintFlags(holder.nameTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.amountTxt.setPaintFlags(holder.amountTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

         */



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void deleteItem(int position) {
        items.remove(position);
        amounts.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getNames() {
        return items;
    }
    public ArrayList<String> getAmounts() {
        return amounts;
    }

    /*
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return true;
    }

     */
}
