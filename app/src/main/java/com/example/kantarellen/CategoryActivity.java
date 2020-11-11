package com.example.kantarellen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class CategoryActivity extends AppCompatActivity {

    private String m_Text = "";
    RecyclerView recyclerView;
    CategoryAdapter mAdapter;
    ArrayList<String> categoryArrayList = new ArrayList<>();
    Realm realm;

    public ArrayList<String> getCategoryArrayList() {
        return categoryArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Realm.init(this);
        realm = Realm.getDefaultInstance();



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

        recyclerView = findViewById(R.id.recyclerView);

        populateRecyclerView();
    }

    private void populateRecyclerView() {

        RealmResults<Category> categories = realm.where(Category.class).sort("position").findAll();
        for(int i = 0; i < categories.size(); i++) {
            assert categories.get(i) != null;
            categoryArrayList.add(categories.get(i).getCategoryName());
        }


        /*
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

         */


        mAdapter = new CategoryAdapter(categoryArrayList);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);

    }

    public void addCategory(String newCategory) {

        realm.executeTransaction(r -> {
            Category category = r.createObject(Category.class, categoryArrayList.size()+1);
            category.setCategoryName(newCategory);
            category.setPosition(categoryArrayList.size()+1);
        });

        categoryArrayList.add(newCategory);
        mAdapter.setData(categoryArrayList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shoppinglist) {
            Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(activity2Intent);
            return true;
        }
        if (id == R.id.action_categories) {
            Intent activity2Intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(activity2Intent);
            return true;
        }
        if(id == R.id.action_recipes) {
            Intent activity2Intent = new Intent(getApplicationContext(), RecipeActivity.class);
            startActivity(activity2Intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}