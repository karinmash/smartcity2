package ono;

public class ConsecutiveTrafficLight  extends TrafficLight {
    private int curr_greenLight_index = 0;
    public ConsecutiveTrafficLight(Junction j) {
        super(j);
    }

    @Override
    protected void setCurr_green_road() {
        this.curr_green_road = this.m_junction.getEnteringRoads().get((curr_greenLight_index++%this.m_junction.getEnteringRoads().size()));
    }

    @Override
    public String toString() {
        return "Sequential " + super.toString();
    }

    @Override
    public void run() {
        while(active.get()) {
//            System.out.println("running");

            check();
        }
    }
    

}
