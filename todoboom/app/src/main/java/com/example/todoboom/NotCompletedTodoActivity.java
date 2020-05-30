package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class NotCompletedTodoActivity extends AppCompatActivity {
    private static final String SP_USER_TEXT_EDIT = "edit_text";


    Button applyButton;
    Button doneButton;
    EditText itemContentTextEditText;
    TextView timeItemCreated;
    TextView timeLastModifiedItem;
    TodoItem todoItem;
    ItemsFirebase fbInstance;
    String id_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_completed_todo);
        id_item = getIntent().getStringExtra("item_id");
        applyButton = findViewById(R.id.applyButton);
        doneButton = findViewById(R.id.doneItemButton);
        itemContentTextEditText = findViewById(R.id.editTextItemContent);
        timeItemCreated = findViewById(R.id.textTimeItemCreated);
        timeLastModifiedItem = findViewById(R.id.textTimeLastModifiedItem);
        fbInstance = ItemsFirebase.getInstance();

        todoItem = fbInstance.getTodoItemById(id_item);

        itemContentTextEditText.setText(todoItem.get_item_str());
        timeLastModifiedItem.setText(todoItem.getTime_last_modified());
        timeItemCreated.setText(todoItem.getTime_item_created());
        initButtons();
    }

    private void initButtons() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemContentTextEditText.getText().toString().equals("")) {
                    String message = "you can't edit an empty TODO item, oh silly!";
                    MainActivity.showMessage(v.getContext(),message);
                } else if (itemContentTextEditText.getText().toString().equals
                        (todoItem.get_item_str())) {
                    String message = "No change has been made";
                    MainActivity.showMessage(v.getContext(),message);
                } else {
                    String itemContentText = itemContentTextEditText.getText().toString();
                    fbInstance.updateContextTodoItem(id_item, itemContentText);
                    todoItem=fbInstance.getTodoItemById(id_item);
                    itemContentTextEditText.setText(itemContentText);
                    timeLastModifiedItem.setText(todoItem.getTime_last_modified());
                    String message = " Todo content has changed successfully, BOOM! ";
                    MainActivity.showMessage(v.getContext(),message);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbInstance.setTodoIsDone(id_item,true);
                String message = "TODO " + todoItem.get_item_str() + " is now DONE. BOOM! ";
                MainActivity.showMessage(v.getContext(),message);
                returnResultToActivity();
            }
        });
    }

    private void returnResultToActivity() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
