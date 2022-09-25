package bmarpc.acpsiam.bdictionary;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class FragmentTranslate extends Fragment implements View.OnClickListener {


    private static final int NUM_PAGES = 2;
    DBHandler dbHandler;
    String PRONUNCIATION_BANGLA = null;
    String PRONUNCIATION_ENGLISH = null;
    TabLayout tabLayout;
    private TextInputLayout searchTextLayout;
    private TextView mainWord, translatedWord, animationText;
    private LinearLayout lottieLayout, hiddenLayout, viewpagerHiddenLayout;
    private MaterialAutoCompleteTextView searchText;
    private ExtendedFloatingActionButton saveButton, speakButton, copyButton;
    private TextToSpeech textToSpeech;
    private String PRONUNCIATION = null;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);

        //Matching status bar color with the top of activity
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = requireActivity().getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, typedValue, true);
        @ColorInt int color = typedValue.data;
        requireActivity().getWindow().setStatusBarColor(color);


        //*Assigning Variables
        dbHandler = new DBHandler(getContext());

        //*Finding IDs
        mainWord = v.findViewById(R.id.main_word);
        translatedWord = v.findViewById(R.id.translated_word);
        animationText = v.findViewById(R.id.fragment_library_animation_text);
        saveButton = v.findViewById(R.id.fragment_translate_save_button);
        speakButton = v.findViewById(R.id.fragment_translate_speak_button);
        copyButton = v.findViewById(R.id.fragment_translate_copy_button);

        lottieLayout = v.findViewById(R.id.lottie_layout_id);
        searchText = v.findViewById(R.id.search_text_id);
        searchTextLayout = v.findViewById(R.id.search_text_layout_id);
        hiddenLayout = v.findViewById(R.id.hidden_translation_layout_id);
        viewpagerHiddenLayout = v.findViewById(R.id.viewpager_hidden_layout);

        viewPager2 = v.findViewById(R.id.pager);
        tabLayout = v.findViewById(R.id.tab_layout_id);



        //*Settting up the adapter to the search EditText
        searchText.setAdapter(MainActivity.arrayAdapter[0]);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = parent.getItemAtPosition(position).toString();
                translateWord(clickedItem);
            }
        });

        mainWord.setText(null);
        translatedWord.setText(null);
        mainTextChecker();




        //*Implementing ClickListeners
        saveButton.setOnClickListener(this);
        speakButton.setOnClickListener(this);
        copyButton.setOnClickListener(this);
        translatedWord.setOnClickListener(this);


        searchTextLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(null);
                mainWord.setText(null);
                mainTextChecker();
            }
        });

        searchTextLayout.setErrorIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTextLayout.setErrorEnabled(false);
                searchText.setText(null);
            }
        });


        searchText.setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (searchText != null) {
                    translateWord(searchText.getText().toString());
                }
            }
            return true;
        });

        return v;
    }


    @SuppressLint("SetTextI18n")
    private void translateWord(String s) {
        searchText.dismissDropDown();
        Cursor cursor = dbHandler.fetchEnToBnData(requireActivity(), s);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                String BANGLA = cursor.getString(2);
                PRONUNCIATION = cursor.getString(3);
                String EN_SYNS = cursor.getString(4);
                String BN_SYNS = cursor.getString(5);
                String SENTS = cursor.getString(6);
                String WORD_TYPE = cursor.getString(7);
                String DEFINITION_EN = cursor.getString(8);
                String DEFINITION_BN = cursor.getString(9);
                String ANTONYMS = cursor.getString(10);

                recordSearchedData(s, BANGLA, PRONUNCIATION, EN_SYNS, BN_SYNS, SENTS, WORD_TYPE, DEFINITION_EN, DEFINITION_BN, ANTONYMS);


                if (PRONUNCIATION != null) {
                    String[] pronsFormatted = PRONUNCIATION.trim().split(",");
                    PRONUNCIATION_ENGLISH = pronsFormatted[0].trim().replaceAll("null", "".trim());
                    PRONUNCIATION_BANGLA = pronsFormatted[pronsFormatted.length - 1].trim().replaceAll("null", "".trim());
                }


                translatedWord.setText(BANGLA + "  ");
                mainWord.setText(s);
                mainTextChecker();

            }

            pagerAdapter = new ScreenSlidePageAdapter(this);
            viewPager2.setAdapter(pagerAdapter);
            viewPager2.setPageTransformer(new ZoomOutPageTransformer());
            new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    String tabTitle = null;

                    if (position==0){
                        tabTitle = "Additional";
                    } else if (position==1){
                        tabTitle = "More";
                    }
                    tab.setText(tabTitle);
                }
            }).attach();


            View view = requireActivity().getWindow().getCurrentFocus();
            if (view != null) {
                InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


        } else {
            searchTextLayout.setErrorEnabled(true);
            searchTextLayout.setError("Spelling might be wrong");
            mainWord.setText(null);
            animationText.setText(R.string.wrong_word);
            mainTextChecker();
        }

    }


    private void mainTextChecker() {
        if (mainWord.getText().toString().trim().isEmpty()) {
            hiddenLayout.setVisibility(View.GONE);
            viewpagerHiddenLayout.setVisibility(View.GONE);
            lottieLayout.setVisibility(View.VISIBLE);
        } else {
            hiddenLayout.setVisibility(View.VISIBLE);
            viewpagerHiddenLayout.setVisibility(View.VISIBLE);
            lottieLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fragment_translate_save_button:
                Toast.makeText(requireActivity(), "save", Toast.LENGTH_SHORT).show();
                break;

            case R.id.fragment_translate_speak_button:

                textToSpeech = new TextToSpeech(requireActivity(), new TextToSpeech.OnInitListener() {

                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {
                            textToSpeech.speak(searchText.getText().toString(), TextToSpeech.QUEUE_ADD, null, null);
                            textToSpeech.setLanguage(Locale.ENGLISH);
                        }

                    }
                });

                break;


            case R.id.translated_word:

                textToSpeech = new TextToSpeech(requireActivity(), new TextToSpeech.OnInitListener() {

                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {
                            if (!PRONUNCIATION_BANGLA.trim().isEmpty() && !PRONUNCIATION_BANGLA.trim().equals("")) {
                                textToSpeech.speak(PRONUNCIATION_BANGLA, TextToSpeech.QUEUE_FLUSH, null, null);
                                textToSpeech.setLanguage(Locale.ENGLISH);
                            } else {
                                Toast.makeText(requireActivity(), "Pronunciation Not Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                break;

            case R.id.fragment_translate_copy_button:
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mainWord.getText().toString(), mainWord.getText().toString() + " : " + translatedWord.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireActivity(), "Copied", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void recordSearchedData(String englishWord, String banglaWord, String prons,
                                   String enSyns, String bnSyns, String sents,
                                   String wordType, String defEn, String defBn, String ants
    ) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.TRANSLATION_DATA), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.ENGLISH_STR), englishWord);
        editor.putString(getString(R.string.BANGLA_STR), banglaWord);
        editor.putString(getString(R.string.PRONS_STR), prons);
        editor.putString(getString(R.string.EN_SYNS_STR), enSyns);
        editor.putString(getString(R.string.BN_SYNS_STR), bnSyns);
        editor.putString(getString(R.string.SENTS_STR), sents);
        editor.putString(getString(R.string.WORD_TYPE_STR), wordType);
        editor.putString(getString(R.string.DEFINITION_EN_STR), defEn);
        editor.putString(getString(R.string.DEFINITION_BN_STR), defBn);
        editor.putString(getString(R.string.ANTONYMS_STR), ants);
        editor.apply();
    }


    private static class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(FragmentTranslate fragmentTranslate) {
            super(fragmentTranslate);
        }


        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new FragmentMainTranslation();
                case 1:
                    return new FragmentAdditionalTranslation();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private static class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull @NotNull View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0f);
            }
        }

    }

}