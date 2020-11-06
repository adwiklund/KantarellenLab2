package com.example.kantarellen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryAdapter mAdapter;
    ArrayList<String> categoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerView);

        populateRecyclerView();
    }

    private void populateRecyclerView() {
        categoryArrayList.add("Frukt & Grönt");
        categoryArrayList.add("Fisk");
        categoryArrayList.add("Kött");
        categoryArrayList.add("Mejeriprodukter");
        categoryArrayList.add("Städprodukter");
        categoryArrayList.add("Godis & Glass");
        categoryArrayList.add("Drycker");
        categoryArrayList.add("Snacks");
        categoryArrayList.add("Barnprodukter");
        categoryArrayList.add("Kaffe & Te");

        mAdapter = new CategoryAdapter(categoryArrayList);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        System.out.println("test = " + categoryArrayList.get(0));
        System.out.println("count = " + mAdapter.getItemCount());

        recyclerView.setAdapter(mAdapter);
    }


}