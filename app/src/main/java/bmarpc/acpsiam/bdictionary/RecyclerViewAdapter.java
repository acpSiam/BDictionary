package bmarpc.acpsiam.bdictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    RecyclerviewModel libraryRecyclerviewModel;
    OnItemClickListener onItemClickListener;
    DBHandler dbHandler;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener clickListener){
        onItemClickListener = clickListener;
    }


    public RecyclerViewAdapter(Context context, RecyclerviewModel libraryRecyclerviewModel) {
        this.context = context;
        this.libraryRecyclerviewModel = libraryRecyclerviewModel;
        dbHandler = new DBHandler(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_library_items, parent, false);
        return new MyViewHolder(view, libraryRecyclerviewModel.getViewType(),onItemClickListener, dbHandler);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.enWord.setText(libraryRecyclerviewModel.getEnWords().get(position));
        holder.bnWord.setText(libraryRecyclerviewModel.getBnWords().get(position));
    }

    @Override
    public int getItemCount() {
        return libraryRecyclerviewModel.getEnWords().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView enWord, bnWord;
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView, String recyclerViewType, OnItemClickListener onItemClickListener, DBHandler dbHandler) {
            super(itemView);

            enWord = itemView.findViewById(R.id.library_en_word_id);
            bnWord = itemView.findViewById(R.id.library_bn_word_id);
            deleteButton = itemView.findViewById(R.id.recycler_view_item_delete_button);

            if (recyclerViewType.equals("favs")){
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                    view.setEnabled(false);
                    dbHandler.deleteFromFav(enWord.getText().toString().trim());
                }
            });

        }
    }
}
