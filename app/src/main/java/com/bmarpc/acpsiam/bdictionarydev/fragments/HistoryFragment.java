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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewSimpleAdapter recyclerViewSimpleAdapter;
    FrameLayout mainLayout;
    ConstraintLayout mainEmptyLayout;
    FloatingActionButton deleteHistoryButton;
    ImageView frontIcon;
    TextView frontText;
    DBHelper dbHelper;
    Cursor historyCursor;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        //*Finding IDs
        recyclerView = v.findViewById(R.id.history_recycler_view_id);
        deleteHistoryButton = v.findViewById(R.id.delete_history_button);
        mainLayout = v.findViewById(R.id.main_layout_id);
        mainEmptyLayout = v.findViewById(R.id.main_layout_empty_id);
        frontIcon = v.findViewById(R.id.history_front_icon_id);
        frontText = v.findViewById(R.id.history_front_text_id);

        dbHelper = new DBHelper(requireContext());
        historyCursor = dbHelper.fetchHistoryDatabase();

        setRecyclerView(historyCursor);
        setupViewState();

        deleteHistoryButton.setOnClickListener(view -> buildAlertDialogue(dbHelper));

        return v;
    }



    private void setRecyclerView(Cursor historyCursor){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewSimpleAdapter = new RecyclerViewSimpleAdapter(requireContext(), "history", historyCursor);
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



    private void buildAlertDialogue(DBHelper dbHelper){
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.clear_history)
                .setIcon(R.drawable.ic_round_delete_forever_24)
                .setPositiveButton(R.string.clear, (dialogInterface, i) -> {
                    dbHelper.clearHistory();
                    mainEmptyLayout.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.GONE);
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        historyCursor.close();
        dbHelper.close();
    }

}