package com.example.kantarellen.Category;

import com.example.kantarellen.Item;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {

    @Index
    private String categoryName;

    @PrimaryKey @Index
    private long id;

    private RealmList<Item> items;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    /*
    public void setId(long id) {
        this.id = id;
    }

     */

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }


}
