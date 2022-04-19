import java.util.ArrayList;

public class PathGrid {

    public int Height;
    public int Width;
    public Game game;

    public PathNode[] nodes;

    public int start = 26;
    public int end = -1;


    public  PathGrid(int w,int h,Game game){
        Height = h;
        Width = w;
        this.game = game;
    }

    public void SetUpGrid(int [] grid){
        nodes = new PathNode[Height*Width];
        for(int i = 0; i < Height; i++){
            for(int o = 0; o < Width; o++){
                 nodes[o+i*Width] = new PathNode(o,i,this);
            }
        }

        for(int i = 0; i < grid.length; i++){
            nodes[i].setPass(grid[i]);
        }

        for(int i = 0; i < nodes.length; i++){
            nodes[i].getNeighbours();
        }
    }

    public void Render(){
        ArrayList<Integer> path = null;
        if(end != -1)
           path = GetPathNoLimit(start,end);

        for(int i = 0; i < nodes.length; i++){
            PathNode pn = nodes[i];
            if(pn.pass == 0){
                game.drawRect(pn.x * 20 +20,pn.y * 20 + 20,19 ,19,0x00ff00);
            }else{
                game.drawRect(pn.x * 20 +20,pn.y * 20 + 20,19 ,19,0xff0000);
            }
            if(path != null)
                if(path.contains(i))
                    game.drawRect(pn.x * 20 +20,pn.y * 20 + 20,19 ,19,0xffff00);
        }
    }

    public ArrayList<Integer> GetPathNoLimit(int Str, int End){
        for(int i = 0; i < nodes.length; i++){
            nodes[i].dis = 1000000000;
        }

        if(Str == End)
            return new ArrayList<Integer>();

        nodes[Str].RecursiveSearch(0,-1);
        ArrayList<Integer> path = nodes[End].TracePath(Str);

        return path;
    }

    public ArrayList<Integer> GetPathLimit(int Str, int End,int lim){
        for(int i = 0; i < nodes.length; i++){
            nodes[i].dis = 1000000000;
        }

        nodes[Str].RecursiveSearch(0,lim);
        ArrayList<Integer> path = nodes[End].TracePath(Str);

        return path;
    }

}
