package com.bmarpc.acpsiam.bdictionarydev.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmarpc.acpsiam.bdictionarydev.R;

public class TranslationInnerFragmentBlank extends Fragment {

    public TranslationInnerFragmentBlank() {
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
        return inflater.inflate(R.layout.fragment_translation_inner_blank, container, false);
    }
}