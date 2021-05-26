package com.example.financemaneger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridView grid;
    private CustomGrid customGrid;
    TextView total, todaydata, salary;
    TextView last, next;
    ImageButton add;
    Button salary_r;
    int h = 0;
    int new_amount = 0;
    long Allamount = 0L;
    long Allsalary = 0L;
    PieChart pieChart;
    ArrayList<PieEntry> zatrats = new ArrayList<>();
    public static final int[] COLORFUL_GRAY = {
            Color.rgb(105, 105, 105), Color.rgb(79, 79, 79), Color.rgb(207, 207, 207),
            Color.rgb(181, 181, 181), Color.rgb(130, 130, 130)};
    private ProgressDialog loader;
    DataBaseHelper myDB;
    ArrayList item, notes, id, amounT, amount, item_B, month, id_B;

    @Override
    protected void onResume() {
        super.onResume();
        updateAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loader = new ProgressDialog(this);
        last = findViewById(R.id.lastmonth);
        next = findViewById(R.id.nextmonth);
        grid = findViewById(R.id.grid);
        add = findViewById(R.id.add_zagl);
        pieChart = findViewById(R.id.pieChart);
        salary_r=  findViewById(R.id.zp);
        todaydata = findViewById(R.id.todaydata);
        total = findViewById(R.id.total);
        salary = findViewById(R.id.salar);

        //объявление массивов
        myDB = new DataBaseHelper(MainActivity.this);
        id = new ArrayList<>();
        notes = new ArrayList<>();
        item = new ArrayList<>();
        amounT = new ArrayList<>();
        amount = new ArrayList<>();
        item_B = new ArrayList<>();
        month = new ArrayList<>();
        id_B = new ArrayList<>();

        updateAll();

        //добавление катерории
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        //запить затраты
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                String image = (String) item.get(position);
                String num = (String) id.get(position);
                addZatrat(image, num, 1);
            }
        });
        //все затраты в категории
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long i) {
                Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
                intent.putExtra("i", 5);
                intent.putExtra("c", (String) item.get(position));
                intent.putExtra("id_i", String.valueOf(id.get(position)));
                MainActivity.this.startActivityForResult(intent, 1);
                return false;
            }
        });
        //месяц назад
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h -= 1;
                amounT.clear();
                zatrats.clear();
                setData();
                updateText();
                updateDiagram(zatrats);
            }
        });
        //месяц вперед
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h += 1;
                amounT.clear();
                zatrats.clear();
                setData();
                updateText();
                updateDiagram(zatrats);
            }
        });
        salary_r.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
                intent.putExtra("i", 4);
                MainActivity.this.startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    //добавление категории
    private void addItem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.zaglav_input, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText note = myView.findViewById(R.id.zagl);
        final Spinner zspinner = myView.findViewById(R.id.zaglspinner);
        final Button cancel = myView.findViewById(R.id.cancel_z);
        final Button save = myView.findViewById(R.id.save_z);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetNote = note.getText().toString();
                String budgetItem = zspinner.getSelectedItem().toString();
                if (budgetItem.equals("Select item")) {
                    Toast.makeText(MainActivity.this, "Вы не указали раздел", Toast.LENGTH_SHORT).show();
                } else {
                    loader.setMessage("adding a item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    myDB.addZagl(budgetItem, budgetNote);
                    updateAll();
                    updateAll();
                    loader.dismiss();
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    void updateAll(){
        storeDataInArrays();
        storeTotal();
        amounT.clear();
        zatrats.clear();
        setData();
        updateText();
        updateDiagram(zatrats);
        updateSalary();
        updateTotal();
    }

    //запись из базы в массив 1
    void storeDataInArrays() {
        id.clear();
        notes.clear();
        item.clear();
        Cursor cursor = myDB.readAllData2();
        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                notes.add(cursor.getString(1));
                item.add(cursor.getString(2));

            }
        }
    }

    //запить из базы в массивы 2
    void storeTotal() {
        Cursor cursor = myDB.readAllData1();
        amount.clear();
        item_B.clear();
        month.clear();
        id_B.clear();
        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                amount.add(cursor.getString(3));
                item_B.add(cursor.getString(4));
                month.add(cursor.getString(5));
                id_B.add(cursor.getString(7));
            }
        }
    }
    //настройка даты и суммы затрат
    public void setData() {
        todaydata.setText(getData(h));

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);
        for (int i = 0; i < item.size(); i++) {
            for (int j = 0; j < amount.size(); j++) {
                if (id.get(i).equals(id_B.get(j)) && !(item_B.get(j).equals("Salary")) && (months.getMonths() + h) == Integer.parseInt((String) month.get(j))) {
                    new_amount += Integer.parseInt((String) amount.get(j));
                }
            }
            amounT.add(String.valueOf(new_amount));
            new_amount = 0;
        }
    }

    //получение даты и обработка её в строку
    public String getData(int h) {
        SimpleDateFormat dataFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, h);
        String date1 = dataFormat1.format(calendar1.getTime());
        return date1;
    }

    //обновление диаграммы (после смены месяца обновляеся при нажатии)
    public void updateDiagram(ArrayList zatrats) {
        for (int i = 0; i < notes.size(); i++) {
            zatrats.add(new PieEntry(-(Integer.parseInt((String) amounT.get(i))), (String) notes.get(i)));
        }
        PieDataSet pieDataSet = new PieDataSet(zatrats, "Затраты");
        pieDataSet.setColors(COLORFUL_GRAY);
        pieDataSet.setValueTextSize(0f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animate();
    }

    //обновление
    public void updateText() {
        customGrid = new CustomGrid(MainActivity.this, notes, item, amounT);
        grid.setAdapter(customGrid);
    }
    public void updateSalary() {
        for (int i = 0; i < amount.size(); i++) {
            if (Integer.parseInt((String) amount.get(i)) > 0)
                Allsalary += Integer.parseInt((String) amount.get(i));
        }
        salary.setText(Allsalary + "p");
        Allsalary=0;
    }
    public void updateTotal() {
        for (int i = 0; i < amount.size(); i++) {
            Allamount += Integer.parseInt((String) amount.get(i));
        }
        total.setText(Allamount + "p");
        Allamount=0;
    }

    //добавление затраты
    private void addZatrat(String image, String num, int tip) {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText note = myView.findViewById(R.id.note);
        final EditText amount_text = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetNote = note.getText().toString();
                String budgetAmount = amount_text.getText().toString();
                if (TextUtils.isEmpty(budgetAmount)) {
                    amount_text.setError("Вы не указали стоимость");
                }
                loader.setMessage("adding a item");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dataFormat.format(calendar.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);
                Weeks weeks = Weeks.weeksBetween(epoch, now);

                if (tip == 2) {
                    myDB.addBook(date, budgetNote, Integer.parseInt(budgetAmount), months.getMonths(), image, weeks.getWeeks(), "0");
                    loader.dismiss();
                    updateAll();
                    dialog.dismiss();
                }
                if (tip == 1) {
                    myDB.addBook(date, budgetNote, -(Integer.parseInt(budgetAmount)), months.getMonths(), image, weeks.getWeeks(), num);
                    loader.dismiss();
                    updateAll();
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //все списки затрат
    public void click(View view) {
        Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
        intent.putExtra("i", 0);
        MainActivity.this.startActivityForResult(intent, 1);
    }

    public void week(View view) {
        Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
        intent.putExtra("i", 1);
        MainActivity.this.startActivityForResult(intent, 1);
    }

    public void month(View view) {
        Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
        intent.putExtra("i", 2);
        MainActivity.this.startActivityForResult(intent, 1);
    }

    public void today(View view) {
        Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
        intent.putExtra("i", 3);
        MainActivity.this.startActivityForResult(intent, 1);
    }

    //зарплата
    public void sarary(View view) {
        String image = "Salary";
        addZatrat(image, "0", 2);
    }
}