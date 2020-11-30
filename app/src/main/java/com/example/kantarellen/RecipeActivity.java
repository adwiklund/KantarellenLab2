package com.example.kantarellen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
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
    private ArrayList<String> items;

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

                LayoutInflater li = LayoutInflater.from(RecipeActivity.this);
                View view = li.inflate(R.layout.recipe_window, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecipeActivity.this, R.style.Theme_MaterialComponents_Dialog_Alert);
                alertDialogBuilder.setView(view);

                ImageView imageView = view.findViewById(R.id.recipeImageView);
                TextView textView = view.findViewById(R.id.textView);
                //ListView listView = view.findViewById(R.id.list);
                RecyclerView rvItems = view.findViewById(R.id.rvItems);
                TextView instructionTextView = view.findViewById(R.id.instructionTextView);
                //ImageView btnCancle = view.findViewById(R.id.btnCancle);
                Button btnAdd = view.findViewById(R.id.btnAdd);

                System.out.println("position = " + position);

                Recipe recipe = realm.where(Recipe.class).equalTo("id", position+1).findFirst();

                System.out.println("id = " + recipe.getId());
                System.out.println("bitmapData = " + recipe.getImage());
                System.out.println("bitmapData length = " + recipe.getImage().length);


                textView.setText(recipe.getName());

                //Recipe test = realm.where(Recipe.class).findFirst();
                //Bitmap testBitmap = createBitmap(test.getImage());
                //imageView.setImageBitmap(testBitmap);

                //Bitmap bitmap = createBitmap(recipe.getImage());
                //imageView.setImageBitmap(bitmap);

                System.out.println("listRecipePicture size = " + listRecipePicture.size());

                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
                bitmap = rotateImage(90, bitmap);

                //imageView.setImageBitmap(listRecipePicture.get(0));
                imageView.setImageBitmap(bitmap);

                /*
                ArrayList<String> itemArrayList = new ArrayList<>();

                itemArrayList.add("Smör");
                itemArrayList.add("Ägg");
                itemArrayList.add("Mjöl");
                itemArrayList.add("Socker");

                 */

                items = new ArrayList<>();

                //RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);
                Item item0 = new Item();
                item0.setItemName("Smör");
                //item0.setId(0);
                item0.setId(realm);
                items.add(item0.getItemName());
                Item item1 = new Item();
                item1.setItemName("Mjölk");
                //item1.setId(1);
                item1.setId(realm);
                items.add(item1.getItemName());
                Item item2 = new Item();
                item2.setItemName("Vetemjöl");
                item2.setId(realm);
                //item2.setId(2);
                items.add(item2.getItemName());
                Item item3 = new Item();
                item3.setItemName("Jäst");
                item3.setId(realm);
                //item3.setId(3);
                items.add(item3.getItemName());

                RecipeItemAdapter itemAdapter = new RecipeItemAdapter(items);

                rvItems.setAdapter(itemAdapter);
                rvItems.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));
                /*
                CategoryAdapter itemAdapter = new CategoryAdapter(itemArrayList);

                listView.setAdapter((ListAdapter) itemAdapter);

                 */


                instructionTextView.setText("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                        "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                        "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                        "deserunt mollit anim id est laborum.\"");

                //btnContinue.setText("string");

                /*
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 7/5/18 your click listener
                    }
                });
                */
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();
                        RealmList<Item> shoppingListItems = shoppingList.getItems();
                        for(int i = 0; i < items.size(); i++) {
                            //shoppingListItems.add(items.get(i));
                        }

                         */

                    }
                });



                AlertDialog alertDialogCongratulations = alertDialogBuilder.create();
                alertDialogCongratulations.show();
                alertDialogCongratulations.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//

                //Toast.makeText(RecipeActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
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
                System.out.println("bitmapData = " + byteArray);
                System.out.println("BitmapData length = " + byteArray.length);

                addRecipe(m_Text, byteArray);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    public void prepareList()
    {
        listRecipe = new ArrayList<String>();
        listRecipePicture = new ArrayList<Bitmap>();

        RealmResults<Recipe> recipes = realm.where(Recipe.class).findAll();

        if(recipes != null) {
            realm.executeTransaction(r -> {
                for(int i = 0; i < recipes.size(); i++) {
                    listRecipe.add(recipes.get(i).getName());

                    System.out.println("recipe = " + recipes.get(i).getName());

                    //byte[] bytes = Base64.decode(recipes.get(i).getImage(), Base64.DEFAULT);
                    //Bitmap bitmap = createBitmap(bytes);
                    //Bitmap bitmap = createBitmap(recipes.get(i).getImage());


                    Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(i).getImage(), 0, recipes.get(i).getImage().length);
                    bitmap = rotateImage(90, bitmap);

                    listRecipePicture.add(bitmap);
                }

            });
        }

    }

    public Bitmap createBitmap(byte[] bitmapData) {
        Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
        System.out.println("bitmapData = " + bitmapData);
        System.out.println("BitmapData length = " + bitmapData.length);
        ByteBuffer buffer = ByteBuffer.wrap(bitmapData);
        bitmap.copyPixelsFromBuffer(buffer);



        return bitmap;
    }

    public void addRecipe(String name, byte[] imageId) {

        realm.executeTransaction(r -> {
            Recipe recipe = r.createObject(Recipe.class, listRecipe.size() + 1);
            //recipe.setId(listRecipe.size() + 1);
            recipe.setName(name);
            //String encodeImage = Base64.encodeToString(imageId, Base64.DEFAULT);
            recipe.setImage(imageId);
            //recipe.setImage(encodeImage);

            listRecipe.add(name);

            //Bitmap bitmap = BitmapFactory.decodeByteArray(imageId, 0, imageId.length);
            System.out.println("bitmapData = " + imageId);
            System.out.println("BitmapData length = " + imageId.length);

            //byte[] bytes = Base64.decode(imageId, Base64.DEFAULT);
            //Bitmap bitmap = createBitmap(bytes);

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageId, 0, imageId.length);

            bitmap = rotateImage(90, bitmap);

            //Bitmap bitmap = createBitmap(imageId);
            listRecipePicture.add(bitmap);

            //ShoppingList newShopList = r.createObject(ShoppingList.class, 1);
            //RealmList<Item> list = new RealmList<>();
            //newShopList.setItems(list);
        });

    }

    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
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
