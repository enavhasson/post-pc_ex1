package com.example.todoboom;

public class TodoItem {
    private String mitem_str;
    private boolean is_selected = false;

    public TodoItem(String item_string) {
        mitem_str = item_string;
    }

    public TodoItem(String item_string, boolean is_selected) {
        this.mitem_str = item_string;
        this.is_selected = is_selected;
    }

    public String get_item_str() {
        return mitem_str;
    }

    public boolean get_is_selected() {
        return is_selected;
    }

//    public void set_is_selected() {
//        this.is_selected = true;
//    }

    public void set_is_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
