package bmarpc.acpsiam.bdictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class LibraryRecyclerViewAdapter extends RecyclerView.Adapter<LibraryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    LibraryRecyclerviewModel libraryRecyclerviewModel;

    public LibraryRecyclerViewAdapter(Context context, LibraryRecyclerviewModel libraryRecyclerviewModel) {
        this.context = context;
        this.libraryRecyclerviewModel = libraryRecyclerviewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_library_items, parent, false);
        return new MyViewHolder(view);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            enWord = itemView.findViewById(R.id.library_en_word_id);
            bnWord = itemView.findViewById(R.id.library_bn_word_id);
        }
    }
}
