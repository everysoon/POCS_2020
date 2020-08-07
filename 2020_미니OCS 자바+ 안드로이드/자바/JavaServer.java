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
	// MYSQL url, userID,userPassword 상수로 선언
	public static String driverName = MiniOCSKey.driverName;
	public static String url = MiniOCSKey.url;
	public static String id = MiniOCSKey.usrID; // user ID
	public static String password = MiniOCSKey.userPassword; // user password

	// 쿼리 수행할 때 동적할당 값 필요하면 PreparedStatement, 동적할당 필요없으면 Statement 객체 사용
	// 쿼리 결과가 있으면 executeQuery() 수행해 ResultSet에 담고, 쿼리 결과없으면 executeUpdate()메서드 호출하여
	// int형 변수에 결과 값 할당
	public Connection conn = null; // DB연결할 Connection 객체참조변수
	public Statement st = null; // 질의 수행할 객체 Statement
	public PreparedStatement pst = null; // 동적할당! 쿼리문의 ?를 바인딩해서 사용 가능
	public ResultSet rs = null; // 질의 결과 저장할 ResultSet

	int count = 0; // sql문 실행 시 영향을 받은 row수를 count하기 위한 변수
	Socket clientSock;

	public static void main(String[] args) {
		JavaServer server = new JavaServer();
		server.dbConn();
		server.socketConn();
	}

	// 소켓 메세지로부터의 작업을 MYSQL과 연동하여 수행 함
	public void dbConn() {
		try {

			Class.forName(driverName); // 1. 드라이버 로딩
			conn = DriverManager.getConnection(url, id, password); // 2. mysql과 연결하기

		} catch (ClassNotFoundException cnfe) {
			System.out.println("JDBC 드라이버 클래스를 찾을 수 없습니다 : " + cnfe.getMessage());
		} catch (Exception ex) {
			System.out.println("DB 연결 에러 : " + ex.getMessage());
		}
	}

	// 안드로이드와 Socket 통신을 위한 연결/메세지 처리
	public void socketConn() {
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			while (true) {
				System.out.println("대기중");
				clientSock = serverSock.accept();
				// 클라이언트를 위한 입출력 스트림 및 스레드 생성
				Thread t = new Thread(new ClientHandler(clientSock));
				t.start();
				System.out.println("Server : 클라이언트 연결 됨");
			}
		} catch (Exception e) {
			System.out.println("Server : 클라이언트 연결 중 이상발생");
			e.printStackTrace();
		}
	}

	// Client와 1:1 대응하는 메시지 수신 스레드
	private class ClientHandler implements Runnable {
		Socket sock; // 클라이언트 연결용 소켓
		ObjectInputStream reader; // 클라이언트로 부터 수신하기 위한 스트림
		ObjectOutputStream writer; // 클라이언트로 송신하기 위한 스트림

		// 구성자. 클라이언트와의 소켓에서 읽기와 쓰기 스트림 만들어 냄
		// 스트림을 만들때 InputStream을 먼저 만들면 Hang함. 그래서 OutputStream먼저 만들었음.
		// 이것은 클라이언트에서 InpitStreams을 먼저 만들기 때문임 안그러면 데드락
		public ClientHandler(Socket clientSock) {
			try {
				sock = clientSock;
				writer = new ObjectOutputStream(clientSock.getOutputStream());
				reader = new ObjectInputStream(clientSock.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// 클라이언트에서 받은 메세지에 따라 상응하는 작업을 수행
		public synchronized void run() {

			SocketMessage message = null;
			SocketMessage.MsgType type = null;
			try {
				while (true) {
					// 읽은 메세지의 종류에 따라 각각 할 일이 정해져 있음.
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
						new Exception("Server : 클라이언트에서 알 수 없는 메세지 도착");
					}

				}
			} catch (EOFException eof) {
				eof.printStackTrace();
			} catch (Exception e) {
				System.out.println("Server : 클라이언트 접속 종료");
				e.printStackTrace();
			}

		}// close run
	}// close ClientHandler
// 데이터베이스 접근에 있어 경쟁조건을 방지하기위해 상호배제 해줌 (synchronized)

	@SuppressWarnings("unused")
	// @SuppressWarnings("unused")는 사용하지 않는 코드 관련 경고 제외!
	private synchronized void dbInsert(String sql) throws SQLException {
		try {
			count = 0;
			dbConn();
			pst = conn.prepareStatement(sql); // SQL문의 작성을 위한 PrepareStatement 객체 생성
			count = pst.executeUpdate(sql); // 쿼리 실행 count는 몇개의 row가 영향을 미쳤는지 row수 반환
			if (count != 0)
				System.out.println("INSERT문 실행 : " + count + "개의 row가 영향을 받았습니다.");
			else
				System.out.println("INSERT 실행 실패");
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
			st = conn.createStatement(); // 1.쿼리 수행을 위한 Statement 객체 생성
			if (activityName.equals(MiniOCSKey.DIAGNOSIS_ACTIVITY)) {
				tmp = new ArrayList<Patient>();
				rs = st.executeQuery(sql); // 2. 쿼리 수행 -> 레코드들은 ResultSet 객체에 추가 됨.
				while (rs.next()) {
					// 3. 실행 결과 출력!
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
				System.out.println("SELECT문 실행 :" + count + "개의 row가 조회되었습니다.");
			}else if(activityName.equals(MiniOCSKey.PRESCRIPTION_ACTIVITY)) {
				//환자의 예전 기록 불러올 때
				tmp = new ArrayList<Patient>();
				rs = st.executeQuery(sql); // 2. 쿼리 수행 -> 레코드들은 ResultSet 객체에 추가 됨.
				while (rs.next()) {
					// 3. 실행 결과 출력!
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
				System.out.println("SELECT문 실행 :" + count + "개의 row가 조회되었습니다.");
			}else if(activityName.equals(MiniOCSKey.MEDICINE_SETUP)) {
				tmpPP = new ArrayList<Medicine>();
				tmpIP = new ArrayList<Medicine>();
				tmpMP = new ArrayList<Medicine>();
				rs = st.executeQuery(sql); // 2. 쿼리 수행 -> 레코드들은 ResultSet 객체에 추가 됨.
				while (rs.next()) {
					// 3. 실행 결과 출력!
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
				System.out.println("SELECT문 실행 :" + count + "개의 row가 조회되었습니다.");
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

	// 해쉬맵에 있는 모든 접속자들에게 주어진 메시지를 보내는 메소드.
	// 반드시 synchronized 된 메소드에서만 호출하기로 함
	private void sendMessage(SocketMessage message) {

		try {
			ObjectOutputStream writer = new ObjectOutputStream(clientSock.getOutputStream());
			writer.writeObject(message); // 그 스트림에 출력
			writer.flush();
		} catch (Exception ex) {
			System.out.println("S : 서버에서 송신 중 이상 발생");
			ex.printStackTrace();
		}

	} // end broadcastMessage
}
