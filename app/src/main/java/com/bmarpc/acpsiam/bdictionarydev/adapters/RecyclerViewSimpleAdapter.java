package com.bmarpc.acpsiam.bdictionarydev.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelper;
import com.google.android.material.textview.MaterialTextView;

public class RecyclerViewSimpleAdapter extends RecyclerView.Adapter<RecyclerViewSimpleAdapter.SimpleViewHolder> {

    Context context;
    String recyclerViewType;
    Cursor itemsCursor;
    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener clickListener){
        onItemClickListener = clickListener;
    }




    public RecyclerViewSimpleAdapter(Context context, String recyclerViewType, Cursor itemsCursor) {
        this.context = context;
        this.recyclerViewType = recyclerViewType;
        this.itemsCursor = itemsCursor;
    }

    @NonNull
    @Override
    public RecyclerViewSimpleAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_simple_items, parent, false);
        return new SimpleViewHolder(view, recyclerViewType, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSimpleAdapter.SimpleViewHolder holder, int position) {
        if (itemsCursor.moveToPosition(position)){
            holder.enWord.setText(itemsCursor.getString(1));
            holder.bnWord.setText(itemsCursor.getString(2));
        }


    }

    @Override
    public int getItemCount() {
        return itemsCursor.getCount();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView enWord, bnWord;
        Button deleteButton;

        public SimpleViewHolder(@NonNull View itemView, String recyclerViewType, OnItemClickListener onItemClickListener) {
            super(itemView);


            enWord = itemView.findViewById(R.id.library_en_word_id);
            bnWord = itemView.findViewById(R.id.library_bn_word_id);
            deleteButton = itemView.findViewById(R.id.recycler_view_item_delete_button);

            if (recyclerViewType.equals("favourites")){
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }


            deleteButton.setOnClickListener(view -> {
                onItemClickListener.onItemClick(getAdapterPosition());
                view.setEnabled(false);
                getUpdatedCursor(enWord.getText().toString());
            });

        }

    }
    private void getUpdatedCursor(String word) {
        // Implement the logic to get an updated cursor after deleting the item
        // For example, you can use your existing DBHelper class:
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.deleteFromFav(word);
        itemsCursor = dbHelper.fetchFavouritesDatabase(); // Update the cursor after deleting the item

    }

}
