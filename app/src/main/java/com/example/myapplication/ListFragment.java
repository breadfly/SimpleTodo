package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<TaskItem> list;
    MyAdapter adapter;
    boolean left, right;

    public ListFragment() {
        // Required empty public constructor
    }

    public void setList(ArrayList<TaskItem> list){
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        left = getArguments().getBoolean("left");
        right = getArguments().getBoolean("right");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyAdapter(list, left, right);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public void refresh(){
        adapter = new MyAdapter(list, left, right);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
