package com.example.kantarellen.ShoppingList;

import com.example.kantarellen.Category.Category;
import com.example.kantarellen.Item;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingList extends RealmObject {


    @PrimaryKey
    private long id;


    private RealmList<Item> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }
}
