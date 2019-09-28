package com.example.firebaseexample5;

public class ClothModel {
    int ID,Price;
    String Name,Size,Color;
String[] Photos;

    public ClothModel(int ID, int price, String name, String size, String color, String[] photos) {
        this.ID = ID;
        Price = price;
        Name = name;
        Size = size;
        Color = color;
        Photos = photos;
    }

    public int getID() {
        return ID;
    }

    public int getPrice() {
        return Price;
    }

    public String getName() {
        return Name;
    }

    public String getSize() {
        return Size;
    }

    public String getColor() {
        return Color;
    }

    public String[] getPhotos() {
        return Photos;
    }
}
