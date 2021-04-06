package com.example.dogcare.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.sql.Blob;

public class DogModel {
    private int id, age;
    private String name, breed;
    private Bitmap image;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) { this.image = image; }

}
