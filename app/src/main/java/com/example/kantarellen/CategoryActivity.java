package com.example.kantarellen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity {

    private String m_Text = "";
    RecyclerView recyclerView;
    CategoryAdapter mAdapter;
    ArrayList<String> categoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        FloatingActionButton newCatButton = ( FloatingActionButton ) findViewById(R.id.newCatButton);
        newCatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                builder.setTitle("Lägg till ny kategori");

                // Set up the input
                final EditText input = new EditText(CategoryActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        addCategory(m_Text);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


            /*
            @Override
            public void onClick(View view) {
                addCategory("Ny Kategori");
                updateCategories();
            }
        });
             */

        recyclerView = findViewById(R.id.recyclerView);

        populateRecyclerView();
        //updateCategories();
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
        //updateCategories();

        mAdapter = new CategoryAdapter(categoryArrayList);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);

        /*
        mAdapter = new CategoryAdapter(categoryArrayList);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        //System.out.println("test = " + categoryArrayList.get(0));
        //System.out.println("count = " + mAdapter.getItemCount());

        recyclerView.setAdapter(mAdapter);

         */
    }
    /*
    private void updateCategories() {
        mAdapter.setData();

        
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);



        //System.out.println("test = " + categoryArrayList.get(0));
        //System.out.println("count = " + mAdapter.getItemCount());

        recyclerView.setAdapter(mAdapter);
    }
    */

    public void addCategory(String newCategory) {

        categoryArrayList.add(newCategory);
        mAdapter.setData(categoryArrayList);
        recyclerView.setAdapter(mAdapter);
    }


}