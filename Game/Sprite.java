import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Sprite {

    public int WIDTH;
    public int HEIGHT;
    public BufferedImage[] img;
    public ArrayList<int[]> Pixels;
    public String Name;

    public Sprite(String Name, int Frames) {
        this.Name = Name;
        img = new BufferedImage[Frames];

        try {
            for (int i = 0; i < Frames; i++)
                img[i] = ImageIO.read(Sprite.class.getResourceAsStream(Name + "_" + Integer.toString(i) + ".png"));
        } catch (Exception e) {
            System.out.println(Name + " Not found");
        }
        Pixels = new ArrayList<int[]>();


        int[] p;

        WIDTH = img[0].getWidth();
        HEIGHT = img[0].getHeight();

        for (int i = 0; i < Frames; i++) {
            p = img[i].getRGB(0, 0, WIDTH, HEIGHT, null, 0, WIDTH);
            Pixels.add(p);
        }
    }
}
