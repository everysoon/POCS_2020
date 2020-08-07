package com.example.a2020_miniocs_final;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import com.example.a2020_miniocs_final.Object.Medicine;
import com.example.a2020_miniocs_final.Object.Patient;

public class SocketMessage implements Serializable {
    // 메시지 타입 정의
    // NO_ACT는 무시할 수 있는 Dummy 메시지. 디버깅용 등으로 사용하기 위해 만들어 놓음
    // Serializable 객체 간 serialVersionUID가 다를 수 있으므로 명시적으로 선언해 줌
    private static final long serialVersionUID =  MiniOCSKey.serialVersionUID;
    public enum MsgType {NO_ACT, OPEN, CLOSE, SELECT, INSERT, UPDATE,RETURN_VALUE};

    private MsgType type;
    private String sql;
    private String sender;
    private String activityName;
    private String contents;
    Patient patient;
    ArrayList<Patient> patientlist;
    ArrayList<Medicine> medicineList;
    private Vector<MiniOCSKey.RecyclerViewItem> returnValue;
    public SocketMessage() {
        this(MsgType.NO_ACT, "","");
    }

    public SocketMessage(MsgType t,String sender, String sql) {
        this.type = t;
        this.sender = sender;
        this.sql = sql;
    }

    public SocketMessage(MsgType t, String sender,String activityName, String sql, String contents) {
        // contents로 보내온 sql문을 ActivityName을 판별하여 다르게 수행하려고 만든 생성자
    	// 안드로이드->자바
        this.type = t;
        this.activityName = activityName;
        this.sql = sql;
        this.contents = contents;
    }

    public SocketMessage(MsgType t,String sender,String contents,ArrayList<Patient> patientlist) {
    	// 자바 ->안드로이드
        this.patientlist = patientlist;
        this.type = t;
        this.sender = sender;
        this.contents = contents;
     
    }
    public SocketMessage(MsgType t,String activityName,ArrayList<Medicine> medicineList,String contents) {
    	// 자바 ->안드로이드
        this.medicineList = medicineList;
        this.type = t;
        this.activityName = activityName;
        this.contents = contents;
    }

    public ArrayList<Patient> getPatientlist() {
		return patientlist;
	}

	public void setPatientlist(ArrayList<Patient> patientlist) {
		this.patientlist = patientlist;
	}

	public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setType(MsgType t) {
        type = t;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public MsgType getType() {
        return type;
    }

    public Vector<MiniOCSKey.RecyclerViewItem> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Vector<MiniOCSKey.RecyclerViewItem> returnValue) {
        this.returnValue = returnValue;
    }


    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

 
	public ArrayList<Medicine> getMedicineList() {
		return medicineList;
	}

	public void setMedicineList(ArrayList<Medicine> medicineList) {
		this.medicineList = medicineList;
	}

	@Override
    public String toString() {
        return "SocketMessage [type=" + type + ", sql=" + sql + ", sender=" + sender + ", activityName=" + activityName
                + ", contents=" + contents + "]";
    }


}
