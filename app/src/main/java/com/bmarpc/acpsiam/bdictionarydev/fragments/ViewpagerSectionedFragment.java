package com.bmarpc.acpsiam.bdictionarydev.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.adapters.RecyclerViewSectionedAdapter;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.LibraryModel;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerSectionedFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewSectionedAdapter recyclerViewSectionedAdapter;
    private List<String> sectionDataList;
    String datasetType;

    public static ViewpagerSectionedFragment newInstance(String section, String datasetType) {
        ViewpagerSectionedFragment fragment = new ViewpagerSectionedFragment();
        Bundle args = new Bundle();
        args.putString("section", section);
        args.putString("datasetType", datasetType);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager_sectioned, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_idioms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);



        String section = getArguments().getString("section");
        datasetType = getArguments().getString("datasetType");
        sectionDataList = getSectionItems(section);

        recyclerViewSectionedAdapter = new RecyclerViewSectionedAdapter(getActivity(), sectionDataList, datasetType);
        recyclerView.setAdapter(recyclerViewSectionedAdapter);




        return view;
    }



    private List<String> getSectionItems(String section) {

        List<String> datasetArray = new ArrayList<>();

        switch (datasetType){
            case "prepositions":
                datasetArray = LibraryModel.prepositionsArrayList;
                break;

            case "idioms":
                datasetArray = LibraryModel.idiomsArrayList;
                break;

            case "proverbs":
                datasetArray = LibraryModel.proverbsArrayList;
                break;
        }

        List<String> sectionItems = new ArrayList<>();
        for (String item : datasetArray) {
            if (item.startsWith(section)) {
                sectionItems.add(item);
            }
        }
        return sectionItems;
    }
}
