package com.example.todoboom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {
    private List<TodoItem> mTodoItem;
    private LayoutInflater mInflater;
    private Context mContext;
    private SharedPreferences m_sp;

    TodoItemAdapter(Context context, List<TodoItem> todoItems, SharedPreferences sp) {
        this.mInflater = LayoutInflater.from(context);
        this.mTodoItem = todoItems;
        this.mContext = context;
        this.m_sp=sp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mTodoItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener
    {
        TextView item_text;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            item_text = itemView.findViewById(R.id.item_textView);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mTodoItem.get(getAdapterPosition()).get_is_selected()) {
                mTodoItem.get(getAdapterPosition()).set_is_selected(true);
                checkBox.setEnabled(false);
                Toast.makeText(mContext, "TODO " + item_text.getText() +
                        " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public boolean onLongClick(View view) {
            // Handle long click
            // Return true to indicate the click was handled
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        String item_text = mTodoItem.get(getAdapterPosition()).get_item_str();
                        Toast toast = Toast.makeText(mContext, "TODO " + item_text +
                                " has been deleted", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 100, 100);
                        toast.show();
                        removeItem(getAdapterPosition());
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialog)
                    .setNegativeButton("No", dialog).show();
            return true;
        }

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TodoItem item = mTodoItem.get(position);

        holder.item_text.setText(item.get_item_str());
        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(item.get_is_selected());
        if (item.get_is_selected()) {
            holder.checkBox.setEnabled(false);
        } else {
            holder.checkBox.setEnabled(true);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                item.set_is_selected(isChecked);
            }
        });
    }

    String getItem(int id) {
        return mTodoItem.get(id).get_item_str();
    }

    private void removeItem(int position){
        Gson gson=new Gson();
        mTodoItem.remove(position);
        SharedPreferences.Editor editor = m_sp.edit();
        editor.putString(MainActivity.SP_TODO_ITEMS, gson.toJson(mTodoItem));
        editor.apply();
        this.notifyItemRemoved(position);
    }


}

