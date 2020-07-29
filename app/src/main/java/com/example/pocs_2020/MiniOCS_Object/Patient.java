package com.example.pocs_2020.MiniOCS_Object;

import com.example.pocs_2020.MiniOCSKey;

import java.io.Serializable;
import java.util.ArrayList;

public class Patient extends MiniOCSKey.RecyclerViewItem implements Serializable {

    int patient_id;
    String patient_name;
    String patient_age;
    String patient_sex;
    String dateOfBirth;
    int patient_barcode;

    Prescription prescription = new Prescription();
    Vital vital = new Vital();
    // 처방 약 이름, tablet,divided,day 모아놓은 ArrayList -> PharmacyActivity에서 사용
    ArrayList<MiniOCSKey.RecyclerViewItem> medicinePrescriptionList =new ArrayList<>();

    public ArrayList<MiniOCSKey.RecyclerViewItem> getMedicinePrescriptionList() {
        return medicinePrescriptionList;
    }

    public void addMedicinePrescriptionList(Prescription prescriptionItem) {
        this.medicinePrescriptionList.add(prescriptionItem);
    }

    public void setMedicinePrescriptionList(ArrayList<MiniOCSKey.RecyclerViewItem> medicinePrescriptionList) {
        this.medicinePrescriptionList = medicinePrescriptionList;
    }

    public Patient(){

    }
    public Patient(int patient_id,String patient_name,String patient_sex,String patient_age){
        this.patient_id = patient_id;
        this.patient_name=patient_name;
        this.patient_sex = patient_sex;
        this.patient_age = patient_age;

    }
    public Patient(int patient_id,String patient_name,String patient_sex,String patient_age,Prescription prescription){
        this.patient_id = patient_id;
        this.patient_name=patient_name;
        this.patient_sex = patient_sex;
        this.patient_age = patient_age;
        this.prescription = prescription;

    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatient_sex() {
        return patient_sex;
    }

    public void setPatient_sex(String patient_sex) {
        this.patient_sex = patient_sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getPatient_barcode() {
        return patient_barcode;
    }

    public void setPatient_barcode(int patient_barcode) {
        this.patient_barcode = patient_barcode;
    }

    public Vital getVital() {
        return vital;
    }

    public void setVital(Vital vital) {
        this.vital = vital;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patient_id=" + patient_id +
                ", patient_name='" + patient_name + '\'' +
                ", patient_age='" + patient_age + '\'' +
                ", patient_sex='" + patient_sex + '\'' +
                '}';
    }
}
