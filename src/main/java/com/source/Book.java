package com.source;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String year;
    private String publisher;
    private String[] urls;

    Book(String inputIsbn, String inputTitle, String inputAuthor, String inputYear, String inputPublisher, String[] inputUrls) {
        isbn = inputIsbn;
        title = inputTitle;
        author = inputAuthor;
        year = inputYear;
        publisher = inputPublisher;
        urls = inputUrls;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher;
    }

    public String[] getUrls() {
        return urls;
    }
}