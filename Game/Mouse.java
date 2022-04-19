import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener,MouseMotionListener{

    public boolean Clicked = false;
    public boolean Pressed = false;
    public int x = 0;
    public int y = 0;
    private Main m;
    private Game g;

    public boolean canScroll = true;
    public Mouse(Main m,Game g){
        m.canvas.addMouseListener(this);
        m.canvas.addMouseMotionListener(this);
        this.m = m;
        this.g = g;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = (int)Math.floor(e.getX()*(double)m.screen_width/(double)m.canvas.getWidth());
        y = (int)Math.floor(e.getY()*(double)m.screen_height/(double)m.canvas.getHeight());

    }



    @Override
    public void mousePressed(MouseEvent e) {

        x = (int)Math.floor(e.getX()*(double)m.screen_width/(double)m.canvas.getWidth());
        y = (int)Math.floor(e.getY()*(double)m.screen_height/(double)m.canvas.getHeight());
        if(!Clicked)
            Pressed = true;
        Clicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Clicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        canScroll = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        canScroll = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = (int)Math.floor(e.getX()*(double)m.screen_width/(double)m.canvas.getWidth());
        y = (int)Math.floor(e.getY()*(double)m.screen_height/(double)m.canvas.getHeight());
    }


}

