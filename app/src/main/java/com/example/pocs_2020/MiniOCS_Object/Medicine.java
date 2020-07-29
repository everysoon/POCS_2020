package com.example.pocs_2020.MiniOCS_Object;

import com.example.pocs_2020.MiniOCSKey;

import java.io.Serializable;

public class Medicine extends MiniOCSKey.RecyclerViewItem implements Serializable {

    int medicine_id;
    String medicine_name;
    String medicine_classfication; // 약 분류 (PP,IP,MP)
    String medicine_composition; // 약 구성 성분
    String medicine_type;
    int total_amount; // 전체 수량
    int stock_count;  // 재고량

    // 약 처방할 때 얼마나 처방하는지 String으로 저장
    String medicinePrescription;

    public Medicine(){}
    //임의의 생성자
    public Medicine(String medicine_name,String medicine_composition,int stock_count,String medicine_type){
        this.medicine_name= medicine_name;
        this.medicine_composition = medicine_composition;
        this.stock_count = stock_count;
        this.medicine_type =medicine_type;
    }

    public Medicine(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicinePrescription() {
        return medicinePrescription;
    }

    public void setMedicinePrescription(String medicinePrescription) {
        this.medicinePrescription = medicinePrescription;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_classfication() {
        return medicine_classfication;
    }

    public void setMedicine_classfication(String medicine_classfication) {
        this.medicine_classfication = medicine_classfication;
    }

    public String getMedicine_composition() {
        return medicine_composition;
    }

    public void setMedicine_composition(String medicine_composition) {
        this.medicine_composition = medicine_composition;
    }

    public String getMedicine_type() {
        return medicine_type;
    }

    public void setMedicine_type(String medicine_type) {
        this.medicine_type = medicine_type;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getStock_count() {
        return stock_count;
    }

    public void setStock_count(int stock_count) {
        this.stock_count = stock_count;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicine_name='" + medicine_name + '\'' +
                ", medicine_composition='" + medicine_composition + '\'' +
                ", medicine_type='" + medicine_type + '\'' +
                ", stock_count=" + stock_count +
                '}';
    }
}
