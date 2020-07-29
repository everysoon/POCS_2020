package com.example.pocs_2020;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPager_Fragment2 extends Fragment {
    RecyclerView PatientWaitRecyclerView,OldMedicalRecordRecyclerView;
    OCSAdapter  waitAdapter, oldAdapter;
    ArrayList<MiniOCSKey.RecyclerViewItem> ALLlist ,OldList;
    int position;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_fragment2,container,false);
        PatientWaitRecyclerView = view.findViewById(R.id.patient_waitList_prescription);
        OldMedicalRecordRecyclerView = view.findViewById(R.id.patient_oldMedicalrecord_prescription);
        // 1.환자 대기리스트 RecyclerView
        Bundle bundle = getArguments();
        ALLlist = (ArrayList<MiniOCSKey.RecyclerViewItem>)bundle.getSerializable(MiniOCSKey.PATIENT_LIST);
        position = (int)bundle.getInt(MiniOCSKey.CLICK_PATIENT_POSITON);
        ALLlist.remove(position); //선택 된 환자 대기 리스트에서 제거!
        PatientWaitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        waitAdapter = new OCSAdapter(getContext(),MiniOCSKey.PATIENTLIST_PRESCRIPTION, ALLlist);
        PatientWaitRecyclerView.setAdapter(waitAdapter);
        waitAdapter.notifyDataSetChanged();
        // 3. 환자 예전 처방 리스트 RecyclerView
        OldList = (ArrayList<MiniOCSKey.RecyclerViewItem>)bundle.getSerializable(MiniOCSKey.OLD_RECORD);
        OldMedicalRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        oldAdapter = new OCSAdapter(getContext(),MiniOCSKey.OLD_RECORD,OldList);
        OldMedicalRecordRecyclerView.setAdapter(oldAdapter);
        oldAdapter.notifyDataSetChanged();
        return view;
    }
}
