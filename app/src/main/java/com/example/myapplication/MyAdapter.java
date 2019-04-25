package com.example.myapplication;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{
    private Context context;
    private ArrayList<TaskItem> list = null; // 아이템의 데이터 저장
    boolean left, right;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    MyAdapter(ArrayList<TaskItem> list, boolean left, boolean right) {
        this.list = list;
        this.left = left;
        this.right = right;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        TaskItem item = list.get(position);

        if(!left) holder.left_arrow.setVisibility(View.INVISIBLE);
        if(!right) holder.right_arrow.setVisibility(View.INVISIBLE);

        holder.name.setText(item.name);
        holder.duedate.setText(item.duedate);
        holder.parent.setTag(item.pk);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name, duedate;
        LinearLayout parent;
        ImageView left_arrow, right_arrow;
        Holder(View itemView) {
            super(itemView) ;
            name = itemView.findViewById(R.id.name);
            duedate = itemView.findViewById(R.id.duedate);
            left_arrow = itemView.findViewById(R.id.left_arrow);
            right_arrow = itemView.findViewById(R.id.right_arrow);
            parent = (LinearLayout)left_arrow.getParent();
        }
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }
}

