public class Projectile {

    public int LifeTime = 240;
    public int Damage = 0;
    public double Speed = 0;
    public double Angle = 0;

    public float real_x = 0;
    public float real_y = 0;

    public int x = 0;
    public int y = 0;

    public boolean Penetration = false;

    public Level level;

    public Projectile(int x,int y,double angle,double speed,int damage,boolean pen,Level level){
        this.x = x;
        this.y = y;
        this.real_x = x;
        this.real_y = y;
        this.Angle = angle;
        this.Speed = speed;
        this.Damage = damage;
        this.level = level;
    }

    public void Update(){
        real_x += Math.cos(Angle)*Speed;
        real_y += Math.sin(Angle)*Speed;
        x = Math.round(real_x);
        y = Math.round(real_y);
        if(WallCollision())
            level.projectiles.remove(this);
        if(PlayerCollsion()){
            level.RealTime -= 4*120;
            level.projectiles.remove(this);
        }
    }

    public void Render(){
        level.game.drawSprite(level.ProjectileSprite,x,y,0,0);
    }

    public boolean WallCollision(){
        for(int i = 0; i < level.tiles.length; i++){
            Tile t = level.tiles[i];
            if( x > t.X - level.block_width/2 && x < t.X + level.block_width/2 && y > t.Y - level.block_width/2 && y < t.Y + level.block_width/2 && t.pass == 1)
                return true;
        }
        return false;
    }

    public boolean PlayerCollsion(){
        Player p = level.player;
        if(p.x > x-5 && p.x < x+5 && p.y > y-5 && p.y < y+5)
            return true;
        return false;
    }
}
