package Game;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Enemys implements IDrawable {

    private int numberOfEnemys;
    private final List<Enemy> enemys;

    Image enemyImage = null;

    public Enemys(int numberOfEnemys) {
        this.numberOfEnemys = numberOfEnemys;
        enemys = new ArrayList<>();
        createArmy(numberOfEnemys);
    }

    public void createArmy(int numberOfEnemys) {
        for (int i = 0; i < numberOfEnemys; i++) {
            Enemy e = new Enemy();
            enemys.add(e);
        }
    }
    
    public void deleteArmy() {
        enemys.removeAll(enemys);
    }

    public void moveArmy() {
        for (Enemy enemy : enemys) {
            enemy.move();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (Enemy enemy : enemys) {
            enemy.draw(g);
        }
    }
    
    public void deleteEnemy(int numberOfEnemy){
        enemys.remove(numberOfEnemy);
    }
    
    public void deleteEnemy(Enemy e){
        enemys.remove(e);
    }

    public List<Enemy> getEnemys() {
        return enemys;
    }
    
    public void regenerateEnemys(int numberOfEnemys){
        this.numberOfEnemys = numberOfEnemys;
        createArmy(numberOfEnemys);
    }

    public int getNumberOfEnemys() {
        return numberOfEnemys;
    }
    

}
