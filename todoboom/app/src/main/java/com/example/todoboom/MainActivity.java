package com.example.todoboom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String SP_USER_TEXT_INPUT = "insert_text";
    public static final String SP_TODO_ITEMS = "TODO_items";

    private TodoItemAdapter m_adapter;
    private ArrayList<TodoItem> m_items = new ArrayList<>();
    private EditText m_insert_text;
    private SharedPreferences m_sp;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_insert_text = findViewById(R.id.insert_eText);
        Button button = findViewById(R.id.createButton);

        m_sp = this.getSharedPreferences("my_items", MODE_PRIVATE);

        if (savedInstanceState != null) {
            Gson gson = new Gson();
            String jsonItems = m_sp.getString(SP_TODO_ITEMS, null);
            this.m_items = gson.fromJson(jsonItems,
                    new TypeToken<ArrayList<TodoItem>>() {
                    }.getType());
            m_insert_text.setText(m_sp.getString(SP_USER_TEXT_INPUT, ""));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_insert_text.getText().toString().equals("")) {
                    String message = "you can't create an empty TODO item, oh silly!";
                    showSnackbar(m_insert_text, message);
                } else {
                    TodoItem todoItem = new TodoItem(m_insert_text.getText().toString(), m_items.size());//todo nums
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

        buildRecyclerView();

        //create log
        Log.d("Number of TODOs", String.valueOf(this.m_adapter.getItemCount()));

    }

    private void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.item_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        m_adapter = new TodoItemAdapter(this, this.m_items, this.m_sp);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(m_adapter);

        TodoItemAdapter.OnItemClickListener onItemClickListener = new TodoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!m_items.get(position).get_is_selected()) {
                    openActivity(m_items.get(position).get_item_str(), position,1);
//                    m_items.get(position).set_is_selected(true);
                } else { //if (m_items.get(position).get_is_selected())
                    openActivity(m_items.get(position).get_item_str(), position,2);
//                    m_items.get(position).set_is_selected(false);
                }
            }
        };
        m_adapter.setOnClickListener(onItemClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences.Editor editor = m_sp.edit();
        editor.putString(SP_TODO_ITEMS, gson.toJson(m_items));
        editor.putString(SP_USER_TEXT_INPUT, m_insert_text.getText().toString());
        editor.apply();
    }

    public void openActivity(String itemText, int id_item,int requestCode){
        Intent intent ;
        if(requestCode==1){
            intent= new Intent(this, NotCompletedTodoActivity.class);
        }
        else {
            intent= new Intent(this, CompletedTodoActivity.class);
        }
        intent.putExtra("item_text", itemText);
        intent.putExtra("item_id", id_item);
        intent.putExtra("Time_item_created", m_items.get(id_item).getTime_item_created());
        intent.putExtra("Time_last_modified", m_items.get(id_item).getTime_last_modified());
        this.startActivityForResult(intent, requestCode);

    }


//    public void openUncompletedActivity(String itemText, int id_item) {
//        Intent intent = new Intent(this, NotCompletedTodoActivity.class);
//        intent.putExtra("item_text", itemText);
//        intent.putExtra("item_id", id_item);
//        intent.putExtra("Time_item_created", m_items.get(id_item).getTime_item_created());
//        intent.putExtra("Time_last_modified", m_items.get(id_item).getTime_last_modified());
//        this.startActivityForResult(intent, 1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                int id_item = data.getIntExtra("item_id",-1);

                String time_last_modified=data.getStringExtra("time_last_modified");
                m_items.get(id_item).setTime_last_modified(time_last_modified);
                String text_item = data.getStringExtra("item_text");
                m_items.get(id_item).setStr_item(text_item);

                boolean is_selected = data.getBooleanExtra("is_selected_item", false);
                if(is_selected){ //todo
                    Toast.makeText(this, "TODO " + text_item +
                            " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
                }
                m_items.get(id_item).set_is_selected(is_selected);


                m_adapter.notifyItemChanged(id_item);

            }
        }
        else{ //if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                int id_item = data.getIntExtra("item_id",-1);
                boolean isDelete = data.getBooleanExtra("is_delete_item", false);
                if (isDelete){
                    m_adapter.removeItem(id_item);
                }
                else {
                    boolean isUnMarkDone = data.getBooleanExtra("is_Un_Mark_As_Doneggg", false);
                    if (isUnMarkDone) {
                        m_items.get(id_item).set_is_selected(false);
                        m_adapter.notifyItemChanged(id_item);
                    }
                }
                //todo
            }
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
    }

    public static void showSnackbar(View view, String message) {
        int duration = Snackbar.LENGTH_SHORT;
        Snackbar.make(view, message, duration).show();
    }


}
