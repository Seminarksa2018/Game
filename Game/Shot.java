import java.util.ArrayList;
import java.util.Random;

public class Shot {

    public Raycast ray;
    public int damage;
    public Point[] points;
    public Player player;

    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public int LifeTime = 20;
    public int bullet = 0;


    public Shot(Raycast ray,int damage, double inac,int pen, Player player){
        Random rand = new Random();
        this.ray = new Raycast(new Point(player.x,player.y),new Point(player.level.game.mouse.x,player.level.game.mouse.y));
        this.damage = damage;
        int t = rand.nextInt();
        if(t < 0){
            t = -1;
        }else{
            t = 1;
        }
        this.ray.Angle += (rand.nextDouble())*inac*t;
        this.player = player;
        points = player.level.CollisionRayCast(this.ray,400);

        for(int i = 0; i < points.length; i++){
            Enemy e = player.level.getEnemyInShot(points[i]);
            if(e != null && !enemies.contains(e)){
                e.TakeDamage(this);
                enemies.add(e);
                int d = rand.nextInt(1)+1;

                for(int o = i; o < points.length; o++){
                    if(rand.nextInt(15) == 0 && d >= 0) {
                        player.level.bloodsplater.add(new Bloodsplater(points[o].x, points[o].y, player.level));
                        d -= 1;
                    }

                }
                if(pen == 1) {
                    pen -= 1;
                    break;
                }
            }
        }
    }

    public void Update(){
        LifeTime -= 1;
        if(LifeTime <= 0){
            player.shots.remove(this);
        }
        bullet += 20;

    }
}
