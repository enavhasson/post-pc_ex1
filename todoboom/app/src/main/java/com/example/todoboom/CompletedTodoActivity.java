package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompletedTodoActivity extends AppCompatActivity {

    Button unMarkDoneButton;
    Button deleteButton;
    TodoItem todoItem;
    ItemsFirebase fbInstance;
    String id_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);

        unMarkDoneButton = findViewById(R.id.unMarkDoneButton);
        deleteButton = findViewById(R.id.deleteButton);
        TextView itemContentTextView = findViewById(R.id.textItemContent);
        TextView timeItemCreated = findViewById(R.id.textTimeItemCreated);
        TextView timeLastModifiedItem = findViewById(R.id.textTimeLastModifiedItem);

        id_item = getIntent().getStringExtra("item_id");
        fbInstance = ItemsFirebase.getInstance();
        todoItem = fbInstance.getTodoItemById(id_item);
        itemContentTextView.setText(todoItem.get_item_str());
        timeLastModifiedItem.setText(todoItem.getTime_last_modified());
        timeItemCreated.setText(todoItem.getTime_item_created());

        initButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        id_item = getIntent().getStringExtra("item_id");
    }


    private void initButtons() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            String message = "TODO " + todoItem.get_item_str()
                                    + " has been deleted";
                            MainActivity.showMessage(v.getContext(), message);
                            fbInstance.deleteTodoItem(id_item);
                            returnResultToActivity();
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete?").setPositiveButton
                        ("Yes", dialog).setNegativeButton("No", dialog).show();
            }
        });

        unMarkDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbInstance.setTodoIsDone(id_item, false);
                String message = "TODO " + todoItem.get_item_str() + " is now DONE. BOOM! ";
                MainActivity.showMessage(v.getContext(), message);
                returnResultToActivity();
            }
        });
    }

    private void returnResultToActivity() {
        setResult(RESULT_OK,  new Intent());
        finish();
    }
}
