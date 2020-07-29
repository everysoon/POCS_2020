package com.example.pocs_2020.MiniOCS_Object;

import com.example.pocs_2020.MiniOCS_Object.Doctor;

import java.io.Serializable;

public class Surgery implements Serializable {

    int surgery_id;
    String pre_Sysmptom; //수술 전 증상
    String post_Sysmptom; // 수술 후 증상
    String surgery_name; // 수술 이름
    String surgery_opinion; // 수술 의사 소견
    String surgery_memo; // 수술 메모
    String reserve_time; // 수술 예약 시간 : yyyy-mm-dd 형식으로 String으로 저장 함
    String surgery_start_time; // 수술 시작 시간
    String surgery_duration;
    //수술을 담당한 의사
    Doctor doctor;

    public int getSurgery_id() {
        return surgery_id;
    }

    public void setSurgery_id(int surgery_id) {
        this.surgery_id = surgery_id;
    }

    public String getPre_Sysmptom() {
        return pre_Sysmptom;
    }

    public void setPre_Sysmptom(String pre_Sysmptom) {
        this.pre_Sysmptom = pre_Sysmptom;
    }

    public String getPost_Sysmptom() {
        return post_Sysmptom;
    }

    public void setPost_Sysmptom(String post_Sysmptom) {
        this.post_Sysmptom = post_Sysmptom;
    }

    public String getSurgery_name() {
        return surgery_name;
    }

    public void setSurgery_name(String surgery_name) {
        this.surgery_name = surgery_name;
    }

    public String getSurgery_opinion() {
        return surgery_opinion;
    }

    public void setSurgery_opinion(String surgery_opinion) {
        surgery_opinion = surgery_opinion;
    }

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String getSurgery_start_time() {
        return surgery_start_time;
    }

    public void setSurgery_start_time(String surgery_start_time) {
        this.surgery_start_time = surgery_start_time;
    }

    public String getSurgery_duration() {
        return surgery_duration;
    }

    public void setSurgery_duration(String surgery_duration) {
        this.surgery_duration = surgery_duration;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getSurgery_memo() {
        return surgery_memo;
    }

    public void setSurgery_memo(String surgery_memo) {
        this.surgery_memo = surgery_memo;
    }
}
