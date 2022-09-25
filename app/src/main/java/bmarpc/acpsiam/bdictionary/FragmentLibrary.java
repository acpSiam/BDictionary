package bmarpc.acpsiam.bdictionary;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentLibrary extends Fragment {

    DBHandler dbHandler;
    LibraryRecyclerviewModel libraryRecyclerviewModel;
    LibraryRecyclerViewAdapter libraryRecyclerViewAdapter;
    RecyclerView recyclerView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);
        dbHandler = new DBHandler(requireActivity());
        recyclerView = v.findViewById(R.id.library_recyclerview_id);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        libraryRecyclerviewModel = new LibraryRecyclerviewModel(MainActivity.enWords, MainActivity.bnWords);
        libraryRecyclerViewAdapter = new LibraryRecyclerViewAdapter(requireActivity(), libraryRecyclerviewModel);
        recyclerView.setAdapter(libraryRecyclerViewAdapter);


        return v;
    }
}