package com.example.pocs_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocs_2020.MiniOCS_Object.Disease;
import com.example.pocs_2020.MiniOCS_Object.Medicine;
import com.example.pocs_2020.MiniOCS_Object.Patient;
import com.example.pocs_2020.MiniOCS_Object.Prescription;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class PrescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView idText, nameText, sexText, ageText; //환자정보
    TextView BPtext, HRtext, BTtext, RESPtext;  //환자 vital정보
    TextView resultText; // 처방한 약들을 보여주는 Text
    Spinner classficationSpinner;
    RecyclerView MedicineRecyclerView;
    Button OPButton; // 수술지시버튼 -> 누르면 환자 정보 수술 접수 대기리스트로 전송

    // IP,PP,MP 에 해당하는 약 이름 arrays에서 받아옴
    String[] IPitems, PPitems, MPitems;
    ArrayList<MiniOCSKey.RecyclerViewItem> IPList, PPList, MPList;

    //RecyclerView에 들어갈 ArrayList 정의 -> 1.처방내역 2.환자대기리스트 3.약
    ArrayList<MiniOCSKey.RecyclerViewItem> ALLlist;
    ArrayList<MiniOCSKey.RecyclerViewItem> OldList;
    OCSAdapter medicineAdapter;

    //ViewPager에 사용될 변수들
    ViewPager viewPager;
    MyPagerAdapter viewPagerAdapter;
    CircleIndicator indicator;
    ViewPager_Fragment1 viewPagerFrag1;
    ViewPager_Fragment2 viewPagerFrag2;
    int pos;
    //Dialog로 받아오는 정보
    Patient getPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        //xml정의
        idText = findViewById(R.id.patient_idText_prescription);
        nameText = findViewById(R.id.patient_nameText_prescription);
        sexText = findViewById(R.id.patient_sexText_prescription);
        ageText = findViewById(R.id.patient_ageText_prescription);
        BPtext = findViewById(R.id.patient_BP_prescription);
        HRtext = findViewById(R.id.patient_HR_prescription);
        BTtext = findViewById(R.id.patient_BT_prescription);
        RESPtext = findViewById(R.id.patient_RESP_prescription);
        resultText = findViewById(R.id.resultText_prescription);
        classficationSpinner = findViewById(R.id.medicine_ClassficationSpinner_prescription);
        MedicineRecyclerView = findViewById(R.id.prescription_medicineRecyclerView);

        // 데이터 세팅 -> RecyclerView에 필요한 Medicine ArrayList 임의의 데이터 넣고, Intent로 환자정보객체 받아오기
        setMedicineData();
        getPatientInfo();

        // 버튼 이벤트 처리
        OPButton = findViewById(R.id.OP_moveButton_prescription);
        OPButton.setOnClickListener(this);
        findViewById(R.id.cancelButton_prescription).setOnClickListener(this);
        findViewById(R.id.saveButton_prescription).setOnClickListener(this);

        // 스피너설정
        String[] items = getResources().getStringArray(R.array.medicineClassfication);
        ArrayAdapter SpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items);
        classficationSpinner.setAdapter(SpinnerAdapter);
        classficationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 스피너 전환에 따라 RecyclerView 전환 코드 넣어주기
                String classfication = (String) classficationSpinner.getSelectedItem();
                if (classfication.equals(MiniOCSKey.PP)) {
                    medicineAdapter.upDateDateList(PPList);
                } else if (classfication.equals(MiniOCSKey.IP)) {
                    medicineAdapter.upDateDateList(IPList);
                } else if (classfication.equals(MiniOCSKey.MP)) {
                    medicineAdapter.upDateDateList(MPList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // RecyclerView 설정
        MedicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineAdapter = new OCSAdapter(PrescriptionActivity.this, MiniOCSKey.MEDICINE, PPList);
        MedicineRecyclerView.setAdapter(medicineAdapter);
        medicineAdapter.setOnItemClickListener(new OCSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Patient patient) {
                pos = position;
            }

            @Override
            public void onMedicineClick(View v, Medicine medicine) {
                // 클릭한 Medicine 객체 받아오기
                String prescriptionText = medicine.getMedicine_name() + " : " + medicine.getMedicinePrescription();
                String getResultText = resultText.getText().toString() + "\n";
                resultText.setText(getResultText.concat(prescriptionText));
            }

            @Override
            public void onItemBarcodeCheck(boolean check) {

            }
        });
        medicineAdapter.notifyDataSetChanged();
        MedicineRecyclerView.setHasFixedSize(true);

        //ViewPager설정 (0)
        viewPagerFrag1 = new ViewPager_Fragment1();
        viewPagerFrag2 = new ViewPager_Fragment2();
        viewPager = findViewById(R.id.viewPager_prescription);
        viewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        // 프래그먼트 생성뒤 프래그먼트2에게 전달 : 전달하기위한 Bundle 생성
        Bundle bundle = new Bundle();
        // 1. 환자 대기 리스트 : 인텐트로 환자 대기 명단 받음
        ALLlist = (ArrayList<MiniOCSKey.RecyclerViewItem>) getIntent().getSerializableExtra(MiniOCSKey.PATIENT_LIST);
        bundle.putSerializable(MiniOCSKey.PATIENT_LIST, ALLlist);
        bundle.putSerializable(MiniOCSKey.CLICK_PATIENT_POSITON, pos);
        // 2. 환자의 예전 의료기록
        // 환자의 ID를 이용해 예전 의료기록 검색해 viewPager_fragment2에 전달 : DB사용
        // 현재는 getPatientInfo()메소드를 사용해 임의의 데이터 넣어놓음 : OldList
        bundle.putSerializable(MiniOCSKey.OLD_RECORD, OldList);
        viewPagerAdapter.setBundle(bundle);

    }

    public void setMedicineData() {
        //MPitems = getResources().getStringArray(R.array.); MP 준비중
        // ** 약 정보 + MP 정보 찾아서 MEDICINE 하나로 묶어 객체로만들기
        IPList = new ArrayList<>();
        PPList = new ArrayList<>();
        MPList = new ArrayList<>();

        //medicine_recyclerView에 들어갈 MP,IP,PP에 따른 약 데이터 추가하는 메소드
        IPitems = getResources().getStringArray(R.array.individual_prescription);
        PPitems = getResources().getStringArray(R.array.predefine_prescription);

        //임의로 데이터 넣어놓음
        for (int i = 0; i < PPitems.length; i++) {
            PPList.add(new Medicine(PPitems[i], "약설명-구성성분", (int)Math.random()*150+1, "POW"));
        }
        for (int i = 0; i < IPitems.length; i++) {
            IPList.add(new Medicine(IPitems[i], "약설명-구성성분", i, "TAB"));
        }
    }

    public void getPatientInfo() {

        getPatient = (Patient) getIntent().getSerializableExtra(MiniOCSKey.PATIENT);
        idText.setText(String.valueOf(getPatient.getPatient_id()));
        nameText.setText(getPatient.getPatient_name());
        sexText.setText(getPatient.getPatient_sex());
        ageText.setText(getPatient.getPatient_age());

        BPtext.setText(getPatient.getVital().getBP());
        HRtext.setText(getPatient.getVital().getHR());
        BTtext.setText(getPatient.getVital().getBT());
        RESPtext.setText(getPatient.getVital().getRESP());
        // 예전의료기록은 임의의 값을 집어 넣음 : 추후 DB에서 얻어옴
        OldList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OldList.add(new Prescription(new Disease(i, "명칭" + i), "memo" + i, "소견" + i, MiniOCSKey.todayCalcultate_yyyyMMdd()));
        }


    }

    @Override
    protected void onResume() {
        //ActivityThread : Performing stop of activity that is not resumed
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent Infointent;
        Intent goConsultation = new Intent(PrescriptionActivity.this, ConsultationRoomActivity.class);
        int id = view.getId();
        switch (id) {
            case R.id.OP_moveButton_prescription:
                // 수술 대기 리스트에 환자 추가하고 토스트로 알려주기
                Infointent = new Intent(PrescriptionActivity.this, SurgeryActivity.class);
                Infointent.putExtra(MiniOCSKey.PATIENT, getPatient);
                finish();
                ALLlist.remove(getPatient);
                goConsultation.putExtra(MiniOCSKey.UPDATE_ALLLIST, ALLlist);
                startActivity(goConsultation);
                break;
            case R.id.cancelButton_prescription:
                resultText.setText("");
                break;
            case R.id.saveButton_prescription:
                //처방 저장
                getPatient.getPrescription().getMedicine().setMedicinePrescription(resultText.getText().toString());
                // 약국대기리스트로 넘기기
                Infointent = new Intent(PrescriptionActivity.this, PharmacyActivity.class);
                Infointent.putExtra(MiniOCSKey.PATIENT, getPatient);
                Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                ALLlist.remove(getPatient);
                goConsultation.putExtra(MiniOCSKey.UPDATE_ALLLIST, ALLlist);
                startActivity(goConsultation);
                break;
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEM = 2;
        ViewPager_Fragment1 viewPagerFragment1;
        ViewPager_Fragment2 viewPagerFragment2;
        Bundle bundle;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    viewPagerFragment1 = new ViewPager_Fragment1();
                    return viewPagerFragment1;
                case 1:
                    viewPagerFragment2 = new ViewPager_Fragment2();
                    viewPagerFragment2.setArguments(bundle);
                    return viewPagerFragment2;
                default:
                    return null;
            }

        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }

        @Override
        public int getCount() {
            return NUM_ITEM;
        }

    }
}
