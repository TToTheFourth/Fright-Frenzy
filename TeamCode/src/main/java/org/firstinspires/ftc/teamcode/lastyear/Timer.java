package org.firstinspires.ftc.teamcode.lastyear;

public class Timer {
    long startTime;
    long compareTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }
    public boolean timeChecker() {
        return System.currentTimeMillis() - startTime >= compareTime;
    }
    public void setCompareTime(long time) {
        compareTime = time;
    }
    public void waitT (long milli) {
        long time = System.currentTimeMillis();
        long target = time + milli;
        while (System.currentTimeMillis() < target) {

        }
    }

}
