package ono;

public class Road {

    private Junction start = null, end=null;
    public Road(Junction start, Junction end){

        this.start = start;
        if(start.equals(end)) {
            this.end = new Junction();
            System.out.println("Road can not connect a junction to itself, the end junction has been\n" +
                    "replaced with "+this.end);

        }
        else {
            this.end = end;
            System.out.println("Creating Road from " +start + " to " +end+ String.format(", length: %.2f", getLength()));
        }

        start.add_exit(this);
        end.add_entrance(this);



    }

    public double getLength(){
        return this.start.calcDistance(end);
    }

    public Junction getStart() {
        return start;
    }

    public Junction getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Road from " + start +" to " + end;
    }
}

