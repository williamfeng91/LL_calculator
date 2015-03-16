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
 * Event fragment
 */
public class EventFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventFragment newInstance(int sectionNumber) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        final RadioButton event_macaron = (RadioButton)rootView.findViewById(R.id.event_macaron);
        final RadioButton event_score_match = (RadioButton)rootView.findViewById(R.id.event_score_match);

        final EditText rankVal = (EditText)rootView.findViewById(R.id.rankVal);
        final EditText expVal = (EditText)rootView.findViewById(R.id.currExpVal);
        final EditText ptsVal = (EditText)rootView.findViewById(R.id.eventPtsVal);
        final TextView macaronLabel = (TextView)rootView.findViewById(R.id.eventMacaronLabel);
        final EditText macaronVal = (EditText)rootView.findViewById(R.id.eventMacaronVal);

        final TextView result1Label = (TextView)rootView.findViewById(R.id.eventResult1Label);
        result1Label.setVisibility(View.GONE);
        final TextView result1Val = (TextView)rootView.findViewById(R.id.eventResult1Val);
        result1Val.setVisibility(View.GONE);
        final TextView result2Label = (TextView)rootView.findViewById(R.id.eventResult2Label);
        result2Label.setVisibility(View.GONE);
        final TextView result2Val = (TextView)rootView.findViewById(R.id.eventResult2Val);
        result2Val.setVisibility(View.GONE);

        event_macaron.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                macaronLabel.setVisibility(View.VISIBLE);
                macaronVal.setVisibility(View.VISIBLE);
            }
        });
        event_score_match.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                macaronLabel.setVisibility(View.GONE);
                macaronVal.setVisibility(View.GONE);
            }
        });

        Button resetButton = (Button)rootView.findViewById(R.id.reset);
        resetButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankVal.setText("");
                expVal.setText("");
                ptsVal.setText("");
                macaronVal.setText("");
                result1Label.setVisibility(View.GONE);
                result1Val.setVisibility(View.GONE);
                result2Label.setVisibility(View.GONE);
                result2Val.setVisibility(View.GONE);
                hideKeyboard();
            }
        });
        Button calculateButton = (Button)rootView.findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currRank, currExp, currPts, currMacaron;

                if (rankVal.getText().length() <= 0) {
                    currRank = 0;
                } else {
                    currRank = Integer.parseInt(rankVal.getText().toString());
                }
                if (expVal.getText().length() <= 0) {
                    currExp = 0;
                } else {
                    currExp = Integer.parseInt(expVal.getText().toString());
                }
                if (ptsVal.getText().length() <= 0) {
                    currPts = 0;
                } else {
                    currPts = Integer.parseInt(ptsVal.getText().toString());
                }
                if (macaronVal.getText().length() <= 0) {
                    currMacaron = 0;
                } else {
                    currMacaron = Integer.parseInt(macaronVal.getText().toString());
                }

                if (false == validateInput(currRank, currExp, currPts, currMacaron)) {
                    return;
                }

                int exp_to_next_rank = Rank.maxRankExp[currRank] - currExp;
                int goalPts;
                int no_of_plays_needed = 0;
                int no_of_plays_needed2 = 0;
                int LP_needed = 0;
                int avgScore;
                int songDifficulty = 25;
                int songExp = 83;
                int songMacaron = 27;
                int songPts = 75;
                if (true == event_macaron.isChecked()) {
                    goalPts = 11000;
                    avgScore = 393;
                    // Solve:
                    // songMacaron * no_of_plays_needed + avgScore * no_of_plays_needed2 = ceil(goalPts - currPts)
                    // songPts * no_of_plays_needed2 = floor(currMacaron + songMacaron * no_of_plays_needed)
                    int x1 = (int) Math.ceil((goalPts - currPts - songMacaron * no_of_plays_needed) / avgScore);
                    int x2 = (currMacaron + songMacaron * no_of_plays_needed) / songPts;
                    while (x1 > x2) {
                        no_of_plays_needed++;
                        x1 = (int) Math.ceil(1.0 * (goalPts - currPts - songMacaron * no_of_plays_needed) / avgScore);
                        x2 = (currMacaron + songMacaron * no_of_plays_needed) / songPts;
                    }
                    no_of_plays_needed2 = (currMacaron + songMacaron * no_of_plays_needed) / songPts;
                    result1Val.setText(Integer.toString(no_of_plays_needed) + " + " + Integer.toString(no_of_plays_needed2));
                } else if (true == event_score_match.isChecked()) {
                    goalPts = 25000;
                    avgScore = 350;
                    no_of_plays_needed = (int) Math.ceil((goalPts - currPts) * 1.0 / avgScore);
                    result1Val.setText(Integer.toString(no_of_plays_needed));
                }
                LP_needed = no_of_plays_needed * songDifficulty;
                int gainedExp = (no_of_plays_needed + no_of_plays_needed2) * songExp;
                int i = 0;
                while (gainedExp >= exp_to_next_rank) {
                    gainedExp -= exp_to_next_rank;
                    exp_to_next_rank = Rank.maxRankExp[currRank + i];
                    LP_needed -= Rank.maxLP[currRank + i + 1];
                    i++;
                }
                result1Label.setVisibility(View.VISIBLE);
                result1Val.setVisibility(View.VISIBLE);
                result2Label.setVisibility(View.VISIBLE);
                result2Val.setText(Integer.toString(LP_needed));
                result2Val.setVisibility(View.VISIBLE);
                hideKeyboard();
            }

            private boolean validateInput(int rank, int exp, int pts, int macaron) {
                if (rank <= 0) {
                    Toast.makeText(getActivity(), getString(R.string.incorrect_rank),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (exp < 0 || exp > Rank.maxRankExp[rank]) {
                    Toast.makeText(getActivity(), getString(R.string.incorrect_rank_exp),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (pts < 0) {
                    Toast.makeText(getActivity(), getString(R.string.event_incorrect_pts),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (true == event_macaron.isChecked()) {
                    if (macaron < 0) {
                        Toast.makeText(getActivity(), getString(R.string.event_incorrect_macaron),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
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