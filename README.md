# POCS_2020
: 안드로이드를 이용한 휴대용 miniOCS 시스템

## 개발환경 : Android 29 / MYSQL 5.6 
(mysql-connector-java-5.1.39-bin.jar 라이브러리 사용)  
사용안드로이드 기기 : LG 10.1 Tablet 
### 1. 설치
mysql 설치 : https://dev.mysql.com/downloads/mysql/  
JDBC를 이용해 mysql을 접근하기위한 라이브러리 설치 : *mysql-connector-java-버전-bin.jar*로 준비!
### 2. 환경설정
#### <MYSQL 환경설정>
1) C:\ProgramData\MySQL\MySQL Server 5.6 으로 이동해 my.ini파일에  
**bind-Address = 자신의 IP주소**를 적어준다
2) 데이터베이스 생성  
**`CREATE DATABASE 데이터베이스이름 DEFAULT charset=utf8;`**
3) 새로 생성한 데이터베이스를 사용한 사용자 생성  
**`CREATE USER 사용자이름 IDENTIFIED BY '패스워드';`**
4) 앞에 생성했던 데이터베이스를 새로 생성한 사용자가 사용하도록 권한부여  
**`GRANT ALL PRIVILEGES ON 데이터베이스이름.* TO '사용자이름'@'localhost' IDENTIFIED BY '패스워드';`**
5) `FLUSH PRIVILGES`;    
**grant 테이블을 reload함으로써 변경 사항을 즉시 반영하도록 한다.**
#### <안드로이드 환경설정>  
1) AndroidManifest 파일에 인터넷 접근 허용 권한 부여  
**`<uses-permission android:name="android.permission.INTERNET"></uses-permission>`**  
2) mysql-connector-java-버전-bin.jar' 라이브러리 안드로이드에 추가 Project->app->libs에 복사  


