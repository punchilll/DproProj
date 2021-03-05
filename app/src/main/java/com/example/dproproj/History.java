package com.example.dproproj;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dproproj.Retrofit.INodeJS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class History extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private Button btn_date,btn_search;
    private TextView txt_date;
    public String created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btn_date = findViewById(R.id.date);
        txt_date = findViewById(R.id.txt_date);
        btn_search = findViewById(R.id.btn_search);
        created_at = "";

        ArrayList< String > Gl = new ArrayList< String >( );
        ArrayList< String > list = new ArrayList< String >( );
        ArrayList< String > money = new ArrayList< String >( );
        ArrayList< String > time = new ArrayList< String >( );
        long date = new Date().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        for(int i=0; i<20; i++) {
            int rand = (int) (Math.random() * 5000) + 100;
                list.add("รายการที่ " + (i + 1));
                money.add("ยอด\t: " + rand);
                time.add(strDate);
                Gl.add( (String)(list.get(i)+"  | "+money.get(i)+"  | "+time.get(i)) );
        }

        //ArrayList arrList = getListData();

        ArrayAdapter< String > dataAdapter = new ArrayAdapter< String >
                ( this, android.R.layout.simple_list_item_1, Gl );
        ListView listview = (ListView) this.findViewById ( R.id.list_view );
        listview.setAdapter(dataAdapter);



        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(History.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                created_at = day+"-"+(month+1)+"-"+year;
                                txt_date.setText(created_at);
                            }
                        }, 0, 0, 0);
                calendar.add(Calendar.YEAR, -10);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList(txt_date.getText().toString());
            }
        });
    }

    public void showList(String created_at){
        compositeDisposable.add(myAPI.history(created_at)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {


                        Toast.makeText(History.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private ArrayList getListData() {
        ArrayList<ListItem> res = new ArrayList<>();
        ArrayList<ListItem> result = new ArrayList<>( );
        long date = new Date().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        for(int i=0; i<20; i++) {
            int rand = (int) (Math.random() * 5000) + 100;
                /*list.add("รายการที่ " + (0 + 1));
                money.add("ยอด\t: " + rand);
                time.add(strDate);*/
            ListItem li = new ListItem();
            li.setList("รายการที่ " + (0 + 1));
            li.setMoney("ยอด\t: " + rand);
            li.setTime(strDate);
            result.set(i, li);
        }
        return result;
    }
}

class ListItem {
    private String list;
    private String money;
    private String time;
    public String getList() {
        return list;
    }
    public void setList(String list) {
        this.list = list;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}