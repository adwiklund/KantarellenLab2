package com.example.kantarellen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//http://www.technotalkative.com/android-gridview-example/

public class RecipeAdapter extends BaseAdapter
{
    private ArrayList<String> listRecipe;
    private ArrayList<Bitmap> listRecipePicture;
    private Activity activity;

    public RecipeAdapter(Activity activity, ArrayList<String> listRecipe, ArrayList<Bitmap> listRecipePicture) {
        super();
        this.listRecipe = listRecipe;
        this.listRecipePicture = listRecipePicture;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return listRecipe.size();
    }

    @Override
    public String getItem(int position) {
        return listRecipe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder
    {
        public ImageView imgViewFlag;
        public TextView txtViewTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);

            view.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.imageView1);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtViewTitle.setText(listRecipe.get(position));

        //view.imgViewFlag.setImageResource(listRecipePicture.get(position));
        view.imgViewFlag.setImageBitmap(listRecipePicture.get(position));

        return convertView;
    }
}
