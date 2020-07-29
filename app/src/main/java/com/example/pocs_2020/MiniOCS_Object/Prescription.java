package com.example.pocs_2020.MiniOCS_Object;

import com.example.pocs_2020.MiniOCSKey;

import java.io.Serializable;

public class Prescription extends MiniOCSKey.RecyclerViewItem implements Serializable  {

    // 처방관련클래스
    int prescription_id;
    String prescription_opinion; // 처방 관련 의사 소견
    String prescription_memo; // 처방 관련 의사 메모사항
    String dept; //어느 부서에서 처방했는지
    // give_out_status -> 약 처방 여부 ( 나중에 환자의 예전 의료기록을 불러올 때 처방(Yes)된 약만 불러오기 위해 필요한 변수 )
    boolean give_out_status;
    String treat_time; // 처방한 날짜,시간을 받아옴
    /** medicine  - tablet, divied, day -> 논문 그림 9번 다이얼로그의
        약을 몇일(medicine_day)동안 몇개의 약을( medicine_tablet) 몇번 나누어 (medicine_divided) 섭취할 것인지 처방할 때 필요한 변수들
     */
    int medicine_tablet;
    int medicine_divided;
    int medicine_day; //

    // 처방한 약 , 질병, 의사, 수술에 대한 클래스
    Medicine medicine = new Medicine();
    Disease disease = new Disease();
    Doctor doctor = new Doctor();
    Surgery surgery = new Surgery();


    // 생성자
    public Prescription(){}
    public Prescription(String dept ){
        this.dept = dept;
    }
    public Prescription(Disease disease,String prescription_memo,String prescription_opinion,String treat_time){
        this.disease = disease;
        this.prescription_memo = prescription_memo;
        this.prescription_opinion = prescription_opinion;
        this.treat_time = treat_time;
    }

    public Prescription(int medicine_tablet, int medicine_divided, int medicine_day, Medicine medicine) {
        this.medicine_tablet = medicine_tablet;
        this.medicine_divided = medicine_divided;
        this.medicine_day = medicine_day;
        this.medicine = medicine;
    }

    //Getter AND Setter
    public Surgery getSurgery() {
        return surgery;
    }

    public void setSurgery(Surgery surgery) {
        this.surgery = surgery;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public String getTreat_time() {
        return treat_time;
    }

    public void setTreat_time(String treat_time) {
        this.treat_time = treat_time;
    }

    public String getPrescription_opinion() {
        return prescription_opinion;
    }

    public void setPrescription_opinion(String prescription_opinion) {
        this.prescription_opinion = prescription_opinion;
    }

    public String getPrescription_memo() {
        return prescription_memo;
    }

    public void setPrescription_memo(String prescription_memo) {
        this.prescription_memo = prescription_memo;
    }


    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(int prescription_id) {
        this.prescription_id = prescription_id;
    }



    public boolean isGive_out_status() {
        return give_out_status;
    }

    public void setGive_out_status(boolean give_out_status) {
        this.give_out_status = give_out_status;
    }

    public int getMedicine_tablet() {
        return medicine_tablet;
    }

    public void setMedicine_tablet(int medicine_tablet) {
        this.medicine_tablet = medicine_tablet;
    }

    public int getMedicine_divided() {
        return medicine_divided;
    }

    public void setMedicine_divided(int medicine_divided) {
        this.medicine_divided = medicine_divided;
    }

    public int getMedicine_day() {
        return medicine_day;
    }

    public void setMedicine_day(int medicine_day) {
        this.medicine_day = medicine_day;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    @Override
    public String toString() {
        return medicine.getMedicine_name()+":"+medicine_tablet +
                "T" + medicine_divided +
                "#" + medicine_day +
                "Day";

    }
}
