package com.example.pocs_2020;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewPager_Fragment1 extends Fragment {
    TextView diseaseCode, treatDay;
    EditText diseaseName, memo, opinion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.viewpager_fragment1, container, false);
        diseaseCode = view.findViewById(R.id.diseaseCode_prescription);
        diseaseName = view.findViewById(R.id.diseaseName_prescription);
        treatDay = view.findViewById(R.id.treat_time_prescription);
        memo = view.findViewById(R.id.surgeryMemo_prescription);
        opinion = view.findViewById(R.id.surgeryOpinion_prescription);
        treatDay.setText(MiniOCSKey.todayCalcultate_yyyyMMdd());
        // 처방 적은걸 객체로 만들어서 Activity에 전달 (지금은 객체만드는것까지만)
        if(diseaseCode.getText().toString()!=null && diseaseName.getText().toString()!=null &&memo.getText().toString()!=null&&opinion.getText().toString()!=null&&treatDay.getText().toString()!=null) {
           /* Log.e("diseaseCode",Integer.parseInt(diseaseCode.getText().toString().trim())+"");
            Prescription prescription = new Prescription(new Disease(Integer.parseInt(diseaseCode.getText().toString()), diseaseName.getText().toString())
                    , memo.getText().toString(), opinion.getText().toString(), treatDay.getText().toString());*/
        }
        return view;
    }
}
