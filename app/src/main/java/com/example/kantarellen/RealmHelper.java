package com.example.kantarellen;

import com.example.kantarellen.Category.Category;
import com.example.kantarellen.Item;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    //WRITE
    public void save(final Item item)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Item s=realm.copyToRealm(item);

            }
        });

    }

    /*
    public void setId(Item item) {
        realm.executeTransaction(r -> {
            Number currentId = realm.where(Item.class).max("id");
            int nextId = 0;
            if (currentId == null) {
                nextId = 1;
            } else {
                nextId = currentId.intValue() + 1;
            }
            item.setId(nextId);
        });
    }

     */

    //READ
    public ArrayList<String> retrieveItemNames()
    {
        ArrayList<String> itemNames=new ArrayList<>();
        RealmResults<Item> items=realm.where(Item.class).sort("category.position").findAll();

        //Item test = realm.where(Item.class).contains("category.categoryName", "Kött").findFirst();
        //if(test != null) System.out.println("Test = " + test.getItemName() + " position = " + test.getCategory().getPosition());


        for(Item i:items)
        {
            //System.out.println("Category Position = " + i.getCategory().getPosition());
            itemNames.add(i.getItemName());
        }

        return itemNames;
    }

    public ArrayList<String> retrieveItemAmounts()
    {
        ArrayList<String> itemAmounts=new ArrayList<>();
        RealmResults<Item> items=realm.where(Item.class).sort("category.position").findAll();

        for(Item i:items)
        {
            itemAmounts.add(i.getAmount());
        }

        return itemAmounts;
    }

    public ArrayList<String> retriveCategories() {
        ArrayList<String> categories = new ArrayList<>();
        RealmResults<Category> realmCategories = realm.where(Category.class).sort("position").findAll();

        for(Category c:realmCategories) {
            categories.add(c.getCategoryName());
        }

        return categories;
    }

    //DELETE
    public void delete() {
        RealmResults<Item> items = realm.where(Item.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for(Item i:items) {
                    i.deleteFromRealm();
                }
            }
        });

    }
}
