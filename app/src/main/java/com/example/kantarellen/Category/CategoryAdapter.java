package com.example.kantarellen.Category;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.Item;
import com.example.kantarellen.R;

import java.util.ArrayList;
import java.util.Collections;


import io.realm.Realm;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements CategoryMoveCallback.ItemTouchHelperContract {

    private ArrayList<String> data;
    Realm realm;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.txtTitle);
        }
    }

    public CategoryAdapter(ArrayList<String> data) {
        realm = Realm.getDefaultInstance();
        this.data = data;
    }

    public void setData(ArrayList<String> data) { this.data = data; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        //realm = Realm.getDefaultInstance();
        RealmResults<Category> categories = realm.where(Category.class).findAll();

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);

                swap(categories, i, i + 1);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);

                swap(categories, i, i - 1);

            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void swap(RealmResults<Category> categories, final int i, final int j) {



        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final int temp = categories.get(i).getPosition();
                categories.get(i).setPosition(j);
                categories.get(j).setPosition(temp);
            }
        });


    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}
