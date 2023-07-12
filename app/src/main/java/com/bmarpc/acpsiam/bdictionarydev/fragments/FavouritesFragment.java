package com.bmarpc.acpsiam.bdictionarydev.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelper;
import com.bmarpc.acpsiam.bdictionarydev.adapters.RecyclerViewSimpleAdapter;

public class FavouritesFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewSimpleAdapter recyclerViewSimpleAdapter;
    FrameLayout mainLayout;
    ConstraintLayout mainEmptyLayout;
    ImageView frontIcon;
    TextView frontText;
    DBHelper dbHelper;
    Cursor favouritesCursor;
    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        //*Finding IDs
        recyclerView = v.findViewById(R.id.favourites_recycler_view_id);
        mainLayout = v.findViewById(R.id.favourites_main_layout_id);
        mainEmptyLayout = v.findViewById(R.id.favourites_main_layout_empty_id);
        frontIcon = v.findViewById(R.id.favourites_front_icon_id);
        frontText = v.findViewById(R.id.favourites_front_text_id);

        dbHelper = new DBHelper(requireContext());
        favouritesCursor = dbHelper.fetchFavouritesDatabase();

        setRecyclerView(favouritesCursor);
        setupViewState();



        recyclerViewSimpleAdapter.setOnItemClickListener(position -> recyclerViewSimpleAdapter.notifyItemRemoved(position));



        return v;
    }



    private void setRecyclerView(Cursor favouritesCursor){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewSimpleAdapter = new RecyclerViewSimpleAdapter(requireContext(), "favourites", favouritesCursor);
        recyclerView.setAdapter(recyclerViewSimpleAdapter);
        recyclerView.scrollToPosition(recyclerViewSimpleAdapter.getItemCount()-1);
    }



    private void setupViewState(){
        if (recyclerViewSimpleAdapter.getItemCount()!=0) {
            mainEmptyLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            mainEmptyLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        favouritesCursor.close();
        dbHelper.close();
    }

}