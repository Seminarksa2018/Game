import java.util.ArrayList;
import java.util.Random;

public class Lasereyes extends Enemy {

    public Raycast ray;
    public Random random = new Random();
    public Point[] points;

    public boolean Attacking = false;
    public int AttackTimer = 0;
    public int AttackTimerDuration = 60;
    public int AttackStunDuration = 70;

    public Point[] laser_point;
    public int laser_timer = -1;
    public int laser_timer_duration = 10;
    public int[] color;

    public Lasereyes(Tile tile, Level level) {
        super(tile.X, tile.Y, level, "LaserEyes");
        Health = 200;
    }

    @Override
    public void Update() {
        if(getDisForPlayer() <= 300)
            Agroed = true;

        if(Agroed) {
            ArrayList<Integer> path = level.pathFinding.GetPathNoLimit(level.getTile(new Point(x, y)), level.getTile(new Point(player.x, player.y)));
            if(getDisForPlayer() < 300 && !Attacking){
                ray = new Raycast(new Point(x,y),new Point(level.player.x,level.player.y));
                points = level.CollisionRayCast(ray,600);
                if(level.getCollisionPlayer(points) != -1){
                    Attacking = true;
                }
            }else{
                ray = null;
            }
            this.path = new ArrayList<Point>();
            for (int i = 0; i < path.size(); i++) {
                Tile t = level.tiles[path.get(i)];
                this.path.add(new Point(t.X, t.Y));
            }

            if(Attacking){
                AttackTimer++;
                if(AttackTimer == AttackTimerDuration){
                    ray = new Raycast(new Point(x,y),new Point(level.player.x,level.player.y));
                    points = level.CollisionRayCast(ray,1000);
                    int t = level.getCollisionPlayer(points);
                    if(t != -1){
                        level.RealTime -= damage;
                    }
                    laser_timer = laser_timer_duration;
                    laser_point = new Point[points.length];
                    for(int i = 0; i < laser_point.length; i++){
                        laser_point[i] = new Point(points[i].x,points[i].y);
                    }
                    color = new int[laser_point.length];
                    for(int i = 0; i < color.length; i++){
                        color[i] = random.nextInt(155)+100;
                    }
                }
                if(AttackTimer >= AttackStunDuration){
                    AttackTimer = 0;
                    Attacking = false;
                }
            }

            if (path.size() > 1 && speed > 0 && !Attacking)
                Move(this.path.get(1));

        }
        update();

        if(Health <= 0){
            for(int i = 0; i <5; i++)
                level.gibblets.add(new Gibblet(x,y,random.nextInt()%4,random.nextInt()%4,random.nextInt()%5+25,"None",level));
            level.RealTime += heal;
            level.enemies.remove(this);
        }
    }

    @Override
    public void Render() {
        if(!Agroed) {
            level.game.drawSprite(level.EyeSprite, x, y, 0, 0);
        }else{
            Raycast ray = new Raycast(new Point(x,y),new Point(player.x,player.y));
            int g = 6;
            if(Attacking)
                g = 10;
            level.game.drawSprite(level.EyeSprite, x, y, 1, 0);
            level.game.drawSprite(level.EyeTracking, x+(int)(g*Math.cos(ray.Angle)), y+(int)(g*Math.sin(ray.Angle)), 0, 0);
        }

        if(laser_timer >= 0){
            int[] c = new int[color.length];
            for(int i = 0; i < c.length; i++){
                c[i] = color[i]*256*256;
            }
            if(laser_point != null && color != null)
            level.game.drawPlasmaBeam(laser_point,c);
        }
        laser_timer -= 1;

    }

    public int getDisForPlayer(){
        return Math.abs(x-player.x)+Math.abs(y-player.y);
    }
}
