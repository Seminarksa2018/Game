import java.util.ArrayList;

public class Player {
    public int x = 0;
    public int y = 0;

    public Level level;

    public float speed_x = 0;
    public float speed_y = 0;
    public float speed = 5;
    public float acc = 1f;
    public float dec = 0.8f;

    public float force_x = 0;
    public float force_y = 0;

    public int collsion_height = 15;
    public int collsion_width = 15;

    public ArrayList<Weapon> weapons;
    public Weapon currentWeapon;

    public ArrayList<Shot> shots = new ArrayList<Shot>();
    public Sprite scroll = new Sprite("Scroll",1);
    public Raycast ray;

    public Player(Tile t, Level level){
        x = t.X;
        y = t.Y;
        this.level = level;
        this.ray = new Raycast(new Point(x,y),new Point(level.game.mouse.x,level.game.mouse.y));
        weapons = new ArrayList<Weapon>();
        weapons.add(new AssultRifel(this));
        weapons.add(new Shotgun(this));
        weapons.add(new Railgun(this));
        currentWeapon = weapons.get(1);
    }

    public void Update(){
        Move();
        ray.p1 = new Point(x,y);
        ray.p2 = new Point(level.game.mouse.x,level.game.mouse.y);
        ray.Update();

        currentWeapon.Update();

        if(level.game.keyboard.keys[49]) {
            currentWeapon = weapons.get(0);
            level.playerWeapon = 0;
        }
        if(level.game.keyboard.keys[50]) {
            currentWeapon = weapons.get(1);
            level.playerWeapon = 1;
        }
        if(level.game.keyboard.keys[51]) {
            currentWeapon = weapons.get(2);
            level.playerWeapon = 2;
        }

        for(int i = 0; i < shots.size(); i++){
            shots.get(i).Update();
        }


    }

    public void Render(){
        //level.game.drawRect(x,y,16,16,0x0000ff);
        level.game.drawSprite(scroll,x,y,0,0);
        for(int i = 0; i < shots.size(); i++){
            level.game.drawShot(shots.get(i).points,(20-shots.get(i).LifeTime)*18,0xffff55);
        }
        currentWeapon.Render();
    }

    public void Move(){
        Keyboard keyboard = level.game.keyboard;

        if(keyboard.keys[65]){
            speed_x -= acc;
            if(speed_x < -speed){
                speed_x = -speed;
            }
        }

        if(keyboard.keys[68]){
            speed_x += acc;
            if(speed_x > speed){
                speed_x = speed;
            }
        }

        if(!keyboard.keys[68] && !keyboard.keys[65]){
            if(speed_x > 0){
                speed_x -= dec;
                if(speed_x < 0)
                    speed_x = 0;
            }

            if(speed_x < 0){
                speed_x += dec;
                if(speed_x > 0)
                    speed_x = 0;
            }
        }

        if(keyboard.keys[87]){
            speed_y -= acc;
            if(speed_y < -speed){
                speed_y = -speed;
            }
        }

        if(keyboard.keys[83]){
            speed_y += acc;
            if(speed_y > speed){
                speed_y = speed;
            }
        }

        if(!keyboard.keys[83] && !keyboard.keys[87]){
            if(speed_y > 0){
                speed_y -= dec;
                if(speed_y < 0)
                    speed_y = 0;
            }

            if(speed_y < 0){
                speed_y += dec;
                if(speed_y > 0)
                    speed_y = 0;
            }
        }
        if(speed_x+force_x > 0){
            MoveRight(Math.round(speed_x+force_x));
        }
        if(speed_x+force_x < 0){
            MoveLeft(-Math.round(speed_x+force_x));
        }

        if(speed_y+force_y < 0){
            MoveUp(-Math.round(speed_y+force_y));
        }

        if(speed_y+force_y> 0){
            MoveDown(Math.round(speed_y+force_y));
        }
    }

    public void MoveRight(int speed){
        while(speed > 0){
            speed -= 1;
            if(checkCollisionRight())
                break;
            x += 1;
        }
    }

    public void MoveLeft(int speed){
        while(speed > 0){
            speed -= 1;
            if(checkCollisionLeft())
                break;
            x -= 1;
        }
    }

    public void MoveUp(int speed){
        while(speed > 0){
            speed -= 1;
            if(checkCollisionUp())
                break;
            y -= 1;
        }
    }

    public void MoveDown(int speed){
        while(speed > 0){
            speed -= 1;
            if(checkCollisionDown())
                break;
            y += 1;
        }
    }

    public boolean checkCollisionRight(){
        for(int i = 0; i < level.tiles.length; i++){
            Tile t = level.tiles[i];
            if(t.pass == 1){
                if(x+collsion_width/2 == t.X-level.block_width/2+2 && y + collsion_height/2 - 2 > t.Y - level.block_width/2 && y-collsion_height/2 + 2 < t.Y + level.block_width/2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCollisionUp(){
        for(int i = 0; i < level.tiles.length; i++){
            Tile t = level.tiles[i];
            if(t.pass == 1){
                if(y-collsion_height/2 == t.Y+level.block_width/2-2 && x + collsion_width/2 - 2 > t.X - level.block_width/2 && x-collsion_width/2 + 2 < t.X + level.block_width/2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCollisionDown(){
        for(int i = 0; i < level.tiles.length; i++){
            Tile t = level.tiles[i];
            if(t.pass == 1){
                if(y+collsion_height/2 == t.Y-level.block_width/2+2 && x + collsion_width/2 - 2 > t.X - level.block_width/2 && x-collsion_width/2 + 2 < t.X + level.block_width/2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCollisionLeft(){
        for(int i = 0; i < level.tiles.length; i++){
            Tile t = level.tiles[i];
            if(t.pass == 1){
                if(x-collsion_width/2 == t.X+level.block_width/2-2 && y + collsion_height/2 - 2 > t.Y - level.block_width/2 && y-collsion_height/2 + 2 < t.Y + level.block_width/2) {
                    return true;
                }
            }
        }
        return false;
    }
}
