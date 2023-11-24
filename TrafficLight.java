package ono;


import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TrafficLight implements Runnable{

    protected Road curr_green_road = null;
    private int delay;
    protected  Junction m_junction;
    private Random random = new Random();
    int turns = 0;
    protected AtomicBoolean active = new AtomicBoolean(true);

    public TrafficLight(Junction j){
        delay = (int)(Math.random() * (2) + 2);
        m_junction = j;
        setCurr_green_road();
        System.out.println(this);
    }
    public void stop(){
        active.set(false);
    }

    public void check()  {
        turns = 0;
        this.setCurr_green_road();

        System.out.println(this);
        try {
            Thread.sleep(delay*1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

    }

    public int getDelay() {
        return delay;
    }
    public Road getCurrentGreen(){
        return curr_green_road;
    }

    protected abstract void setCurr_green_road();

    @Override
    public String toString() {
        return "TrafficLights " + m_junction + String.format(" , delay= %d: green light on ", this.delay) + curr_green_road;
    }
}

