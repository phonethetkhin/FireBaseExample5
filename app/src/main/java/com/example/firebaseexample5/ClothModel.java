package com.example.firebaseexample5;

public class ClothModel {
    int ID,Price;
    String Name,Size,Photo,Color;

    public ClothModel(int ID, int price, String name, String size, String photo, String color) {
        this.ID = ID;
        Price = price;
        Name = name;
        Size = size;
        Photo = photo;
        Color = color;
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

    public String getPhoto() {
        return Photo;
    }

    public String getColor() {
        return Color;
    }
}
