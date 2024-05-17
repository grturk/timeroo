package com.example.stillringstimer;

import android.widget.TextView;

public class Button_up_down {

    private TextView secondsView;
    private TextView hundredthsView;
    private long startTime;
    private boolean minFlag;
    private TextView minutesView;

    public Button_up_down (TextView secondsView, TextView hundredthsView, boolean minFlag, TextView minutesView) {
        this.secondsView = secondsView;
        this.hundredthsView = hundredthsView;
        int[] time = getTimeFromView();
        this.startTime = time[0] * 1000L + time[1] * 10;
        this.minFlag = minFlag;
        if (minFlag) {
            this.minutesView = minutesView;
        }
    }

    private int[] getTimeFromView() {
        String secondsString = secondsView.getText().toString();
        int secondsInput = Integer.parseInt(secondsString);

        String hundredthsString = hundredthsView.getText().toString();
        int hundredthsInput = Integer.parseInt(hundredthsString);
        if (minFlag) {
            String minutesString = minutesView.getText().toString();
            int minutesInput = Integer.parseInt(minutesString);
            return new int[]{secondsInput, hundredthsInput, minutesInput};
        }
        return new int[]{secondsInput, hundredthsInput};
    }

    public void secUp() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];
        int min = 0;

        if (minFlag) {
            min = time[2];
            if (sec < 59) {
                sec++;
            } else {
                sec = 0;
            }
            startTime = min * 60000L + sec * 1000L + hun * 10;

            String minString = String.format("%02d", min);
            String secString = String.format("%02d", sec);

            minutesView.setText(minString);
            secondsView.setText(secString);

        } else {
            if (sec < 59) {
                sec++;
            } else {
                sec = 0;
            }

            startTime = sec * 1000L + hun * 10;

            String secString = String.format("%02d", sec);
            secondsView.setText(secString);
        }

    }

    public void secDown() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];
        int min = 0;

        if (minFlag) {
            min = time[2];
            if (sec > 0) {
                sec--;
            } else {
                sec = 59;
            }
            startTime = min * 60000L + sec * 1000L + hun * 10;

            String minString = String.format("%02d", min);
            String secString = String.format("%02d", sec);

            minutesView.setText(minString);
            secondsView.setText(secString);
        } else {
            if (sec > 0) {
                sec--;
            } else {
                sec = 59;
            }

            startTime = sec * 1000L + hun * 10;

            String secString = String.format("%02d", sec);
            secondsView.setText(secString);
        }
    }

    public void hunUp() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];
        int min = 0;

        if (minFlag) {
            min = time[2];

            if (hun == 0) {
                hun += 50;
            } else {
                hun = 0;

            }

            startTime = min * 60000L + sec * 1000L + hun * 10;

            String minString = String.format("%02d", min);
            String secString = String.format("%02d", sec);
            String hunString = String.format("%02d", hun);

            minutesView.setText(minString);
            secondsView.setText(secString);
            hundredthsView.setText(hunString);
        } else {
            if (hun == 0) {
                hun += 50;
            } else if (sec >= 59) {
                sec = 0;
                hun = 0;
            } else {
                hun = 0;
                sec += 1;
            }

            startTime = sec * 1000L + hun * 10;

            String secString = String.format("%02d", sec);
            String hunString = String.format("%02d", hun);
            secondsView.setText(secString);
            hundredthsView.setText(hunString);
        }
    }

    public void hunDown() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];
        int min = 0;

        if (minFlag) {
            min = time[2];
            if (hun == 50) {
                hun -= 50;
            } else {
                hun = 50;
            }

            startTime = min * 60000L + sec * 1000L + hun * 10;

            String minString = String.format("%02d", min);
            String secString = String.format("%02d", sec);
            String hunString = String.format("%02d", hun);

            minutesView.setText(minString);
            secondsView.setText(secString);
            hundredthsView.setText(hunString);

        } else {
            if (hun == 50) {
                hun -= 50;
            } else if (sec == 0) {
                sec = 59;
                hun = 50;
            } else {
                hun = 50;
                sec -= 1;
            }

            startTime = sec * 1000L + hun * 10;

            String secString = String.format("%02d", sec);
            String hunString = String.format("%02d", hun);
            secondsView.setText(secString);
            hundredthsView.setText(hunString);
        }

        startTime = sec * 1000L + hun * 10;

        String secString = String.format("%02d", sec);
        String hunString = String.format("%02d", hun);
        secondsView.setText(secString);
        hundredthsView.setText(hunString);
    }

    public void minUp() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];
        int min = time[2];

        if (min == 59) {
            min = 0;
        } else {
            min += 1;
        }

        startTime = min * 60000L + sec * 1000L + hun * 10;

        String minString = String.format("%02d", min);
        String secString = String.format("%02d", sec);
        String hunString = String.format("%02d", hun);

        minutesView.setText(minString);
        secondsView.setText(secString);
        hundredthsView.setText(hunString);
    }
    public void minDown() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];
        int min = time[2];

        if (min == 0) {
            min = 59;
        } else {
            min -= 1;
        }

        startTime = min * 60000L + sec * 1000L + hun * 10;

        String minString = String.format("%02d", min);
        String secString = String.format("%02d", sec);
        String hunString = String.format("%02d", hun);

        minutesView.setText(minString);
        secondsView.setText(secString);
        hundredthsView.setText(hunString);
    }
    public long getStartTime() {
        return startTime;
    }
}
