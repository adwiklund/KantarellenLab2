package com.example.kantarellen;

import com.example.kantarellen.Category.Category;
import com.example.kantarellen.Item;
import com.example.kantarellen.Recipe.Recipe;
import com.example.kantarellen.ShoppingList.ShoppingList;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
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

                Item i = realm.copyToRealm(item);
                ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();
                RealmList<Item> listItems = shoppingList.getItems();
                listItems.add(i);
                shoppingList.setItems(listItems);


            }
        });

    }

    public void fillShoppingList(ShoppingList shoppingList) {

        //RealmResults<Item> items=realm.where(Item.class).sort("category.position").findAll();
        RealmResults<Item> items = shoppingList.getItems().sort("category.position");
        RealmList<Item> listItems = new RealmList<>();
        for(Item i:items) {
            listItems.add(i);

        }
        realm.executeTransaction(r -> {
            shoppingList.setItems(listItems);
        });
    }

    //READ
    public ArrayList<String> retrieveItemNames()
    {
        ArrayList<String> itemNames=new ArrayList<>();
        ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();
        RealmResults<Item> items = shoppingList.getItems().sort("category.position");

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
        ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();
        RealmResults<Item> items = shoppingList.getItems().sort("category.position");
        //RealmResults<Item> items=realm.where(Item.class).sort("category.position").findAll();

        for(Item i:items)
        {
            itemAmounts.add(i.getAmount());
        }

        return itemAmounts;
    }

    public ArrayList<String> retrieveCategories() {
        ArrayList<String> categories = new ArrayList<>();
        RealmResults<Category> realmCategories = realm.where(Category.class).sort("position").findAll();

        for(Category c:realmCategories) {
            categories.add(c.getCategoryName());
        }

        return categories;
    }

    public ArrayList<String> retrieveInstructions() {
        ArrayList<String> instructions = new ArrayList<>();
        RealmResults<Recipe> realmRecipes = realm.where(Recipe.class).findAll();

        for(Recipe r: realmRecipes) {
            instructions.add(r.getInstructions());
        }
        return instructions;
    }

    //DELETE
    public void delete() {
        RealmResults<Item> items = realm.where(Item.class).findAll();
        ShoppingList shoppingList = realm.where(ShoppingList.class).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                shoppingList.getItems().clear();

            }
        });

    }
}
