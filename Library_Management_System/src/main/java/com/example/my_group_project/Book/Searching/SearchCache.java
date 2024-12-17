package com.example.my_group_project.Book.Searching;

import com.example.my_group_project.Book.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCache {
    private Map<String, List<Book>> cache;

    public SearchCache() {
        this.cache = new HashMap<>();
    }

    public void put(String query, List<Book> books) {
        cache.put(query, books);
    }

    public List<Book> get(String query) {
        return cache.get(query);
    }

    public boolean contains(String query) {
        return cache.containsKey(query);
    }

    public void clear() {
        cache.clear();
    }
}
