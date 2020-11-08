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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

//http://www.technotalkative.com/android-gridview-example/

public class RecipeActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    /** Called when the activity is first created. */


    private RecipeAdapter mAdapter;
    private ArrayList<String> listRecipe;
    private ArrayList<Bitmap> listRecipePicture;
    private String m_Text = "";

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
                        listRecipe.add(m_Text);
                        imagePicker();

                        //addRecipe(m_Text);
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

        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);

         */


        /*
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

         */

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
                listRecipePicture.add(selectedImage);
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
        listRecipePicture = new ArrayList<Bitmap>();
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

    public void addRecipe(String name, Bitmap imageId) {
        listRecipe.add(name);
        listRecipePicture.add(imageId);
    }
}
