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

public class TranslationInnerFragmentAdditional extends Fragment {


    MaterialTextView wordType;
    LinearLayout wordTypeLayout, defEnLayout, defBnLayout, defEnContainer, defBnContainer;

    String WORD_TYPE, DEF_EN, DEF_BN;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translation_inner_additional, container, false);

        //*Finding IDs
        wordType = v.findViewById(R.id.word_type_id);
        wordTypeLayout = v.findViewById(R.id.word_type_layout_id);
        defEnContainer = v.findViewById(R.id.definition_en_layout_container_id);
        defEnLayout = v.findViewById(R.id.definition_en_layout_id);
        defBnContainer = v.findViewById(R.id.definition_bn_container_layout_id);
        defBnLayout = v.findViewById(R.id.definition_bn_layout_id);


        getTranslations();

        return v;
    }



    private void getTranslations(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), Context.MODE_PRIVATE);
        WORD_TYPE = sharedPreferences.getString(getString(R.string.WORD_TYPE_STR), "");
        DEF_BN = sharedPreferences.getString(getString(R.string.DEFINITION_BN_STR), "");
        DEF_EN = sharedPreferences.getString(getString(R.string.DEFINITION_EN_STR), "");


        wordType.setText(WORD_TYPE);


        if (DEF_BN != null) {
            String[] formatted = DEF_BN.trim().split(";");

            int x = 0;
            do {
                View child = getLayoutInflater().inflate(R.layout.extra_sentences_layout, defBnLayout, false);
                defBnLayout.addView(child);
                MaterialTextView defBn = child.findViewById(R.id.sentences_id_inflate);
                defBn.setText(Html.fromHtml(formatted[x].trim(), Html.FROM_HTML_MODE_COMPACT));
                x++;
            } while (x < formatted.length);
        }

        if (DEF_EN != null) {
            String[] formatted = DEF_EN.trim().split("\\.;");

            int x = 0;
            do {
                View child = getLayoutInflater().inflate(R.layout.extra_sentences_layout, defEnLayout, false);
                defEnLayout.addView(child);
                MaterialTextView defEn = child.findViewById(R.id.sentences_id_inflate);
                defEn.setText(Html.fromHtml(formatted[x].trim(), Html.FROM_HTML_MODE_COMPACT));
                x++;
            } while (x < formatted.length);
        }


        validateFields();
    }





    private void validateFields() {
        if (wordType.getText().toString().trim().equals("")) {
            wordTypeLayout.setVisibility(View.GONE);
        } else {
            wordTypeLayout.setVisibility(View.VISIBLE);
        }


        if (DEF_BN.trim().equals("")) {
            defBnContainer.setVisibility(View.GONE);
        } else {
            defBnContainer.setVisibility(View.VISIBLE);
        }


        if (DEF_EN.trim().equals("")) {
            defEnContainer.setVisibility(View.GONE);
        } else {
            defEnContainer.setVisibility(View.VISIBLE);
        }
    }
}