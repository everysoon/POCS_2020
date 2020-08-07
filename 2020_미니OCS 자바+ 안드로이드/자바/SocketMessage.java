package com.example.a2020_miniocs_final;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import com.example.a2020_miniocs_final.Object.Medicine;
import com.example.a2020_miniocs_final.Object.Patient;

public class SocketMessage implements Serializable {
    // �޽��� Ÿ�� ����
    // NO_ACT�� ������ �� �ִ� Dummy �޽���. ������ ������ ����ϱ� ���� ����� ����
    // Serializable ��ü �� serialVersionUID�� �ٸ� �� �����Ƿ� ��������� ������ ��
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
        // contents�� ������ sql���� ActivityName�� �Ǻ��Ͽ� �ٸ��� �����Ϸ��� ���� ������
    	// �ȵ���̵�->�ڹ�
        this.type = t;
        this.activityName = activityName;
        this.sql = sql;
        this.contents = contents;
    }

    public SocketMessage(MsgType t,String sender,String contents,ArrayList<Patient> patientlist) {
    	// �ڹ� ->�ȵ���̵�
        this.patientlist = patientlist;
        this.type = t;
        this.sender = sender;
        this.contents = contents;
     
    }
    public SocketMessage(MsgType t,String activityName,ArrayList<Medicine> medicineList,String contents) {
    	// �ڹ� ->�ȵ���̵�
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
