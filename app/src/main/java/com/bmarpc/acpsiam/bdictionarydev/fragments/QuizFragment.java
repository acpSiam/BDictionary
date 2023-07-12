package com.bmarpc.acpsiam.bdictionarydev.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.activities.MainActivity;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelper;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperIdioms;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperPrepositions;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperProverbs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment {
    private final long COUNTDOWN_TIME = 10000; // 10 seconds
    String correctAnswer, question;
    HashMap wrongAnswersHashMap = new HashMap();
    HashMap correctAnswersHashMap = new HashMap();
    HashMap skippedAnswersHashMap = new HashMap();
    LottieAnimationView quizTimerLottie;
    private TextView questionTextView, questionNumberTextView, quizTimerTextView;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;
    private Cursor quizCursor;
    private Cursor quizCursorWrongOptions;
    private int currentQuestionIndex;
    private int score;
    private CountDownTimer countDownTimer;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        // Initialize the views
        quizTimerTextView = root.findViewById(R.id.quiz_timer_id);
        quizTimerLottie = root.findViewById(R.id.quiz_timer_lottie_id);
        questionTextView = root.findViewById(R.id.quiz_question_text_view_id);
        questionNumberTextView = root.findViewById(R.id.quiz_question_number_text_view_id);
        option1RadioButton = root.findViewById(R.id.quiz_option1_radio_button_id);
        option2RadioButton = root.findViewById(R.id.quiz_option2_radio_button_id);
        option3RadioButton = root.findViewById(R.id.quiz_option3_radio_button_id);
        option4RadioButton = root.findViewById(R.id.quiz_option4_radio_button_id);
        optionsRadioGroup = root.findViewById(R.id.quiz_options_radio_group_id);
        submitButton = root.findViewById(R.id.quiz_submit_button_id);

        // Retrieve the quiz data from the cursor

        // Set the initial question index and score
        currentQuestionIndex = 1;
        score = 0;


        viewQuizStartDialog();

        submitButton.setOnClickListener(v -> {
            // Find the ID of the selected radio button
            int selectedAnswerIndex = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedAnswerIndex == -1) {
                Toast.makeText(getActivity(), R.string.please_select_an_answer, Toast.LENGTH_SHORT).show();
                return;
            }


            RadioButton radioButton = root.findViewById(selectedAnswerIndex);
            String radioButtonText = radioButton.getText().toString();

            if (radioButtonText == correctAnswer) {
                // If the radio button contains text similar to the answer the score given
                score++;
                correctAnswersHashMap.put(question, correctAnswer);
            } else {
                wrongAnswersHashMap.put(question, radioButtonText);
            }

            // Move to the next question or show the final score
            if (quizCursor.moveToNext()) {
                currentQuestionIndex++;
                displayQuestion();
                countDownTimer.cancel();
                startCountdownTimer();
            } else {
                countDownTimer.cancel();
                showScore();
            }
        });


        return root;
    }


    private void displayQuestion() {
        // Retrieve the question and options from the cursor
        question = quizCursor.getString(0);
        correctAnswer = quizCursor.getString(1);
        String wrongOption1 = quizCursorWrongOptions.getString(0);
        quizCursorWrongOptions.moveToNext();
        String wrongOption2 = quizCursorWrongOptions.getString(0);
        quizCursorWrongOptions.moveToNext();
        String wrongOption3 = quizCursorWrongOptions.getString(0);

        // Shuffle the options
        List<String> optionsList = Arrays.asList(correctAnswer, wrongOption1, wrongOption2, wrongOption3);
        Collections.shuffle(optionsList);

        // Display the question and shuffled options
        questionTextView.setText(question);
        option1RadioButton.setText(optionsList.get(0));
        option2RadioButton.setText(optionsList.get(1));
        option3RadioButton.setText(optionsList.get(2));
        option4RadioButton.setText(optionsList.get(3));

        optionsRadioGroup.clearCheck();
    }


    private void showScore() {
        String scoreMessage = "You scored " + score + " out of " + currentQuestionIndex;
        Toast.makeText(getActivity(), scoreMessage, Toast.LENGTH_LONG).show();

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Scoreboard")
                .setMessage(scoreMessage
                        + "\n\n" + "Skipped: "
                        + skippedAnswersHashMap.size() + "\n\n"
                        + "Wrong: " + wrongAnswersHashMap.size() + "\n\n"
                        + "Corrected: " + correctAnswersHashMap.size())
                .setPositiveButton("Play again", (dialog, which) -> {
                    dialog.dismiss();
                    currentQuestionIndex++;
                    viewQuizStartDialog();
                }).setNegativeButton("Finish", (dialog, which) -> {
                    ((MainActivity) getActivity()).selectFragment(new TranslationFragment(), true, "BDictionary");
                    dialog.dismiss();
                }).create().show();

    }


    public void viewQuizStartDialog() {

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_quiz_options);


        // Assign the views with IDs to variables
        ChipGroup chipGroup = dialog.findViewById(R.id.quiz_start_chip_group_id);
        Chip wordsChip = dialog.findViewById(R.id.quiz_start_words_chip_id);
        Chip idiomsChip = dialog.findViewById(R.id.quiz_start_idioms_chip_id);
        Chip proverbsChip = dialog.findViewById(R.id.quiz_start_proverbs_chip_id);
        Chip prepositionsChip = dialog.findViewById(R.id.quiz_start_prepositions_chip_id);
        Chip numberOfQuestionsChip = dialog.findViewById(R.id.quiz_start_number_of_qurstions_chip_id);
        MaterialButton startButton = dialog.findViewById(R.id.quiz_dialog_start_button_id);
        MaterialButton cancelButton = dialog.findViewById(R.id.quiz_dialog_cancel_button_id);
        final String[] selectedNumberOfQuestions = {"10"};


        numberOfQuestionsChip.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_menu_layout, null);

            final String[] items = {"10", "20", "30"};

            // Create the PopupWindow
            final PopupWindow popupWindow = new PopupWindow(popupView, numberOfQuestionsChip.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupView.setBackgroundResource(R.drawable.custom_dropdown_menu_item_background);

            // Set the items for the dropdown menu
            ListView listView = popupView.findViewById(R.id.dropdown_list_view);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);

            // Set an item click listener for the dropdown menu
            listView.setOnItemClickListener((parent, view, position, id) -> {
                selectedNumberOfQuestions[0] = items[position];
                numberOfQuestionsChip.setText(getString(R.string.number_of_questions) + selectedNumberOfQuestions[0]);
                popupWindow.dismiss();
            });

            // Show the dropdown menu below the Chip
            popupWindow.showAsDropDown(numberOfQuestionsChip);
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> selectedChips = new ArrayList<>();

                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    if (chip.isChecked()) {
                        String chipText = chip.getText().toString().toLowerCase();
                        selectedChips.add(chipText);
                    }
                }


                int numberOfQuestions = Integer.valueOf(selectedNumberOfQuestions[0]);
                int numberOfSelectedChips = selectedChips.size();
                int[] sizeOfEachSection = getSizeOfEachSection(numberOfQuestions, numberOfSelectedChips);


                ArrayList<Cursor> cursorArrayList = new ArrayList<>();
                ArrayList<Cursor> wrongAnswersCursorArrayList = new ArrayList<>();
                for (int i = 0; i < sizeOfEachSection.length; i++) {
                    switch (selectedChips.get(i)) {
                        case "words":
                            cursorArrayList.add(getWordsQuizCursor(sizeOfEachSection[i]));
                            wrongAnswersCursorArrayList.add(getWrongAnswersForWordsQuizCursor(sizeOfEachSection[i]));
                            break;
                        case "idioms":
                            cursorArrayList.add(getIdiomsQuizCursor(sizeOfEachSection[i]));
                            wrongAnswersCursorArrayList.add(getWrongAnswersForIdiomsQuizCursor(sizeOfEachSection[i]));
                            break;
                        case "proverbs":
                            cursorArrayList.add(getProverbsQuizCursor(sizeOfEachSection[i]));
                            wrongAnswersCursorArrayList.add(getWrongAnswersForProverbsQuizCursor(sizeOfEachSection[i]));
                            break;
                        case "prepositions":
                            cursorArrayList.add(getPrepositionsQuizCursor(sizeOfEachSection[i]));
                            wrongAnswersCursorArrayList.add(getWrongAnswersForPrepositionsQuizCursor(sizeOfEachSection[i]));
                            break;
                    }


                }


                MergeCursor mergeCursor = new MergeCursor(cursorArrayList.toArray(new Cursor[cursorArrayList.size()]));
                MergeCursor mergeWrongAnswersCursor = new MergeCursor(wrongAnswersCursorArrayList
                        .toArray(new Cursor[wrongAnswersCursorArrayList.size()]));

                mergeCursor.moveToFirst();
                mergeWrongAnswersCursor.moveToFirst();

                quizCursor = mergeCursor;
                quizCursorWrongOptions = mergeWrongAnswersCursor;

                dialog.dismiss();
                displayQuestion();
                startCountdownTimer();


            }



            private int[] getSizeOfEachSection(int numberOfQuestions, int numberOfSelectedChips) {
                int quotient = numberOfQuestions / numberOfSelectedChips;
                int remainder = numberOfQuestions % numberOfSelectedChips;

                int[] array = new int[numberOfSelectedChips];

                Arrays.fill(array, quotient);

                if (remainder > 0) {
                    array[0] += remainder;
                }

                return array;
            }
        });


        cancelButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).selectFragment(new TranslationFragment(), true, "BDictionary");
            dialog.dismiss();
        });


        dialog.setOnCancelListener(dialog1 -> {
            ((MainActivity) getActivity()).selectFragment(new TranslationFragment(), true, "BDictionary");
            dialog1.dismiss();
        });


        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void startCountdownTimer() {


        questionNumberTextView.setText(getResources().getString(R.string.question)
                + " " + currentQuestionIndex);
        quizTimerLottie.playAnimation();

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                quizTimerTextView.setText(String.valueOf(millisUntilFinished / 1000));

                // Calculate the progress percentage based on the remaining time

            }

            @Override
            public void onFinish() {
                // Set the animation progress to the end

                skippedAnswersHashMap.put(question, correctAnswer);

                // Move to the next question or show the final score
                if (quizCursor.moveToNext()) {
                    currentQuestionIndex++;
                    displayQuestion();
                    startCountdownTimer();
                } else {
                    showScore();
                }
            }
        };

        // Start the countdown timer
        countDownTimer.start();
    }


    private Cursor getWordsQuizCursor(int numberOfQuestions) {
        DBHelper dbHelper = new DBHelper(getContext());
        Cursor cursor = dbHelper.getRandomItemsForQuiz(numberOfQuestions);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getIdiomsQuizCursor(int numberOfQuestions) {
        DBHelperIdioms dbHelperIdioms = new DBHelperIdioms(getContext());
        Cursor cursor = dbHelperIdioms.getRandomItemsForQuiz(numberOfQuestions);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getProverbsQuizCursor(int numberOfQuestions) {
        DBHelperProverbs dbHelperProverbs = new DBHelperProverbs(getContext());
        Cursor cursor = dbHelperProverbs.getRandomItemsForQuiz(numberOfQuestions);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getPrepositionsQuizCursor(int numberOfQuestions) {
        DBHelperPrepositions dbHelperPrepositions = new DBHelperPrepositions(getContext());
        Cursor cursor = dbHelperPrepositions.getRandomItemsForQuiz(numberOfQuestions);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getWrongAnswersForWordsQuizCursor(int numberOfQuestions) {
        DBHelper dbHelper = new DBHelper(getContext());
        Cursor cursor = dbHelper.getRandomWrongAnswerItemsForQuiz(numberOfQuestions * 3);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getWrongAnswersForProverbsQuizCursor(int numberOfQuestions) {
        DBHelperProverbs dbHelperProverbs = new DBHelperProverbs(getContext());
        Cursor cursor = dbHelperProverbs.getRandomWrongAnswerItemsForQuiz(numberOfQuestions * 3);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getWrongAnswersForIdiomsQuizCursor(int numberOfQuestions) {
        DBHelperIdioms dbHelperIdioms = new DBHelperIdioms(getContext());
        Cursor cursor = dbHelperIdioms.getRandomWrongAnswerItemsForQuiz(numberOfQuestions * 3);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getWrongAnswersForPrepositionsQuizCursor(int numberOfQuestions) {
        DBHelperPrepositions dbHelperPrepositions = new DBHelperPrepositions(getContext());
        Cursor cursor = dbHelperPrepositions.getRandomWrongAnswerItemsForQuiz(numberOfQuestions * 3);
        cursor.moveToFirst();
        return cursor;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}
