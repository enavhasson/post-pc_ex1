package com.example.todoboom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {
    private List<TodoItem> mTodoItem;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    TodoItemAdapter(Context context, List<TodoItem> todoItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mTodoItem = todoItems;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem item = mTodoItem.get(position);
        holder.item_text.setText(item.getM_item_string());
    }

    @Override
    public int getItemCount() {
        return mTodoItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_text;
        public Button checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            item_text = itemView.findViewById(R.id.item_textView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return mTodoItem.get(id).getM_item_string();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
