package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;

/**
 * The last defender of earth...
 *
 * Notice how this class has methods equivalent to those in the Alien class.
 */
public class Ship implements MouseListener, MouseMotionListener, IDrawable {

    Image shipImage = new ImageIcon(getClass().getClassLoader().getResource("ship.png")).getImage();

    public static int SHIP_HEIGHT = 0;
    public static int SHIP_WIDTH = 0;

    private int x = 0;
    private int heightPosition = 0;

    //We are only going to allow one shot at a time
    public Shot shot = null;

    private boolean hitState = false;

    /**
     *
     * @param ap
     */
    public Ship(AstroPanic ap) {
        SHIP_WIDTH = shipImage.getWidth(ap);
        SHIP_HEIGHT = shipImage.getHeight(ap);
        //Dynamically work out the starting position of the ship
        x = (int) ((AstroPanic.WIDTH / 2));
        heightPosition = AstroPanic.HEIGHT - SHIP_HEIGHT;
    }

    /**
     * We will use the mouse to fly sour ship
     *
     * @param me
     */
    @Override
    public void mouseMoved(MouseEvent me) {
        int newX = me.getX();

        // Controll bounderies 
        if (newX > (AstroPanic.WIDTH - (SHIP_WIDTH / 2) - AstroPanic.WD / 2)) {
            x = AstroPanic.WIDTH - SHIP_WIDTH + AstroPanic.WD / 2;
        } else if (newX < SHIP_WIDTH / 2) {
            x = SHIP_WIDTH / 2;
        } else {
            x = newX;
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        mouseMoved(me);
        mouseClicked(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        AstroPanic.pauseGame(false);
    }

    @Override
    public void mouseExited(MouseEvent me) {
        AstroPanic.pauseGame(true);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        //Enemys army = astroPanic.getEnemys();
        if (!Shot.shotting) {
            shot = new Shot(x, heightPosition);
            shot.startShotting();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        g.drawImage(shipImage, x-SHIP_WIDTH/2, heightPosition-SHIP_HEIGHT, null);

        if (shot != null) {
            if (checkBoundaires(shot) == false) {
                shot.stopShotting();
                shot = null;
            }
        }
        if (Shot.shotting) {
            shot.moveShot();
            shot.draw(g);
        }

    }

    public boolean checkBoundaires(Shot shot) {
        int height = AstroPanic.HEIGHT;
        if (shot.getY() < 0) {
            return false;
        }
        return true;
    }

    public Shot getShot() {
        return shot;
    }

    public int getX() {
        return x;
    }

    public int getHeightPosition() {
        return heightPosition;
    }

}
