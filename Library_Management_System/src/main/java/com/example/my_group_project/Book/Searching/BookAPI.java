package com.example.my_group_project.Book.Searching;

import com.example.my_group_project.Book.Book;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class BookAPI {
    private static final String APPLICATION_NAME = "GoogleBooksJavaFXApp";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Books getBooksService() throws GeneralSecurityException, IOException {
        return new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<Book> getRecommendedBooks(String genre) throws IOException, GeneralSecurityException {
        Books books = getBooksService();
        String query = "subject:" + genre; // Assuming genre is a subject in Google Books API
        Books.Volumes.List volumesList = books.volumes().list(query);
        Volumes volumes = volumesList.execute();
        return extractBooks(volumes);
    }

    public static List<Book> searchBooks(String query) throws IOException, GeneralSecurityException {
        Books books = getBooksService();
        Books.Volumes.List volumesList = books.volumes().list(query);
        Volumes volumes = volumesList.execute();
        return extractBooks(volumes);
    }

    private static List<Book> extractBooks(Volumes volumes) {
        List<Book> bookList = new ArrayList<>();
        if (volumes.getItems() != null) {
            for (Volume volume : volumes.getItems()) {
                String bookID = volume.getId();
                String title = volume.getVolumeInfo().getTitle();
                String author = (volume.getVolumeInfo().getAuthors() != null) ? String.join(", ", volume.getVolumeInfo().getAuthors()) : "No authors found";
                String imageURL = (volume.getVolumeInfo().getImageLinks() != null) ? volume.getVolumeInfo().getImageLinks().getThumbnail() : null;
                String description = (volume.getVolumeInfo().getDescription() != null) ? volume.getVolumeInfo().getDescription() : "No description available";
                String genre = (volume.getVolumeInfo().getCategories() != null) ? String.join(", ", volume.getVolumeInfo().getCategories()) : "No genre available";
                // Retrieve and parse the published date
                String publishDate = volume.getVolumeInfo().getPublishedDate();
                int yearPublic;
                if (publishDate != null) {
                    try {
                    LocalDate date = LocalDate.parse(publishDate, DateTimeFormatter.ISO_DATE);
                    yearPublic = date.getYear(); } catch (DateTimeParseException e) {
                    // Handle the case where the date is in a different format, e.g., just the year
                    try {
                        yearPublic = Integer.parseInt(publishDate.substring(0, 4));
                    } catch (NumberFormatException ex) {
                        // If parsing fails, set a default value or handle the error accordingly
                        yearPublic = -1; // Use -1 or another value to indicate an unknown year
                        }
                    }
                } else {
                    // Default value if the publish date is not available
                    yearPublic = -1; // Use -1 or another value to indicate an unknown year
                }
                bookList.add(new Book(bookID, title, author, imageURL, description, genre,0, yearPublic));
            }
        } return bookList;
    }
}