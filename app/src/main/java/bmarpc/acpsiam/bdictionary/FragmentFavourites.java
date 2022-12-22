package bmarpc.acpsiam.bdictionary;

import android.database.Cursor;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentFavourites extends Fragment {
    DBHandler dbHandler;
    RecyclerView recyclerView;
    RecyclerViewAdapter libraryRecyclerViewAdapter;
    RecyclerviewModel recyclerviewModel;
    ConstraintLayout favsEmptyLayout;


    ArrayList<String> enWordFav, bnWordFav;

    public FragmentFavourites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        //*Finding IDs
        recyclerView = v.findViewById(R.id.favourites_fragment_recycler_view_id);
        favsEmptyLayout = v.findViewById(R.id.favourites_empty_state_layout_id);

        dbHandler = new DBHandler(requireActivity());
        enWordFav = new ArrayList<>();
        bnWordFav = new ArrayList<>();

        Cursor cursor = dbHandler.fetchFavouritesDatabase();

        if (cursor!=null && cursor.getCount()!=0){
            favsEmptyLayout.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                enWordFav.add(cursor.getString(1));
                bnWordFav.add(cursor.getString(2));
            }
        } else {
            favsEmptyLayout.setVisibility(View.VISIBLE);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerviewModel = new RecyclerviewModel(enWordFav, bnWordFav, "favs");
        libraryRecyclerViewAdapter = new RecyclerViewAdapter(getContext(), recyclerviewModel);
        recyclerView.setAdapter(libraryRecyclerViewAdapter);

        libraryRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                enWordFav.remove(position);
                bnWordFav.remove(position);
                libraryRecyclerViewAdapter.notifyItemRemoved(position);
                if (enWordFav.isEmpty()){
                    favsEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        return v;
    }
}