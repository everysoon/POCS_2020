package com.example.a2020_miniocs_final;

public class MiniOCSKey {
	// ���߿� ���Ǵ� ������ �����ϴ� MiniOCSKey Ŭ����
	
    public static final long serialVersionUID = 8358731843579430211L;
    public static final String driverName = "com.mysql.jdbc.Driver";
    public static final String url ="jdbc:mysql://localhost/Android?useUnicode=true&characterEncoding=utf8";
    // jdbc:mysql://localhost/db�̸�
   	//?useUnicode=true&characterEncoding=utf8 �� mysql�� ������ �Է��� �� �ѱ� ������ �� ���� utf8�� ���ڵ�!
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
    public static final String OS = "�����ܰ�";
    public static final String ENT = "�̺����İ�";
    public static final String MG = "����";
    public static final String PD = "�Ҿư�";
    public static final String GS = "�ܰ�";
    public static final String EY = "�Ȱ�";
    public static final String ALL = "ALL";
    
    public static class RecyclerViewItem{}
}
