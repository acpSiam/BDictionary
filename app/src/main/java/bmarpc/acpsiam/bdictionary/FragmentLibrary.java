package bmarpc.acpsiam.bdictionary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLibrary extends Fragment {

    DBHandler dbHandler;
    RecyclerviewModel libraryRecyclerviewModel;
    RecyclerViewAdapter libraryRecyclerViewAdapter;
    RecyclerView recyclerView;
    static String viewType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);
        dbHandler = new DBHandler(requireActivity());
        recyclerView = v.findViewById(R.id.library_recyclerview_id);

        viewType = "library";


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        libraryRecyclerviewModel = new RecyclerviewModel(SetupActivity.enWords, SetupActivity.bnWords, viewType);
        libraryRecyclerViewAdapter = new RecyclerViewAdapter(requireActivity(), libraryRecyclerviewModel);

        recyclerView.setAdapter(libraryRecyclerViewAdapter);


        return v;
    }

}