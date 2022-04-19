public abstract class Weapon {

    public Player player;

    public Weapon(Player player){
        this.player = player;
    }

    public abstract void Update();

    public abstract void Render();
}
