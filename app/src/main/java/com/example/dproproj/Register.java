package com.example.dproproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dproproj.Retrofit.INodeJS;
import com.example.dproproj.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText username,password,f_name,l_name,email,tel;

    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        btn_register = (Button) findViewById(R.id.btn_register);

        username = (EditText) findViewById(R.id.username_r);
        password = (EditText) findViewById(R.id.password_r);
        f_name = (EditText) findViewById(R.id.f_name_r);
        l_name = (EditText) findViewById(R.id.l_name_r);
        email = (EditText) findViewById(R.id.email_r);
        tel = (EditText) findViewById(R.id.tel_r);

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser(username.getText().toString(), password.getText().toString(), f_name.getText().toString(),
                        l_name.getText().toString(), email.getText().toString(), tel.getText().toString());
            }
        });
    }

    public void registerUser(String username, String password, String f_name, String l_name, String email, String tel){
        compositeDisposable.add(myAPI.registerUser(username,password,f_name,l_name,email,tel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(Register.this, " "+s, Toast.LENGTH_SHORT).show();
                        openLogin();
                    }
                })
        );
    }

    public void openLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}