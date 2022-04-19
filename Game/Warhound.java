import java.util.ArrayList;
import java.util.Random;

public class Warhound extends Enemy{

    public Random random = new Random();

    public int AttactTimer = 0;
    public int AttackDuration = 30;
    public boolean Attacking = false;




    public Warhound(Tile t,Level level){
        super(t.X,t.Y,level,"WarHound");
        Health = 300;
    }

    @Override
    public void Update() {
        if(getDisForPlayer() <= 300)
            Agroed = true;

        if(Agroed) {
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
                    if(getDisForPlayer() <= 60) {
                        level.RealTime -= damage*2;
                        for(int i = 0; i <15; i++)
                        level.gibblets.add(new Gibblet(x,y,random.nextInt()%4,random.nextInt()%4,random.nextInt()%5+25,"None",level));
                        level.enemies.remove(this);
                    }
                }
            }

            if(getDisForPlayer() < 60){
                Attacking = true;
            }
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
        if(!Attacking)
        level.game.drawSpriteScaleable(level.WarHound,x,y,0,1.3f);
        else{
            int f = (AttactTimer/5)%2+1;
            level.game.drawSpriteScaleable(level.WarHound,x,y,f,1.3f);
        }
    }

    public int getDisForPlayer(){
        return Math.abs(x-player.x)+Math.abs(y-player.y);
    }
}
