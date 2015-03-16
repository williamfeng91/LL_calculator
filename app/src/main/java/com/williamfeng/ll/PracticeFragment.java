package com.williamfeng.ll;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Practice fragment
 */
public class PracticeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PracticeFragment newInstance(int sectionNumber) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PracticeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_practice, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        final RadioButton rarityN = (RadioButton)rootView.findViewById(R.id.rarity_N);
        final RadioButton rarityR = (RadioButton)rootView.findViewById(R.id.rarity_R);
        final RadioButton raritySR = (RadioButton)rootView.findViewById(R.id.rarity_SR);
        final RadioButton rarityUR = (RadioButton)rootView.findViewById(R.id.rarity_UR);

        final RadioButton idolized_true = (RadioButton)rootView.findViewById(R.id.idolized_true);
        final RadioButton idolized_false = (RadioButton)rootView.findViewById(R.id.idolized_false);

        final EditText levelVal = (EditText)rootView.findViewById(R.id.lvVal);
        final EditText expVal = (EditText)rootView.findViewById(R.id.expVal);

        final TextView resultLabel = (TextView)rootView.findViewById(R.id.resultLabel);
        resultLabel.setVisibility(View.GONE);
        final TextView resultVal = (TextView)rootView.findViewById(R.id.resultVal);
        resultVal.setVisibility(View.GONE);

        Button resetButton = (Button)rootView.findViewById(R.id.reset);
        resetButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                rarityN.setChecked(true);
                rarityR.setChecked(false);
                raritySR.setChecked(false);
                rarityUR.setChecked(false);
                idolized_true.setChecked(true);
                idolized_false.setChecked(false);
                levelVal.setText("");
                expVal.setText("");
                resultLabel.setVisibility(View.GONE);
                resultVal.setVisibility(View.GONE);
                hideKeyboard();
            }
        });
        Button calculateButton = (Button)rootView.findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rarity rarity = getRarity();
                int[] maxExp = getMaxExp(rarity);
                int currLv, currExp;

                if (levelVal.getText().length() <= 0) {
                    currLv = 1;
                } else {
                    currLv = Integer.parseInt(levelVal.getText().toString());
                }
                if (expVal.getText().length() <= 0) {
                    currExp = maxExp[currLv];
                } else {
                    currExp = Integer.parseInt(expVal.getText().toString());
                }

                if (false == validateInput(rarity, currLv, currExp, maxExp[currLv])) {
                    return;
                }

                int expNeeded = currExp;
                for (int i = currLv + 1; i < MaxLevel.maxLevelMap.get(rarity); i++) {
                    expNeeded += maxExp[i];
                }
                resultLabel.setVisibility(View.VISIBLE);
                resultVal.setText(Integer.toString(expNeeded));
                resultVal.setVisibility(View.VISIBLE);
                hideKeyboard();
            }

            private boolean validateInput(Rarity rarity, int level, int exp, int maxExp) {
                if (null == rarity) {
                    return false;
                }
                if (level <= 0 || level >= MaxLevel.maxLevelMap.get(rarity)) {
                    Toast.makeText(getActivity(), getString(R.string.practice_incorrect_level),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (exp <= 0 || exp > maxExp) {
                    Toast.makeText(getActivity(), getString(R.string.practice_incorrect_exp),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

            public Rarity getRarity() {
                if (true == rarityN.isChecked()) {
                    if (true == idolized_false.isChecked()) {
                        return Rarity.N;
                    } else {
                        return Rarity.N_PLUS;
                    }
                } else if (true == rarityR.isChecked()) {
                    if (true == idolized_false.isChecked()) {
                        return Rarity.R;
                    } else {
                        return Rarity.R_PLUS;
                    }
                } else if (true == raritySR.isChecked()) {
                    if (true == idolized_false.isChecked()) {
                        return Rarity.SR;
                    } else {
                        return Rarity.SR_PLUS;
                    }
                } else if (true == rarityUR.isChecked()) {
                    if (true == idolized_false.isChecked()) {
                        return Rarity.UR;
                    } else {
                        return Rarity.UR_PLUS;
                    }
                } else {
                    return null;
                }
            }

            private int[] getMaxExp(Rarity rarity) {
                switch (rarity) {
                    case N:
                    case N_PLUS:
                        return RequiredExp.N;
                    case R:
                    case R_PLUS:
                        return RequiredExp.R;
                    case SR:
                    case SR_PLUS:
                        return RequiredExp.SR;
                    case UR:
                    case UR_PLUS:
                        return RequiredExp.UR;
                    default:
                        return null;
                }
            }
        });
        return rootView;
    }
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}