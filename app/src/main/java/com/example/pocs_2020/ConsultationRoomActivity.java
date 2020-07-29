package com.example.pocs_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pocs_2020.MiniOCS_Object.Medicine;
import com.example.pocs_2020.MiniOCS_Object.Patient;
import com.example.pocs_2020.MiniOCS_Object.Prescription;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ConsultationRoomActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner deptSpinner;
    String getDept;
    String getPatientID;
    EditText idText;
    Patient clickPatient = new Patient();
    /**
     * PD : PeDiatrics(소아과) , OS : Ortho Surgery (정형외과) ,
     * GS : General Surgery(일반 외과) , ENT : Ear,Nose & Throat (이비인후과)
     * EY : EYe (안과)  , MG : Medicus Greatus (내과)
     */
    ArrayList<MiniOCSKey.RecyclerViewItem> ALLlist = new ArrayList<>();
    ArrayList<MiniOCSKey.RecyclerViewItem> PDlist = new ArrayList<>();
    ArrayList<MiniOCSKey.RecyclerViewItem> OSlist = new ArrayList<>();
    ArrayList<MiniOCSKey.RecyclerViewItem> GSlist = new ArrayList<>();
    ArrayList<MiniOCSKey.RecyclerViewItem> ENTlist = new ArrayList<>();
    ArrayList<MiniOCSKey.RecyclerViewItem> EYlist = new ArrayList<>();
    ArrayList<MiniOCSKey.RecyclerViewItem> MGlist = new ArrayList<>();
    RecyclerView patientRecyclerView;

    Patient getPatient;
    OCSAdapter adapter;
    /**
     * vital Dialog를 이용해 vital을 체크한 환자들은 콜백리스너를 통해 vital체크했다고 true를 리턴함
     * ->  vitalCheck 변수로 받아 스레드의 while 루프를 진행 또는 정지를 관리할 변수 : vitalCheck
     */
    Boolean vitalCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_room);

        deptSpinner = findViewById(R.id.deptSpinner_consultation);
        findViewById(R.id.refreshButton_consultation).setOnClickListener(this);
        patientRecyclerView = findViewById(R.id.patient_waitList_consultation);
        idText = findViewById(R.id.patient_idText_consultation);

        //스피너 설정
        String[] items = getResources().getStringArray(R.array.dept);
        ArrayAdapter SpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items);
        deptSpinner.setAdapter(SpinnerAdapter);
        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 스피너로 부서 변경될 때마다 아래의 환자 recyclerView 변하도록 코딩하기
                getDept = (String) deptSpinner.getSelectedItem();
                if (getDept.equals(MiniOCSKey.ALL)) {
                    adapter.upDateDateList(ALLlist);
                    //전체
                } else if (getDept.equals(MiniOCSKey.OS)) {
                    adapter.upDateDateList(OSlist);
                    //정형외과
                } else if (getDept.equals(MiniOCSKey.MG)) {
                    adapter.upDateDateList(MGlist);
                    //내과
                } else if (getDept.equals(MiniOCSKey.ENT)) {
                    adapter.upDateDateList(ENTlist);
                    //이비인후과
                } else if (getDept.equals(MiniOCSKey.EY)) {
                    adapter.upDateDateList(EYlist);
                    // 안과
                } else if (getDept.equals(MiniOCSKey.GS)) {
                    //외과
                    adapter.upDateDateList(GSlist);
                } else if (getDept.equals(MiniOCSKey.PD)) {
                    adapter.upDateDateList(PDlist);
                    //소아과
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // setPatientDate();

        //RecyclerView 설정
        if (getIntent().getSerializableExtra(MiniOCSKey.UPDATE_ALLLIST) != null)
            ALLlist = (ArrayList<MiniOCSKey.RecyclerViewItem>) getIntent().getSerializableExtra(MiniOCSKey.UPDATE_ALLLIST);
        else {
            ALLlist = new ArrayList<>();
           // TemporaryData();
        }
        patientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OCSAdapter(ConsultationRoomActivity.this, MiniOCSKey.PATIENTLIST_CONSULTATION, ALLlist);
        patientRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OCSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Patient getPatient) {
                getPatientID = String.valueOf(getPatient.getPatient_id());
                clickPatient = getPatient;
            }

            @Override
            public void onMedicineClick(View v, Medicine medicine) {
            }

            @Override
            public void onItemBarcodeCheck(boolean check) {
                vitalCheck = check;
                barcode();
                idText.requestFocus();
            }
        });
        adapter.notifyDataSetChanged();
        patientRecyclerView.setHasFixedSize(true);

    }

    public void SqlData() {
        ALLlist.add(new Patient(1, "홍길동", "남", "25", new Prescription("정형외과")));
        ALLlist.add(new Patient(2, "복순이", "여", "24", new Prescription("외과")));
        ALLlist.add(new Patient(3, "무궁화", "남", "23", new Prescription("내과")));
        ALLlist.add(new Patient(4, "장미", "여", "22", new Prescription("안과")));
        ALLlist.add(new Patient(5, "백합", "남", "21", new Prescription("이비인후과")));

    }

    //수정하기 -> 접수화면에서 보낸 intent로 환자정보받기 오류
    public void setPatientDate() {
        /*
         *  접수화면에서 보낸 intent로 환자 정보 받기
         *  접수 할 때 환자의 진료부서에 따라 각 다른 리스트에 추가
         *  -> 스피너 변화 할 때마다 recyclerView에 들어갈 리스트 변경
         */
        getPatient = (Patient) getIntent().getSerializableExtra(MiniOCSKey.PATIENT);
        Log.e("getPatient", String.valueOf(getPatient));
        String whatdept = getPatient.getPrescription().getDept();
        if (getDept != null) {
            if (whatdept != null) {
                if (getDept.equals(MiniOCSKey.OS)) {
                    //정형외과
                    OSlist.add(getPatient);
                    Log.e("addOS", "addOs");
                } else if (getDept.equals(MiniOCSKey.MG)) {
                    //내과
                    MGlist.add(getPatient);
                    Log.e("addMG", "addMG");
                } else if (getDept.equals(MiniOCSKey.ENT)) {
                    //이비인후과
                    ENTlist.add(getPatient);
                    Log.e("addENT", "addENT");
                } else if (getDept.equals(MiniOCSKey.EY)) {
                    // 안과
                    EYlist.add(getPatient);
                    Log.e("addEY", "addEY");
                } else if (getDept.equals(MiniOCSKey.GS)) {
                    //외과
                    GSlist.add(getPatient);
                    Log.e("addGS", "addGS");
                } else if (getDept.equals(MiniOCSKey.PD)) {
                    //소아과
                    PDlist.add(getPatient);
                    Log.e("addPD", "addPD");
                }
                //전체 리스트에는 항상 추가
                ALLlist.add(getPatient);
                Log.e("addAll", "addAll");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("conResume", "conResume");
    }

    public void barcode() {
        MiniOCSKey.waitDialog(ConsultationRoomActivity.this);
        idText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String barcode = editable.toString();
                if (getPatientID != null) {
                    //patientID가 null이 아니고
                    if (vitalCheck) {
                        // vital 데이터를 입력한 환자이고
                        if (getPatientID.equals(idText.getText().toString())) {
                            // idText에 바코드 값이 들어오고,  id가 일치하면 !
                            // 화면전환
                            MiniOCSKey.startDialog();
                            Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
                            intent.putExtra(MiniOCSKey.PATIENT, clickPatient);
                            intent.putExtra(MiniOCSKey.PATIENT_LIST, ALLlist);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "환자의 ID를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("getPatientID NULL!", "getPatientID NULL!");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.refreshButton_consultation:
                //새로고침 버튼 누를 때 recyclerView 갱신 이벤트 추가
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
