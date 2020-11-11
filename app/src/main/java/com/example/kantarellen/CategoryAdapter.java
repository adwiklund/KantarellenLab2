package com.example.kantarellen;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import io.realm.Realm;

import io.realm.RealmList;
import io.realm.RealmResults;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

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



        //System.out.println("position before = " + categories.get(fromPosition).getPosition());
        //System.out.println("position before = " + categories.get(toPosition).getPosition());
        /*
        realm.executeTransaction(r -> {
            final Category cat1 = realm.where(Category.class).equalTo("position", fromPosition).findFirst();
            final Category cat2 = realm.where(Category.class).equalTo("position", toPosition).findFirst();
            System.out.println("cat1 Pos = " + cat1.getPosition());
            System.out.println("cat2 Pos = " + cat2.getPosition());
            cat1.setPosition(toPosition);
            cat2.setPosition(fromPosition);
            /*
            categories.get(toPosition).setPosition(fromPosition);
            categories.get(fromPosition).setPosition(toPosition);
            realm.copyToRealmOrUpdate(categories);

             */
        //});

        //System.out.println("position after = " + categories.get(fromPosition).getPosition());
        //System.out.println("position after = " + categories.get(toPosition).getPosition());



        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute (Realm realm) {
                Category cat1 = realm.where(Category.class).equalTo("position", fromPosition).findFirst();
                if(cat1 == null) {
                    cat1 = realm.createObject(Category.class, fromPosition);
                }
                cat1.setPosition(toPosition);

                Category cat2 = realm.where(Category.class).equalTo("position", toPosition).findFirst();
                if(cat2 == null) {
                    cat2 = realm.createObject(Category.class, toPosition);
                }
                cat2.setPosition(fromPosition);
            }
        });


        notifyItemMoved(fromPosition, toPosition);
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
