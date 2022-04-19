import org.omg.PortableInterceptor.DISCARDING;

import java.util.ArrayList;
import java.util.Random;

public class Level {

    public PathGrid pathFinding;
    public Random rand;
    public Game game;

    public int depth = 0;

    public int widht = 25;
    public int height = 15;

    public int grid_offset_x = 640;
    public int grid_offset_y = 360;
    public int block_width = 42;

    public Sprite spr = new Sprite("TileWall",8);
    public Sprite BloodSplater = new Sprite("Bloodsplater",6);
    public Sprite Floor = new Sprite("FloorTile",1);
    public Sprite ProjectileSprite = new Sprite("Projectile",1);
    public Sprite EyeSprite = new Sprite("Eye",2);
    public Sprite EyeTracking = new Sprite("EyeTracking",1);
    public Sprite SingleShot = new Sprite("SingleShot",1);
    public Sprite WarHound = new Sprite("WarHound",3);
    public Sprite Exit = new Sprite("NextStage",6);

    public int[][] grid;
    public Tile[] tiles;
    public ArrayList<Integer> OccupiedTiles;

    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Bloodsplater> bloodsplater;
    public ArrayList<Gibblet> gibblets;
    public ArrayList<Projectile> projectiles;

    public int ExitTile = 0;

    public boolean PlayerAttacked = false;
    public int EnemyPassiveTimer = 0;
    public int EnemyPassiveDuration = 120;

    public int NumberOfEnemies = 0;
    public int EnemyVariety = 0;
    public int LevelDifficulty = 0;

    public int RealTime = 30*120;
    public int DisplayTime = 30*120;
    public int dislaySpeed = 3;
    public int MaxTime =30*120;

    public ArrayList<String> EnemiesKilled;

    public int playerWeapon = 1;

    public Level(long seed, int depth, int Type,Game game){
        rand = new Random();
        this.game = game;
        EnemiesKilled = new ArrayList<String>();
        generate();
    }

    public void generate(){

        if(depth != 0){
            RealTime += 10*120;
        }

        depth ++;

        grid_offset_x = game.screen_width/2 - (block_width*(widht-1)/2);
        grid_offset_y = game.screen_height/2 - (block_width*(height-1)/2);

        pathFinding = new PathGrid(widht,height,game);
        grid = new int[widht][height];

        for(int i = 0; i < height; i++){
            for(int o = 0; o < widht; o++){
                grid[o][i] = 1;
            }
        }

        int o_x = 3;
        int o_y = 3;
        int o_dir = 0;
        int o_dir_change = 0;
        int o_dir_lock = 2;
        for(int i = 0; i < 150; i++){
            if(o_dir_change%10 == 0)
                o_dir_lock = Math.abs(rand.nextInt())%4 + 1;
            while (true){
                o_dir_change ++;
                if(o_dir_change%o_dir_lock == 0) {
                    o_dir = Math.abs(rand.nextInt()) % 4;
                }
                if(o_dir == 0 && o_x > 1){
                    o_x -= 1;
                    break;
                }
                if(o_dir == 1 && o_x < widht-2){
                    o_x += 1;
                    break;
                }
                if(o_dir == 2 && o_y > 1){
                    o_y -= 1;
                    break;
                }
                if(o_dir == 3 && o_y < height-2){
                    o_y += 1;
                    break;
                }
            }
            if(grid[o_x][o_y] == 0)
                i -= 1;
            grid[o_x][o_y] = 0;
        }
        int[] g = new int[widht*height];
        for(int i = 0; i < height; i++){
            for(int o = 0; o < widht; o++){
                g[i*widht+o] = grid[o][i];
            }
        }
        pathFinding.SetUpGrid(g);


        tiles = new Tile[widht*height];
        for(int i = 0; i < height; i++){
            for(int o = 0; o < widht; o++){
                tiles[i*widht+o] = new Tile(o,i,i*widht+o,grid[o][i],this);
            }
        }

        for(int i = 0; i < tiles.length; i++){
            tiles[i].neighbours = tiles[i].getNeighbours();
        }
        OccupiedTiles = new ArrayList<Integer>();
        player = new Player(tiles[GetFreeTile()],this);
        player.currentWeapon = player.weapons.get(playerWeapon);


        PlayerAttacked = false;
        EnemyPassiveTimer = 0;

        if(depth == 1){
            NumberOfEnemies = 3;
            EnemyVariety = 1;
        }else if(depth <= 3){
            NumberOfEnemies = 3+rand.nextInt(2);
            EnemyVariety = 2;
        }else{
            NumberOfEnemies = 4+rand.nextInt(3);
            EnemyVariety = 4;
        }



        enemies = new ArrayList<Enemy>();
            for(int i = 0; i < NumberOfEnemies; i++){
                int e = rand.nextInt(EnemyVariety);
                if(e == 0){
                    enemies.add(new Warhound(tiles[GetFreeTile()],this));
                }
                if(e == 1){
                    enemies.add(new SingleShotEnemy(tiles[GetFreeTile()],this));
                }
                if(e == 2){
                    enemies.add(new Lasereyes(tiles[GetFreeTile()],this));
                }
                if(e == 3){
                    enemies.add(new Flamer(tiles[GetFreeTile()],this));
                }

            }



        bloodsplater = new ArrayList<Bloodsplater>();
        gibblets = new ArrayList<Gibblet>();
        projectiles = new ArrayList<Projectile>();
        ExitTile = GetFreeTile();

    }

    public void Update(){
        EnemyPassiveTimer ++;



        if(game.mouse.Clicked || EnemyPassiveTimer >= EnemyPassiveDuration)
            PlayerAttacked = true;

        player.Update();
        for(int i = 0; i < tiles.length; i++) {
            if (tiles[i].mouse_over())
                pathFinding.end = i;
        }
        if(PlayerAttacked)
        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.Update();
        }

        for(int i = 0; i < gibblets.size(); i++){
            gibblets.get(i).Update();
        }
        for(int i = 0; i < projectiles.size(); i++){
            projectiles.get(i).Update();
        }

        ManageTime();

        if(PlayerOnTile(ExitTile) && enemies.size() == 0)
            generate();



    }


    public void Render(){

        Point[] points = CollisionRayCast(player.ray,400);


        for(int i = 0; i < tiles.length; i++){
            tiles[i].Render();
        }

        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.Render();
        }
        for(int i = 0; i < bloodsplater.size(); i++){
            bloodsplater.get(i).Render();
        }
        for(int i = 0; i < gibblets.size(); i++){
            gibblets.get(i).Render();
        }
        for(int i = 0; i < projectiles.size(); i++){
            projectiles.get(i).Render();
        }
        drawTimeBar();
        player.Render();

    }

    public Point[] CollisionRayCast(Raycast ray,int len){
        Point[] points = new Point[len];

        for(int i = 0; i < len; i++){
            int x = (int)Math.round(Math.cos(ray.Angle)*i)+ray.p1.x;
            int y = (int)Math.round(Math.sin(ray.Angle)*i)+ray.p1.y;
            points[i] = new Point(x,y);
            if(checkCollsionWithBlock(points[i])){
                len = i;
                break;
            }
        }

        Point[] p = new Point[len];

        for(int i = 0; i < len; i++){
            p[i] = points[i];
        }

        return p;
    }

    public boolean checkCollsionWithBlock(Point p){
        for(int i = 0; i < tiles.length; i++){
            Tile t = tiles[i];
            if(t.pass == 1){
                if(p.x >= t.X - block_width/2 && p.x < t.X + block_width/2 && p.y >= t.Y - block_width/2 && p.y < t.Y + block_width/2)
                    return true;
            }
        }
        return false;
    }

    public int getTile(Point p){
        for(int i = 0; i < tiles.length; i++){
            Tile t = tiles[i];

                if(p.x >= t.X - block_width/2 && p.x < t.X + block_width/2 && p.y >= t.Y - block_width/2 && p.y < t.Y + block_width/2)
                    return i;

        }
        return -1;
    }

    public Enemy getEnemyInShot(Point p){
        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            if(p.x >= e.x - e.Collision_X/2 && p.x < e.x + e.Collision_X/2 && p.y >= e.y - e.Collision_Y/2 && p.y < e.y + e.Collision_Y/2){
                return e;
            }
        }
        return null;
    }

    public int getCollisionPlayer(Point[] points){
        for(int i = 0; i < points.length; i++){
            Point p = points[i];
            if(p.x > player.x - 5 && p.x < player.x + 5 && p.y > player.y - 5 && p.y < player.y + 5)
                return i;
        }
        return -1;
    }

    public int GetFreeTile(){
        while(true){
            int r = rand.nextInt(tiles.length);
            if(tiles[r].pass == 0 && !OccupiedTiles.contains(r)){
                OccupiedTiles.add(r);
                return r;
            }
        }
    }

    public void drawTimeBar(){
        int width = 500;
        int height = 30;
        game.drawRect(game.screen_width/2,50,width,height,0xffffff);
        game.drawRect(game.screen_width/2,50,width-2,height-2,0x000000);
        int w1 = Math.round(width*(float)RealTime/MaxTime);
        int w2 = Math.round(width*(float)DisplayTime/MaxTime);

        if(w1 == w2){
            game.drawRect(game.screen_width/2,50,w1,height,0xff00ff);
        }
        if(w2 > w1){
            game.drawRect(game.screen_width/2,50,w2,height,0xff0000);
            game.drawRect(game.screen_width/2,50,w1,height,0xff00ff);
        }
        if(w2 < w1){
            game.drawRect(game.screen_width/2,50,w1,height,0x00ff00);
            game.drawRect(game.screen_width/2,50,w2,height,0xff00ff);
        }
    }

    public void ManageTime(){
        RealTime -= 3;
        dislaySpeed ++;
        if(RealTime > MaxTime){
            RealTime = MaxTime;
        }
        if(DisplayTime < RealTime){
            DisplayTime += dislaySpeed;
            if(DisplayTime > RealTime){
                DisplayTime = RealTime;
                dislaySpeed = 3;
            }
        }
        if(DisplayTime >RealTime){
            DisplayTime -= dislaySpeed;
            if(DisplayTime < RealTime) {
                DisplayTime = RealTime;
                dislaySpeed = 3;
            }
        }

        if(RealTime <= 0){
            for(int i = 0; i < EnemiesKilled.size(); i++){
                System.out.println(EnemiesKilled.get(i));
            }
            game.gameOver = new GameOver(EnemiesKilled,depth,0,game);
        }
    }

    public boolean PlayerOnTile(int T){
        Tile t = tiles[T];
        if(player.x > t.X-block_width/2 && player.x < t.X+block_width/2 && player.y > t.Y-block_width/2 && player.y < t.Y+block_width/2)
            return true;
        return false;
    }
}
