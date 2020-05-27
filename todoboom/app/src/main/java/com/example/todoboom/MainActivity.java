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
    public static final String SP_USER_TEXT_INPUT = "insert_text";
    public static final String SP_TODO_ITEMS = "TODO_items";

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
                    m_adapter.notifyDataSetChanged();//todo
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(m_adapter);

        TodoItemAdapter.OnItemClickListener onItemClickListener = new TodoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String position) {
                if (!m_itemsFB.getTodoItemById(position).get_isDone()) {
                    openActivity(position,1);
                } else { //if (m_items.get(position).get_is_selected())
                    openActivity(position,2);
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
//        intent.putExtra("item_text", itemText);
        intent.putExtra("item_id", id_item);
//        intent.putExtra("Time_item_created", m_items.get(id_item).getTime_item_created());
//        intent.putExtra("Time_last_modified", m_items.get(id_item).getTime_last_modified());
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
        if ((requestCode==1 || requestCode==2) && (resultCode==RESULT_OK))
        {
            m_adapter.updateItems(m_itemsFB.getAllTodoItemsSorted());//todo
            m_adapter.notifyDataSetChanged();
        }
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                assert data != null;
//                String id_item = data.getStringExtra("item_id");
//
////                String time_last_modified=data.getStringExtra("time_last_modified");
////                m_items.get(id_item).setTime_last_modified(time_last_modified);
////                String text_item = data.getStringExtra("item_text");
////                m_items.get(id_item).setStr_item(text_item);
//
////                boolean is_selected = data.getBooleanExtra("is_selected_item", false);
////                if(is_selected){ //todo
////                    Toast.makeText(this, "TODO " + text_item +
////                            " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
////                }
////                m_items.get(id_item).set_isDone(is_selected);
//
//                m_adapter.notifyDataSetChanged();
//
//            }
//        }
//        else{ //if (requestCode == 2) {
//            if (resultCode == RESULT_OK) {
//                assert data != null;
//                String id_item = data.getStringExtra("item_id");
//                if (!m_itemsFB.checkTodoItemExist(id_item))
//                {
//                    m_adapter.notifyItemChanged(m_adapter.getIndexByID(id_item));
//                }
//                m_adapter.notifyDataSetChanged();
//            }
//        }
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
