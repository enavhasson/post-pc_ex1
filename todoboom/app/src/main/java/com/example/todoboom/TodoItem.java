package com.example.todoboom;
import android.icu.util.LocaleData;

import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.LocalDateTime;

class TodoItem {
    private String str_item;
    private boolean is_selected ;
    private String time_last_modified;
    private String time_item_created;
    private int id;

    TodoItem(String str_item,String time_last_modified,String time_item_created) {
        this.is_selected= false;
        this.str_item = str_item;
        this.time_last_modified=time_last_modified;
        this.time_item_created=time_item_created;
    }

    TodoItem(String str_item,int id) {
        this.is_selected= false;
        this.str_item = str_item;
        this.time_item_created=java.text.DateFormat.getDateTimeInstance().format(new Date());
        this.time_last_modified=time_item_created;
        this.id=id;
    }


    TodoItem(String str_item, boolean is_selected) {
        this.str_item = str_item;
        this.is_selected = is_selected;
    }

    public void setTime_last_modified(String time_last_modified) {
        this.time_last_modified = time_last_modified;
    }

    public int getId() {
        return id;
    }

    public void setStr_item(String str_item) {
        this.str_item = str_item;
    }

    public String getTime_item_created() {
        return time_item_created;
    }

    public String getTime_last_modified() {
        return time_last_modified;
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
