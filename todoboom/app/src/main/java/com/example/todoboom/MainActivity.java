package com.example.todoboom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    public static final int Not_Completed_Todo_Activity = 1;
    public static final int Completed_Todo_Activity = 2;

    private ItemsFirebase m_itemsFB;
    private TodoItemAdapter m_adapter;
    private EditText m_insert_text;

    @Override
    protected void onResume() {
        super.onResume();
        m_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_insert_text = findViewById(R.id.insert_eText);
        m_itemsFB= ItemsFirebase.getInstance();
        buildRecyclerView();
        Button button = findViewById(R.id.createButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_insert_text.getText().toString().equals("")) {
                    String message = "you can't create an empty TODO item, oh silly!";
                    showSnackbar(m_insert_text, message);
                } else {
                    TodoItem todoItem = new TodoItem(m_insert_text.getText().toString());
                    m_itemsFB.addTodoItem(todoItem,m_adapter);
                    m_adapter.notifyDataSetChanged();
                    m_insert_text.setText("");
                }
            }
        });

        //create log
        Log.d("Number of TODOs", String.valueOf(this.m_itemsFB.getItemsSize()));
        m_adapter.notifyDataSetChanged();
        m_itemsFB.loadData(m_adapter);
    }


    private void buildRecyclerView() {
        m_adapter = new TodoItemAdapter(this.m_itemsFB.getAllTodoItemsSorted());
        RecyclerView recyclerView = findViewById(R.id.item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,
                false));
        recyclerView.setAdapter(m_adapter);

        TodoItemAdapter.OnItemClickListener onItemClickListener = new TodoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String position) {
                if (!m_itemsFB.getTodoItemById(position).get_isDone()) {
                    openActivity(position,Not_Completed_Todo_Activity);
                } else { //if (m_items.get(position).get_is_selected())
                    openActivity(position,Completed_Todo_Activity);
                }
            }
        };
        m_adapter.setOnClickListener(onItemClickListener);
    }

    public void openActivity( String id_item,int requestCode){
        TodoItem item = m_itemsFB.getTodoItemById(id_item);
        Intent intent ;
        if(requestCode==1){
            intent= new Intent(this, NotCompletedTodoActivity.class);
        }
        else {
            intent= new Intent(this, CompletedTodoActivity.class);
        }
        intent.putExtra("item_id", id_item);
        this.startActivityForResult(intent, requestCode);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==Not_Completed_Todo_Activity || requestCode==Completed_Todo_Activity)
                && (resultCode==RESULT_OK))
        {
            m_adapter.updateItems(m_itemsFB.getAllTodoItemsSorted());
            m_adapter.notifyDataSetChanged();
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
    }

    public static void showSnackbar(View view, String message) {
        int duration = Snackbar.LENGTH_LONG;
        Snackbar.make(view, message, duration).show();
    }

    public static void showMessage(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
