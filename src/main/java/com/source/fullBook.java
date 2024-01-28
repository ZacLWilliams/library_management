package com.source;

public class fullBook {
    private String isbn;
    private String genRating;
    private String title;
    private String author;
    private String year;
    private String publisher;
    private String[] urls;

    fullBook(String inputIsbn, String inputGenRating, String inputTitle, String inputAuthor, String inputYear, String inputPublisher, String[] inputUrls) {
        isbn = inputIsbn;
        genRating = inputGenRating;
        title = inputTitle;
        author = inputAuthor;
        year = inputYear;
        publisher = inputPublisher;
        urls = inputUrls;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getRating() {
        return genRating;
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
