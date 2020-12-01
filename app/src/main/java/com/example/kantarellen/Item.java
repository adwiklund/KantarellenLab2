package com.example.kantarellen;

import com.example.kantarellen.Category.Category;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    @Index
    private String itemName;

    @PrimaryKey
    private long id;

    private Category category;

    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getId() {
        return id;
    }

    public void setId(Realm realm) {
         realm.executeTransaction(r -> {
        Number currentId = realm.where(Item.class).max("id");
        int nextId = 0;
        if (currentId == null) {
            nextId = 1;
        } else {
            nextId = currentId.intValue() + 1;
        }
        //item.setId(nextId);
        this.id = nextId;
    });
    }
}
