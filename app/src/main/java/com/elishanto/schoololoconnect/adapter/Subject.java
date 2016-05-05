package com.elishanto.schoololoconnect.adapter;

import android.support.v4.util.Pair;

import java.util.List;


public class Subject {
    public String name;
    public List<Pair<String, String>> marks;
    public String avg;

    public Subject(String name, List<Pair<String, String>> marks, String avg) {
        this.name = name;
        this.marks = marks;
        this.avg = avg;
    }
}
