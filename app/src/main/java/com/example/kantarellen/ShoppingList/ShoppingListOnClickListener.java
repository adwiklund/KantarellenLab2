package com.example.kantarellen.ShoppingList;

import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.R;

public class ShoppingListOnClickListener implements View.OnClickListener{
    @Override
    public void onClick(View view) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        int itemPosition = rv.indexOfChild(view);

    }
}
