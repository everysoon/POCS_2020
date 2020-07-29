package com.example.pocs_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocs_2020.MiniOCS_Object.Medicine;
import com.example.pocs_2020.MiniOCS_Object.Patient;
import com.example.pocs_2020.MiniOCS_Object.Prescription;

import java.util.ArrayList;

public class PharmacyActivity extends AppCompatActivity {

    RecyclerView waitListView;
    OCSAdapter adapter;
    ArrayList<MiniOCSKey.RecyclerViewItem> waitList;
    ArrayList<MiniOCSKey.RecyclerViewItem> prescriptionList;
    Patient clickPatient;
    Boolean barcodeCheck;
    TextView barcodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        Patient getPatient = (Patient) getIntent().getSerializableExtra(MiniOCSKey.PATIENT);
        if (getPatient != null)
            Log.e("Pharmacy_getPatient", getPatient.toString());
        waitList = new ArrayList<>();
        prescriptionList = new ArrayList<>();
        //임의의 데이터 넣어놓음
        waitList.add(new Patient(1, "박민선", "여", "24", new Prescription(3, 3, 3, new Medicine("G1"))));
        waitList.add(new Patient(2, "영희", "여", "24", new Prescription(2, 2, 2, new Medicine("PS1"))));
        waitList.add(new Patient(3, "철수", "남", "24", new Prescription(2, 3, 1, new Medicine("G1"))));
        prescriptionList.add(new Prescription(3,3,3,new Medicine("G1")));
        prescriptionList.add(new Prescription(3,3,3,new Medicine("G2")));
        prescriptionList.add(new Prescription(2,2,2,new Medicine("PS1")));
        //xml정의
        barcodeText = findViewById(R.id.patient_idText_pharmacy);
        Button refreshButton = findViewById(R.id.refreshButton_pharmacy);
        waitListView = findViewById(R.id.patient_waitList_pharmacy);
        // RecyclerView 설정
        waitListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OCSAdapter(PharmacyActivity.this, MiniOCSKey.PHARMACY, waitList);
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
        //버튼 클릭리스너 정의
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void barcode() {
        MiniOCSKey.waitDialog(PharmacyActivity.this);
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
                                            CustomDialog PharmacyDialog = new CustomDialog(PharmacyActivity.this);
                                            PharmacyDialog.setupPharmacyDialog(PharmacyActivity.this,clickPatient, prescriptionList);
                                            waitList.remove(clickPatient);
                                            barcodeCheck = false;
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

}
