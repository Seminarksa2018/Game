import sun.security.provider.SHA;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    public Main main;
    public int screen_width;
    public int screen_height;
    public int[] pixels;


    public Mouse mouse;
    public Keyboard keyboard;

    public Level level;
    public GameOver gameOver;

    public int Camera_X = 0;
    public int Camera_Y = 0;
    public double CameraTimer = 0;
    public int CameraShake = 30;
    public double ShakeAmount = 0;

    public Sprite Numbers = new Sprite("Number",10);

    public int angle = 0;

    public Game(Main m){
        this.main = m;
        screen_width = m.screen_width;
        screen_height = m.screen_height;
        pixels = new int[screen_height*screen_width];
        mouse = new Mouse(m,this);
        keyboard = new Keyboard(m);
        level = new Level(138712,0,0,this);
        gameOver = null;
    }

    public void Update(){
        if(gameOver == null) {
            level.Update();
        }else{
            gameOver.Update();
        }
        CameraShake();
        angle ++;
        mouse.Pressed = false;
    }

    public void Render(){
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = 0x000000;
        }
        if(gameOver == null) {
            level.Render();
        }else{
            gameOver.Render();
        }

        Display();
    }

    public void Display(){
        for(int i = 0; i < pixels.length; i++){
            main.pixels[i] = pixels[i];
        }
    }

    public void CameraShake(){
        CameraTimer += Math.PI/20;
        Camera_X = (int)Math.round(Math.cos(CameraTimer)*ShakeAmount*CameraShake);
        Camera_Y = (int)Math.round(Math.sin(CameraTimer)*ShakeAmount*CameraShake);
        ShakeAmount -= 0.04;
        if(ShakeAmount >= 0.6)
            ShakeAmount = 0.6;
        if(ShakeAmount < 0)
            ShakeAmount = 0;
    }

    public void drawRect(int x,int y,int w,int h,int color){
        x -= w/2-Camera_X;
        y -= h/2-Camera_Y;

        for(int i = y; i < y+h; i++){
            for(int o = x; o < x+w; o++){
                if(i >= 0 && i < screen_height && o >= 0 && o < screen_width)
                    pixels[i*screen_width+o] = color;
            }
        }
    }



    public void drawSpriteMono(Sprite spr,int x,int y, int f,int color){
        x -= spr.WIDTH/2-Camera_X;
        y -= spr.HEIGHT/2-Camera_Y;
        int [] pix = spr.Pixels.get(f);

        for(int i = y; i < y+spr.HEIGHT; i++){
            for(int o = x; o <x+spr.WIDTH; o++){
                if(i > 0 && i < screen_height && o > 0 && o < screen_width && pix[(i-y)*spr.WIDTH+(o-x)] != 0)
                    pixels[i*screen_width+o] = color;
            }
        }
    }

    public void drawSpriteBrightnessBuffer(Sprite spr,int x,int y,int f){
        x -= spr.WIDTH/2-Camera_X;
        y -= spr.HEIGHT/2-Camera_Y;
        int [] pix = spr.Pixels.get(f);

        for(int i = y; i < y+spr.HEIGHT; i++){
            for(int o = x; o <x+spr.WIDTH; o++){
                if(i > 0 && i < screen_height && o > 0 && o < screen_width && pix[(i-y)*spr.WIDTH+(o-x)] != 0) {
                    int c1 = pix[(i-y)*spr.WIDTH+(o-x)];
                    int c2 = pixels[i*screen_width+o];
                    int b1 = Math.max(c1&0xff0000,Math.max(c1&0x00ff00,c1&0x0000ff));
                    int b2 = Math.max(c2&0xff0000,Math.max(c2&0x00ff00,c2&0x0000ff));
                    if(b1 > b2)
                    pixels[i * screen_width + o] = pix[(i - y) * spr.WIDTH + (o - x)];
                }
            }
        }
    }

    public void drawSprite(Sprite spr,int x,int y, int f,int color){
        x -= spr.WIDTH/2-Camera_X;
        y -= spr.HEIGHT/2-Camera_Y;
        int [] pix = spr.Pixels.get(f);

        for(int i = y; i < y+spr.HEIGHT; i++){
            for(int o = x; o <x+spr.WIDTH; o++){
                if(i > 0 && i < screen_height && o > 0 && o < screen_width && pix[(i-y)*spr.WIDTH+(o-x)] != 0)
                    pixels[i*screen_width+o] = pix[(i-y)*spr.WIDTH+(o-x)];
            }
        }
    }

    public void drawLine(Point p1,Point p2, int color){
        if(p1.x == p2.x){
            if(p2.y > p1.y){
                int i = p1.y;
                while(true){
                    i += 1;
                    if(i > 0 && i < screen_height && p1.x > 0 && p1.x < screen_width) {
                        pixels[i * screen_width + p1.x] = color;
                    }else{
                        break;
                    }
                }
            }
            if(p2.y < p1.y){
                int i = p1.y;
                while(true){
                    i -= 1;
                    if(i > 0 && i < screen_height && p1.x > 0 && p1.x < screen_width){
                        pixels[i*screen_width+p1.x] =color;

                    }else{
                        break;
                    }
            }
        } }

        if(p1.y == p2.y){

            if(p2.x > p1.x){
                int i = p1.x;
                while(true){
                    i += 1;
                    if(p1.y > 0 && p1.y < screen_height && i > 0 && i < screen_width) {
                        pixels[p1.y * screen_width + i] = color;
                    }else{
                        break;
                    }
                }
            }
            if(p2.x < p1.x){
                int i = p1.x;
                while(true){
                    i -= 1;
                    if(p1.y > 0 && p1.y < screen_height && i > 0 && i < screen_width) {
                        pixels[p1.y * screen_width + i] = color;
                    }else{
                        break;
                    }
                    }
                }
        }

        if(p1.x != p2.x && p1.y != p2.y){
            float k = (float)(p2.y - p1.y)/(float)(p2.x -p1.x);

            if(p1.x < p2.x){
                int o = p1.x;
                while(true){
                    o += 1;
                    int i = Math.round((o-p1.x)*k)+p1.y;
                    if(i > 0 && i < screen_height && o > 0 && o < screen_width){
                        pixels[i*screen_width+o] = color;
                    }else{
                        break;
                    }
                }
            }
            if(p1.x > p2.x){
                int o = p1.x;
                while(true){
                    o -= 1;
                    int i = Math.round((o-p1.x)*k)+p1.y;
                    if(i > 0 && i < screen_height && o > 0 && o < screen_width){
                        pixels[i*screen_width+o] = color;
                    }else{
                        break;
                    }
                }
            }
            k = 1/k;
            if(p1.y < p2.y){
                int i = p1.y;
                while(true){
                    i += 1;
                    int o = Math.round((i-p1.y)*k)+p1.x;
                    if(i > 0 && i < screen_height && o > 0 && o < screen_width){
                        pixels[i*screen_width+o] = color;
                    }else{
                        break;
                    }
                }
            }
            if(p1.y > p2.y){
                int i = p1.y;
                while(true){
                    i -= 1;
                    int o = Math.round((i-p1.y)*k)+p1.x;
                    if(i > 0 && i < screen_height && o > 0 && o < screen_width){
                        pixels[i*screen_width+o] = color;
                    }else{
                        break;
                    }
                }
            }
        }

    }

    public void drawPlasmaBeam(Point[] points,int[] color){
        for(int i = 0; i < points.length; i++){
            Point p = points[i];
            int x = p.x;
            int y = p.y;
            if(y+Camera_Y > 0 && y+Camera_Y < screen_height && x+Camera_X > 0 && x+Camera_X < screen_width)
                drawRect(x,y,2,2,color[i]);
        }
    }

    public void drawPointArray(Point[] points, int color){
        for(int i = 0; i < points.length; i++){
            Point p = points[i];
            int x = p.x;
            int y = p.y;
            if(y+Camera_Y > 0 && y+Camera_Y < screen_height && x+Camera_X > 0 && x+Camera_X < screen_width)
                pixels[(y+Camera_Y)*screen_width+(x+Camera_X)] = color;
        }
    }

    public void drawShot(Point[] points,int LifeTime, int color){
        for(int i = 0; i < points.length; i++){
            Point p = points[i];
            int x = p.x;
            int y = p.y;
            if(y+Camera_Y > 0 && y+Camera_Y < screen_height && x+Camera_X > 0 && x+Camera_X < screen_width && i > LifeTime - 20 && i < LifeTime + 20)
                pixels[(y+Camera_Y)*screen_width+(x+Camera_X)] = color;
        }
    }

    public void drawSpriteScaleable(Sprite spr,int x,int y,int f,float s){
        int w = Math.round(spr.WIDTH*s);
        int h = Math.round(spr.HEIGHT*s);
        x -= w/2-Camera_X;
        y -= h/2-Camera_Y;

        int[] pix = spr.Pixels.get(f);

        for(int i = y; i < y+h; i++){
            for(int o = x; o < x+w; o++){
                int sx = (int)Math.floor((float)(o-x)/w*spr.WIDTH);
                int sy = (int)Math.floor((float)(i-y)/h*spr.HEIGHT);
                if(i > 0 && i < screen_height && o > 0 && o < screen_width && pix[(sy)*spr.WIDTH+(sx)] != 0){
                    pixels[i*screen_width+o] = pix[(sy)*spr.WIDTH+(sx)];
                }
            }
        }
    }

    public void drawNumber(int x,int y,int num,int color){
        ArrayList<Integer> digets = new ArrayList<Integer>();
        while (num > 0){
            digets.add(num%10);
            num = num/10;
        }

        for(int i = digets.size()-1; i >= 0; i--){
            drawSpriteMono(Numbers,x+16*(digets.size()-i),y,digets.get(i),color);
        }
    }
}
