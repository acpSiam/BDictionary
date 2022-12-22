package bmarpc.acpsiam.bdictionary;

import static android.content.Context.CLIPBOARD_SERVICE;

import static bmarpc.acpsiam.bdictionary.R.string.DEFINITION_BN_STR;
import static bmarpc.acpsiam.bdictionary.R.string.DEFINITION_EN_STR;
import static bmarpc.acpsiam.bdictionary.R.string.WORD_TYPE_STR;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


    private int NUM_PAGES;
    DBHandler dbHandler;
    String PRONUNCIATION_BANGLA = null;
    String PRONUNCIATION_ENGLISH = null;
    String ENGLISH, BANGLA, EN_SYNS, BN_SYNS, SENTS, WORD_TYPE, DEFINITION_EN, DEFINITION_BN, ANTONYMS;
    String EN_SYNS1, BN_SYNS1, ANTS1, SENTS1; //Prefs
    String WORD_TYPE1, DEF_EN1, DEF_BN1; //Prefs
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


    @ColorInt int colorNormalSaveButtonTint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);


        Resources.Theme theme  = requireActivity().getTheme();

        //Matching status bar color with the top of activity
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, typedValue, true);
        @ColorInt int color = typedValue.data;
        requireActivity().getWindow().setStatusBarColor(color);



        TypedValue colorNormalFavButton = new TypedValue();
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, colorNormalFavButton, true);
        colorNormalSaveButtonTint = colorNormalFavButton.data;


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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, SetupActivity.enWords);
        searchText.setAdapter(arrayAdapter);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = parent.getItemAtPosition(position).toString();
                translateWord(clickedItem.trim());
            }
        });


        //*Cleaning up the fields
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


            //To filter keyListener from being trigerred twice
            if (event.getAction()!=KeyEvent.ACTION_DOWN)
                return true;

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (searchText != null) {
                    translateWord(searchText.getText().toString().trim());
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
                if (searchTextLayout.getError()!=null){
                    searchTextLayout.setError(null);
                }
                ENGLISH = s;
                BANGLA = cursor.getString(2);
                PRONUNCIATION = cursor.getString(3);
                EN_SYNS = cursor.getString(4);
                BN_SYNS = cursor.getString(5);
                SENTS = cursor.getString(6);
                WORD_TYPE = cursor.getString(7);
                DEFINITION_EN = cursor.getString(8);
                DEFINITION_BN = cursor.getString(9);
                ANTONYMS = cursor.getString(10);

            }


            recordSearchedData(s, BANGLA, PRONUNCIATION, EN_SYNS, BN_SYNS, SENTS, WORD_TYPE, DEFINITION_EN, DEFINITION_BN, ANTONYMS);
            dbHandler.addToHistory(ENGLISH, BANGLA);
            Toast.makeText(getContext(), "called1", Toast.LENGTH_SHORT).show();




            //*Changing Save Button Color Based On User Search
            if (dbHandler.favouritesExists(ENGLISH)){
                saveButton.setIcon(requireActivity().getDrawable(R.drawable.ic_baseline_favorite_24));
                saveButton.setIconTint(ColorStateList.valueOf(Color.RED));
            } else {
                saveButton.setIcon(requireActivity().getDrawable(R.drawable.ic_round_favorite_border_24));
                saveButton.setIconTint(ColorStateList.valueOf(colorNormalSaveButtonTint));
            }

            if (PRONUNCIATION != null) {
                String[] pronsFormatted = PRONUNCIATION.trim().split(",");
                PRONUNCIATION_ENGLISH = pronsFormatted[0].trim().replaceAll("null", "".trim());
                PRONUNCIATION_BANGLA = pronsFormatted[pronsFormatted.length - 1].trim().replaceAll("null", "".trim());
            }


            translatedWord.setText(BANGLA + "  ");
            mainWord.setText(s);
            mainTextChecker();


            pagerAdapter = new ScreenSlidePageAdapter(this);
            viewPager2.setAdapter(pagerAdapter);
            viewPager2.setPageTransformer(new ZoomOutPageTransformer());
            new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    String tabTitle = null;

                    if (position == 0) {
                        tabTitle = "More";
                    } else if (position == 1) {
                        tabTitle = "Additional";
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
            searchTextLayout.setError(getString(R.string.wrong_spelling));
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
                dbHandler.addFavWords(ENGLISH, BANGLA);
                Toast.makeText(requireActivity(), "Added to favourites", Toast.LENGTH_SHORT).show();
                saveButton.setIcon(requireActivity().getDrawable(R.drawable.ic_baseline_favorite_24));
                saveButton.setIconTint(ColorStateList.valueOf(Color.RED));
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


        EN_SYNS1 = sharedPreferences.getString(getString(R.string.EN_SYNS_STR), "");
        BN_SYNS1 = sharedPreferences.getString(getString(R.string.BN_SYNS_STR), "");
        ANTS1 = sharedPreferences.getString(getString(R.string.ANTONYMS_STR), "");
        SENTS1 = sharedPreferences.getString(getString(R.string.SENTS_STR), "");

        WORD_TYPE1 = sharedPreferences.getString(getString(WORD_TYPE_STR), "");
        DEF_BN1 = sharedPreferences.getString(getString(DEFINITION_BN_STR), "");
        DEF_EN1 = sharedPreferences.getString(getString(DEFINITION_EN_STR), "");

    }


    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(FragmentTranslate fragmentTranslate) {
            super(fragmentTranslate);
        }


        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {

            Fragment returnFragment = null;

            if (getFragmentState() == 1) {
                returnFragment = new FragmentAdditionalTranslation();
            } else if (getFragmentState() == 2) {
                returnFragment = new FragmentMainTranslation();
            } else if (getFragmentState() == 3) {
                returnFragment = new FragmentBlankTranslation();
            } else {
                switch (position) {
                    case 0:
                        returnFragment = new FragmentMainTranslation();
                        break;
                    case 1:
                        returnFragment = new FragmentAdditionalTranslation();
                        break;
                    default:
                        return null;
                }
            }
            return returnFragment;


        }

        @Override
        public int getItemCount() {

            if (getFragmentState() == 1 || getFragmentState() == 2 || getFragmentState() == 3) {
                NUM_PAGES = 1;
            } else {
                NUM_PAGES = 2;
            }

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


    public int getFragmentState() {
        int fragmentState = 0;

         if (EN_SYNS1.trim().equals("") && BN_SYNS1.trim().equals("") && ANTS1.trim().equals("") && SENTS1.trim().equals("")
                && WORD_TYPE1.trim().equals("") && DEF_BN1.trim().equals("") && DEF_EN1.trim().equals("")) {
            fragmentState = 3;
        }
        else if (EN_SYNS1.trim().equals("") && BN_SYNS1.trim().equals("") && ANTS1.trim().equals("") && SENTS1.trim().equals("")) {
            fragmentState = 1;
        }
        else if (WORD_TYPE1.trim().equals("") && DEF_BN1.trim().equals("") && DEF_EN1.trim().equals("")) {
            fragmentState = 2;
        }
        else {
            fragmentState = 0;
        }

        return fragmentState;
    }


}