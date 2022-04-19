import java.util.Random;

public class Bloodsplater {

    public int x = 0;
    public int y = 0;
    public Level level;
    public int Frame;

    public Bloodsplater(int x,int y,Level level){
        this.x = x;
        this.y = y;
        this.level = level;
        this.Frame = new Random().nextInt(level.BloodSplater.Pixels.size());
    }

    public void Render(){
        level.game.drawSprite(level.BloodSplater,x,y,Frame,0xffffff);
    }

}
