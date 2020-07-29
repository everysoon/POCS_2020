package com.example.pocs_2020.MiniOCS_Object;

import java.io.Serializable;

public class Vital implements Serializable {

    int vital_id;
    String BP;
    String BT;
    String RESP;
    String HR;

    String Time; //vital 측정 시간

    public Vital() {
    }

    public int getVital_id() {
        return vital_id;
    }

    public void setVital_id(int vital_id) {
        this.vital_id = vital_id;
    }

    public String getBP() {
        return BP;
    }

    public void setBP(String BP) {
        this.BP = BP;
    }

    public String getBT() {
        return BT;
    }

    public void setBT(String BT) {
        this.BT = BT;
    }

    public String getRESP() {
        return RESP;
    }

    public void setRESP(String RESP) {
        this.RESP = RESP;
    }

    public String getHR() {
        return HR;
    }

    public void setHR(String HR) {
        this.HR = HR;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
