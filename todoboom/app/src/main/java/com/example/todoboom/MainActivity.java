package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private TodoItemAdapter adapter;
    private ArrayList<TodoItem> items = new ArrayList<>();
    private EditText insert_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insert_text = findViewById(R.id.insert_eText);
        Button button = findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insert_text.getText().toString().equals("")){
                    String message = "you can't create an empty TODO item, oh silly!";
                    int duration = Snackbar.LENGTH_SHORT;
                    showSnackbar(insert_text, message, duration);
                }
                else {
                    TodoItem todoItem= new TodoItem(insert_text.getText().toString());
                    items.add(todoItem);
                    adapter.notifyItemInserted(items.size()-1);
                    insert_text.setText("");
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.item_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TodoItemAdapter(this, items);
        recyclerView.setAdapter(adapter);
        if(savedInstanceState!=null) {
            String[] string_array_items = savedInstanceState.getStringArray("TODO_items");
            assert string_array_items != null;
            for (int i = 0; i < string_array_items.length; i++)
                items.add(i,new TodoItem(string_array_items[i]));
            insert_text.setText(savedInstanceState.getString("insert_text"));
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String[] string_array_items = new String[items.size()] ;
        for (int i = 0; i < items.size(); i++)
            string_array_items[i] = items.get(i).getM_item_string();
        outState.putStringArray("TODO_items", string_array_items);
        outState.putString("insert_text", insert_text.toString());
    }

    public void onItemClick(View view, int position) {
        Toast.makeText(this, "TODO " + adapter.getItem(position) + " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

    }

//        final EditText insert_text = (EditText) findViewById(R.id.eText);
//
//        final TextView show_text = (TextView) findViewById(R.id.input_textView);
//
//        Button button = (Button) findViewById(R.id.createButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (insert_text.getText().toString().equals("")){
//                    String message = "you can't create an empty TODO item, oh silly!";
//                    int duration = Snackbar.LENGTH_SHORT;
//                    showSnackbar(insert_text, message, duration);
//                }
//                else {
//                    show_text.setText(insert_text.getText().toString());
//                    insert_text.setText("");
//                }
//            }
//        });


    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
}
