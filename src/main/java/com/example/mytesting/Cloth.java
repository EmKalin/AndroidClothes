package com.example.mytesting;

public class Cloth {
    @Override
    public String toString() {
        return "Cloth{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    private String name;
    private String type;
    private String color;
    private String imageUrl;
    private String image;

    public Cloth() {

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImage() {
        return image;
    }

    public Cloth(String kName, String kType, String kColor, String kImageUrl, String kImage){
        name = kName;
        type = kType;
        color = kColor;
        imageUrl = kImageUrl;
        image = kImage;
    }

}
