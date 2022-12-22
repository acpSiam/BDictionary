package bmarpc.acpsiam.bdictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class FragmentBlankTranslation extends Fragment {

    public FragmentBlankTranslation() {
        // Required empty public constructor
    }


    TextView tv;
    String SEARCHED_TEXT;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blank_translation, container, false);

        tv = v.findViewById(R.id.textsID);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.TRANSLATION_DATA), Context.MODE_PRIVATE);



        SEARCHED_TEXT = sharedPreferences.getString(getString(R.string.ENGLISH_STR), "");
        tv.setText(SEARCHED_TEXT);



        return v;
    }
}











