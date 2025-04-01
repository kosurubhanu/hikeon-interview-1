package com.flipcart.search.carina.demo.cucumber.steps;

import com.opencsv.bean.CsvBindByName;

public class CsvRow {

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "price")
    private String price;

    @CsvBindByName(column = "rating")
    private String rating;

    @CsvBindByName(column = "reviews")
    private String reviews;

    public CsvRow(){}
    public CsvRow(String name, String price,String rating,String reviews) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}