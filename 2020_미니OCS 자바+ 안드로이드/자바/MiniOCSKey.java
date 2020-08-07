package com.example.a2020_miniocs_final;

public class MiniOCSKey {
	// 개발에 사용되는 변수를 관리하는 MiniOCSKey 클래스
	
    public static final long serialVersionUID = 8358731843579430211L;
    public static final String driverName = "com.mysql.jdbc.Driver";
    public static final String url ="jdbc:mysql://localhost/Android?useUnicode=true&characterEncoding=utf8";
    // jdbc:mysql://localhost/db이름
   	//?useUnicode=true&characterEncoding=utf8 는 mysql로 데이터 입력할 때 한글 깨지는 거 방지 utf8로 인코딩!
    public static final String usrID ="root";
    public static final String userPassword ="0610";
    public static final String java ="JAVA";

    // activityName
    public static final String DIAGNOSIS_ACTIVITY = "DiagnosisActivity";
    public static final String PRESCRIPTION_ACTIVITY = "PrescriptionActivity";
    public static final String MEDICINE_SETUP = "medicine_setup";
    public static final String OLD_RECORD = "oldRecord";
    public static final String MEDICINE_PP = "PP";
    public static final String MEDICINE_IP = "IP";
    public static final String MEDICINE_MP = "MP";
    // deptName
    public static final String OS = "정형외과";
    public static final String ENT = "이비인후과";
    public static final String MG = "내과";
    public static final String PD = "소아과";
    public static final String GS = "외과";
    public static final String EY = "안과";
    public static final String ALL = "ALL";
    
    public static class RecyclerViewItem{}
}
