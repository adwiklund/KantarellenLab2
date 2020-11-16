package com.example.kantarellen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private String m_Text = "";
    MapPopup mapPopup;
    Realm realm;
    private RealmResults<Category> categories;
    //RealmChangeListener realmChangeListener;


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

        Realm.init(this);

        //Realm.deleteRealm(Realm.getDefaultConfiguration());

        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        Realm.deleteRealm(config);

        realm = Realm.getInstance(config);
        //realm = Realm.getDefaultInstance();

        System.out.println("path: " + realm.getPath());

        setupCategories();

        final ArrayList<String> shoppinglist = new ArrayList<>();

        ShoppingList realmShopList = realm.where(ShoppingList.class).findFirst();

        if(realmShopList == null) {
            realm.executeTransaction(r -> {
                ShoppingList newShopList = r.createObject(ShoppingList.class, 1);
                RealmList<Item> list = new RealmList<>();
                newShopList.setItems(list);
            });
        } else {
            realm.executeTransaction(r -> {
                //ShoppingList realmShopList = realm.where(ShoppingList.class).findFirst();
                for(int i = 0; i < realmShopList.getItems().size(); i++) {
                    shoppinglist.add(realmShopList.getItems().get(i).getItemName());
                }
            });
        }


        /*
        realm.executeTransaction(r -> {
            Category category = r.createObject(Category.class, 1);
            category.setCategoryName("Frukt");

            Category category1 = r.createObject(Category.class, 2);
            category1.setCategoryName("Kött");

        });

         */

        categories = realm.where(Category.class).findAllAsync();
        categories.addChangeListener(realmChangeListener);

        RealmResults<Category> results = realm.where(Category.class).findAll();
        System.out.println("test = " + results.size());

        mapPopup = new MapPopup();

        listView = (ListView) findViewById(R.id.mobile_list);

        //final ArrayList<String> shoppinglist = new ArrayList<>();

        final ArrayList<String> inventory = new ArrayList<>();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, shoppinglist);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //private int save = -1;
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                //int itemPosition     = position;

                // ListView Clicked item value
                //String  itemValue    = (String) listView.getItemAtPosition(position);
                TextView row = (TextView) view;
                //System.out.println("flag = " + row.getPaintFlags());

                if(row.getPaintFlags() == 0 || row.getPaintFlags() == 1281) {
                    row.setPaintFlags(row.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    row.setPaintFlags(0);
                }

                //row.setPaintFlags(row.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //parent.getChildAt(itemPosition).setBackgroundColor(Color.GREEN);


                //save = position;

            }

        });


        //final mapPopup popup = new mapPopup();


        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

       // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        CategoryActivity categoryActivity = new CategoryActivity();
        final ArrayList<String> categoryArrayList = categoryActivity.getCategoryArrayList();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ny vara");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        m_Text = input.getText().toString();

                        addItem(m_Text);



                        if (inventory.contains(m_Text)) {

                            shoppinglist.add(m_Text);
                        } else {

                            inventory.add(m_Text);
                            shoppinglist.add(m_Text);

                        }



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

        FloatingActionButton del = findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Rensa listan?");

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(r -> {
                            ShoppingList newshoppingList = realm.where(ShoppingList.class).findFirst();
                            RealmList<Item> items = newshoppingList.getItems();
                            items.deleteAllFromRealm();
                            shoppinglist.removeAll(shoppinglist);
                            //recreate();
                            shoppinglist.clear();
                            adapter.notifyDataSetChanged();
                        });

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

    }

    public void addItem(String itemName) {

        ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();
        if(shoppingList == null) {
            createShoppingList();
            addItem(itemName);
        }
        if(shoppingList.getItems() == null) {
            RealmList<Item> list = new RealmList<>();
            shoppingList.setItems(list);
        }

        realm.executeTransaction(r -> {
            ShoppingList sl = realm.where(ShoppingList.class).findFirst();
            Number currentId = sl.getItems().max("id");
            int nextId = 0;
            if(currentId == null) {
                nextId = 1;
            } else {
                nextId = currentId.intValue() + 1;
            }
            Item item = r.createObject(Item.class, nextId);
            item.setItemName(itemName);
            shoppingList.getItems().add(item);
        });
    }

    public void createShoppingList() {
        realm.executeTransaction(r -> {
            ShoppingList newShopList = r.createObject(ShoppingList.class, 1);
            RealmList<Item> list = new RealmList<>();
            newShopList.setItems(list);
        });
    }


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



        realm.executeTransaction(r -> {
            if(r.isEmpty()) {
                for(int i = 0; i < categoryList.size(); i++) {
                    Category category = r.createObject(Category.class, i);
                    category.setCategoryName(categoryList.get(i));
                    category.setPosition(i);
                }
            }

        });
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