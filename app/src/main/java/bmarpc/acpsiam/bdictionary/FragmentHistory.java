package bmarpc.acpsiam.bdictionary;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentHistory extends Fragment {

    RecyclerView recyclerView;
    DBHandler dbHandler;
    RecyclerviewModel recyclerviewModel;
    RecyclerViewAdapter recyclerViewAdapter;
    Cursor cursor;


    FrameLayout historyLayout;
    ConstraintLayout historyEmptyLayout;

    ArrayList<String> enHistory, bnHistory;


    FloatingActionButton deleteHistoryButton;

    public FragmentHistory() {
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
        historyLayout = v.findViewById(R.id.history_not_empty_state_layout_id);
        historyEmptyLayout = v.findViewById(R.id.history_empty_state_layout_id);


        dbHandler = new DBHandler(requireActivity());
        enHistory = new ArrayList<>();
        bnHistory = new ArrayList<>();


        cursor = dbHandler.fetchHistoryDatabase();

        if (cursor!=null && cursor.getCount()!=0) {
            historyEmptyLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.VISIBLE);
            while (cursor.moveToNext()) {
                enHistory.add(cursor.getString(1));
                bnHistory.add(cursor.getString(2));
            }
        } else {
            historyEmptyLayout.setVisibility(View.VISIBLE);
            historyLayout.setVisibility(View.GONE);
        }


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerviewModel = new RecyclerviewModel(enHistory, bnHistory, "history");
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), recyclerviewModel);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.scrollToPosition(recyclerViewAdapter.getItemCount()-1);


        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                enHistory.remove(position);
                bnHistory.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
            }
        });

        deleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Clear History?")
                        .setIcon(R.drawable.ic_round_delete_forever_24)
                        .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHandler.clearHistory();
                                recyclerviewModel.getBnWords().clear();
                                recyclerviewModel.getEnWords().clear();
                                recyclerViewAdapter.notifyDataSetChanged();
                                historyEmptyLayout.setVisibility(View.VISIBLE);
                                historyLayout.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();


            }
        });

        return v;
    }
}