package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class NotCompletedTodoActivity extends AppCompatActivity {
    private static final String SP_USER_TEXT_EDIT = "edit_text";

    Button applyButton;
    Button doneButton;
    String itemContentText;
    EditText itemContentTextEditText;
    TextView timeItemCreated;
    TextView timeLastModifiedItem;
//    TodoItem todoItem;
    SharedPreferences sp;
    int id_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_completed_todo);
        //unchecked_items

        Intent intentCreatedMe = getIntent();
        sp = this.getSharedPreferences("unchecked_item", MODE_PRIVATE);
        itemContentText=intentCreatedMe.getStringExtra("item_text");
        id_item=intentCreatedMe.getIntExtra("item_id",-1);
//        itemContentText = todoItem.get_item_str();

        applyButton = findViewById(R.id.applyButton);
        doneButton = findViewById(R.id.doneItemButton);
        itemContentTextEditText = findViewById(R.id.editTextItemContent);
        timeItemCreated = findViewById(R.id.textTimeItemCreated);
        timeLastModifiedItem = findViewById(R.id.textTimeLastModifiedItem);


        if (savedInstanceState != null) {
            //todo
            itemContentTextEditText.setText(sp.getString(SP_USER_TEXT_EDIT, ""));
        }else{
            itemContentTextEditText.setText(itemContentText);
            timeLastModifiedItem.setText(intentCreatedMe.getStringExtra("Time_last_modified"));
            timeItemCreated.setText(intentCreatedMe.getStringExtra("Time_item_created"));
        }
        initButtons();
    }

    private void initButtons() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemContentTextEditText.getText().toString().equals("")) {
                    String message = "you can't edit an empty TODO item, oh silly!";
                    MainActivity.showSnackbar(itemContentTextEditText, message);
                } else if (itemContentTextEditText.getText().toString().equals(itemContentText)) {
                    String message = "No change has been made";
                    MainActivity.showSnackbar(itemContentTextEditText, message);
                } else {
                    itemContentText = itemContentTextEditText.getText().toString();
                    timeLastModifiedItem.setText(java.text.DateFormat.getDateTimeInstance().format(new Date()));
                    //save new item and empty input
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(SP_USER_TEXT_EDIT, itemContentText);
                    editor.apply();

                    String message = " Todo content has changed successfully, BOOM! ";
                    MainActivity.showSnackbar(itemContentTextEditText, message); //TODO
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                todoItem.set_is_selected(true);
                String message = "TODO "+itemContentText +" is now DONE. BOOM! ";
                //todo
                MainActivity.showSnackbar(itemContentTextEditText, message);
//                Toast.makeText(v.getContext(), "TODO " + message +
//                        " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
//
                returnResultToActivity(true);
            }
        });
    }

    private void returnResultToActivity(boolean is_selected){
        Intent intentBack=new Intent();
        intentBack.putExtra("item_text",itemContentText);
        intentBack.putExtra("is_selected_item",is_selected);
        String x=timeLastModifiedItem.getText().toString();//todo
        intentBack.putExtra("item_id",id_item);
        intentBack.putExtra("time_last_modified",timeLastModifiedItem.getText().toString());
        setResult(RESULT_OK,intentBack);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_USER_TEXT_EDIT, itemContentText);
        editor.apply();
    }
}
