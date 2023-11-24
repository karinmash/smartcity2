package ono;

public class Point {
    final double MAX_X = 800;
    final double MAX_Y = 400;
    final double MIN_X= 0, MIN_Y = 0;

    protected double x,y;

    public Point(){
        this.x = get_random(MAX_X, MIN_X);
        this.y = get_random(MAX_Y, MIN_Y);
        //for debugging reasons
        if(! (this instanceof Junction))
            System.out.println(String.format("Creating Point (%.2f, %.2f)", x,y));
    }

    public Point(double raw_x, double raw_y){
        setX(raw_x);
        setY(raw_y);
        //for debugging reasons
        if(! (this instanceof Junction))
            System.out.println(String.format("Creating Point (%.2f, %.2f)", x,y));

    }

    /**
     *
     * @param point
     * @param max
     * @param min
     * @return
     */
    private boolean validate_point(double point , double max, double min) {
        return  point<=max && point>=min;
    }
    private void setX(double x){
        if( validate_point(x, MAX_X, MIN_X))
            this.x = x;
        else{
            this.x = get_random(MAX_X, MIN_X);
            System.out.println(String.format("%.2f is illegal value for x and has been replaced with\n" +
                    "%.2f", x,this.x));
        }
    }
    private void setY(double y){
        if(validate_point(y, MAX_Y, MIN_Y))
            this.y = y;
        else{
            this.y = get_random(MAX_Y, MIN_Y);
            System.out.println(String.format("%.2f is illegal value for y and has been replaced with\n" +
                    "%.2f", y,this.y));
        }
    }


    protected double get_random(double max, double min){
        return (Math.random() * (max - min + 1) + min);

    }

    public double calcDistance(Point other){
        return Math.sqrt((other.y - this.y) * (other.y - this.y) + (other.x - this.x) * (other.x - this.x));
    }


}
