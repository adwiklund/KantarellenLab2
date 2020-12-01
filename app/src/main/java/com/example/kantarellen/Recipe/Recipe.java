package com.example.kantarellen.Recipe;

import android.graphics.Bitmap;
import android.media.Image;

import com.example.kantarellen.Item;

import javax.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Recipe extends RealmObject {

    @PrimaryKey
    private long id;

    private RealmList<Item> items;

    private byte[] image;
    //private String image;

    private String name;

    @Nullable
    private String instructions;

    /*
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

     */

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


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
