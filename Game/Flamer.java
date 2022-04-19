import java.util.ArrayList;
import java.util.Random;

public class Flamer extends Enemy{
    public Random random = new Random();

    public int AttactTimer = 0;
    public int AttackDuration = 30;
    public boolean Attacking = false;

    public int attackCooldown = 100;
    public int CooldownTimer = 0;

    public Flamer(Tile t,Level level){
        super(t.X,t.Y,level,"TripleShot");
        Health = 300;
    }

    @Override
    public void Update() {
        if(getDisForPlayer() <= 500)
            Agroed = true;

        if(Agroed) {
            Raycast ray = new Raycast(new Point(x,y),new Point(level.player.x,level.player.y));
            Point[] points = level.CollisionRayCast(ray,400);
            ArrayList<Integer> path = level.pathFinding.GetPathNoLimit(level.getTile(new Point(x, y)), level.getTile(new Point(player.x, player.y)));

            this.path = new ArrayList<Point>();
            for (int i = 0; i < path.size(); i++) {
                Tile t = level.tiles[path.get(i)];
                this.path.add(new Point(t.X, t.Y));
            }

            if (path.size() > 1 && speed > 0 && !Attacking)
                Move(this.path.get(1));

            if(Attacking){
                AttactTimer += 1;
                if(AttactTimer >= AttackDuration){
                    AttactTimer = 0;
                    Attacking = false;
                    if(level.getCollisionPlayer(points) != -1){
                        level.projectiles.add(new Projectile(x,y,ray.Angle+Math.PI/25,7,damage,false,level));
                        level.projectiles.add(new Projectile(x,y,ray.Angle,7,damage,false,level));
                        level.projectiles.add(new Projectile(x,y,ray.Angle-Math.PI/25,7,damage,false,level));
                    }
                }
            }

            if(getDisForPlayer() < 300 && level.getCollisionPlayer(points) != -1 && CooldownTimer <= 0){
                CooldownTimer = attackCooldown;
                Attacking = true;
            }
            CooldownTimer -= 1;
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
        level.game.drawRect(x,y,25,25,0xffaa00);
    }

    public int getDisForPlayer(){
        return Math.abs(x-player.x)+Math.abs(y-player.y);
    }
}
