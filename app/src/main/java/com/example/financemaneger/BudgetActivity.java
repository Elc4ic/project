package com.example.financemaneger;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    TextView no_data;
    DataBaseHelper myDB;
    private TextView TotalBudgetAmountTextView;
    private FloatingActionButton del;
    int  sum=0;
    int vid;
    String longClick;
    String id_z;


    private ProgressDialog loader;
    CustomAdapter customAdapter;
    ArrayList date, notes,amount, mount,id,image,week,num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        loader = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        no_data= findViewById(R.id.no_data);
        TotalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);
        del=findViewById(R.id.del);
        id_z=getIntent().getStringExtra("id_i");


        myDB = new DataBaseHelper(BudgetActivity.this);
        id = new ArrayList<>();
        notes = new ArrayList<>();
        date = new ArrayList<>();
        amount = new ArrayList<>();
        mount = new ArrayList<>();
        week = new ArrayList<>();
        image = new ArrayList<>();
        num = new ArrayList<>();

        storeDataInArrays();

        vid=getIntent().getIntExtra("i",0);
        if(vid==0) {
            for (int i = 0; i < amount.size(); i++) {
                sum += Integer.parseInt((String) amount.get(i));
            }
            TotalBudgetAmountTextView.setText("Total budget amount: " + sum + "р");
        }
        if(vid==3) {
            SimpleDateFormat dataFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar1 = Calendar.getInstance();
            String date1 = dataFormat1.format(calendar1.getTime());
            for (int i = 0; i < amount.size(); i++) {
                if(date.get(i).equals(date1)) sum += Integer.parseInt((String) amount.get(i));
            }
            TotalBudgetAmountTextView.setText("today amount: " + sum + "р");
        }
        if(vid==2) {
            MutableDateTime epoch = new MutableDateTime();
            epoch.setDate(0);
            DateTime now = new DateTime();
            Months months = Months.monthsBetween(epoch, now);
            for (int i = 0; i < amount.size(); i++) {
                if(Integer.parseInt((String)mount.get(i))==months.getMonths()) sum += Integer.parseInt((String) amount.get(i));
            }
            TotalBudgetAmountTextView.setText("month amount: " + sum + "р");
        }
        if(vid==1) {
            MutableDateTime epoch = new MutableDateTime();
            epoch.setDate(0);
            DateTime now = new DateTime();
            Weeks weeks = Weeks.weeksBetween(epoch, now);
            for (int i = 0; i < amount.size(); i++) {
                if(Integer.parseInt((String)week.get(i))==weeks.getWeeks()) sum += Integer.parseInt((String) amount.get(i));
            }
            TotalBudgetAmountTextView.setText("week amount: " + sum + "р");
        }
        if(vid==5) {
            del.setVisibility(View.VISIBLE);
            longClick=getIntent().getStringExtra("c");
            for (int i = 0; i < amount.size(); i++) {
                if(image.get(i).equals(longClick)) sum += Integer.parseInt((String) amount.get(i));
            }
            TotalBudgetAmountTextView.setText(longClick+" amount: " + sum + "р");
        }
        if(vid==4) {
            longClick="Salary";
            for (int i = 0; i < amount.size(); i++) {
                if(image.get(i).equals("Salary")) sum += Integer.parseInt((String) amount.get(i));
            }
            TotalBudgetAmountTextView.setText("Salary amount: " + sum + "р");
        }

        customAdapter = new CustomAdapter(BudgetActivity.this,this,id,notes,date,amount,mount,image,week,vid,num,id_z);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BudgetActivity.this));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData1();
        if(cursor.getCount() == 0){
            no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                notes.add(cursor.getString(1));
                date.add(cursor.getString(2));
                amount.add(cursor.getString(3));
                image.add(cursor.getString(4));
                mount.add(cursor.getString(5));
                week.add(cursor.getString(6));
                num.add(cursor.getString(7));
            }
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataBaseHelper myDB = new DataBaseHelper(BudgetActivity.this);
                myDB.deleteOneZagl(id_z);
                for (int j = 0; j <image.size(); j++) {
                    if(num.get(j).equals(id_z)) myDB.deleteOneRow(String.valueOf(id.get(j)));
                }
                Intent intent = new Intent(BudgetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void del(View view) {
        confirmDialog();
    }
}