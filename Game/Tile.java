
public class Tile {
    public int x;
    public int y;
    public int X;
    public int Y;
    public int self;

    public int walldraw = 0;

    public int pass = 0;

    public Level level;

    public int[] neighbours;

    public int Timer = 0;

    public Tile(int x,int y,int self,int pass,Level level){
        this.x = x;
        this.y = y;
        this.X = x*level.block_width+level.grid_offset_x;
        this.Y = y*level.block_width+level.grid_offset_y;
        this.self = self;
        this.level = level;
        this.pass = pass;
    }

    public void Render(){

        int lvl_bw = level.block_width;

        if(level.enemies.size() == 0 && level.ExitTile == self){
            Timer ++;
            int f = Timer/5;
            if(f > 5)
                f = 5;
            level.game.drawSprite(level.Exit,X,Y,f,0);
            return;
        }

        if(pass == 0){
            level.game.drawSprite(level.Floor,X,Y,0,0xffffff);
        }else {
            for(int i = 0; i < neighbours.length; i++){
                Tile t = level.tiles[neighbours[i]];
                if(t.pass == 0) {
                    if (t.y == y - 1 && t.x == x)
                        level.game.drawSpriteBrightnessBuffer(level.spr, X, Y, 0);

                    if (t.y == y + 1 && t.x == x) {
                        walldraw = 1;
                        level.game.drawSpriteBrightnessBuffer(level.spr, X, Y, 3);
                    }

                    if (t.x == x + 1 && t.y == y)
                        level.game.drawSpriteBrightnessBuffer(level.spr, X, Y, 1);

                    if (t.x == x - 1 && t.y == y)
                        level.game.drawSpriteBrightnessBuffer(level.spr, X, Y, 2);

                }
                if(t.walldraw == 1){
                    if (t.x == x + 1 && t.y == y)
                        level.game.drawSpriteBrightnessBuffer(level.spr, X, Y, 1);

                    if (t.x == x - 1 && t.y == y)
                        level.game.drawSpriteBrightnessBuffer(level.spr, X, Y, 2);
                }
            }
        }
    }

    public boolean mouse_over(){
        int mx = level.game.mouse.x;
        int my = level.game.mouse.y;
        int bw = level.block_width/2;

        return mx < x*bw*2+level.grid_offset_x+bw && mx > x*bw*2+level.grid_offset_x-bw && my < y*bw*2+level.grid_offset_y+bw && my >y*bw*2+level.grid_offset_y-bw;
    }

    public int[] getNeighbours(){
        int[] a = new int[8];
        for(int i = 0; i < 8; i++){
            a[i] = -1;
        }
        int t = 0;
        Tile [] tiles = level.tiles;
        for(int i = 0; i < tiles.length; i++){
            if(Math.abs(tiles[i].x-x) == 1 && tiles[i].y-y == 0){
                a[t] = i;
                t ++;
            }
            if(Math.abs(tiles[i].y-y) == 1 && tiles[i].x-x == 0){
                a[t] = i;
                t ++;
            }
            if(Math.abs(tiles[i].y-y) == 1 && Math.abs(tiles[i].x-x) == 1){
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
        return f;
    }
}
