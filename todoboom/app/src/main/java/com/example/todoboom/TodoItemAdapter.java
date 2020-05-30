package com.example.todoboom;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {
    private ArrayList<TodoItem> mTodoItem;
    private OnItemClickListener mListener;

    TodoItemAdapter(ArrayList<TodoItem> items) {
        mTodoItem = items;
    }

    void addItem(TodoItem item){
        mTodoItem.add(item);
        this.notifyDataSetChanged();
    }

    void updateItems(ArrayList<TodoItem> items){
        mTodoItem.clear();
        mTodoItem.addAll(items);
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String position);
    }

    void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo,
                parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public int getItemCount() {
        return mTodoItem.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_text;
        CheckBox checkBox;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            item_text = itemView.findViewById(R.id.item_textView);
            checkBox = itemView.findViewById(R.id.checkBox);
            item_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(getItem(position).getId());
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TodoItem item = mTodoItem.get(position);

        assert item != null;
        holder.item_text.setText(item.get_item_str());
        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(item.get_isDone());
        if (item.get_isDone()) {
            holder.checkBox.setEnabled(false);
        } else {
            holder.checkBox.setEnabled(true);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                item.set_isDone(isChecked);
            }
        });
    }

    TodoItem getItem(int id) {
        return mTodoItem.get(id);
    }

}

