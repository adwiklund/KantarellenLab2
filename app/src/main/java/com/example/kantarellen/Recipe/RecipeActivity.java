package com.example.kantarellen.Recipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.Category.Category;
import com.example.kantarellen.Category.CategoryActivity;
import com.example.kantarellen.Item;
import com.example.kantarellen.MainActivity;
import com.example.kantarellen.R;
import com.example.kantarellen.RealmHelper;
import com.example.kantarellen.ShoppingList.ShoppingList;
import com.example.kantarellen.ShoppingList.ShoppingListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private ArrayList<String> listRecipeInstructions;
    private String m_Text = "";
    Realm realm;
    byte[] byteArray;
    private ArrayList<String> items;
    private ArrayList<String> amounts;

    private GridView gridView;
    RealmList<Item> recipeItems;

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
                Button btnAddIngredient = view.findViewById(R.id.btnAddIngredient);
                TextView instructionTextView = view.findViewById(R.id.instructionTextView);
                //ImageView btnCancle = view.findViewById(R.id.btnCancle);
                Button btnEditInstructions = view.findViewById(R.id.btnEditInstructions);
                Button btnAddToList = view.findViewById(R.id.btnAddToList);

                Recipe recipe = realm.where(Recipe.class).equalTo("id", position+1).findFirst();

                textView.setText(recipe.getName());

                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
                bitmap = rotateImage(90, bitmap);

                imageView.setImageBitmap(bitmap);

                instructionTextView.setText(recipe.getInstructions());

                recipeItems = recipe.getItems();

                items = new ArrayList<>();
                amounts = new ArrayList<>();

                for(int i = 0; i < recipeItems.size(); i++) {
                    items.add(recipeItems.get(i).getItemName());
                    amounts.add(recipeItems.get(i).getAmount());
                }

                //RecipeItemAdapter itemAdapter = new RecipeItemAdapter(items);
                RecipeItemAdapter itemAdapter = new RecipeItemAdapter(items, amounts);

                btnAddIngredient.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       displayInputDialog();
                   }
                });

                rvItems.setAdapter(itemAdapter);
                rvItems.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));

                btnEditInstructions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        instructionTextView.setCursorVisible(true);
                        instructionTextView.setFocusableInTouchMode(true);
                        instructionTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                        instructionTextView.requestFocus();
                    }
                });

                btnAddToList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();
                        RealmHelper helper = new RealmHelper(realm);

                        for(Item i : recipeItems) {
                            helper.save(i);
                        }

                    }
                });



                AlertDialog alertDialogCongratulations = alertDialogBuilder.create();
                alertDialogCongratulations.show();
                alertDialogCongratulations.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//

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
                        imagePicker();
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
        listRecipeInstructions = new ArrayList<String>();

        RealmResults<Recipe> recipes = realm.where(Recipe.class).findAll();

        if(recipes != null) {
            realm.executeTransaction(r -> {
                for(int i = 0; i < recipes.size(); i++) {
                    listRecipe.add(recipes.get(i).getName());

                    System.out.println("recipe = " + recipes.get(i).getName());

                    Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(i).getImage(), 0, recipes.get(i).getImage().length);
                    bitmap = rotateImage(90, bitmap);

                    listRecipePicture.add(bitmap);

                    listRecipeInstructions.add(recipes.get(i).getInstructions());
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
            recipe.setName(name);
            recipe.setImage(imageId);

            String testInstructions = "Test Instructions";
            recipe.setInstructions(testInstructions);

            listRecipe.add(name);

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageId, 0, imageId.length);

            bitmap = rotateImage(90, bitmap);

            listRecipePicture.add(bitmap);

            listRecipeInstructions.add(testInstructions);

        });

    }

    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }

    private void displayInputDialog()
    {
        Dialog d=new Dialog(this);
        d.setTitle("Ny Vara");
        d.setContentView(R.layout.shoppinglist_input_dialog);
        RealmHelper helper=new RealmHelper(realm);
        ArrayList<String> categoryList = helper.retrieveCategories();

        EditText nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        EditText amountEditTxt = (EditText) d.findViewById(R.id.amountEditText);
        Spinner categorySpinner = (Spinner) d.findViewById(R.id.categorySpinner);
        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);

        final Category[] category = new Category[1];


        ArrayAdapter categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RecipeActivity.this, categoryList.get(i),Toast.LENGTH_SHORT).show();
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
                realm.executeTransaction(r -> {
                            recipeItems.add(i);
                        });
                //helper.save(i);
                items.add(i.getItemName());
                amounts.add(i.getAmount());
                nameEditTxt.setText("");
                amountEditTxt.setText("");

            }
        });

        d.show();
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
