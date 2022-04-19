public class Gibblet {

    public int x = 0;
    public int y = 0;
    public int z = 0;

    public float vel_x = 0;
    public float vel_y = 0;
    public float vel_z = 0;

    public float gravity = 1.5f;

    public Sprite spr;

    public Level level;

    public boolean HasCreatedBloodSplater = false;


    public Gibblet(int x,int y,float vel_x,float vel_y,float vel_z,String spr, Level level){
        this.x = x;
        this.y = y;
        this.vel_x = vel_x;
        this.vel_y = vel_y;
        this.vel_z = vel_z;
        this.level = level;
    }

    public void Update(){
        x += vel_x;
        y += vel_y;
        z += vel_z;
        vel_z -= gravity;
        if(z <= 0) {
            if(!HasCreatedBloodSplater){
                HasCreatedBloodSplater = true;
                level.bloodsplater.add(new Bloodsplater(x,y,level));
            }
            z = 0;
            vel_z = 0;
            vel_x = 0;
            vel_y = 0;
        }
    }

    public void Render(){
        level.game.drawRect(x,y-z,10,10,0xffffff);
    }
}
