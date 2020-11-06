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
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private String m_Text = "";
    MapPopup mapPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapPopup = new MapPopup();

        listView = (ListView) findViewById(R.id.mobile_list);

        final ArrayList<String> shoppinglist = new ArrayList<>();


        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };



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
                row.setPaintFlags(row.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //parent.getChildAt(itemPosition).setBackgroundColor(Color.GREEN);


                //save = position;

            }

        });


        //final mapPopup popup = new mapPopup();


        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

       // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ny vara");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (inventory.contains(m_Text)) {
                            shoppinglist.add(m_Text);
                        } else {
                            //int i = mapPopup.showMapPopup(MainActivity.this);
                            //int i = showMapPopup();
                            mapPopup.showMapPopup(MainActivity.this);
                            //System.out.println("i = " + i);
                            //editor.putInt(m_Text, i);
                            //editor.apply();
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
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
            }
        });

        //int defaultValue = getResources().getInteger(0);
        int highScore = sharedPref.getInt(getString(R.string.preference_file_key), 0);

        System.out.println("test = " + highScore);
    }
    /*
    public void showMapPopup() {
        return mapPopup.showMapPopup();
    }
    */

    /*
    private void showMapPopup(final Activity context)
    {
        // Inflate the popup_layout.xml
        ConstraintLayout viewGroup = (ConstraintLayout) context.findViewById(R.id.maplayout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.map_layout, viewGroup);

        // Creating the PopupWindow
        final PopupWindow mapLayout = new PopupWindow(context);
        mapLayout.setContentView(layout);
        mapLayout.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mapLayout.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mapLayout.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -20;
        int OFFSET_Y = 95;

        // Clear the default translucent background
        mapLayout.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        //mapLayout.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        mapLayout.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X,  OFFSET_Y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.button);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mapLayout.dismiss();
            }
        });

    }

     */

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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_categories) {
            Intent activity2Intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(activity2Intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}