package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AstroPanic extends JComponent implements Runnable {

    public static int WIDTH = 750;//The width of the frame
    public static int HEIGHT = 400;//The height of the frame

    public static final int WD = 15;//The width of the frame
    public static final int HD = 30;//The height of the frame

    private static boolean paused = false;

    private final int gameSpeed = 50;//Try 500

    private Enemys enemys = null;
    private Ship ship = null;
    private final String gameTitle = "Astro Panic";

    private boolean run = false;
    private int score = 0;
    private int numberOfEnemys = 2;
    private int oldNumberOfEnemys = 2;
    private int lives = 3;
    private int level = 1;

    private final Graphics offscreen_high;
    private final BufferedImage offscreen;

    //private final Image backGroundImage = new ImageIcon("images\\background.png").getImage();
    private final Image backGroundImage = new ImageIcon(getClass().getClassLoader().getResource("background.png")).getImage();
    private final Image pauseImage = new ImageIcon(getClass().getClassLoader().getResource("pause.png")).getImage();
    private final Image heartImage = new ImageIcon(getClass().getClassLoader().getResource("heart.png")).getImage();
    private final Image explosionImage = new ImageIcon(getClass().getClassLoader().getResource("explosion.png")).getImage();

    private JFrame appFrame;

    public AstroPanic() {
        setPreferredSize(new Dimension(WIDTH - WD, HEIGHT - HD));
        ship = new Ship(this);
        enemys = new Enemys(numberOfEnemys);

        offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        offscreen_high = offscreen.createGraphics();
        
        addMouseListener(ship);
        addMouseMotionListener(ship);
    }

    public void showGame() {
        appFrame = new JFrame(gameTitle);
        appFrame.setContentPane(this);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.pack();
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);
        appFrame.setVisible(true);

        appFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_P: {
                        paused = true;
                        break;
                    }
                    case KeyEvent.VK_SPACE: {
                        paused = false;
                        break;
                    }
                    case KeyEvent.VK_F1: {
                        helpGame();
                        break;
                    }
                    case KeyEvent.VK_F2: {
                        restartGame();
                        break;
                    }
                    case KeyEvent.VK_ESCAPE: {
                        System.exit(score);
                        break;
                    }
                }
            }
        });
        startGame();
    }

    public static void pauseGame(boolean state) {
        paused = state;
    }

    public void stopGame() {
        run = false;
    }

    public void hitEnemyScore() {
        score += 2;
    }

    public void regenerateEnemys() {
        //System.out.println(numberOfEnemys);
        enemys.regenerateEnemys(numberOfEnemys);
    }
    
    public void deleteEnemys(){
        enemys.deleteArmy();
        numberOfEnemys = 2;
        oldNumberOfEnemys = 2;
    }

    public void replacesEnemys() {
        for (Enemy en : enemys.getEnemys()) {
            en.generatePosition();
        }
    }

    public void collisonShip() {
        score -= 4;
        lives--;
        replacesEnemys();
        if (lives == 0) {
            JOptionPane.showMessageDialog(appFrame, "GAME OVER!!!!\nFinal score : " + (score));
            score = 0;
            restart();
        }
       
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }

    }

    public void startGame() {
        run = true;
        Thread thread = new Thread(this);
        thread.start();
    }
    
     public void helpGame() {
        // TODO
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int style = Font.BOLD | Font.ITALIC;
        String strS = Integer.toString(score);
        String strL = Integer.toString(level);
        Font f = new Font(strS, style, 20);

        offscreen_high.drawImage(backGroundImage, 0, 0, this);
        /*offscreen_high.setColor(Color.white);
        offscreen_high.drawRect(0, 0, WIDTH, HEIGHT);
        offscreen_high.fillRect(0, 0, WIDTH, HEIGHT);*/
        enemys.draw(offscreen_high);
        ship.draw(offscreen_high);
        
        g.drawImage(offscreen, 0, 0, this);
        g.setColor(Color.red);

        g.setFont(f);
        int x = f.getSize();
        g.drawString("Skóre: " + strS + "    Level: " + strL, x, x);
        for (int i = 1; i <= lives; i++) {
            g.drawImage(heartImage, WIDTH - heartImage.getWidth(appFrame) * i, 0, appFrame);
        }

        if (paused) {
            g.drawImage(pauseImage, WIDTH / 2 - pauseImage.getWidth(appFrame) / 2, HEIGHT / 2 - pauseImage.getHeight(appFrame) / 2, appFrame);
        }
    }

    public void moveEnemys() {
        enemys.moveArmy();
    }

    public void restart() {
        //restartingGraphics();
        deleteEnemys();
        regenerateNumEnemys();
        level = 1;
        lives = 3;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        regenerateEnemys();
        oldNumberOfEnemys = numberOfEnemys;
    }

    public void restartGame() {
        deleteEnemys();
        level = 1;
        lives = 3;
        regenerateNumEnemys();
        regenerateEnemys();
    }
    
    public void regenerateNumEnemys(){
        //System.out.println(oldNumberOfEnemys+" "+numberOfEnemys);
        numberOfEnemys = oldNumberOfEnemys + (int) (Math.random() * level + oldNumberOfEnemys);
        oldNumberOfEnemys = numberOfEnemys;
        //System.out.println(oldNumberOfEnemys+" "+numberOfEnemys);
    }

    public boolean checkCollision() {
        for (Enemy en : enemys.getEnemys()) {

            /*
             *   Collision of two rectangle, if is new rectangle rectC higher or is null then collison is true else false
             *   rectB has X, Y same as image of Ship
             */
            Rectangle rectA = new Rectangle(en.getXPos(), en.getYPos(), Enemy.ENEMY_WIDTH, Enemy.ENEMY_HEIGHT);
            Rectangle rectB = new Rectangle(ship.getX() - Ship.SHIP_WIDTH / 2, ship.getHeightPosition() - Ship.SHIP_HEIGHT / 3, Ship.SHIP_WIDTH, Ship.SHIP_HEIGHT);
            Rectangle rectC = new Rectangle();

            Rectangle.intersect(rectA, rectB, rectC);
            if (rectC.width >= 0 && rectC.height >= 0) {
                Graphics g = this.getGraphics();
                g.drawImage(explosionImage, ship.getX()-Ship.SHIP_WIDTH/2, ship.getHeightPosition()-Ship.SHIP_HEIGHT, this);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkHitEnemy() {
        
        Shot s = ship.getShot();
        for (Enemy en : enemys.getEnemys()) {

            Rectangle rectA = new Rectangle(en.getXPos(), en.getYPos(), Enemy.ENEMY_WIDTH, Enemy.ENEMY_HEIGHT);
            Rectangle rectB = new Rectangle(s.getX(), s.getY(), Shot.SHOT_WIDTH, Shot.SHOT_HEIGHT);
            Rectangle rectC = new Rectangle();

            Rectangle.intersect(rectA, rectB, rectC);
            if (rectC.width >= 0 && rectC.height >= 0) {
                Graphics g = this.getGraphics();
                g.drawImage(explosionImage, s.getX(), s.getY(), this);
                enemys.deleteEnemy(en);
                s.stopShotting();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (run) {
            try {
                Thread.sleep(gameSpeed);
            } catch (InterruptedException ie) {
            }
            if (!paused) {
                enemys.moveArmy();
                if (checkCollision()) {
                    collisonShip();
                }
                if (Shot.shotting) {
                    if (checkHitEnemy()) {
                        hitEnemyScore();
                        numberOfEnemys--;
                    }
                }
                if (numberOfEnemys == 0) {
                    level++;
                    regenerateNumEnemys();
                    regenerateEnemys();
                }
            }
            repaint();
        }
    }

    public Enemys getEnemys() {
        return enemys;
    }

    public static void main(String[] args) {
        AstroPanic game = new AstroPanic();
        game.showGame();
    }

}
