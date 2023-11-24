package ono;

import java.util.Random;

public class RandomTrafficLight extends TrafficLight {
    public RandomTrafficLight(Junction j) {
        super(j);
    }

    @Override
    protected void setCurr_green_road() {
        int size = this.m_junction.getEnteringRoads().size();
        Random rand = new Random();
        int random_index =rand.nextInt(size);
        this.curr_green_road = this.m_junction.getEnteringRoads().get(random_index);
    }

    @Override
    public String toString() {
        return "Random " + super.toString();
    }

    @Override
    public void run() {
        while (active.get()) {
//            System.out.println("running");
            check();
        }
    }
}

