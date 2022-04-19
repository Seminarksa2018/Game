public class Raycast {

    public double Angle = 0;

    public Point p1;
    public Point p2;

    public Raycast(Point p1,Point p2){
        this.p1 = p1;
        this.p2 = p2;
        Angle = getAngle(p1,p2);
    }

    public void Update(){
        Angle = getAngle(p1,p2);
    }

    public double getAngle(Point p1,Point p2){
        if(p1.y == p2.y){
            if(p2.x > p1.x){
                return 0;
            }else{
                return Math.PI;
            }
        }
        if(p1.x == p2.x){
            if(p2.y > p1.y){
                return Math.PI/2;
            }else{
                return -Math.PI/2;
            }
        }

        double k = (double)(p2.y - p1.y)/(double)(p2.x - p1.x);

        double angle = Math.atan(k);
        if(p2.x < p1.x){
            if(p2.y < p1.y){
                return -Math.PI + angle;
            }else{
                return Math.PI+angle;
            }
        }
        return angle;
    }
}
