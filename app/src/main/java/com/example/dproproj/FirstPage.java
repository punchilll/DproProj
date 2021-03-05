package com.example.dproproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    private Button btn_logout;
    private Button btn_service_charge;
    private Button btn_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        btn_logout = (Button) findViewById(R.id.logout);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogin();
            }
        });

        btn_service_charge = (Button) findViewById(R.id.service_charge);
        btn_service_charge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openServiceCharge();
            }
        });

        btn_history = (Button) findViewById(R.id.history);
        btn_history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHistory();
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openServiceCharge(){
        Intent intent = new Intent(this, ServiceCharge.class);
        startActivity(intent);
    }

    public void openHistory(){
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }
}