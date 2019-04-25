package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TabbedActivity extends AppCompatActivity {
    TabLayout menuTab;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    String id;
    SQLiteDatabase db;
    ListFragment[] fr;

    public ArrayList<TaskItem> dataRefresh(int type){
        ArrayList<TaskItem> list = new ArrayList<TaskItem>();
        String sqlSelect = "SELECT * FROM " + id + " WHERE type=" + type;
        Cursor cursor = db.rawQuery(sqlSelect,null);
        while(cursor.moveToNext()){
            TaskItem t = new TaskItem();
            t.setPk(cursor.getInt(0));
            t.setName(cursor.getString(1));
            t.setSpec(cursor.getString(2));
            t.setDuedate(cursor.getString(3));
            list.add(t);
        }
        return list;
    }

    public void newPages(){
        fr = new ListFragment[3];
        for(int i=0; i<3; i++){
            fr[i] = new ListFragment();
            Bundle bdl = new Bundle();
            bdl.putString("id", id);
            if(i == 0) bdl.putBoolean("left", false);
            else bdl.putBoolean("left", true);
            if(i==2) bdl.putBoolean("right", false);
            else bdl.putBoolean("right", true);
            fr[i].setArguments(bdl);
            fr[i].setList(dataRefresh(i));
        }
    }

    public void onLeftClick(View view){
        int current = mViewPager.getCurrentItem();
        if(current == 0) return;
        int pk = (int)((View)view.getParent()).getTag();
        String sql = "UPDATE " + id + " SET type=" + (current-1) + " WHERE pk=" + pk;
        db.execSQL(sql);
        for(int i=0; i<3; i++){
            fr[i].setList(dataRefresh(i));
            fr[i].refresh();
        }
    }

    public void onRightClick(View view){
        int current = mViewPager.getCurrentItem();
        if(current == 2) return;
        int pk = (int)((View)view.getParent()).getTag();
        String sql = "UPDATE " + id + " SET type=" + (current+1) + " WHERE pk=" + pk;
        db.execSQL(sql);
        for(int i=0; i<3; i++){
            fr[i].setList(dataRefresh(i));
            fr[i].refresh();
        }
    }

    public void onEditClick(View view){
        int current = mViewPager.getCurrentItem();
        int pk = (int)((View)view.getParent()).getTag();

        Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
        addIntent.putExtra("userID", id);
        addIntent.putExtra("pk", pk);
        addIntent.putExtra("type", current);
        startActivity(addIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        String dbName = "taskdb.db";
        int dbVersion=1;
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, dbName, null, dbVersion);
        // 생성자가 아니라 attach 된 다음에 해야함 getActivity()는 ...
        db = helper.getWritableDatabase();
        String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS " + id +
                " (pk INTEGER PRIMARY KEY AUTOINCREMENT, NAME STRING, SPEC STRING, DATE STRING, type INTEGER)" ;
        db.execSQL(sqlCreateTbl);

        newPages();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        menuTab = (TabLayout) findViewById(R.id.menuTab);
        menuTab.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
                addIntent.putExtra("userID", id);
                addIntent.putExtra("pk", -1);
                addIntent.putExtra("type", 0);
                startActivity(addIntent);
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fr[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            if(position == 0) return "TODO";
            else if(position == 1) return "DOING";
            else if(position==2) return "DONE";
            else return "TODO";
        }
    }
}