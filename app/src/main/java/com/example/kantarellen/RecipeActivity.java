package com.example.kantarellen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

//http://www.technotalkative.com/android-gridview-example/

public class RecipeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    /** Called when the activity is first created. */


    private RecipeAdapter mAdapter;
    private ArrayList<String> listRecipe;
    private ArrayList<Bitmap> listRecipePicture;
    private String m_Text = "";
    Realm realm;
    byte[] byteArray;

    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        realm = Realm.getDefaultInstance();

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

                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View view = li.inflate(R.layout.recipe_window, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext(), R.style.AppTheme_PopupOverlay);
                alertDialogBuilder.setView(view);

                ImageView imageView = view.findViewById(R.id.imageView);
                TextView textView = view.findViewById(R.id.textView);
                //ListView listView = view.findViewById(R.id.list);
                TextView multiTextView = view.findViewById(R.id.editTextTextMultiLine);
                //ImageView btnCancle = view.findViewById(R.id.btnCancle);
                //Button btnContinue = view.findViewById(R.id.btnContinue);

                System.out.println("position = " + position);

                Recipe recipe = realm.where(Recipe.class).equalTo("id", position+1).findFirst();

                System.out.println(recipe.getId());


                textView.setText(recipe.getName());
                imageView.setImageBitmap(createBitmap(recipe.getImage()));

                //btnContinue.setText("string");

                /*
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 7/5/18 your click listener
                    }
                });
                btnContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 7/5/18 your click listener
                    }
                });

                 */

                AlertDialog alertDialogCongratulations = alertDialogBuilder.create();
                alertDialogCongratulations.show();
                alertDialogCongratulations.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//

                Toast.makeText(RecipeActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton newRecipeButton = ( FloatingActionButton ) findViewById(R.id.newRecipeButton);
        newRecipeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
                builder.setTitle("Lägg till nytt recept");

                // Set up the input
                final EditText input = new EditText(RecipeActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        //listRecipe.add(m_Text);
                        imagePicker();


                        //System.out.println("byteArray here = " + byteArray);
                        //addRecipe(m_Text, byteArray);
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

    private void imagePicker() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                System.out.println("Hello");
                InputStream inputStream = RecipeActivity.this.getContentResolver().openInputStream(data.getData());
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                //listRecipePicture.add(selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                System.out.println("Bytearray = " + byteArray);

                addRecipe(m_Text, byteArray);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    /*
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        System.out.println("resultCode = " + resultCode);
        System.out.println("RESULT_OK = " + RESULT_OK);
        if (resultCode == RESULT_OK) {
            //if (reqCode == REQUEST_CODE) {
            System.out.println("Hej?");
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    //ImageView imageView = new ImageView(this);
                    //Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
                    //imageView.setImageBitmap(selectedImage);
                    System.out.println("Hola?");
                    listRecipePicture.add(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

          //  }
        }
    }
    */

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {


        System.out.println(requestCode);
        if (requestCode == REQUEST_CODE) {
            //TODO: action
            System.out.println("Hello?");
        }
    }

     */




    public void prepareList()
    {
        listRecipe = new ArrayList<String>();
        listRecipePicture = new ArrayList<Bitmap>();
        /*
        Recipe recipe;
        if((recipe = realm.where(Recipe.class).findFirst()) == null) {
            return;
        }

         */

        RealmResults<Recipe> recipes = realm.where(Recipe.class).findAll();

        if(recipes != null) {
            realm.executeTransaction(r -> {
                for(int i = 0; i < recipes.size(); i++) {
                    listRecipe.add(recipes.get(i).getName());

                    System.out.println("recipe = " + recipes.get(i).getName());

                    Bitmap bitmap = createBitmap(recipes.get(i).getImage());

                    //Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(i).getImage(), 0, recipes.get(i).getImage().length);

                    listRecipePicture.add(bitmap);
                }

            });
        }
        /*
        realm.executeTransaction(r -> {
            for(int i = 0; i < recipes.size(); i++) {
                listRecipe.add(recipes.get(i).getName());

                Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(i).getImage(), 0, recipes.get(i).getImage().length);

                listRecipePicture.add(bitmap);
            }

            //ShoppingList newShopList = r.createObject(ShoppingList.class, 1);
            //RealmList<Item> list = new RealmList<>();
            //newShopList.setItems(list);
        });

         */


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

        //listRecipePicture.add(R.drawable.india);
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

    public Bitmap createBitmap(byte[] bitmapData) {
        Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
        System.out.println("bitmapData = " + bitmapData);
        ByteBuffer buffer = ByteBuffer.wrap(bitmapData);
        bitmap.copyPixelsFromBuffer(buffer);
        return bitmap;
    }

    public void addRecipe(String name, byte[] imageId) {

        realm.executeTransaction(r -> {
            Recipe recipe = r.createObject(Recipe.class, listRecipe.size() + 1);
            //recipe.setId(listRecipe.size() + 1);
            recipe.setName(name);
            recipe.setImage(imageId);

            listRecipe.add(name);

            //Bitmap bitmap = BitmapFactory.decodeByteArray(imageId, 0, imageId.length);
            Bitmap bitmap = createBitmap(imageId);
            listRecipePicture.add(bitmap);

            //ShoppingList newShopList = r.createObject(ShoppingList.class, 1);
            //RealmList<Item> list = new RealmList<>();
            //newShopList.setItems(list);
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
