package com.example.pocs_2020;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScheduleFragment extends Fragment {

    String deptName;

    int[] textViewId = {
            R.id.today_9, R.id.tomorrow_9, R.id.later_9,
            R.id.today_10, R.id.tomorrow_10, R.id.later_10,
            R.id.today_11, R.id.tomorrow_11, R.id.later_11,
            R.id.today_12, R.id.tomorrow_12, R.id.later_12,
            R.id.today_13, R.id.tomorrow_13, R.id.later_13,
            R.id.today_14, R.id.tomorrow_14, R.id.later_14,
            R.id.today_15, R.id.tomorrow_15, R.id.later_15,
            R.id.today_16, R.id.tomorrow_16, R.id.later_16
    };

    TextView[] textViews = new TextView[24];

    public ScheduleFragment(String deptName) {
        this.deptName = deptName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        for (int i = 0; i < textViewId.length; i++) {
            textViews[i] = view.findViewById(textViewId[i]);
        }
        //test
        if (deptName.equals(MiniOCSKey.OS)) {
            //정형외과
            textViews[0].setText("순정형외과");
            textViews[0].setBackgroundColor(Color.YELLOW);
            textViews[0].setTextColor(Color.BLACK);
            textViews[0].setTextSize(20);
        } else if (deptName.equals(MiniOCSKey.MG)) {
            //내과
            textViews[2].setText("순내과");
            textViews[2].setBackgroundColor(Color.YELLOW);
            textViews[2].setTextColor(Color.BLACK);
            textViews[2].setTextSize(20);
        } else if (deptName.equals(MiniOCSKey.ENT)) {
            //이비인후과
            textViews[3].setText("순이빈후과");
            textViews[3].setBackgroundColor(Color.YELLOW);
            textViews[3].setTextColor(Color.BLACK);
            textViews[3].setTextSize(20);
        } else if (deptName.equals(MiniOCSKey.EY)) {
            // 안과
            textViews[1].setText("순안과");
            textViews[1].setBackgroundColor(Color.YELLOW);
            textViews[1].setTextColor(Color.BLACK);
            textViews[1].setTextSize(20);
        } else if (deptName.equals(MiniOCSKey.GS)) {
            //외과
            textViews[4].setText("순외과");
            textViews[4].setBackgroundColor(Color.YELLOW);
            textViews[4].setTextColor(Color.BLACK);
            textViews[4].setTextSize(20);
        } else if (deptName.equals(MiniOCSKey.PD)) {
            //소아과
            textViews[6].setText("순소아과");
            textViews[6].setBackgroundColor(Color.YELLOW);
            textViews[6].setTextColor(Color.BLACK);
            textViews[6].setTextSize(20);
        }

        return view;
    }
}
