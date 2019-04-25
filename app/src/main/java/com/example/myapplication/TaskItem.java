package com.example.myapplication;

import java.util.Calendar;
import java.util.Date;

public class TaskItem {
    String name;
    String spec;
    String duedate;
    int pk;

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDuedate() {
        return duedate;
    }

    public int getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public String getSpec() {
        return spec;
    }
}
