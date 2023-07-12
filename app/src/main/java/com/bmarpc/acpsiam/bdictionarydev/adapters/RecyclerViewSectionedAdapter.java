package com.bmarpc.acpsiam.bdictionarydev.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelper;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperIdioms;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperPrepositions;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperProverbs;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.MyMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class RecyclerViewSectionedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> dataList;
    private final String datasetType;
    private final Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;


    public RecyclerViewSectionedAdapter(Context context, List<String> dataList, String datasetType) {
        this.context = context;
        this.dataList = dataList;
        this.datasetType = datasetType;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_sectioned_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String item = dataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        bindItem(itemViewHolder, item);
        setSelectionState(itemViewHolder);
        setClickListener(itemViewHolder);

        DBHelper dbHelper = new DBHelper(context);
        boolean isFavorite = dbHelper.favouritesExists(item);
        if (isFavorite) {
            itemViewHolder.favouritesButton.setIcon(context.getDrawable(R.drawable.ic_baseline_favorite_24));
        } else {
            itemViewHolder.favouritesButton.setIcon(context.getDrawable(R.drawable.ic_round_favorite_border_24));
        }

    }

    private void bindItem(ItemViewHolder holder, String item) {
        Cursor datasetReturnedCursor;

        switch (datasetType) {
            case "idioms":
                DBHelperIdioms dbHelperIdioms = new DBHelperIdioms(context);
                datasetReturnedCursor = dbHelperIdioms.fetchEnToBnIdiomsData(item);
                if (datasetReturnedCursor.moveToNext()) {
                    holder.idiom.setText(item);
                    holder.idiomBn.setText(datasetReturnedCursor.getString(2));

                    String example = datasetReturnedCursor.getString(3);
                    if (example == null) {
                        holder.idiomExampleContainer.setVisibility(View.GONE);
                    } else {
                        holder.idiomExample.setText(example);
                    }

                    String definition = datasetReturnedCursor.getString(4);
                    if (definition == null) {
                        holder.idiomDefinitionContainer.setVisibility(View.GONE);
                    } else {
                        holder.idiomDefinition.setText(definition);
                    }
                }
                datasetReturnedCursor.close();
                break;

            case "prepositions":
                DBHelperPrepositions dbHelperPrepositions = new DBHelperPrepositions(context);
                datasetReturnedCursor = dbHelperPrepositions.fetchEnToBnPrepositionsData(item);
                if (datasetReturnedCursor.moveToNext()) {
                    holder.idiom.setText(item);
                    holder.idiomBn.setText(datasetReturnedCursor.getString(2));
                    holder.idiomExample.setText(datasetReturnedCursor.getString(3));
                    holder.idiomDefinitionContainer.setVisibility(View.GONE);
                }
                datasetReturnedCursor.close();
                break;

            case "proverbs":
                DBHelperProverbs dbHelperProverbs = new DBHelperProverbs(context);
                datasetReturnedCursor = dbHelperProverbs.fetchEnToBnProverbsData(item);
                if (datasetReturnedCursor.moveToNext()) {
                    holder.idiom.setText(item);
                    holder.idiomBn.setText(datasetReturnedCursor.getString(2));
                    holder.idiomExampleContainer.setVisibility(View.GONE);
                    holder.idiomDefinitionContainer.setVisibility(View.GONE);
                    holder.arrowSign.setVisibility(View.GONE);
                }
                datasetReturnedCursor.close();
                break;
        }
    }

    private void setSelectionState(ItemViewHolder holder) {
        boolean isSelected = holder.getAdapterPosition() == selectedPosition;
        if (isSelected) {
            holder.extrasLinearLayout.setVisibility(View.VISIBLE);
            holder.arrowSign.animate().rotation(180).start();
        } else {
            holder.extrasLinearLayout.setVisibility(View.GONE);
            holder.arrowSign.animate().rotation(0).start();
        }
    }

    private void setClickListener(ItemViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = RecyclerView.NO_POSITION; // Clicked again, hide the view
            } else {
                selectedPosition = holder.getAdapterPosition(); // Clicked for the first time, show the view
            }
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
        });

        holder.favouritesButton.setOnClickListener(v -> {
            String mainText = holder.idiom.getText().toString();
            String meaning = holder.idiomBn.getText().toString();

            toggleFavoriteState(holder, mainText, meaning);
        });




        holder.copyButton.setOnClickListener(v -> {
            String mainText = holder.idiom.getText().toString();
            String meaning = holder.idiomBn.getText().toString();
            String example = holder.idiomExample.getText().toString();
            String definition = holder.idiomDefinition.getText().toString();

            StringBuilder text = new StringBuilder(mainText
                    + "\n" + context.getString(R.string.meaning) + ": " + meaning);

            if (!example.trim().isEmpty()) {
                text.append("\n" + context.getString(R.string.sentences) + ": " + example);
            }
            if (!definition.trim().isEmpty()) {
                text.append("\n" + context.getString(R.string.def) + ": " + definition);
            }
            MyMethods.copyToClipboard(context, text.toString());
        });
    }



    private void toggleFavoriteState(ItemViewHolder holder, String mainText, String meaning) {
        DBHelper dbHelper = new DBHelper(context);
        boolean isFavorite = dbHelper.favouritesExists(mainText);
        if (isFavorite) {
            dbHelper.deleteFromFav(mainText);
            Toast.makeText(context, R.string.removed_from_favourites, Toast.LENGTH_SHORT).show();
            holder.favouritesButton.setIcon(context.getDrawable(R.drawable.ic_round_favorite_border_24));
        } else {
            dbHelper.addFavWords(mainText, meaning);
            Toast.makeText(context, R.string.added_to_favourites, Toast.LENGTH_SHORT).show();
            holder.favouritesButton.setIcon(context.getDrawable(R.drawable.ic_baseline_favorite_24));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // If multiple view types are needed, return the appropriate view type here
        return super.getItemViewType(position);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView idiom, idiomBn, idiomExample, idiomDefinition;
        ImageView arrowSign;
        LinearLayout extrasLinearLayout, idiomExampleContainer, idiomDefinitionContainer;
        MaterialButton copyButton, favouritesButton;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            idiom = itemView.findViewById(R.id.idiom_english_id);
            idiomBn = itemView.findViewById(R.id.idiom_meaning_id);
            idiomExample = itemView.findViewById(R.id.idiom_example_id);
            idiomExampleContainer = itemView.findViewById(R.id.idiom_example_layout_id);
            idiomDefinition = itemView.findViewById(R.id.idiom_definition_id);
            idiomDefinitionContainer = itemView.findViewById(R.id.idiom_definition_layout_id);
            arrowSign = itemView.findViewById(R.id.idiom_arrow_down_imageview_id);
            extrasLinearLayout = itemView.findViewById(R.id.idiom_extras_linear_layout_id);
            favouritesButton = itemView.findViewById(R.id.idioms_items_favourites_button_id);
            copyButton = itemView.findViewById(R.id.idioms_items_copy_button_id);

            extrasLinearLayout.setVisibility(View.GONE);
        }
    }
}
