import java.util.ArrayList;

public class PathNode {

    public int x = 0;
    public int y = 0;
    public int self = 0;
    public PathGrid pg = null;

    public int[] neighbours;
    public int pass = 0;
    public PathNode[] nodes;
    public int dis;

    public PathNode(int x,int y,PathGrid pg){
        this.pg = pg;
        this.self = pg.Width*y+x;
        this.x = x;
        this.y = y;
    }

    public void setPass(int p){
        pass = p;
    }

    public void getNeighbours(){
        int[] a = new int[8];
        for(int i = 0; i < 8; i++){
            a[i] = -1;
        }
        int t = 0;
        nodes = pg.nodes;
        for(int i = 0; i < nodes.length; i++){
            if(Math.abs(nodes[i].x-x) == 1 && nodes[i].y-y == 0){
                a[t] = i;
                t ++;
            }
            if(Math.abs(nodes[i].y-y) == 1 && nodes[i].x-x == 0){
                a[t] = i;
                t ++;
            }
        }

        int[] f = new int[t];
        int m = 0;

        for(int i = 0; i < a.length; i++){
            if(a[i] != -1){
                f[m] = a[i];
                m++;
            }
        }
        neighbours = f;
    }

    public void RecursiveSearch(int dis, int lim){
        this.dis = dis;
        dis ++;
        if(dis > lim && lim != -1)
            return;
        for(int i = 0; i < neighbours.length; i++){
            PathNode pn = nodes[neighbours[i]];
            if(pn.dis > dis && pn.pass == 0)
                pn.RecursiveSearch(dis,lim);
        }
    }

    public ArrayList<Integer> TracePath(int Start){
        if(self == Start){
            ArrayList<Integer> path = new ArrayList<Integer>();
            path.add(self);
            return path;
        }
        int m = 0;
        for(int i = 0; i < neighbours.length; i++){
                if(nodes[neighbours[m]].dis > nodes[neighbours[i]].dis)
                    m = i;
        }

        ArrayList<Integer> path = nodes[neighbours[m]].TracePath(Start);
        path.add(self);
        return path;
    }

}
