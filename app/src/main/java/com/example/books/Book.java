package com.example.books;

public class Book {

    private String mImageLink;

    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;
    private String mCategory;
    private double mPrice;
    private String mBuyLink;

    public Book(String imageLink,String title,String author,String publishedDate,String category,double price,String buyLink){
        mImageLink = imageLink;
        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
        mCategory = category;
        mPrice = price;
        mBuyLink = buyLink;
    }

    public String getmImageLink() {
        return mImageLink;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public String getmCategory() {
        return mCategory;
    }

    public double getmPrice() {
        return mPrice;
    }

    public String getmBuyLink() {
        return mBuyLink;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mPublishedDate='" + mPublishedDate + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mPrice=" + mPrice +
                '}';
    }
}
