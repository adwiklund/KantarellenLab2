package com.example.kantarellen;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.kantarellen.Category.Category;
import com.example.kantarellen.Category.CategoryActivity;
import com.example.kantarellen.Recipe.RecipeActivity;
import com.example.kantarellen.ShoppingList.ShoppingList;
import com.example.kantarellen.ShoppingList.ShoppingListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    private RealmResults<Category> categories;
    //ArrayList<String> shoppinglist;

    RecyclerView rv;
    ArrayList<String> items;
    ArrayList<String> amounts;
    ArrayList<String> categoryList;
    ShoppingListAdapter shoppingListAdapter;
    ArrayAdapter categoryAdapter;
    EditText nameEditTxt;
    EditText amountEditTxt;
    Spinner categorySpinner;
    RealmHelper helper;
    ShoppingList shoppingList;


    private final OrderedRealmCollectionChangeListener<RealmResults<Category>> realmChangeListener = (people, changeSet) -> {
        String insertions = changeSet.getInsertions().length == 0 ? "" : "\n - Insertions: " + Arrays.toString(changeSet.getInsertions());
        String deletions = changeSet.getDeletions().length == 0 ? "" : "\n - Deletions: " + Arrays.toString(changeSet.getDeletions());
        String changes = changeSet.getChanges().length == 0 ? "" : "\n - Changes: " + Arrays.toString(changeSet.getChanges());

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.mainRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
       // categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        Realm.init(this);

        //Realm.deleteRealm(Realm.getDefaultConfiguration());

        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        //Realm.deleteRealm(config);

        realm = Realm.getInstance(config);
        //realm = Realm.getDefaultInstance();

        shoppingList = realm.where(ShoppingList.class).findFirst();
        if(shoppingList == null) {
            realm.executeTransaction(r -> {
                shoppingList = realm.createObject(ShoppingList.class, 1);
            });
        }
        /*
        if(shoppingList.getId() != 1) {
            realm.executeTransaction(r -> {
                shoppingList = realm.createObject(ShoppingList.class, 1);
            });
        }

         */

        /*
        RealmList<Item> tempList = new RealmList<>();
        realm.executeTransaction(r -> {
                    shoppingList.setItems(tempList);
                });

         */

        helper = new RealmHelper(realm);
        helper.fillShoppingList(shoppingList);
        items = helper.retrieveItemNames();
        amounts = helper.retrieveItemAmounts();


        //RealmResults<Item> realmItems = realm.where(Item.class).findAll();
        //shoppingList.setItems(realmItems);

        shoppingListAdapter = new ShoppingListAdapter(this, items, amounts);
        rv.setAdapter(shoppingListAdapter);

        setupCategories();

        categories = realm.where(Category.class).findAllAsync();
        categories.addChangeListener(realmChangeListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayInputDialog();

            }
        });

        FloatingActionButton del = findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helper.delete();

                items = helper.retrieveItemNames();
                amounts = helper.retrieveItemAmounts();
                shoppingListAdapter = new ShoppingListAdapter(MainActivity.this, items, amounts);
                rv.setAdapter(shoppingListAdapter);

            }
        });

    }

    private void displayInputDialog()
    {
        Dialog d=new Dialog(this);
        d.setTitle("Ny Vara");
        d.setContentView(R.layout.shoppinglist_input_dialog);
        RealmHelper helper=new RealmHelper(realm);
        categoryList = helper.retrieveCategories();

        nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        amountEditTxt = (EditText) d.findViewById(R.id.amountEditText);
        categorySpinner = (Spinner) d.findViewById(R.id.categorySpinner);
        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);

        final Category[] category = new Category[1];


        categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, categoryList.get(i),Toast.LENGTH_SHORT).show();
                category[0] = realm.where(Category.class).findAll().get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                Item i = new Item();
                i.setItemName(nameEditTxt.getText().toString());
                i.setAmount(amountEditTxt.getText().toString());
                i.setId(realm);
                Category selectedCategory = category[0];
                i.setCategory(selectedCategory);


                //SAVE
                //RealmHelper helper=new RealmHelper(realm);
                //helper.setId(i);
                helper.save(i);

                nameEditTxt.setText("");
                amountEditTxt.setText("");

                //REFRESH
                items = helper.retrieveItemNames();
                amounts = helper.retrieveItemAmounts();
                categoryList = helper.retrieveCategories();
                shoppingListAdapter = new ShoppingListAdapter(MainActivity.this, items, amounts);
                categoryAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, categoryList);
                rv.setAdapter(shoppingListAdapter);
                categorySpinner.setAdapter(categoryAdapter);


            }
        });

        d.show();
    }

    /*
    private void setItemCategory(Category category, Item item) {
        realm.executeTransaction(r -> {
            item.setCategory(category);
        });
        shoppinglist.add(item.getItemName());
    }

     */


    public void setupCategories() {

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("Frukt & Grönt");
        categoryList.add("Fisk");
        categoryList.add("Kött");
        categoryList.add("Mejeriprodukter");
        categoryList.add("Städprodukter");
        categoryList.add("Godis & Glass");
        categoryList.add("Drycker");
        categoryList.add("Snacks");
        categoryList.add("Barnprodukter");
        categoryList.add("Kaffe & Te");

        RealmResults<Category> realmCategories = realm.where(Category.class).findAll();

        if(realmCategories.size() == 0) {
            realm.executeTransaction(r -> {
                for (int i = 0; i < categoryList.size(); i++) {
                    Category category = r.createObject(Category.class, i);
                    category.setCategoryName(categoryList.get(i));
                    category.setPosition(i);
                }
            });
        }

/*
        realm.executeTransaction(r -> {
            if(r.isEmpty()) {
                for(int i = 0; i < categoryList.size(); i++) {
                    System.out.println("here2");
                    Category category = r.createObject(Category.class, i);
                    category.setCategoryName(categoryList.get(i));
                    category.setPosition(i);
                }
            }

        });

 */

    }

    @Override
    public void onResume() {

        super.onResume();
        items = helper.retrieveItemNames();
        amounts = helper.retrieveItemAmounts();
        shoppingListAdapter = new ShoppingListAdapter(MainActivity.this, items, amounts);
        rv.setAdapter(shoppingListAdapter);
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
}
