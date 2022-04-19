public class Railgun extends Weapon {

    public int shotTimer = 45;
    public int Timer = 0;

    public Railgun(Player player){
        super(player);
    }

    @Override
    public void Update() {
        Mouse mouse = player.level.game.mouse;
        if(mouse.Clicked && Timer <= 0){
            Timer = shotTimer;
            player.shots.add(new Shot(player.ray,150,0,5,player));
            player.level.game.ShakeAmount += 0.2;
        }
        Timer -= 1;
    }

    @Override
    public void Render() {

    }
}
