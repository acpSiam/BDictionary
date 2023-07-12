package com.bmarpc.acpsiam.bdictionarydev.otherclasses;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.helpers.LanguageHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class LanguagePreference extends Preference {

    public LanguagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.extra_preference_screen_language_layout);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        //*Setting Language Preference
        Chip enLanguageChip = holder.itemView.findViewById(R.id.preference_screen_en_language_pref_chip_id);
        Chip bnLanguageChip = holder.itemView.findViewById(R.id.preference_screen_bn_language_pref_chip_id);
        ChipGroup languageChipGroup = holder.itemView.findViewById(R.id.preference_screen_language_chip_group_id);
        if (LanguageHelper.getCurrentLocalePref(getContext()).equals("en")) {
            enLanguageChip.setSelected(true);
            bnLanguageChip.setSelected(false);
            languageChipGroup.check(enLanguageChip.getId());
        } else {
            enLanguageChip.setSelected(false);
            bnLanguageChip.setSelected(true);
            languageChipGroup.check(bnLanguageChip.getId());
        }
        enLanguageChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LanguageHelper.setLocale(getContext(), "en");
            MyMethods.restartActivity((Activity) getContext()); //*Restart the activity to apply the new locale effect
        });

        bnLanguageChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LanguageHelper.setLocale(getContext(), "bn");
            MyMethods.restartActivity((Activity) getContext()); //*Restart the activity to apply the new locale effect
        });

    }
}
