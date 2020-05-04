package com.example.todoboom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {
    private List<TodoItem> mTodoItem;
    private LayoutInflater mInflater;
    //    private ItemClickListener mClickListener;
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

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        TodoItem item = mTodoItem.get(position);
//        holder.item_text.setText(item.get_item_str());
////        holder.checkBox.setChecked(item.get_isDone());
//
//    }

    @Override
    public int getItemCount() {
        return mTodoItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_text;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            item_text = itemView.findViewById(R.id.item_textView);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (checkBox.isChecked()) {
                        mTodoItem.get(getAdapterPosition()).set_is_selected(true);
                        checkBox.setEnabled(false);
                        Toast.makeText(mContext, "TODO " + item_text.getText() + " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
                    }
        }
    }


//            checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (checkBox.isChecked()) {
//                        mTodoItem.get(getAdapterPosition()).set_isDone_T();
//                        checkBox.setEnabled(false);
//                        Toast.makeText(mContext, "TODO " + item_text.getText() + " is now DONE. BOOM! ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TodoItem item = mTodoItem.get(position);
        String content = "<b>lalalla</b>";
        holder.item_text.setText(item.get_item_str());

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(item.get_is_selected());
        if(item.get_is_selected()){
            holder.checkBox.setEnabled(false);
        }
        else{
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

//    // allows clicks events to be caught
//    void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//
//    }
//
//    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }


}
