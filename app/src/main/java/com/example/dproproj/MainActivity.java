package com.example.dproproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dproproj.Retrofit.INodeJS;
import com.example.dproproj.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText username,password;

    ImageView logo;
    ImageView icon1;
    ImageView icon2;
    private Button btn_login;
    private Button btn_toRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        ImageView logo=(ImageView) findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.logo);
        ImageView icon1=(ImageView) findViewById(R.id.imageView2);
        icon1.setImageResource(R.drawable.user);
        ImageView icon2=(ImageView) findViewById(R.id.imageView3);
        icon2.setImageResource(R.drawable.key);

        btn_login = (Button) findViewById(R.id.login);
        btn_toRegister = (Button) findViewById(R.id.btn_toRegister);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginUser(username.getText().toString(),password.getText().toString());

            }
        });

        btn_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    public void loginUser(String username, String password){
        compositeDisposable.add(myAPI.loginUser(username,password)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("password")) {
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            openFirstPage();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }
    public void openFirstPage(){
        Intent intent = new Intent(this, FirstPage.class);
        startActivity(intent);
    }

    public void openRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}