package com.example.financemaneger;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {
    DataBaseHelper myDB;
    private EditText note_in, amount_in;
    private Button update, delete;
    private String id, note;
    private int old_amount = 0;
    int new_a = 0;
    private int amount;
    private int vid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);

        note_in = findViewById(R.id.note_in);
        amount_in = findViewById(R.id.amount_in);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        myDB = new DataBaseHelper(UpdateActivity.this);
        id = getIntent().getStringExtra("id");
        old_amount = getIntent().getIntExtra("amount", 0);
        vid = getIntent().getIntExtra("ras",0);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(amount_in.getText().toString());
                note = note_in.getText().toString();
                new_a = amount - old_amount;
                if(vid==4) myDB.updateData(id, note, amount);
                else myDB.updateData(id, note, -amount);

                finish();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
    }

}