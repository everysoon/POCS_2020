package com.example.pocs_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocs_2020.MiniOCS_Object.Patient;

import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RegistrateActivity extends AppCompatActivity implements View.OnClickListener {

    //dateOfBirth는 생년월일
    TextView idText, nameText, todayText, ageText;
    Spinner yearSpinner, monthSpinner, daySpinner;
    Spinner deptSpinner, doctorSpinner, officeSpinner;
    RadioButton RadiocheckedButton;
    RadioGroup sexGroup;

    //스피너 설정을 위한 String[]배열 -> res/layout/values/arrays에 정의된 문자열 배열을 가져와서 사용
    String[] years, months, days, depts, doctors, offices;
    // 환자 생년월일을 받을 변수 세개
    String DOB_year, DOB_month, DOB_day;
    // 환자 진료 부서를 저장 할 Prescription 변수
    Patient newPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);

        //    xml정의
        idText = findViewById(R.id.patient_idText_registrate);
        nameText = findViewById(R.id.patient_nameText_registrate);
        todayText = findViewById(R.id.todayText_registrate);
        ageText = findViewById(R.id.ageText_registrate);
        todayText.setText(MiniOCSKey.todayCalcultate_yyyyMMddEE());
        //    RadioButton - > AppCompatRadioButton : 라디오버튼에 색깔주기위해(buttonTint) AppCompat꺼 사용
        sexGroup = findViewById(R.id.sexGroup);
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadiocheckedButton = (RadioButton) findViewById(sexGroup.getCheckedRadioButtonId());
            }
        });

        //    스피너 설정
        deptSpinner = findViewById(R.id.deptSpinner_registrate);
        doctorSpinner = findViewById(R.id.doctorSpinner_registrate);
        officeSpinner = findViewById(R.id.officeSpinner_registrate);

        yearSpinner = findViewById(R.id.yearspinner_registrate);
        monthSpinner = findViewById(R.id.monthspinner_registrate);
        daySpinner = findViewById(R.id.dayspinner_registrate);

        //    스피너에 들어갈 데이터 배열은 res/layout/values/arrays에 정의하고 가져옴
        depts = getResources().getStringArray(R.array.dept);
        months = getResources().getStringArray(R.array.month);
        days = getResources().getStringArray(R.array.day);
        years = getResources().getStringArray(R.array.year);
        doctors = getResources().getStringArray(R.array.doctor);
        offices = getResources().getStringArray(R.array.office);
        //  arrays에 배열에 년도를 역순정렬하기위해 뒤집어줌
        List<String> list = Arrays.asList(years);
        Collections.reverse(list);
        years = list.toArray(new String[list.size()]);
        //    스피너 Adapter 설정
        ArrayAdapter DeptAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, depts);
        deptSpinner.setAdapter(DeptAdapter);
        ArrayAdapter YearAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years);
        yearSpinner.setAdapter(YearAdapter);
        ArrayAdapter MonthAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthSpinner.setAdapter(MonthAdapter);
        ArrayAdapter DaysAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days);
        daySpinner.setAdapter(DaysAdapter);
        ArrayAdapter DoctorsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, doctors);
        doctorSpinner.setAdapter(DoctorsAdapter);
        ArrayAdapter OfficesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, offices);
        officeSpinner.setAdapter(OfficesAdapter);
        // yearSpinner 를 이용해 자동 나이 계산을 위해 이벤트 설정
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectYear = (String) yearSpinner.getSelectedItem();
                DOB_year = selectYear;
                int age = 2020 - Integer.parseInt(selectYear.trim()) + 1;
                ageText.setText(String.valueOf(age));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "생년월일을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        //    버튼 이벤트 설정
        findViewById(R.id.barcodeButton_registrate).setOnClickListener(this);
        findViewById(R.id.registrateButton_registrate).setOnClickListener(this);

    }

    public void createPatientEntry() {
        newPatient = new Patient();
        newPatient.setPatient_name(nameText.getText().toString());
        newPatient.setPatient_id(Integer.parseInt(idText.getText().toString()));
        newPatient.setPatient_sex(RadiocheckedButton.getText().toString());
        newPatient.setPatient_age(ageText.getText().toString());
        DOB_month = (String) monthSpinner.getSelectedItem();
        DOB_day = (String) daySpinner.getSelectedItem();
        newPatient.setDateOfBirth(DOB_year+"-"+DOB_month+"-"+DOB_day);
        newPatient.getPrescription().setTreat_time(todayText.getText().toString());
        newPatient.getPrescription().setDept((String) deptSpinner.getSelectedItem());
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.barcodeButton_registrate:
                // 1.바코드리더기 읽어서 ID 설정 -> idText로 포커스 옮긴 뒤 바코드 딸깍
                idText.requestFocus();
                // 2. 설정된 ID idText에 setText하기
                break;
            case R.id.registrateButton_registrate:
                // 1.환자 정보 저장하는 코드 넣기 -> 데이터베이스
                // 2.대기리스트에 환자 정보 넘겨주기 (0)
                // 3. 접수 완료되면 토스트메세지띄우고 HomeActivity로 이동 (0)
                Toast.makeText(this, "접수가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                createPatientEntry(); // 접수되면 환자 객체로 만들어 정보 저장  -> 추후 데이터베이스에 그대로 넘겨주기
                try{
                    Log.e("환자보내기","insert");
                    ObjectOutputStream writer = MiniOCSKey.writer;
                    MiniOCSKey.sockInit();
                    String sql = "INSERT INTO patient VALUES ("+
                            newPatient.getPatient_id()+",'"+newPatient.getPatient_name()+"','"+newPatient.getPatient_sex()+"',"+
                            newPatient.getPatient_age()+",'"+newPatient.getDateOfBirth()+"')";
                    writer.writeObject(new AndroidConnMessage(AndroidConnMessage.MsgType.INSERT,sql));
                    Log.e("sql?",sql);
                    MiniOCSKey.sockClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(this,ConsultationRoomActivity.class);
                intent.putExtra(MiniOCSKey.PATIENT,newPatient);
                startActivity(intent);
                finish();
                break;
        }
    }
}
