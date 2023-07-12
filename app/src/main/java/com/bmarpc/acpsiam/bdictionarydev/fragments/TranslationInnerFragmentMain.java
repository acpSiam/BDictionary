package com.bmarpc.acpsiam.bdictionarydev.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.google.android.material.textview.MaterialTextView;

public class TranslationInnerFragmentMain extends Fragment {


    MaterialTextView englishSyns, banglaSyns, ants;
    LinearLayout sentencesLayout, enSynsLAyout, bnSynsLayout, antsLayout, sentsContainerLayout;

    String EN_SYNS, BN_SYNS, ANTS, SENTS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translation_inner_main, container, false);


        //*Finding IDs
        englishSyns = v.findViewById(R.id.english_syns_id);
        banglaSyns = v.findViewById(R.id.bangla_syns_id);
        ants = v.findViewById(R.id.english_ants_id);
        sentencesLayout = v.findViewById(R.id.sentences_layout_id);
        enSynsLAyout = v.findViewById(R.id.en_syns_layout_id);
        bnSynsLayout = v.findViewById(R.id.bn_syns_layout_id);
        antsLayout = v.findViewById(R.id.ants_layout_id);
        sentsContainerLayout = v.findViewById(R.id.sents_layout_container_id);


        getTranslations();

        return v;
    }


    private void getTranslations() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), Context.MODE_PRIVATE);
        EN_SYNS = sharedPreferences.getString(getString(R.string.EN_SYNS_STR), "");
        BN_SYNS = sharedPreferences.getString(getString(R.string.BN_SYNS_STR), "");
        ANTS = sharedPreferences.getString(getString(R.string.ANTONYMS_STR), "");
        SENTS = sharedPreferences.getString(getString(R.string.SENTS_STR), "");


        englishSyns.setText(EN_SYNS);
        banglaSyns.setText(BN_SYNS);
        ants.setText(ANTS);


        if (SENTS != null) {
            String[] sentsFormatted = SENTS.trim().split("\\.,");

            int x = 0;
            do {
                View child = getLayoutInflater().inflate(R.layout.extra_sentences_layout, sentencesLayout, false);
                sentencesLayout.addView(child);
                MaterialTextView sentences = child.findViewById(R.id.sentences_id_inflate);
                sentences.setText(Html.fromHtml(sentsFormatted[x].trim(), Html.FROM_HTML_MODE_COMPACT));
                x++;
            } while (x < sentsFormatted.length);
        }

        validateFields();
    }


    private void validateFields() {
        if (englishSyns.getText().toString().trim().equals("")) {
            enSynsLAyout.setVisibility(View.GONE);
        } else {
            enSynsLAyout.setVisibility(View.VISIBLE);
        }


        if (banglaSyns.getText().toString().trim().equals("")) {
            bnSynsLayout.setVisibility(View.GONE);
        } else {
            enSynsLAyout.setVisibility(View.VISIBLE);
        }


        if (ants.getText().toString().trim().equals("")) {
            antsLayout.setVisibility(View.GONE);
        } else {
            antsLayout.setVisibility(View.VISIBLE);
        }


        if (SENTS.trim().equals("")) {
            sentsContainerLayout.setVisibility(View.GONE);
        } else {
            sentsContainerLayout.setVisibility(View.VISIBLE);
        }

    }
}