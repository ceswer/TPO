package zad1.Models;

public class Book {
    private String publisher, author, title;
    private Double price;

    public Book(String publisher, String author, String title, Double price) {
        this.publisher = publisher;
        this.author = author;
        this.title = title;
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return publisher + " " + author + " " + title + " " + price;
    }
}