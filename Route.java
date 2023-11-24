package ono;

import java.util.ArrayList;

public class Route {
    private ArrayList<Road> random_route ;
    public Route(ArrayList<Road>route){

        if(route!=null)
            random_route = route;
        else
            random_route =new ArrayList<>();

    }

    public Road getRoad(int index){
        return random_route.get(index);
    }

    public double getLength(){
        double sum = 0;
        for(int i=0;i<random_route.size();i++){
            sum += random_route.get(i).getLength();
        }
        return sum;
    }
    public int num_of_roads(){
        return random_route.size();
    }

    @Override
    public String toString() {
        return this.random_route.toString();
    }

    public void addRoad(int i, Road r) {
        this.random_route.add(i, r);
    }

    public boolean isCircleRoute(){
        return this.random_route.get(0).equals(this.random_route.get(this.random_route.size()-1));
    }
}

