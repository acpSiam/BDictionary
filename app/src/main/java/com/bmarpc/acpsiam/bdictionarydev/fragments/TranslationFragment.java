package com.bmarpc.acpsiam.bdictionarydev.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelper;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperIdioms;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperPrepositions;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperProverbs;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.LibraryModel;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.MyMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class TranslationFragment extends Fragment implements View.OnClickListener {

    DBHelper dbHelper;
    DBHelperIdioms dbHelperIdioms;
    DBHelperProverbs dbHelperProverbs;
    DBHelperPrepositions dbHelperPrepositions;
    String PRONUNCIATION_BANGLA = null;
    String PRONUNCIATION_ENGLISH = null;
    String ENGLISH, BANGLA, EN_SYNS, BN_SYNS, SENTS, WORD_TYPE, DEFINITION_EN, DEFINITION_BN, ANTONYMS;
    String EN_SYNS1, BN_SYNS1, ANTS1, SENTS1; //Prefs
    String WORD_TYPE1, DEF_EN1, DEF_BN1; //Prefs
    TabLayout tabLayout;
    @ColorInt
    int colorNormalSaveButtonTint;
    MaterialButton voiceInputButton;
    private TextInputLayout searchTextLayout;
    private TextView mainWord, translatedWord, animationText;
    private LinearLayout lottieLayout, hiddenLayout, viewpagerHiddenLayout;
    private MaterialAutoCompleteTextView searchText;
    private ExtendedFloatingActionButton saveButton, speakButton, copyButton;
    private TextToSpeech textToSpeech;
    private String PRONUNCIATION = null;
    private ViewPager2 viewPager2;
    // ActivityResultLauncher for speech recognition
    private ActivityResultLauncher<Intent> speechRecognitionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_translation, container, false);


        Resources.Theme theme = requireActivity().getTheme();

        TypedValue colorNormalFavButton = new TypedValue();
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, colorNormalFavButton, true);
        colorNormalSaveButtonTint = colorNormalFavButton.data;


        //*Assigning Variables
        dbHelper = new DBHelper(getContext());
        dbHelperIdioms = new DBHelperIdioms(getContext());
        dbHelperProverbs = new DBHelperProverbs(getContext());
        dbHelperPrepositions = new DBHelperPrepositions(getContext());

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

        voiceInputButton = v.findViewById(R.id.voice_input_button);


        //*Setting the end icon for Translation Text Input
        updateEndIconVisibility(searchTextLayout.getEditText().getText().toString());

        // Add a TextWatcher to monitor changes in the TextInputEditText
        searchTextLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Update the end icon visibility based on text length
                updateEndIconVisibility(editable.toString());
            }
        });


        // Initialize the ActivityResultLauncher
        speechRecognitionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get the speech recognition results as a list of strings
                        ArrayList<String> speechResults = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (speechResults != null && !speechResults.isEmpty()) {
                            // Set the recognized speech as the text in the TextInputLayout
                            String recognizedText = speechResults.get(0);
                            searchTextLayout.getEditText().setText(recognizedText.split(" ")[0]);
                            translateWord(recognizedText.split(" ")[0].trim());
                            updateEndIconVisibility(recognizedText.split(" ")[0]);
                            searchTextLayout.clearFocus();
                        }
                    }
                });


        voiceInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchTextLayout.getEditText().getText())) {
                    startVoiceInput();
                }
            }
        });


        //*Setting up the adapter to the search EditText
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, LibraryModel.allEnWordsArrayList);
        searchText.setAdapter(arrayAdapter);

        searchText.setOnItemClickListener((parent, view, position, id) -> {
            String clickedItem = parent.getItemAtPosition(position).toString();
            if (clickedItem.endsWith("(IDIOM)")) {
                clickedItem = clickedItem.replace("(IDIOM)", " ").trim();
            } else if (clickedItem.endsWith("(PROVERB)")) {
                clickedItem = clickedItem.replace("(PROVERB)", " ").trim();
            } else if (clickedItem.endsWith("(PREPOSITIONS)")) {
                clickedItem = clickedItem.replace("(PREPOSITIONS)", " ").trim();
            }
            translateWord(clickedItem.trim());
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


        searchTextLayout.setEndIconOnClickListener(v12 -> {
            searchText.setText(null);
            mainWord.setText(null);
            mainTextChecker();
        });

        searchTextLayout.setErrorIconOnClickListener(v13 -> {
            searchTextLayout.setErrorEnabled(false);
            searchText.setText(null);
        });


        searchText.setOnKeyListener((v1, keyCode, event) -> {


            //To filter keyListener from being triggered twice
            if (event.getAction() != KeyEvent.ACTION_DOWN)
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


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void translateWord(String s) {
        searchText.dismissDropDown();
        ArrayList<Cursor> cursorArrayList = new ArrayList<>();
        Cursor cursor = dbHelper.fetchEnToBnData(s);
        Cursor idiomCursor = dbHelperIdioms.fetchEnToBnIdiomsData(s);
        Cursor proverbsCursor = dbHelperProverbs.fetchEnToBnProverbsData(s);
        Cursor prepositionsCursor = dbHelperPrepositions.fetchEnToBnPrepositionsData(s);

        cursorArrayList.add(idiomCursor);
        cursorArrayList.add(prepositionsCursor);
        cursorArrayList.add(proverbsCursor);
        cursorArrayList.add(cursor);

        Cursor finalCursor = null;
        for (int x = 0; x < cursorArrayList.size(); x++) {
            if (cursorArrayList.get(x).getCount() != 0) {
                finalCursor = cursorArrayList.get(x);
                break;
            }
        }
        if (finalCursor != null) {
            while (finalCursor.moveToNext()) {
                if (searchTextLayout.getError() != null) {
                    searchTextLayout.setError(null);
                }


                if (finalCursor == cursor) {
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
                } else if (finalCursor == idiomCursor) {
                    ENGLISH = s;
                    BANGLA = idiomCursor.getString(2);
                    DEFINITION_EN = idiomCursor.getString(4);
                    SENTS = idiomCursor.getString(3);
                    PRONUNCIATION = "";
                    EN_SYNS = "";
                    BN_SYNS = "";
                    WORD_TYPE = "Idiom";
                    DEFINITION_BN = "";
                    ANTONYMS = "";
                } else if (finalCursor == proverbsCursor) {
                    ENGLISH = s;
                    BANGLA = proverbsCursor.getString(2);
                    DEFINITION_EN = "";
                    SENTS = "";
                    PRONUNCIATION = "";
                    EN_SYNS = "";
                    BN_SYNS = "";
                    WORD_TYPE = "";
                    DEFINITION_BN = "";
                    ANTONYMS = "";
                } else if (finalCursor == prepositionsCursor) {
                    ENGLISH = s;
                    BANGLA = prepositionsCursor.getString(2);
                    DEFINITION_EN = "";
                    SENTS = prepositionsCursor.getString(3);
                    PRONUNCIATION = "";
                    EN_SYNS = "";
                    BN_SYNS = "";
                    WORD_TYPE = "";
                    DEFINITION_BN = "";
                    ANTONYMS = "";
                }


            }


            recordSearchedData(s, BANGLA, PRONUNCIATION, EN_SYNS, BN_SYNS, SENTS, WORD_TYPE, DEFINITION_EN, DEFINITION_BN, ANTONYMS);
            dbHelper.addToHistory(ENGLISH, BANGLA);
            Toast.makeText(getContext(), "called1", Toast.LENGTH_SHORT).show();


            //*Changing Save Button Color Based On User Search
            if (dbHelper.favouritesExists(ENGLISH)) {
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

            FragmentStateAdapter pagerAdapter = new ScreenSlidePageAdapter(this);
            viewPager2.setAdapter(pagerAdapter);
            viewPager2.setPageTransformer(new ZoomOutPageTransformer());
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                String tabTitle = null;

                if (position == 0) {
                    tabTitle = getString(R.string.more);
                } else if (position == 1) {
                    tabTitle = getString(R.string.additional);
                }
                tab.setText(tabTitle);
            }).attach();


            View view = requireActivity().getWindow().getCurrentFocus();
            if (view != null) {
                InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


        } else {
            Toast.makeText(requireActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            searchTextLayout.setErrorEnabled(true);
            searchTextLayout.setError(getString(R.string.wrong_spelling));
            mainWord.setText(null);
            animationText.setText(R.string.wrong_word_lottie_speech);
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


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.fragment_translate_save_button) {
            dbHelper.addFavWords(ENGLISH, BANGLA);
            Toast.makeText(requireActivity(), R.string.added_to_favourites, Toast.LENGTH_SHORT).show();
            saveButton.setIcon(requireActivity().getDrawable(R.drawable.ic_baseline_favorite_24));
            saveButton.setIconTint(ColorStateList.valueOf(Color.RED));
        } else if (viewId == R.id.fragment_translate_speak_button) {
            textToSpeech = new TextToSpeech(requireActivity(), i -> {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.speak(ENGLISH, TextToSpeech.QUEUE_ADD, null, null);
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            });
        } else if (viewId == R.id.translated_word) {
            textToSpeech = new TextToSpeech(requireActivity(), i -> {
                if (i == TextToSpeech.SUCCESS) {
                    if (!PRONUNCIATION_BANGLA.trim().isEmpty() && !PRONUNCIATION_BANGLA.trim().equals("")) {
                        textToSpeech.speak(PRONUNCIATION_BANGLA, TextToSpeech.QUEUE_FLUSH, null, null);
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    } else {
                        Toast.makeText(requireActivity(), R.string.pronunciation_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (viewId == R.id.fragment_translate_copy_button) {
            String text = mainWord.getText().toString() + " : " + translatedWord.getText().toString();
            MyMethods.copyToClipboard(requireContext(), text);
        }

    }


    public void recordSearchedData(String englishWord, String banglaWord, String prons,
                                   String enSyns, String bnSyns, String sents,
                                   String wordType, String defEn, String defBn, String ants
    ) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), Context.MODE_PRIVATE);
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

        WORD_TYPE1 = sharedPreferences.getString(getString(R.string.WORD_TYPE_STR), "");
        DEF_BN1 = sharedPreferences.getString(getString(R.string.DEFINITION_BN_STR), "");
        DEF_EN1 = sharedPreferences.getString(getString(R.string.DEFINITION_EN_STR), "");

    }

    public int getFragmentState() {
        int fragmentState;

        if (EN_SYNS1.trim().equals("") && BN_SYNS1.trim().equals("") && ANTS1.trim().equals("") && SENTS1.trim().equals("")
                && WORD_TYPE1.trim().equals("") && DEF_BN1.trim().equals("") && DEF_EN1.trim().equals("")) {
            fragmentState = 3;
        } else if (EN_SYNS1.trim().equals("") && BN_SYNS1.trim().equals("") && ANTS1.trim().equals("") && SENTS1.trim().equals("")) {
            fragmentState = 1;
        } else if (WORD_TYPE1.trim().equals("") && DEF_BN1.trim().equals("") && DEF_EN1.trim().equals("")) {
            fragmentState = 2;
        } else {
            fragmentState = 0;
        }

        return fragmentState;
    }

    private void updateEndIconVisibility(String text) {
        if (TextUtils.isEmpty(text)) {
            voiceInputButton.setVisibility(View.VISIBLE);
            searchTextLayout.setEndIconVisible(false);
        } else {
            voiceInputButton.setVisibility(View.GONE);
            searchTextLayout.setEndIconVisible(true);
        }

    }

    private void startVoiceInput() {
        // Check if the device supports speech recognition
        if (SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            // Create an intent to prompt the user for speech input
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            // Start the speech recognition activity with the specified intent
            speechRecognitionLauncher.launch(intent);
        } else {
            // Speech recognition is not available on this device, show an error message
            Toast.makeText(requireContext(), "Speech recognition is not supported on your device.", Toast.LENGTH_SHORT).show();
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

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(TranslationFragment fragmentTranslate) {
            super(fragmentTranslate);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {

            Fragment returnFragment;

            if (getFragmentState() == 1) {
                returnFragment = new TranslationInnerFragmentAdditional();
            } else if (getFragmentState() == 2) {
                returnFragment = new TranslationInnerFragmentMain();
            } else if (getFragmentState() == 3) {
                returnFragment = new TranslationInnerFragmentBlank();
            } else {
                switch (position) {
                    case 0:
                        returnFragment = new TranslationInnerFragmentMain();
                        break;
                    case 1:
                        returnFragment = new TranslationInnerFragmentAdditional();
                        break;
                    default:
                        return null;
                }
            }
            return returnFragment;


        }

        @Override
        public int getItemCount() {
            int NUM_PAGES;
            if (getFragmentState() == 1 || getFragmentState() == 2 || getFragmentState() == 3) {
                NUM_PAGES = 1;
            } else {
                NUM_PAGES = 2;
            }
            return NUM_PAGES;
        }
    }


}