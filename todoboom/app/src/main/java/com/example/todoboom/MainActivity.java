package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TodoItemAdapter adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<TodoItem> items = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterItems = new TodoItemAdapter(this, items);
        adapterItems.setClickListener((TodoItemAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapterItems);
    }

    public void onItemClick(View view, int position) {
        Toast.makeText(this, "TODO " + adapterItems.getItem(position) + " is now DONE. BOOM! " + position, Toast.LENGTH_SHORT).show();
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
