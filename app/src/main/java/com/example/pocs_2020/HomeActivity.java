package com.example.pocs_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Socket sock;
    ObjectOutputStream writer;
    ObjectInputStream reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.registrateButton).setOnClickListener(this);
        findViewById(R.id.consoultationRoomButton).setOnClickListener(this);
        findViewById(R.id.pharmacyButton).setOnClickListener(this);
        findViewById(R.id.surgeryButton).setOnClickListener(this);
        findViewById(R.id.statisticsButton).setOnClickListener(this);
       // initSock();

    }
/*    public void initSock(){
        try{
            //소켓을 생성하고 입출력스트림을 소켓에 연결
            sock = MiniOCSKey.sock;
            reader = MiniOCSKey.reader;
            writer = MiniOCSKey.writer;
            MiniOCSKey.sockInit();
        }catch (Exception e){
            Log.e("initSocket","서버 접속에 실패!");
            e.printStackTrace();
        }
    }*/
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.registrateButton:
                startActivity(new Intent(this,RegistrateActivity.class));
                break;
            case R.id.consoultationRoomButton:
                startActivity(new Intent(this,ConsultationRoomActivity.class));
                finish();
                break;
            case R.id.pharmacyButton:
                startActivity(new Intent(this,PharmacyActivity.class));
                finish();
                break;
            case R.id.surgeryButton:
                startActivity(new Intent(this,SurgeryActivity.class));
                finish();
                break;
            case R.id.statisticsButton:
                startActivity(new Intent(this,StatisticsActivity.class));
                finish();
                break;
        }
    }
}
