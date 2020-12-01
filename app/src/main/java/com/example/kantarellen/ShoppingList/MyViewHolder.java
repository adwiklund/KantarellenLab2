package com.example.kantarellen.ShoppingList;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;

    public MyViewHolder(View itemView) {
        super(itemView);

        nameTxt= (TextView) itemView.findViewById(R.id.nameTxt);
    }
}
