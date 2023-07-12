package com.bmarpc.acpsiam.bdictionarydev.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.adapters.ViewPagerSectionedAdapter;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.LibraryModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepositionsFragment extends Fragment {


    private Map<String, Integer> sectionPositions;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ProgressBar progressBar;

    public PrepositionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepositions, container, false);


        progressBar = view.findViewById(R.id.example_progress_prepositions);
        viewPager = view.findViewById(R.id.viewpager_prepositions_id);
        tabLayout = view.findViewById(R.id.tabLayout_prepositions_id);

        setupViewPager();
        updateTabLayout();

        return view;
    }





    private void setupViewPager() {
        List<String> sections = new ArrayList<>();
        sectionPositions = new HashMap<>();

        for (int i = 0; i < LibraryModel.prepositionsArrayList.size(); i++) {
            String item = LibraryModel.prepositionsArrayList.get(i);
            String section = item.substring(0, 1);
            if (!sectionPositions.containsKey(section)) {
                sections.add(section);
                sectionPositions.put(section, i);
            }
        }

        // Simulating the loading process
//        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        new android.os.Handler().postDelayed(
                () -> {
                    ViewPagerSectionedAdapter sectionPagerAdapter = new ViewPagerSectionedAdapter(getParentFragmentManager(), sections, "prepositions");
                    viewPager.setAdapter(sectionPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);

                    // After data is loaded, hide the progress bar and show the ViewPager and TabLayout
                    progressBar.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                },
                250 // Delay in milliseconds
        );
    }

    private void updateTabLayout() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                String tabText = tab.getText().toString();
                if (sectionPositions.containsKey(tabText)) {
                    int position = sectionPositions.get(tabText);
                    String sectionItem = LibraryModel.prepositionsArrayList.get(position);
                    tab.setText(sectionItem.substring(0, 1));
                }
            }
        }
    }



}