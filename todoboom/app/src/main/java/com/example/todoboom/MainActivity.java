package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String SP_USER_TEXT_INPUT = "insert_text";
    private static final String SP_IS_DONE_TODO_ITEMS = "is_done_TODO_items";
    private static final String SP_STR_TODO_ITEMS = "str_TODO_items";
    private static final String SP_TODO_ITEMS = "TODO_items";

    private TodoItemAdapter m_adapter;
    private ArrayList<TodoItem> m_items = new ArrayList<>();
    private EditText m_insert_text;
    private SharedPreferences m_sp;
    private Gson gson =new Gson();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_insert_text = findViewById(R.id.insert_eText);
        Button button = findViewById(R.id.createButton);

        m_sp = this.getSharedPreferences("my_items",MODE_PRIVATE);

        if (savedInstanceState != null) {
            Gson gson = new Gson();
            String jsonItems = m_sp.getString(SP_TODO_ITEMS, null);
            this.m_items = gson.fromJson(jsonItems,
                    new TypeToken<ArrayList<TodoItem>>() {}.getType());
            m_insert_text.setText(m_sp.getString(SP_USER_TEXT_INPUT, ""));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_insert_text.getText().toString().equals("")) {
                    String message = "you can't create an empty TODO item, oh silly!";
                    int duration = Snackbar.LENGTH_SHORT;
                    showSnackbar(m_insert_text, message, duration);
                } else {
                    TodoItem todoItem = new TodoItem(m_insert_text.getText().toString());
                    m_items.add(todoItem);
                    m_adapter.notifyItemInserted(m_items.size() - 1);
                    m_insert_text.setText("");

                    //save new item and empty input
                    SharedPreferences.Editor editor = m_sp.edit();
                    String json_items = gson.toJson(m_items);
                    editor.putString(SP_TODO_ITEMS, json_items);
                    editor.putString(SP_USER_TEXT_INPUT, m_insert_text.getText().toString());
                    editor.apply();

                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.item_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        m_adapter = new TodoItemAdapter(this, this.m_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(m_adapter);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences.Editor editor = m_sp.edit();
        editor.putString(SP_TODO_ITEMS, gson.toJson(m_items));
        editor.putString(SP_USER_TEXT_INPUT, m_insert_text.getText().toString());
        editor.apply();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
}
