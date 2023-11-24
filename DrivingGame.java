package ono;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DrivingGame {
    private Map map = null;
    ArrayList<Vehicle> vehicles = null;
    public DrivingGame(int junctions, int vehicles){
        map = new Map(junctions);
        this.createVehicles(vehicles);

    }
    private void createVehicles(int num){
        Random r = new Random();

        this.vehicles = new ArrayList<Vehicle>();
        for(int i=0;i<num;i++) {
            Vehicle v = new Vehicle(map);
            this.vehicles.add(v);
            if(r.nextBoolean() && !v.getRandom_route().isCircleRoute()){
                Road start = v.getRandom_route().getRoad(0);
                Road end = v.getRandom_route().getRoad(v.getRandom_route().num_of_roads()-1);

                Route shortest = map.calcShortestPath(start.getStart(), end.getEnd());
                v.setRandom_route(shortest);
                System.out.println(String.format("The path has been replaced with the shortest one: %s", shortest));
            }
        }
    }
    public void play(int turns){
        for(int i=0;i<turns;i++){
            System.out.println(String.format("\nTurn %d", i+1));
            for(int j =0;j<vehicles.size();j++){
                vehicles.get(j).move();
            }
            map.checkLights();
        }
    }

    public void play(){
        Timer t = new Timer(1);

        ArrayList<Thread> threads= new ArrayList<Thread>();

        ExecutorService e = Executors.newFixedThreadPool(vehicles.size());
        ExecutorService others = Executors.newFixedThreadPool(2);
//        threads.add(new Thread(t));
//        threads.add(new Thread(map));

        for(int i = 0; i< vehicles.size();i++){
            threads.add( new Thread(vehicles.get(i)));
        }

        for(int i =0;i<threads.size();i++){
            e.execute(threads.get(i));
        }
        others.execute(new Thread(t));
        others.execute(new Thread(map));
        e.shutdown();
        others.shutdown();

        while(!vehiclesFinished()) {
            try {
//                System.out.println("waiting");
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        //get here when vehicles finish.
        map.deactivate();
        t.shutdown();
//        awaitTerminationAfterShutdown(others);
        System.out.println("All vehicles have arrived to their destination.");




    }
    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    boolean vehiclesFinished(){
        for (int i=0;i<this.vehicles.size();i++)
        {
            if(vehicles.get(i).isActive()) {
                return false;

            }
        }
        return true;
    }

}
