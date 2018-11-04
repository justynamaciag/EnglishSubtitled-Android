package com.justyna.englishsubtitled.games;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;
import com.justyna.englishsubtitled.utilities.WordButtonsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ABCDFragment extends Fragment implements WordButtonsAdapter.customButtonListener {

    OnDataPass dataPasser;
    List<Translation> translations;
    TextView wordTextView;
    Random rand = new Random();
    Translation currentTranslation;
    View view;
    boolean finishLessonSuccess = true;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_abcd, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            translations = (List<Translation>) bundle.getSerializable("translations");
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }
        List<Button> buttonList = prepareGame();
        callGame(buttonList);

        return view;
    }

    private void callGame(List<Button> buttonList){

        ListView listView = (ListView) view.findViewById(R.id.abcdListView);
        WordButtonsAdapter a = new WordButtonsAdapter(getContext(), buttonList);
        a.setCustomButtonListner(ABCDFragment.this);
        listView.setAdapter(a);

        Drawable background = view.getBackground();
        listView.setDivider(background);
    }

    public List<Button> prepareGame() {

        String wordPL = currentTranslation.getPlWord();
        wordTextView = view.findViewById(R.id.wordTextView);
        wordTextView.setText(currentTranslation.getEngWord());

        HashSet<String> buttonSet = new HashSet<>();
        buttonSet.add(wordPL);
        while (buttonSet.size() < 4)
            buttonSet.add(getRandomTranslation().getPlWord());

        List<Button> buttonList = new ArrayList<>();
        for (String b : buttonSet) {
            Button btn = new Button(getContext());
            btn.setText(b);
            buttonList.add(btn);
        }

        Collections.shuffle(buttonList);

        return buttonList;

    }

    private Translation getRandomTranslation() {
        int random = rand.nextInt(translations.size());
        return translations.get(random);
    }

    @Override
    public void onButtonClickListner(Button b) {

        String clicked = String.valueOf(b.getText());
        if (clicked.equals(currentTranslation.getPlWord())) {
            Toast.makeText(view.getContext(), "Great!", Toast.LENGTH_SHORT).show();
            passData(finishLessonSuccess);
        }
    }

    public void passData(boolean data) {
        dataPasser.onDataPass(data);
    }

    public interface OnDataPass {
        void onDataPass(boolean data);
    }

}
