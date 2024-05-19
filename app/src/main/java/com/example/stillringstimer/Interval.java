package com.example.stillringstimer;

import java.io.Serializable;
import java.util.List;

public class Interval implements Serializable {
    private long workDuration;
    private long restDuration;
    private int repetitions;

    public Interval(long workDuration, long restDuration, int repetitions) {
        this.workDuration = workDuration;
        this.restDuration = restDuration;
        this.repetitions = repetitions;
    }

    // Getters and setters
    public long getWorkDuration() { return workDuration; }
    public void setWorkDuration(long workDuration) { this.workDuration = workDuration; }

    public long getRestDuration() { return restDuration; }
    public void setRestDuration(long restDuration) { this.restDuration = restDuration; }

    public int getRepetitions() { return repetitions; }
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
}
