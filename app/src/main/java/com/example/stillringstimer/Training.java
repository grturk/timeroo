package com.example.stillringstimer;


import java.io.Serializable;
import java.util.List;

public class Training implements Serializable {
    private String name;
    private List<Interval> intervals;

    public Training(String name, List<Interval> intervals) {
        this.name = name;
        this.intervals = intervals;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Interval> getIntervals() { return intervals; }
    public void setIntervals(List<Interval> intervals) { this.intervals = intervals; }
}

