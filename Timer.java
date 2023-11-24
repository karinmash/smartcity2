package ono;

import static java.lang.Thread.sleep;

public class Timer implements Runnable {
    private volatile boolean running = true;
    private int sleep_seconds;
    private int seconds = 0;

    public Timer(int sleep_seconds){
        this.sleep_seconds = sleep_seconds;
    }

    @Override
    public void run() {

        while(running){
            this.task();
            try {
                sleep(this.sleep_seconds*1000);
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void task(){
        // Hours, minutes and seconds
        var hrs = ~~(this.seconds / 3600);
        var mins = ~~((this.seconds % 3600) / 60);
        var secs = ~~this.seconds % 60;

        var ret = "";

        if (hrs > 0) {
            ret += "" + hrs + ":" + (mins < 10 ? "0" : "");
        }

        ret += "" + mins + ":" + (secs < 10 ? "0" : "");
        ret += "" + secs;
        System.out.println(ret);
        this.seconds++;
    }
    public void shutdown(){
        this.running = false;
    }
}

