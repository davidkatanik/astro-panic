package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Shot implements IDrawable {

    public static int shotSpeed = 20;
    public static int SHOT_WIDTH = 5;
    public static int SHOT_HEIGHT = 10;
    public static boolean shotting = false;
    private int x = 0;
    private int y = 0;
    
    Image shotImage = new ImageIcon(getClass().getClassLoader().getResource("shot.png")).getImage();

    public Shot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void startShotting() {
        shotting = true;
    }

    public void stopShotting() {
        shotting = false;
    }

    public void moveShot() {
        y -= shotSpeed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(shotImage, x, y, null);
    }
    
}
