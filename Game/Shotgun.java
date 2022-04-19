public class Shotgun extends Weapon{

    public int shotTimer = 30;
    public int Timer = 0;

    public int ShotCount = 6;

    public Shotgun(Player player){
        super(player);
    }

    @Override
    public void Update() {
        Mouse mouse = player.level.game.mouse;
        if(mouse.Clicked && Timer <= 0){
            Timer = shotTimer;
            for(int i = 0; i < ShotCount; i++)
            player.shots.add(new Shot(player.ray,40,Math.PI/20,1,player));
            player.level.game.ShakeAmount += 1;
        }
        Timer -= 1;
    }

    @Override
    public void Render() {

    }
}
