package com.example.todoboom;

class TodoItem {
    private String str_item;
    private boolean is_selected = false;

    TodoItem(String str_item) {
        this.str_item = str_item;
    }

    TodoItem(String str_item, boolean is_selected) {
        this.str_item = str_item;
        this.is_selected = is_selected;
    }

    String get_item_str() {
        return str_item;
    }

    boolean get_is_selected() {
        return is_selected;
    }

    void set_is_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
