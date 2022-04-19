import java.util.Random;

/**
 * Created by Tine on 22.6.2018.
 */
public class HoloUi {

    public int x;
    public int y;
    public int w = 0;
    public int h = 0;

    public int dw;
    public int dh;

    public int c;

    private Game game;
    private Random rand = new Random();

    public float transp = 0.5f;
    public float transp_dif = 0.003f;
    public float transp_dif_max = 0.2f;

    public HoloUi(int x,int y,int w,int h,int c,Game g){
        this.x = x;
        this.y = y;
        this.c = c;
        this.dw = w;
        this.dh = h;
        this.game = g;
    }

    public void Update(){
        if(w < dw){
            w += 5;
            if(w > dw)
                w = dw;
        }

        if(h < dh){
            h += 5;
            if(h > dh)
                h = dh;
        }
        transp += transp_dif;
        if(Math.abs(transp -0.5f) > transp_dif_max)
            transp_dif = transp_dif*-1;

    }

    public void Render(){
        RenderHolo();
    }

    public void RenderHolo(){

        for(int i = y-h/2; i < y+h/2; i++){
            for(int o = x-w/2; o < x+w/2; o++){
                float r = transp;
                if(i >= 0 && o >= 0 && i < game.screen_height && o < game.screen_width) {
                    float t = (float)((x-o)*(x-o)+(y-i)*(y-i))/(float)((w/2)*(w/2)+(h/2)*(h/2));
                    r += t*0.3f;
                    int pb = game.pixels[i*game.screen_width+o]&0x0000ff;
                    int pg = (game.pixels[i*game.screen_width+o]&0x00ff00)/256;
                    int pr = (game.pixels[i*game.screen_width+o]&0xff0000)/256/256;
                    int cb = c&0x0000ff;
                    int cg = (c&0x00ff00)/256;
                    int cr = (c&0xff0000)/256/256;
                    game.pixels[i*game.screen_width+o] = (int)(cr*r+pr*(1-r))*256*256+( (int)(cg*r+pg*(1-r)))*256+(int)(cb*r+pb*(1-r));
                }
            }
        }
    }
}
