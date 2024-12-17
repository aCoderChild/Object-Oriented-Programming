package com.example.my_group_project.Book.Searching;

import com.example.my_group_project.Book.Book;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookRecommendation {
    public List<Book> recommendBooks(String query, List<Book> books) {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        Map<CharSequence, Integer> queryVector = createTfIdfVector(query);

        return books.stream()
                .sorted((book1, book2) -> {
                    Map<CharSequence, Integer> vector1 = createTfIdfVector(book1.getDescription());
                    Map<CharSequence, Integer> vector2 = createTfIdfVector(book2.getDescription());

                    double similarity1 = cosineSimilarity.cosineSimilarity(queryVector, vector1);
                    double similarity2 = cosineSimilarity.cosineSimilarity(queryVector, vector2);

                    return Double.compare(similarity2, similarity1); // Sort by descending similarity
                })
                .collect(Collectors.toList());
    }

    private Map<CharSequence, Integer> createTfIdfVector(String text) {
        Map<CharSequence, Integer> vector = new HashMap<>();
        String[] terms = text.toLowerCase().split("\\W+");
        for (String term : terms) {
            vector.put(term, vector.getOrDefault(term, 0) + 1);
        }
        return vector;
    }
}

/*
How the BookRecommendation Class Works:
Purpose:
The primary goal of the BookRecommendation class is to sort and recommend books based on their similarity to a user's search query. This is done using AI techniques to provide more relevant search results.
Components:
Cosine Similarity: A mathematical approach to measure the cosine of the angle between two vectors. It's commonly used in information retrieval to determine how similar two documents are.
TF-IDF Vectorization: Stands for Term Frequency-Inverse Document Frequency. This technique converts text into numerical vectors, weighting terms based on their importance in a document and across all documents.
Key Methods:
recommendBooks(String query, List<Book> books):
Purpose: This method takes a user's query and a list of books, then sorts the books based on their similarity to the query.
Process:
Vectorization: The query and book descriptions are converted into TF-IDF vectors.
Similarity Calculation: Cosine similarity is calculated between the query vector and each book's vector.
Sorting: Books are sorted by their similarity scores in descending order.
createTfIdfVector(String text):
Purpose: Converts a given text (query or book description) into a TF-IDF vector.
Process:
Tokenization: Splits the text into individual terms.
Vector Creation: Creates a vector where each term is weighted based on its frequency.
 */
