package com.example.todoboom;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

class TodoItem {
    private String item_text;
    private boolean is_done;
    private String time_last_modified;
    private String time_item_created;
    private String id = UUID.randomUUID().toString();


    TodoItem(String item_text) {
        this.is_done = false;
        this.item_text = item_text;
        this.time_item_created = java.text.DateFormat.getDateTimeInstance().format(new Date());
        this.time_last_modified = time_item_created;
    }

    TodoItem(String item_text, String id) {
        this.is_done = false;
        this.item_text = item_text;
        this.time_item_created = java.text.DateFormat.getDateTimeInstance().format(new Date());
        this.time_last_modified = time_item_created;
        this.id = id;
    }

    TodoItem(TodoItem item) {
        this.is_done = item.is_done;
        this.item_text = item.item_text;
        this.time_item_created = item.time_item_created;
        this.time_last_modified = item.time_item_created;
        this.id = item.id;
    }


    TodoItem(String item_text, boolean is_selected) {
        this.item_text = item_text;
        this.is_done = is_selected;
    }

    TodoItem(String item_text, boolean is_done, String time_last_modified,
             String time_item_created, String id) {
        this.is_done = is_done;
        this.item_text = item_text;
        this.time_item_created = time_item_created;
        this.time_last_modified = time_item_created;
        this.id = id;
    }

    TodoItem(DocumentSnapshot docS) {
        this.is_done= Objects.requireNonNull((Objects.requireNonNull(docS.getData())).get(ItemsFirebase.KEY_IS_DONE)).toString().equals("true");
        this.item_text = (String) (docS.getData()).get(ItemsFirebase.KEY_STR_ITEM);
        this.time_item_created = (String) (docS.getData()).get(ItemsFirebase.KEY_TIME_ITEM_CREATED);
        this.time_last_modified = (String) (docS.getData()).get(ItemsFirebase.KEY_TIME_LAST_MODIFIED);
        this.id = (String) (docS.getData()).get(ItemsFirebase.KEY_ID_ITEM);
    }

//    TodoItem(DocumentSnapshot docS) {
//        assert docS.getString(ItemsInFirebase.KEY_IS_DONE)!=null;
//        this.is_done= docS.getString(ItemsInFirebase.KEY_IS_DONE).equals("true");
//        this.item_text = docS.getString(ItemsInFirebase.KEY_STR_ITEM);
//        this.time_item_created = docS.getString(ItemsInFirebase.KEY_TIME_ITEM_CREATED);
//        this.time_last_modified = docS.getString(ItemsInFirebase.KEY_TIME_LAST_MODIFIED);
//        this.id = docS.getString(ItemsInFirebase.KEY_ID_ITEM);
//    }

//    public TodoItem(Object item_text, Object is_done, Object time_last_modified, Object time_item_created, Object id) {
//        if (item_text.getClass()  String;
//
//    }

    public void setTime_last_modified(String time_last_modified) {
        this.time_last_modified = time_last_modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItem_text(String item_text) {
        this.item_text = item_text;
    }

    public String getTime_item_created() {
        return time_item_created;
    }

    public String getTime_last_modified() {
        return time_last_modified;
    }

    String get_item_str() {
        return item_text;
    }

    boolean get_isDone() {
        return is_done;
    }

    void set_isDone(boolean is_done) {
        this.is_done = is_done;
    }
}
