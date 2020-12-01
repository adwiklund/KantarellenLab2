package com.example.kantarellen.ShoppingList;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    TextView amountTxt;

    public MyViewHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
        amountTxt = (TextView) itemView.findViewById(R.id.amountTxt);
    }
}
