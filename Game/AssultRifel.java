public class AssultRifel extends Weapon {

    public int shotTimer = 5;
    public int Timer = 0;

    public AssultRifel(Player player){
        super(player);
    }

    @Override
    public void Update() {
        Mouse mouse = player.level.game.mouse;
        if(mouse.Clicked && Timer <= 0){
            Timer = shotTimer;
            player.shots.add(new Shot(player.ray,40,Math.PI/50,1,player));
            player.level.game.ShakeAmount += 0.1;
        }
        Timer -= 1;
    }

    @Override
    public void Render() {

    }
}
