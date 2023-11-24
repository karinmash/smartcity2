package ono;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Map implements Runnable {
    private ArrayList<Junction> junctions= null;
    private ArrayList<Road> roads= null;
    private ArrayList<TrafficLight> trafficLights = new ArrayList<>();
    AtomicBoolean active = new AtomicBoolean(true);


    public Map(int wanted_junctions){
        build_junctions(wanted_junctions);
        build_roads(wanted_junctions);
        build_trafficLights(wanted_junctions);

    }
    public Map(ArrayList<Junction> junctions,ArrayList<Road>roads){
        this.junctions = junctions;
        this.roads = roads;
        build_trafficLights(junctions.size());
    }

    private void build_junctions(int wanted_junctions){
        junctions = new ArrayList<Junction>();
        for(int i=0;i<wanted_junctions;i++)
            junctions.add(new Junction());
    }
    private void build_roads(int wanted_roads){
        Random  random =  new Random();
        roads = new ArrayList<Road>();
        boolean found = false;
        for(int i=0;i<wanted_roads;i++)
        {
            found = false;
            for(int j=0;j<wanted_roads;j++){
                if(random.nextBoolean() && i!=j){
                    found = true;
                    roads.add(new Road(junctions.get(i), junctions.get(j)));
                }
            }
            //TODO if we did not happen to create a road for this junction, we want to force once
            if(!found){
                //debugging
                System.out.println(String.format("no created from index %d", i));
            }
        }
    }
    private void build_trafficLights(int num_of_junctions){

        Random r = new Random();
        int low = 1;
        int high = 5;

        Random  random =  new Random();
        for(int i=0;i<junctions.size();i++){
            int result = r.nextInt(high-low) + low;

//            if(result != 4)
//                continue;
            if(junctions.get(i).getEnteringRoads().size() ==0)
                continue;
            if(random.nextBoolean())
                junctions.get(i).setLight(new RandomTrafficLight(junctions.get(i)));
            else
                junctions.get(i).setLight(new ConsecutiveTrafficLight(junctions.get(i)));
            trafficLights.add(junctions.get(i).getLight());
        }


    }
    public Route createRoute(){

        int route_length =1;
        ArrayList<Road> random_route = new ArrayList<Road>();
        Integer[] array = new Integer[junctions.size()];
        for(int i=0;i<junctions.size();i++)
            array[i] = i;
        List<Integer> l = Arrays.asList(array);
        Collections.shuffle(l);

        random_route.add(new Road(junctions.get(l.get(0)), junctions.get(l.get(1))));
        for(int i=1;i<junctions.size()-1;i++){
            if(route_length>=4 || junctions.get(l.get(i)).getExitingRoads().size() ==0)
                break;
            route_length++;
            random_route.add(new Road(junctions.get(l.get(i)), junctions.get(l.get(i+1))));
        }
        return new Route(random_route);
    }

    public void checkLights(){
        for(int i=0;i<junctions.size();i++){
            //not all junctions have lights
            if(junctions.get(i).getLight()!=null)
                junctions.get(i).getLight().check();
        }

    }
    public Route calcShortestPath(Junction start, Junction end){
        Boolean[] visited = new Boolean[junctions.size()];
        Arrays.fill(visited, Boolean.FALSE);

        Double[] lengths = new Double[junctions.size()];
        Arrays.fill(lengths, Double.MAX_VALUE);

        ArrayList<Junction> points_on_route =
                new ArrayList<>(Collections.nCopies(junctions.size(), null));


        int start_index = junctions.indexOf(start);
        int end_index = junctions.indexOf(end);
        lengths[start_index] = (double)0;

        while(!visited[end_index]){
            int min = min_index(visited, lengths); //TODO, if returns -1, there is logic error
            visited[min] = true;
            relax(min, visited, lengths, points_on_route);
        }
        return create_road(start_index, end_index, points_on_route);
    }
    private void relax(int src_index, Boolean[] visited, Double[] lengths, ArrayList<Junction> points_on_route){
        for(int i=0;i<this.roads.size();i++){
            if(roads.get(i).getStart() == junctions.get(src_index)){
                int end_index = junctions.indexOf(roads.get(i).getEnd());
                if(lengths[end_index] > lengths[src_index] +
                        junctions.get(end_index).calcDistance(junctions.get(src_index))){
                    lengths[end_index] = lengths[src_index] +
                            junctions.get(end_index).calcDistance(junctions.get(src_index));
                    points_on_route.set(end_index , junctions.get(src_index));
                }

            }
        }
    }
    private Route create_road(int start, int end, ArrayList<Junction> points_on_route){
        Route route = new Route(null);
        if(points_on_route.get(end) == null && end!=start)
            return null; //no route.
        int i = end;
        while(i!=start){
            Junction start_j = points_on_route.get(i);
            Junction end_j = this.junctions.get(i);
            Road r =find_road(start_j, end_j);//TODO exception if null
            route.addRoad(0, r);
            i=this.junctions.indexOf(start_j);
        }
        return route;
    }

    private int min_index(Boolean[] visited, Double[] lengths){
        double min = Double.MAX_VALUE;
        int min_index = -1;
        for(int i=0;i<lengths.length;i++) {
            if (!visited[i] && lengths[i] <= min) {
                min = lengths[i];
                min_index = i;
            }
        }
        return min_index;
    }

    private Road find_road(Junction start, Junction end){
        for(int i=0;i<roads.size();i++){
            if(roads.get(i).getStart().equals(start) &&
                    roads.get(i).getEnd().equals(end))
                return roads.get(i);
        }
        return null;
    }

    public ArrayList<Junction> getJunctions() {
        return junctions;
    }

    @Override
    public void run() {
//        System.out.println("running");

        ExecutorService e = Executors.newFixedThreadPool(trafficLights.size());

        for(int i =0;i<trafficLights.size();i++){
            e.execute(new Thread(trafficLights.get(i)));
        }
        e.shutdown();
        while (active.get()){ }
        for (TrafficLight light:trafficLights
             ) {
            light.stop();

        }
        return;

    }

    public void deactivate() {
        this.active.set(false);
    }
    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}