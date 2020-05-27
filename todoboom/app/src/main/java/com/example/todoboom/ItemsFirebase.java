package com.example.todoboom;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class ItemsFirebase {
    static final String KEY_IS_DONE = "is_done";
    static final String KEY_STR_ITEM = "item_text";
    static final String KEY_TIME_LAST_MODIFIED = "time_last_modified";
    static final String KEY_TIME_ITEM_CREATED = "time_item_created";
    static final String KEY_ID_ITEM = "id";
    private static final String KEY_IS_SELECTED_ITEM = "is_selected_item";
    private String LOG_TAG_ADD_ITEM = "Add Item To Firebase";
    private static ItemsFirebase single_instance = null;
    private HashMap<String, TodoItem> items;
    private String LOG_TAG = "UsersInFirebaseManager";
    private FirebaseFirestore db;
    private CollectionReference notebookRef;


    static ItemsFirebase getInstance() {
        if (single_instance == null) {
            single_instance = new ItemsFirebase();
        }
        return single_instance;
    }

    void checkConnectedFireBase() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("TAG############", "connected");
                } else {
                    Log.d("TAG####################", "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG###################", "Listener was cancelled");
            }
        });
        int x = 1;
    }

    private ItemsFirebase() {
        items = new HashMap<String, TodoItem>();
        db = FirebaseFirestore.getInstance();
        notebookRef = db.collection("todo_items");
        checkConnectedFireBase();
//        loadData();
    }

    Map<String, Object> covertFromObjToMap(TodoItem item) {
        Gson gson = new Gson();
        String jsonItem = gson.toJson(item);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(jsonItem, type);
    }

    public void addItem1(final TodoItem item) {
        notebookRef.document(item.getId())
                .set(covertFromObjToMap(item));
        items.put(item.getId(), item);
    }

    public void addTodoItem(final TodoItem item, final TodoItemAdapter adapter) {
        final TodoItem todoItem = new TodoItem(item);
        notebookRef.document(todoItem.getId())
                .set(covertFromObjToMap(todoItem))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG_ADD_ITEM, "Firebase added todo item with ID: " + todoItem.getId());
//                if (item.getId() == null) {
//                    item.setId(documentReference.getId());
//                }
                        notebookRef.document(todoItem.getId()).set(covertFromObjToMap(todoItem));
                        items.put(todoItem.getId(), todoItem);
                        adapter.addItem(item);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG_ADD_ITEM, "Error adding todo item with ID: " + item.getId(), e);
                    }   //todo log type w,d,i...
                });

    }

    void setTodoIsDone(String idItem, Boolean isDone) {
        TodoItem newItem = items.get(idItem);
        assert newItem != null;
        newItem.set_isDone(isDone);
        items.put(idItem, newItem);
        notebookRef.document(idItem).set(covertFromObjToMap(newItem));

    }

    void deleteTodoItem(String idItem) {
        items.remove(idItem);
        notebookRef.document(idItem).delete();
    }

    void updateContextTodoItem(String idItem, String context) {
        TodoItem newItem = items.get(idItem);
        assert newItem != null;
        newItem.setItem_text(context);
        String curTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        newItem.setTime_last_modified(curTime);
        //todo

        notebookRef.document(idItem).update(KEY_STR_ITEM, context);
        notebookRef.document(idItem).update(KEY_TIME_LAST_MODIFIED, curTime);
        items.put(idItem, newItem);
    }

    boolean checkTodoItemExist(String idItem) {
        return items.containsKey(idItem);
    }

    TodoItem getTodoItemById(String idItem) {
        return items.get(idItem);
    }

    ArrayList<TodoItem> getAllTodoItemsSorted() {
        ArrayList<TodoItem> allItems = new ArrayList<TodoItem>(items.values());
        Collections.sort(allItems, new SortbyTimeCreated());
        return allItems;
    }

    int getItemsSize() {
        return items.size();
    }

    public void loadData(final TodoItemAdapter adapter) {
        notebookRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot docS : queryDocumentSnapshots) {
                    TodoItem todoItem = new TodoItem(docS);
//                    TodoItem todoItem=docS.toObject(TodoItem.class);
//                    TodoItem todoItem = new TodoItem(docS.getData().get(KEY_STR_ITEM), docS.getData().get(KEY_IS_DONE), docS.getData().get(KEY_TIME_LAST_MODIFIED), docS.getData().get(KEY_TIME_ITEM_CREATED), docS.getData().get(KEY_ID_ITEM));
//                    String x = docS.getId();//todo
                    items.put(todoItem.getId(), todoItem);
                    adapter.updateItems(getAllTodoItemsSorted());
                    //todo text input
                }

            }
        });

    }


    private void refreshEditTime(String position, TextView updatedAt) {
        String curTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

        items.get(position).setTime_last_modified(curTime);
        notebookRef.document(position).update(KEY_TIME_LAST_MODIFIED, curTime);
        updatedAt.setText("Was last updated at: ");
        updatedAt.append(curTime);
    }


}
