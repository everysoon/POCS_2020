package com.example.pocs_2020;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.pocs_2020.MiniOCS_Object.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SurgeryRegistrareActivity extends AppCompatActivity implements View.OnClickListener {

    Patient getPatient;
    TextView idText, nameText, sexText, ageText;
    Spinner doctorSpinner;
    EditText surgeryName, preSysmptom, postSysmptom, surgicalOpinion;
    Calendar cal = Calendar.getInstance();
    // 수술 시간 받을 변수
    String getYear,getMonth,getDay;  // Surgery클래스 reserve_time 으로 넣기
    String surgeryStartTime; // Surgery 클래스 surgery_start_time으로 넣기
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgery_registrare);

        getPatient = (Patient) getIntent().getSerializableExtra(MiniOCSKey.PATIENT);
        //xml 정의
        idText = findViewById(R.id.patient_idText_surgeryRegistrate);
        nameText = findViewById(R.id.patient_nameText_surgeryRegistrate);
        sexText = findViewById(R.id.patient_sexText_surgeryRegistrate);
        ageText = findViewById(R.id.patient_ageText_surgeryRegistrate);
        doctorSpinner = findViewById(R.id.op_surgery_DoctorSpinner);
        surgeryName = findViewById(R.id.op_surgeryName);
        preSysmptom = findViewById(R.id.op_Pre_sysmptom);
        postSysmptom = findViewById(R.id.op_Post_sysmptom);
        surgicalOpinion = findViewById(R.id.op_surgicalOpinion);
        //버튼 리스너 정의
        findViewById(R.id.op_daySelectButton).setOnClickListener(this);
        findViewById(R.id.op_timeSelectButton).setOnClickListener(this);
        findViewById(R.id.op_saveButton).setOnClickListener(this);
        findViewById(R.id.op_cancleButton).setOnClickListener(this);
        settingPatientInfo();
    }

    public void settingPatientInfo() {
        idText.setText(getPatient.getPatient_id()+"");
        nameText.setText(getPatient.getPatient_name());
        sexText.setText(getPatient.getPatient_sex());
        ageText.setText(getPatient.getPatient_age());

    }

    public void clickDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(SurgeryRegistrareActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Log.e("year",String.valueOf(year));
                Log.e("month",String.valueOf(month+1));
                Log.e("day",String.valueOf(day));
                getYear = String.valueOf(year);
                getMonth = String.valueOf(month+1);
                getDay = String.valueOf(day);
                Log.e("clickDatePicker?",getYear + getMonth + getDay);
            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        datePicker.getDatePicker().setMaxDate(new Date().getTime());
        datePicker.show();
    }

    public void clickTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(SurgeryRegistrareActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                Log.e("TimePicker",hour+"시"+min+"분");
                surgeryStartTime = hour+":"+min;
            }
        },cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true); // 마지막의 true는 시간을 24시간으로 보이겠다의 의미

        timePicker.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.op_daySelectButton:
                clickDatePicker();
                break;
            case R.id.op_timeSelectButton:
                clickTimePicker();
                break;
            case R.id.op_saveButton:
                // 저장하고 수술 대기 목록으로 넘어가기 (0)
                // 수술 접수한 환자는 대기 목록에서 제거! (0)
                ArrayList<MiniOCSKey.RecyclerViewItem> getWaitList = (ArrayList<MiniOCSKey.RecyclerViewItem>)getIntent().getSerializableExtra(MiniOCSKey.PATIENT_LIST);
                getWaitList.remove(getPatient);
                getPatient.getPrescription().getSurgery().setSurgery_start_time(surgeryStartTime);
                getPatient.getPrescription().getSurgery().setReserve_time(getYear+"-"+getMonth+"-"+getDay);
                Intent intent = new Intent(SurgeryRegistrareActivity.this,SurgeryActivity.class);
                intent.putExtra(MiniOCSKey.PATIENT_LIST,getWaitList);
                startActivity(intent);
                break;
            case R.id.op_cancleButton:
                finish();
                break;
        }
    }
}
