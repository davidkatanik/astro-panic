package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Enemy implements IDrawable {

    public static int ENEMY_HEIGHT;
    public static int ENEMY_WIDTH;

    private final int MAXSPEED = 6;
    
    private int x = 0;
    private int y = 0;
    private int speedX;
    private int speedY;
    
    

    private boolean hitState = false;

    Image enemyImage = new ImageIcon(getClass().getClassLoader().getResource("enemy.png")).getImage();

    public Enemy() {
        generatePosition();
        
        speedX = -MAXSPEED + (int) (Math.random() * ((MAXSPEED - (-MAXSPEED)) + 1));
        speedY = -MAXSPEED + (int) (Math.random() * ((MAXSPEED - (-MAXSPEED)) + 1));
        
        
        ENEMY_WIDTH = enemyImage.getWidth(null);
        ENEMY_HEIGHT = enemyImage.getHeight(null);
    }

    public void generatePosition() {
        x = 2 * ENEMY_WIDTH + (int) (Math.random() * (AstroPanic.WIDTH / 2));
        y = 2 * ENEMY_WIDTH + (int) (Math.random() * (AstroPanic.HEIGHT / 2));
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXPos() {
        return x;
    }

    int getYPos() {
        return y;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(enemyImage, x, y, null);
    }

    public void move() {
        int width = AstroPanic.WIDTH;
        int height = AstroPanic.HEIGHT;

        y += speedY;
        x += speedX;
        if (x > width - ENEMY_WIDTH) {
            speedX = -speedX;
            x = width - ENEMY_WIDTH;
        }
        if (x < 1) {
            speedX = -speedX;
            x = 0;
        }
        if (y > height - 2 * ENEMY_HEIGHT) {
            speedY = -speedY;
            y = height - 2 * ENEMY_HEIGHT;
        }
        if (y < 1) {
            speedY = -speedY;
            y = 0;
        }
    }

}
