package ono;

import javafx.scene.effect.Light;

import java.util.concurrent.atomic.AtomicBoolean;

public class Vehicle implements Runnable {
    int speed =0;
    AtomicBoolean is_active = new AtomicBoolean(true);
    public Route getRandom_route() {
        return random_route;
    }

    public void setRandom_route(Route random_route) {
        this.random_route = random_route;
    }

    private Route random_route = null;
    static int total_created = 0;
    private int id;
    private int curr_road = 1;
    private double passed_length = 0;
    public Vehicle(Map map){
        speed = (int)(Math.random() * (120 - 65 ) + 65) ;
        random_route = map.createRoute();
        id = ++total_created;

        System.out.println("Creating " + this.toString() + " ");
    }
    public void move(){
        System.out.println(String.format("Vehicle %d is moving on the ", id) + random_route.getRoad(curr_road));
        passed_length+=speed;
        if (passed_length >=random_route.getRoad(curr_road).getLength() && curr_road<random_route.num_of_roads()) {
            TrafficLight light = random_route.getRoad(curr_road).getEnd().getLight();
            if(light == null){
                System.out.println(String.format("Vehicle %d is passing %s as it does not have light at junction", this.id, random_route.getRoad(curr_road).getEnd()));
                return;
            }
            if(!random_route.getRoad(curr_road).equals(light.getCurrentGreen()))
                System.out.println(String.format("Vehicle %d is waiting for green light at %s", this.id, random_route.getRoad(curr_road).getEnd()));
            while(!random_route.getRoad(curr_road).equals(light.getCurrentGreen())) {
                try {
//                System.out.println("waiting");
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            curr_road++;
            passed_length = 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Vehicle %d, speed: %d, path: ", id, speed) + random_route;
    }



    @Override
    public void run() {
        while (curr_road<random_route.num_of_roads()){

            move();
        }
        System.out.println(String.format("Vehicle %d arrived to its destination: ", id) + " " + random_route.getRoad(curr_road-1).getEnd());
        is_active.set(false);

    }

    boolean isActive(){
        return this.is_active.get();
    }
}

