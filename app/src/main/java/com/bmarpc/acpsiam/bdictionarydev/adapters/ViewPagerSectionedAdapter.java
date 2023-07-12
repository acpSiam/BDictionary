package com.bmarpc.acpsiam.bdictionarydev.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bmarpc.acpsiam.bdictionarydev.fragments.ViewpagerSectionedFragment;

import java.util.List;

public class ViewPagerSectionedAdapter extends FragmentPagerAdapter {

    private List<String> sections;
    private String datasetType;
    public ViewPagerSectionedAdapter(@NonNull FragmentManager fm, List<String> sections, String datasetType) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.sections = sections;
        this.datasetType = datasetType;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String section = sections.get(position);
        return ViewpagerSectionedFragment.newInstance(section, datasetType);
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sections.get(position);
    }
}
