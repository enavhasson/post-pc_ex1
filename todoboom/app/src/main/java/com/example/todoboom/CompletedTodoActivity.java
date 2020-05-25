package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class CompletedTodoActivity extends AppCompatActivity {

    Button unMarkDoneButton;
    Button deleteButton;
    String itemContentText;
    TextView itemContentTextView;
    TextView timeItemCreated;
    TextView timeLastModifiedItem;
    boolean isUnMarkDone;
    boolean isDelete;
    //    TodoItem todoItem;

    int id_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);

        Intent intentCreatedMe = getIntent();
        itemContentText=intentCreatedMe.getStringExtra("item_text");
        id_item=intentCreatedMe.getIntExtra("item_id",-1);
//        itemContentText = todoItem.get_item_str();

        unMarkDoneButton = findViewById(R.id.unMarkDoneButton);
        deleteButton = findViewById(R.id.deleteButton);
        itemContentTextView = findViewById(R.id.textItemContent);
        timeItemCreated = findViewById(R.id.textTimeItemCreated);
        timeLastModifiedItem = findViewById(R.id.textTimeLastModifiedItem);

        itemContentTextView.setText(itemContentText);
        //todo
        isDelete=false;
        isUnMarkDone=true;
        timeLastModifiedItem.setText(intentCreatedMe.getStringExtra("Time_last_modified"));
        timeItemCreated.setText(intentCreatedMe.getStringExtra("Time_item_created"));

        initButtons();
    }

    private void initButtons() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        isDelete=true;
                        Toast toast = Toast.makeText(v.getContext(), "TODO " + itemContentText +
                                " has been deleted", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 100, 100);
                        toast.show();
                        returnResultToActivity();
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialog)
                    .setNegativeButton("No", dialog).show();
            }
        });

        unMarkDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnMarkDone=true;
                String message = "TODO "+itemContentText +" is now DONE. BOOM! ";
                MainActivity.showSnackbar(itemContentTextView, message);
                returnResultToActivity();
            }
        });
    }
    private void returnResultToActivity(){
        Intent intentBack=new Intent();
        intentBack.putExtra("is_delete_item",isDelete);
        intentBack.putExtra("is_Un_Mark_As_Done",isUnMarkDone);
        intentBack.putExtra("item_id",id_item);
        setResult(RESULT_OK,intentBack);
        finish();
    }
}
