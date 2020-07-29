package com.example.pocs_2020;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ScheduleActivity extends AppCompatActivity {
    Spinner surgeryRoomSpineer;
    ArrayAdapter DeptAdapter;


    ScheduleFragment MGfrag = new ScheduleFragment(MiniOCSKey.MG);//내과
    ScheduleFragment GSfrag = new ScheduleFragment(MiniOCSKey.GS);//외과
    ScheduleFragment EYfrag = new ScheduleFragment(MiniOCSKey.EY);//안과
    ScheduleFragment PDfrag = new ScheduleFragment(MiniOCSKey.PD);//소아과
    ScheduleFragment ENTfrag = new ScheduleFragment(MiniOCSKey.ENT);//이비인후과
    ScheduleFragment OSfrag = new ScheduleFragment(MiniOCSKey.OS);//정형외과

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 없애기
        setContentView(R.layout.activity_schedule);

        //스피너 정의
        surgeryRoomSpineer = findViewById(R.id.surgeryRoomNumber_Spinner);
        String[] depts = getResources().getStringArray(R.array.dept);
        // 0 번인 전체 원소 지우려고 List변환 뒤 지워준 뒤 다시 String[]로 변환
        List<String> list = new ArrayList<String>();
        Collections.addAll(list,depts);
        list.remove("전체");
        depts = list.toArray(new String[list.size()]);

        DeptAdapter = new ArrayAdapter(ScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item, depts);
        surgeryRoomSpineer.setAdapter(DeptAdapter);

        findViewById(R.id.custom_surgery_cancleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //프래그먼트 세팅

        changeFragment( 0);
        surgeryRoomSpineer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String getDept = (String) surgeryRoomSpineer.getSelectedItem();
                if (getDept.equals(MiniOCSKey.OS)) {
                    //정형외과
                    changeFragment( 0);
                } else if (getDept.equals(MiniOCSKey.MG)) {
                    //내과
                    changeFragment(1);
                } else if (getDept.equals(MiniOCSKey.ENT)) {
                    //이비인후과
                    changeFragment( 2);
                } else if (getDept.equals(MiniOCSKey.EY)) {
                    // 안과
                    changeFragment( 3);
                } else if (getDept.equals(MiniOCSKey.GS)) {
                    //외과
                    changeFragment( 4);
                } else if (getDept.equals(MiniOCSKey.PD)) {
                    //소아과
                    changeFragment(5);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void changeFragment(int number) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        switch (number) {
            case 0:
                trans.replace(R.id.schedule_fragment, OSfrag).commit();
                break;
            case 1:
                trans.replace(R.id.schedule_fragment, MGfrag).commit();
                break;
            case 2:
                trans.replace(R.id.schedule_fragment, ENTfrag).commit();
                break;
            case 3:
                trans.replace(R.id.schedule_fragment, EYfrag).commit();
                break;
            case 4:
                trans.replace(R.id.schedule_fragment, GSfrag).commit();
                break;
            case 5:
                trans.replace(R.id.schedule_fragment, PDfrag).commit();
        }
    }

}
