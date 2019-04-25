package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    EditText editName, editSpec;
    TextView tvDate, tvTime;
    Button deleteBtn, submitBtn, pickDate, pickTime;
    SQLiteDatabase db;
    String userID;
    int pk=-1, type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editName = (EditText)findViewById(R.id.editName);
        editSpec = (EditText)findViewById(R.id.editSpec);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvTime = (TextView)findViewById(R.id.tvTime);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        pickDate = (Button)findViewById(R.id.pickDate);
        pickTime = (Button)findViewById(R.id.pickTime);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);

        String dbName = "taskdb.db";
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, dbName, null, 1);
        db = helper.getWritableDatabase();

        Intent tossedIntent = getIntent();
        userID = tossedIntent.getStringExtra("userID");
        pk = tossedIntent.getIntExtra("pk", -1);
        type = tossedIntent.getIntExtra("type", 0);
        if(pk != -1){
            String sql = "SELECT * FROM " + userID + " WHERE pk = " + pk;
            Cursor cur = db.rawQuery(sql,null);
            if(cur.moveToFirst()){
                editName.setText(cur.getString(1));
                editSpec.setText(cur.getString(2));
                String datetime = cur.getString(3);
                tvDate.setText(datetime.substring(0,10));
                tvTime.setText(datetime.substring(11, 16));
            }
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터 수정하기
                if(!tvDate.getText().toString().matches("\\d{4}-\\d{2}-\\d{2}")){
                    Toast.makeText(AddActivity.this, "날짜를 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!tvTime.getText().toString().matches("\\d{2}:\\d{2}")){
                    Toast.makeText(AddActivity.this, "시간을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                String sql ="";
                if(pk != -1){
                    sql = "UPDATE " + userID +
                            " SET name='" + editName.getText().toString()+
                            "', spec='"+ editSpec.getText().toString() +
                            "', date='" + tvDate.getText().toString() + ' ' + tvTime.getText().toString() +
                            "', type=" + type + " WHERE pk = "+ pk;
                }
                else{
                    sql = "insert into "+userID + " values (NULL, '"+
                            editName.getText().toString() + "', '" +
                            editSpec.getText().toString() + "', '" +
                            tvDate.getText().toString() + ' ' + tvTime.getText().toString() + "',0)";
                }
                db.execSQL(sql);

                // tabbedactivity 켜기
                Intent tabIntent = new Intent(getApplicationContext(), TabbedActivity.class);
                tabIntent.putExtra("id", userID);
                tabIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(tabIntent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert을 이용해 종료시키기
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
                dialog  .setTitle("삭제 확인")
                        .setMessage("항목을 삭제합니다")
                        .setPositiveButton("삭제합니다", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(pk != -1){
                                    String sqlDelete = "delete from "+userID+" WHERE pk="+pk;
                                    db.execSQL(sqlDelete);
                                }

                                Intent tabIntent = new Intent(getApplicationContext(), TabbedActivity.class);
                                tabIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                tabIntent.putExtra("id", userID);
                                startActivity(tabIntent);
                            }
                        })
                        .setNeutralButton("취소합니다", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AddActivity.this, "취소했습니다", Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
            }
        });
        pickDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String res = String.format("%04d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
                                tvDate.setText(res);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        pickTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String res = String.format("%02d:%02d", hourOfDay, minute);
                                tvTime.setText(res);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }
}
