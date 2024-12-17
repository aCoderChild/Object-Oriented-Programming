package com.example.my_group_project.Book;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BorrowInfo {
    private SimpleIntegerProperty accessCount;
    private SimpleIntegerProperty borrowCount;

    public BorrowInfo(int accessCount, int borrowCount) {
        this.accessCount = new SimpleIntegerProperty(accessCount);
        this.borrowCount = new SimpleIntegerProperty(borrowCount);
    }

    // Getter methods (if needed)
    public int getAccessCount() {
        return accessCount.get();
    }

    public int getBorrowCount() {
        return borrowCount.get();
    }

    // Property methods for TableView binding
    public SimpleIntegerProperty accessCountProperty() {
        return accessCount;
    }

    public SimpleIntegerProperty borrowCountProperty() {
        return borrowCount;
    }
}
