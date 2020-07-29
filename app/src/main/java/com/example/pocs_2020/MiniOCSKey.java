package com.example.pocs_2020;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MiniOCSKey {
    public static ProgressDialog oDialog;
    /**
     * PD : PeDiatrics(소아과) , OS : Ortho Surgery (정형외과) ,
     * GS : General Surgery(일반 외과) , ENT : Ear,Nose & Throat (이비인후과)
     * EY : EYe (안과)  , MG : Medicus Greatus (내과)
     */

    //    intent Message
    public static final String PATIENT = "patient";
    public static final String PATIENTLIST_CONSULTATION = "patientlist_consultation";
    public static final String PATIENTLIST_PRESCRIPTION = "patientlist_prescription";
    public static final String PHARMACY = "pharmacy";
    public static final String SURGERY = "surgery";
    public static final String MEDICINE = "medicine";
    public static final String PRESCRIPTION = "prescription";
    public static final String PATIENT_LIST = "patientList";
    public static final String OLD_RECORD = "oldRecord";
    public static final String CLICK_PATIENT_POSITON = "click_patient_position";
    public static final String UPDATE_ALLLIST = "update_allList";
    public static final String MEDICNE_PRESCRIPTION = "medicine_prescription";


    //    dept
    public static final String OS = "정형외과";
    public static final String ENT = "이비인후과";
    public static final String MG = "내과";
    public static final String PD = "소아과";
    public static final String GS = "외과";
    public static final String EY = "안과";
    public static final String ALL = "전체";

    //    Adapter
    public static final int PATIENTLIST_CONSULTATION_KEY = 0;
    public static final int PRESCRIPTION_KEY = 1;
    public static final int PHARMACY_KEY = 2;
    public static final int OLDRECORD_KEY = 3;
    public static final int MEDICINE_KEY = 4;
    public static final int PATIENTLIST_PRESCRIPTION_KEY = 5;
    public static final int MEDICINE_PRESCRIPTION_KEY = 6;

    //Spinner
    public static final String PP = "Pre-define Prescription[ PP ]";
    public static final String IP = "Individual Prescription[ IP ]";
    public static final String MP = "Miscellaneous Prescription[ MP ]";


    public static class RecyclerViewItem {
    }

    static public String todayCalcultate_yyyyMMddEE() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일", Locale.getDefault()); // yyyy-MM-dd HH:mm:ss
        String today = dateFormat.format(currentTime);
        return today;
    }

    static public String todayCalcultate_yyyyMMdd() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String today = dateFormat.format(currentTime);
        return today;
    }

    static public String todayCalcultate_HHmmss() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH시:mm분:ss초", Locale.getDefault());
        String today = dateFormat.format(currentTime);
        return today;
    }
   static public void waitDialog(Context mContext){
        oDialog = new ProgressDialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        oDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        oDialog.setMessage("잠시만 기다려 주세요.");
    }
    static public void startDialog(){
        oDialog.show();
    }
}
