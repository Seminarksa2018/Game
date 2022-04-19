import java.util.ArrayList;

public abstract class Enemy {

    public int x = 0;
    public int y = 0;
    public String Name;
    public Level level;
    public Player player;

    public int Health = 80;

    public int damage = 5*120;
    public int heal = 2*120;

    public int speed_max = 3;
    public float speed = 4;
    public float speed_recovery = 0.2f;

    public int Collision_X = 25;
    public int Collision_Y = 25;

    public boolean Agroed = false;

    public ArrayList<Point> path;

    public Enemy(int x,int y,Level level,String Name){
        this.x = x;
        this.y = y;
        this.Name = Name;
        this.level = level;
        this.player = level.player;
    }

    public abstract void Update();

    public abstract void Render();

    public void update(){

        if(speed < speed_max){
            speed += speed_recovery;
            if(speed > speed_max)
                speed = speed_max;
        }
        if(Health <= 0){
            level.EnemiesKilled.add(Name);
        }
    }

    public void Move(Point p){
        if(p.x != x){
            if(x < p.x){
                x += Math.round(speed);
                if(x > p.x)
                    x = p.x;
            }else{
                x -= Math.round(speed);
                if(x < p.x)
                    x = p.x;
            }
        }

        if(p.y != y){
            if(y < p.y){
                y += Math.round(speed);
                if(y > p.y)
                    y = p.y;
            }else{
                y -= Math.round(speed);
                if(y < p.y)
                    y = p.y;
            }
        }
    }

    public void TakeDamage(Shot shot){
        Health -= shot.damage;
        speed -= 2;
        if(speed < -2){
            speed = -2;
        }
        Agroed = true;
    }
}
