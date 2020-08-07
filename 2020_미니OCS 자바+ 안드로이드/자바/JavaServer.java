package com.example.a2020_miniocs_final;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.example.a2020_miniocs_final.MiniOCSKey.RecyclerViewItem;
import com.example.a2020_miniocs_final.Object.Medicine;
import com.example.a2020_miniocs_final.Object.Patient;

public class JavaServer {
	// MYSQL url, userID,userPassword ����� ����
	public static String driverName = MiniOCSKey.driverName;
	public static String url = MiniOCSKey.url;
	public static String id = MiniOCSKey.usrID; // user ID
	public static String password = MiniOCSKey.userPassword; // user password

	// ���� ������ �� �����Ҵ� �� �ʿ��ϸ� PreparedStatement, �����Ҵ� �ʿ������ Statement ��ü ���
	// ���� ����� ������ executeQuery() ������ ResultSet�� ���, ���� ��������� executeUpdate()�޼��� ȣ���Ͽ�
	// int�� ������ ��� �� �Ҵ�
	public Connection conn = null; // DB������ Connection ��ü��������
	public Statement st = null; // ���� ������ ��ü Statement
	public PreparedStatement pst = null; // �����Ҵ�! �������� ?�� ���ε��ؼ� ��� ����
	public ResultSet rs = null; // ���� ��� ������ ResultSet

	int count = 0; // sql�� ���� �� ������ ���� row���� count�ϱ� ���� ����
	Socket clientSock;

	public static void main(String[] args) {
		JavaServer server = new JavaServer();
		server.dbConn();
		server.socketConn();
	}

	// ���� �޼����κ����� �۾��� MYSQL�� �����Ͽ� ���� ��
	public void dbConn() {
		try {

			Class.forName(driverName); // 1. ����̹� �ε�
			conn = DriverManager.getConnection(url, id, password); // 2. mysql�� �����ϱ�

		} catch (ClassNotFoundException cnfe) {
			System.out.println("JDBC ����̹� Ŭ������ ã�� �� �����ϴ� : " + cnfe.getMessage());
		} catch (Exception ex) {
			System.out.println("DB ���� ���� : " + ex.getMessage());
		}
	}

	// �ȵ���̵�� Socket ����� ���� ����/�޼��� ó��
	public void socketConn() {
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			while (true) {
				System.out.println("�����");
				clientSock = serverSock.accept();
				// Ŭ���̾�Ʈ�� ���� ����� ��Ʈ�� �� ������ ����
				Thread t = new Thread(new ClientHandler(clientSock));
				t.start();
				System.out.println("Server : Ŭ���̾�Ʈ ���� ��");
			}
		} catch (Exception e) {
			System.out.println("Server : Ŭ���̾�Ʈ ���� �� �̻�߻�");
			e.printStackTrace();
		}
	}

	// Client�� 1:1 �����ϴ� �޽��� ���� ������
	private class ClientHandler implements Runnable {
		Socket sock; // Ŭ���̾�Ʈ ����� ����
		ObjectInputStream reader; // Ŭ���̾�Ʈ�� ���� �����ϱ� ���� ��Ʈ��
		ObjectOutputStream writer; // Ŭ���̾�Ʈ�� �۽��ϱ� ���� ��Ʈ��

		// ������. Ŭ���̾�Ʈ���� ���Ͽ��� �б�� ���� ��Ʈ�� ����� ��
		// ��Ʈ���� ���鶧 InputStream�� ���� ����� Hang��. �׷��� OutputStream���� �������.
		// �̰��� Ŭ���̾�Ʈ���� InpitStreams�� ���� ����� ������ �ȱ׷��� �����
		public ClientHandler(Socket clientSock) {
			try {
				sock = clientSock;
				writer = new ObjectOutputStream(clientSock.getOutputStream());
				reader = new ObjectInputStream(clientSock.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// Ŭ���̾�Ʈ���� ���� �޼����� ���� �����ϴ� �۾��� ����
		public synchronized void run() {

			SocketMessage message = null;
			SocketMessage.MsgType type = null;
			try {
				while (true) {
					// ���� �޼����� ������ ���� ���� �� ���� ������ ����.
					message = (SocketMessage) reader.readObject();
					System.out.println(message.toString()+"??");
					type = message.getType();
					if (type == SocketMessage.MsgType.OPEN) {
						System.out.println(message.getSql());
					} else if (type == SocketMessage.MsgType.CLOSE) {
						System.out.println(message.getSql());
						writer.close();
						reader.close();
						sock.close();
					} else if (type == SocketMessage.MsgType.INSERT) {
						dbInsert(message.getSql());
					} else if (type == SocketMessage.MsgType.SELECT) {
						dbSelect(message.getSql(), message.getActivityName(),writer,message.getContents());
					} else if (type == SocketMessage.MsgType.NO_ACT) {
						System.out.println("Server : NO_ACT!");
						continue;
					} else {
						new Exception("Server : Ŭ���̾�Ʈ���� �� �� ���� �޼��� ����");
					}

				}
			} catch (EOFException eof) {
				eof.printStackTrace();
			} catch (Exception e) {
				System.out.println("Server : Ŭ���̾�Ʈ ���� ����");
				e.printStackTrace();
			}

		}// close run
	}// close ClientHandler
// �����ͺ��̽� ���ٿ� �־� ���������� �����ϱ����� ��ȣ���� ���� (synchronized)

	@SuppressWarnings("unused")
	// @SuppressWarnings("unused")�� ������� �ʴ� �ڵ� ���� ��� ����!
	private synchronized void dbInsert(String sql) throws SQLException {
		try {
			count = 0;
			dbConn();
			pst = conn.prepareStatement(sql); // SQL���� �ۼ��� ���� PrepareStatement ��ü ����
			count = pst.executeUpdate(sql); // ���� ���� count�� ��� row�� ������ ���ƴ��� row�� ��ȯ
			if (count != 0)
				System.out.println("INSERT�� ���� : " + count + "���� row�� ������ �޾ҽ��ϴ�.");
			else
				System.out.println("INSERT ���� ����");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null)
				st.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
	}

	private synchronized void dbSelect(String sql, String activityName, ObjectOutputStream writer, String contents) throws SQLException {
		ArrayList<Patient> tmp;
		ArrayList<Medicine> tmpPP,tmpMP,tmpIP;
		try {
			dbConn();
			count = 0;
			st = conn.createStatement(); // 1.���� ������ ���� Statement ��ü ����
			if (activityName.equals(MiniOCSKey.DIAGNOSIS_ACTIVITY)) {
				tmp = new ArrayList<Patient>();
				rs = st.executeQuery(sql); // 2. ���� ���� -> ���ڵ���� ResultSet ��ü�� �߰� ��.
				while (rs.next()) {
					// 3. ���� ��� ���!
					int patient_id = rs.getInt(1);
					String patient_name = rs.getString(2);
					String patient_sex = rs.getString(3);
					int patient_age = rs.getInt(4);
					String dateOfBirth = rs.getString(5);
					Patient patient = new Patient(patient_id, patient_name, patient_sex, patient_age, dateOfBirth);
					tmp.add(patient);
					count++;
				}
				writer.writeObject(new SocketMessage(SocketMessage.MsgType.RETURN_VALUE,activityName,contents,tmp));
				writer.flush();
				System.out.println("SELECT�� ���� :" + count + "���� row�� ��ȸ�Ǿ����ϴ�.");
			}else if(activityName.equals(MiniOCSKey.PRESCRIPTION_ACTIVITY)) {
				//ȯ���� ���� ��� �ҷ��� ��
				tmp = new ArrayList<Patient>();
				rs = st.executeQuery(sql); // 2. ���� ���� -> ���ڵ���� ResultSet ��ü�� �߰� ��.
				while (rs.next()) {
					// 3. ���� ��� ���!
					int patient_id = rs.getInt(2);
					String diagnosis = rs.getString(4);
					String when_registration = rs.getString(5);
					String when_treat = rs.getString(6);
					String status = rs.getString(7);
					Patient oldRecord = new Patient(patient_id, diagnosis, when_registration, when_treat, status);
					System.out.println("oldRecord"+oldRecord.toString());
					tmp.add(oldRecord);
					count++;
				}
				writer.writeObject(new SocketMessage(SocketMessage.MsgType.RETURN_VALUE, activityName,contents,tmp));
				writer.flush();
				System.out.println(tmp.toString()+"?");
				System.out.println("SELECT�� ���� :" + count + "���� row�� ��ȸ�Ǿ����ϴ�.");
			}else if(activityName.equals(MiniOCSKey.MEDICINE_SETUP)) {
				tmpPP = new ArrayList<Medicine>();
				tmpIP = new ArrayList<Medicine>();
				tmpMP = new ArrayList<Medicine>();
				rs = st.executeQuery(sql); // 2. ���� ���� -> ���ڵ���� ResultSet ��ü�� �߰� ��.
				while (rs.next()) {
					// 3. ���� ��� ���!
					String classfication = rs.getString(2);
					String name = rs.getString(3);
					String composition = rs.getString(4);
					String type = rs.getString(5);
					int stock_count = rs.getInt(6);
					Medicine medicine = new Medicine(name,composition,type,stock_count);
					System.out.println(medicine.toString());
					if(classfication.equals(MiniOCSKey.MEDICINE_PP)) {
						tmpPP.add(medicine);
						System.out.println("PP : "+medicine.toString());
					}else if(classfication.equals(MiniOCSKey.MEDICINE_MP)) {
						tmpMP.add(medicine);
						System.out.println("MP : "+medicine.toString());
					}else if(classfication.equals(MiniOCSKey.MEDICINE_IP)) {
						tmpIP.add(medicine);
						System.out.println("IP : "+medicine.toString());
					}
					count++;
				}
				writer.writeObject(new SocketMessage(SocketMessage.MsgType.RETURN_VALUE, activityName,tmpPP,MiniOCSKey.MEDICINE_PP));
				writer.flush();
				writer.writeObject(new SocketMessage(SocketMessage.MsgType.RETURN_VALUE, activityName,tmpIP,MiniOCSKey.MEDICINE_IP));
				writer.flush();
				writer.writeObject(new SocketMessage(SocketMessage.MsgType.RETURN_VALUE, activityName,tmpMP,MiniOCSKey.MEDICINE_MP));
				writer.flush();
				System.out.println("SELECT�� ���� :" + count + "���� row�� ��ȸ�Ǿ����ϴ�.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null)
				st.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
	}

	// �ؽ��ʿ� �ִ� ��� �����ڵ鿡�� �־��� �޽����� ������ �޼ҵ�.
	// �ݵ�� synchronized �� �޼ҵ忡���� ȣ���ϱ�� ��
	private void sendMessage(SocketMessage message) {

		try {
			ObjectOutputStream writer = new ObjectOutputStream(clientSock.getOutputStream());
			writer.writeObject(message); // �� ��Ʈ���� ���
			writer.flush();
		} catch (Exception ex) {
			System.out.println("S : �������� �۽� �� �̻� �߻�");
			ex.printStackTrace();
		}

	} // end broadcastMessage
}
