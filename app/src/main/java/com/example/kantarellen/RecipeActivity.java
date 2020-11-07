package com.example.kantarellen;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipeActivity extends Activity {
    /** Called when the activity is first created. */

    private RecipeAdapter mAdapter;
    private ArrayList<String> listRecipe;
    private ArrayList<Integer> listRecipePicture;

    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        prepareList();

        // prepared arraylist and passed it to the Adapter class
        mAdapter = new RecipeAdapter(this, listRecipe, listRecipePicture);

        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(mAdapter);

        // Implement On Item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Toast.makeText(RecipeActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void prepareList()
    {
        listRecipe = new ArrayList<String>();
        /*
        listRecipe.add("india");
        listRecipe.add("Brazil");
        listRecipe.add("Canada");
        listRecipe.add("China");
        listRecipe.add("France");
        listRecipe.add("Germany");
        listRecipe.add("Iran");
        listRecipe.add("Italy");
        listRecipe.add("Japan");
        listRecipe.add("Korea");
        listRecipe.add("Mexico");
        listRecipe.add("Netherlands");
        listRecipe.add("Portugal");
        listRecipe.add("Russia");
        listRecipe.add("Saudi Arabia");
        listRecipe.add("Spain");
        listRecipe.add("Turkey");
        listRecipe.add("United Kingdom");
        listRecipe.add("United States");


         */
        listRecipePicture = new ArrayList<Integer>();
        //listRecipePicture.add(R.drawable.ic_launcher_background);
        /*
        listFlag.add(R.drawable.brazil);
        listFlag.add(R.drawable.canada);
        listFlag.add(R.drawable.china);
        listFlag.add(R.drawable.france);
        listFlag.add(R.drawable.germany);
        listFlag.add(R.drawable.iran);
        listFlag.add(R.drawable.italy);
        listFlag.add(R.drawable.japan);
        listFlag.add(R.drawable.korea);
        listFlag.add(R.drawable.mexico);
        listFlag.add(R.drawable.netherlands);
        listFlag.add(R.drawable.portugal);
        listFlag.add(R.drawable.russia);
        listFlag.add(R.drawable.saudi_arabia);
        listFlag.add(R.drawable.spain);
        listFlag.add(R.drawable.turkey);
        listFlag.add(R.drawable.united_kingdom);
        listFlag.add(R.drawable.united_states);


         */
    }

    public void addRecipe(String name, int imageId) {
        listRecipe.add(name);
        listRecipePicture.add(imageId);
    }
}
