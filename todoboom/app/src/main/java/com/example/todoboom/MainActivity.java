package com.example.todoboom;

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
    TodoItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<TodoItem> items = new ArrayList<>();
        final EditText insert_text = findViewById(R.id.insert_eText);
        CheckBox checkBox = findViewById(R.id.checkBox);
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
