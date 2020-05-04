package com.example.todoboom;

public class TodoItem {
    private String m_item_string;
    private Boolean m_isDone = false;

    public TodoItem( String item_string){
        m_item_string=item_string;
    }

    public String getM_item_string() {
        return m_item_string;
    }

}
