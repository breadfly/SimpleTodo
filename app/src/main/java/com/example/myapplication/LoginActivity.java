package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText editId, editPw;
    Button loginBtn;
    HashMap<String, String> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accounts = new HashMap<String, String>();
        accounts.put("jihye", "1234");
        accounts.put("yogurt", "happy");

        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editId.getText().toString();
                String pw = editPw.getText().toString();

                if(accounts.containsKey(id)){
                    if(accounts.get(id).equals(pw)){
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), TabbedActivity.class);
                        intent.putExtra("id", id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid PW", Toast.LENGTH_LONG).show();
                        editPw.setText("");
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                    editId.setText("");
                    editPw.setText("");
                }
            }
        });
    }
}
