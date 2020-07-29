package com.example.pocs_2020;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pocs_2020.MiniOCS_Object.Medicine;
import com.example.pocs_2020.MiniOCS_Object.Patient;

import java.util.ArrayList;


public class CustomDialog implements View.OnClickListener {

    Context mContext;
    Dialog dig;
    //MedicineDialog 위젯
    EditText tabletText, dividedText, dayText;
    String medicinePrescription = "";


    public interface OnSetupListener {
        void onMedicinePrescription(String medicinePrescription);

        void onVitalCheck(Patient vitalPatient);
    }

    OnSetupListener mListener = null;

    public void setOnSetupListener(OnSetupListener listener) {
        this.mListener = listener;
    }

    public CustomDialog(Context mContext) {
        this.mContext = mContext;
    }

    //VitalDialog 위젯
    EditText BPtext, HRtext, BTtext, Resptext;
    Patient vitalPatient;
    //PharmacyDialog 위젯
    TextView patientName;
    RecyclerView prescriptionView;
    OCSAdapter adapter;

    //MedicineDialog
    public void setupMedicineDialog(Medicine medicine) {

        //커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성
        dig = new Dialog(mContext);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE); // 액티비티의 타이틀바를 숨김
        dig.setContentView(R.layout.customdialog_medicine_prescription); // 커스텀 다이얼로그의 레이아웃을 정의
        dig.show(); // 보여주기 !

        // 커스텀 다이얼로그의 xml 위젯 정의
        dig.findViewById(R.id.customDialog_OKButton).setOnClickListener(this);
        dig.findViewById(R.id.customDialog_cancelButton).setOnClickListener(this);
        TextView medicineNameText = dig.findViewById(R.id.customDialog_medicineName);
        tabletText = dig.findViewById(R.id.customDialog_medicine_Type);
        dividedText = dig.findViewById(R.id.customDialog_medicine_divided);
        dayText = dig.findViewById(R.id.customDialog_medicine_TakingDay);

        medicineNameText.setText(medicine.getMedicine_name());


    }

    //VitalDialog
    public void setupVitalDialog(Patient patient) {
        vitalPatient = patient;
        //커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성
        dig = new Dialog(mContext);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE); // 액티비티의 타이틀바를 숨김
        dig.setContentView(R.layout.customdialog_vital); // 커스텀 다이얼로그의 레이아웃을 정의
        dig.show(); // 보여주기 !
        // xml 위젯 정의
        dig.findViewById(R.id.custom_vital_saveButton).setOnClickListener(this);
        dig.findViewById(R.id.custom_vital_cancleButton).setOnClickListener(this);
        TextView patient_nameText = dig.findViewById(R.id.custom_vital_patient_name);
        patient_nameText.setText(vitalPatient.getPatient_name());
        BPtext = dig.findViewById(R.id.custom_vital_BP);
        BTtext = dig.findViewById(R.id.custom_vital_BT);
        HRtext = dig.findViewById(R.id.custom_vital_HR);
        Resptext = dig.findViewById(R.id.custom_vital_Resp);

    }

    //PharmacyDialog
    public void setupPharmacyDialog(Context mContext,Patient patient, ArrayList<MiniOCSKey.RecyclerViewItem> prescriptionItems) {
        //커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성
        dig = new Dialog(mContext);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE); // 액티비티의 타이틀바를 숨김
        dig.setContentView(R.layout.customdialog_medicine_pharmacy); // 커스텀 다이얼로그의 레이아웃을 정의
        dig.show(); // 보여주기

        //xml정의
        dig.findViewById(R.id.custom_pharmacy_saveButton).setOnClickListener(this); // 저장 버튼
        dig.findViewById(R.id.custom_pharmacy_cancleButton).setOnClickListener(this); // 취소 버튼
        dig.findViewById(R.id.custom_pharmacy_modificationButton).setOnClickListener(this); // 수정 버튼
        patientName = dig.findViewById(R.id.custom_pharmacy_patinet_name);
        patientName.setText(patient.getPatient_name());
        prescriptionView = dig.findViewById(R.id.custom_pharmacy_prescriptionView);
        adapter = new OCSAdapter(mContext,MiniOCSKey.MEDICNE_PRESCRIPTION,prescriptionItems);
        prescriptionView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.customDialog_OKButton:
                // MedicineDialogButton
                // ok버튼을 누르면 처방 묶어서 String으로 전달
                String tablet = tabletText.getText().toString();
                String divided = dividedText.getText().toString();
                String day = dayText.getText().toString();
                medicinePrescription = tablet + "T" + divided + "#" + day + "Day";
                mListener.onMedicinePrescription(medicinePrescription);
                dig.dismiss(); //다이얼로그 종료
                break;
            case R.id.customDialog_cancelButton:
                // MedicineDialogButton
                medicinePrescription = "";
                dig.dismiss();
                break;
            case R.id.custom_surgery_cancleButton:
                // SurgeryScheduleButton
                dig.dismiss();
                break;
            case R.id.custom_vital_saveButton:
                // VitalDialogButton
                Log.e("saveButton", "saveButton");
                vitalPatient.getVital().setBP(BPtext.getText().toString());
                vitalPatient.getVital().setHR(HRtext.getText().toString());
                vitalPatient.getVital().setBT(BTtext.getText().toString());
                vitalPatient.getVital().setRESP(Resptext.getText().toString());
                mListener.onVitalCheck(vitalPatient);
                dig.dismiss();
                break;
            case R.id.custom_vital_cancleButton:
                // VitalDialogButton
                dig.dismiss();
                break;
            case R.id.custom_pharmacy_modificationButton:
                // 수정 어떻게 ?..

                break;
            case R.id.custom_pharmacy_cancleButton:
                dig.dismiss();
                break;
            case R.id.custom_pharmacy_saveButton:
                // 저장누르면 모든 정보 저장
                break;
        }
    }

}
