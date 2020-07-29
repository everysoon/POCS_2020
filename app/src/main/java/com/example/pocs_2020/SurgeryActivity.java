package com.example.pocs_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pocs_2020.MiniOCS_Object.Medicine;
import com.example.pocs_2020.MiniOCS_Object.Patient;
import com.example.pocs_2020.MiniOCS_Object.Prescription;

import java.util.ArrayList;

public class SurgeryActivity extends AppCompatActivity implements View.OnClickListener {

    EditText barcodeText;
    RecyclerView waitListView;
    ArrayList<MiniOCSKey.RecyclerViewItem> waitList;
    OCSAdapter adapter;
    Boolean barcodeCheck;
    // 선택되면 보내질 변수 - 환자객체
    Patient clickPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgery);

        waitList = new ArrayList<>();
        // xml정의
        findViewById(R.id.refreshButton_surgery).setOnClickListener(this);
        findViewById(R.id.schedule_surgery).setOnClickListener(this);
        barcodeText = findViewById(R.id.patient_idText_surgery);
        waitListView = findViewById(R.id.patient_waitList_surgery);

        Patient getPatient = (Patient) getIntent().getSerializableExtra(MiniOCSKey.PATIENT);
        if (getPatient != null) {
            Log.e("Surgery_getPatient", getPatient.toString());
            waitList.add(getPatient);
        } else if(getIntent().getSerializableExtra(MiniOCSKey.PATIENT_LIST)!= null){
            waitList = (ArrayList<MiniOCSKey.RecyclerViewItem>) getIntent().getSerializableExtra(MiniOCSKey.PATIENT_LIST);
        } else{
            //현재 임의의 데이터 넣어놓음
            waitList.add(new Patient(1, "박민선", "여", "24", new Prescription("외과")));
            waitList.add(new Patient(2, "ㅇㅇㅇ", "여", "24", new Prescription("내과")));
            waitList.add(new Patient(3, "ㅁㅁㅁ", "여", "24", new Prescription("정형외과")));
        }

        // RecyclerView 설정
        waitListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OCSAdapter(SurgeryActivity.this, MiniOCSKey.SURGERY, waitList);
        waitListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OCSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Patient getPatient) {
                clickPatient = getPatient;
            }

            @Override
            public void onMedicineClick(View v, Medicine medicine) {
            }

            @Override
            public void onItemBarcodeCheck(boolean check) {
                barcodeCheck = check;
                barcodeText.requestFocus();
                barcode();
            }
        });
        adapter.notifyDataSetChanged();
        waitListView.setHasFixedSize(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void barcode() {
        MiniOCSKey.waitDialog(SurgeryActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (barcodeCheck) {
                        if (!barcodeText.getText().toString().matches("")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (String.valueOf(clickPatient.getPatient_id()) != null) {
                                        if (String.valueOf(clickPatient.getPatient_id()).equals(barcodeText.getText().toString())) {
                                            MiniOCSKey.startDialog();
                                            //id가 일치하면
                                            Intent intent = new Intent(SurgeryActivity.this, SurgeryRegistrareActivity.class);
                                            intent.putExtra(MiniOCSKey.PATIENT, clickPatient);
                                            intent.putExtra(MiniOCSKey.PATIENT_LIST,waitList);
                                            startActivity(intent);
                                            barcodeCheck = false;
                                            Thread.currentThread().interrupt();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "환자의 ID를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.e("getPatientID NULL!", "getPatientID NULL!");
                                    }
                                }

                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.refreshButton_surgery:
                adapter.notifyDataSetChanged();
                break;
            case R.id.schedule_surgery:
                startActivity(new Intent(SurgeryActivity.this, ScheduleActivity.class));
                break;
        }
    }
}
