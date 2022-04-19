import java.util.ArrayList;

public class GameOver {

    public Game game;
    public ArrayList<String> Enemies;
    public int depth = 0;
    public int score = 0;

    public int secTimer = 500;

    public int EnemyDisplayTimer = 0;

    public Sprite Eye = new Sprite("Eye",1);

    public GameOver(ArrayList<String> EnemiesKilled,int depth,int score,Game game){
        this.game = game;
        this.Enemies = EnemiesKilled;
        this.depth = depth;
        this.score = score;
    }

    public void Update(){
        secTimer -= 1;
        EnemyDisplayTimer ++;



        if(game.mouse.Clicked && EnemyDisplayTimer > 20){
            game.level = new Level(213213,0,0,game);
            game.gameOver = null;
        }



    }

    public void Render(){
        game.drawNumber(100,100,secTimer,0xffff00);
        int e = 0;
        for(int i = 0; i < Enemies.size(); i++){
            if(EnemyDisplayTimer / 10 > i){
                e ++;
                switch (Enemies.get(i)){
                    case "WarHound":
                        game.drawRect(100+(i%8)*21,100+(i/8)*42,42,42,0xff0000);
                        break;
                    case "SingleShot":
                        game.drawRect(100+(i%8)*21,100+(i/8)*42,42,42,0xffff00);
                        break;
                    case "TripleShot":
                        game.drawRect(100+(i%8)*21,100+(i/8)*42,42,42,0xff8800);
                        break;
                    case "LaserEyes":
                        game.drawSprite(Eye,100+(i%8)*21,100+(i/8)*42,0,0xffffff);
                        break;
                }
            }else{
                break;
            }
        }
        game.drawNumber(100,50,e,0xffaaaa);
    }
}
