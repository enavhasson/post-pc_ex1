package com.example.todoboom;

import java.util.Comparator;

class SortbyTimeCreated implements Comparator<TodoItem> {
    // Used for sorting in ascending order of
    // Time_item_created
    public int compare(TodoItem a, TodoItem b) {
        return a.getTime_item_created().compareTo(b.getTime_item_created());
    }
}
