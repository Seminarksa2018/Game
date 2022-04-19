import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class Main implements Runnable{


    public int screen_width;
    public int screen_height;
    public boolean running = true;
    public int FPS = 120;

    public Canvas canvas;
    private JFrame frame;
    public BufferedImage img;
    public int[] pixels;

    public static void main(String[] args){
        Thread main = new Thread(new Main());
        main.run();
    }

    public Main(){
        createCanvas();
        createJframe();
        createImg();
    }

    public void run() {
        runGame();
    }


    public void createJframe(){
        screen_width = 1280;
        screen_height = 720;
        frame = new JFrame("projekt informatika");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(new Dimension(screen_width,screen_height));
        frame.setVisible(true);
        frame.add(canvas);
    }

    public void createCanvas(){
        canvas = new Canvas();
        canvas.setMinimumSize(new Dimension(screen_width,screen_height));
        canvas.setMaximumSize(new Dimension(screen_width,screen_height));
    }

    public void runGame(){
        Game game = new Game(this);
        while(running){
            long currentTime = System.currentTimeMillis();
            game.Update();
            game.Render();
            Display();
            try {
                long c = currentTime + (1000 / (long) FPS) - System.currentTimeMillis();
                updatePix();
                if(c > 0) {
                    System.out.print(":");
                    Thread.sleep(c);
                }else{
                    System.out.println("_");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.dispose();
    }

    public void Display(){
        BufferStrategy bs = canvas.getBufferStrategy();
        if(bs == null){
            canvas.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img,0,0,canvas.getWidth(),canvas.getHeight(),null);
        g.dispose();
        bs.show();
    }

    public void createImg(){
        img = new BufferedImage(screen_width,screen_height,BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }

    public void updatePix(){
        Random r = new Random();
        int color = r.nextInt();
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = r.nextInt();
        }
    }
}
