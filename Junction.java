package ono;


import java.util.ArrayList;
import java.util.Random;

public class Junction extends Point {
    private ArrayList<Road> enteringRoads, exitingRoads;
    private TrafficLight light = null;

    int id = 0;
    static int total_created=1;

    public Junction(){
        super();
        init();
    }
    public Junction(double x, double y){
        super(x,y);
        init();
    }

    private void init(){
        this.id = total_created++;
        System.out.println(String.format("Creating Junction %d at Point (%.2f, %.2f)",this.id, this.x, this.y));
        enteringRoads = new ArrayList<Road>();
        exitingRoads = new ArrayList<Road>();
//        Random random =  new Random();
//        if(random.nextBoolean()){
//            this.light = new RandomTrafficLight(this);
//        }

    }

    public void add_entrance(Road r){
        enteringRoads.add(r);
    }
    public void add_exit(Road r){
        exitingRoads.add(r);
    }

    @Override
    public String toString() {
        return "Junction " + id ;
    }

    /**
     * Gets entering_roads.
     *
     * @return Value of entering_roads.
     */
    public ArrayList<Road> getEnteringRoads() {
        return enteringRoads;
    }

    /**
     * Gets exitingRoads.
     *
     * @return Value of exitingRoads.
     */
    public ArrayList<Road> getExitingRoads() {
        return exitingRoads;
    }

    /**
     * Sets new light.
     *
     * @param light New value of light.
     */
    public void setLight(TrafficLight light) {
        this.light = light;
    }

    /**
     * Gets light.
     *
     * @return Value of light.
     */
    public TrafficLight getLight() {
        return light;
    }
}

