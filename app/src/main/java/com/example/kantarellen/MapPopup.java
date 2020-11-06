package com.example.kantarellen;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MapPopup {

    private static PopupWindow mapLayout;
    //private int shelfId;
    final int[] shelfId = new int[1];

    public MapPopup(PopupWindow mapLayout) {
        this.mapLayout = mapLayout;
    }

    public MapPopup() {

    }


    public void showMapPopup(final Activity context)
    {
        // Inflate the popup_layout.xml
        ConstraintLayout viewGroup = (ConstraintLayout) context.findViewById(R.id.maplayout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.map_layout, viewGroup);

        // Creating the PopupWindow
        mapLayout = new PopupWindow(context);
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
        /*
        Button one = (Button) context.findViewById(R.id.button);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) context.findViewById(R.id.button2);
        two.setOnClickListener(this);
        Button three = (Button) context.findViewById(R.id.button3);
        three.setOnClickListener(this);

         */
        //int shelfId;

        // Getting a reference to Close button, and close the popup when clicked.
        //int shelfId = 0;

        Button close = (Button) layout.findViewById(R.id.button);
        close.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                shelfId[0] = 1;
                System.out.println("shelfId[0] = " + shelfId[0]);
                setShelf(shelfId[0]);
                mapLayout.dismiss();
            }
        });

        //System.out.println("shelfid =" + shelfId[0]);
        //return shelfId[0];
    }

    public void setShelf(int shelfId) {

    }
    /*
    @Override
    public void onClick(View view) {
        mapLayout.dismiss();
    }

     */
}
