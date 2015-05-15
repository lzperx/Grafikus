package Control;

import Model.*;
import View.ViewFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/* Az új játék felépítéséért felelős objektum.
   Az inicializálás is itt történik meg.
 */
public class NewGame {
    private Dimension dimension;
    public GameMapContainer gameMap;
    public GameControl controller;
    public ViewFrame viewFrame;


    public NewGame(int weight, int height) {
        dimension = new Dimension(weight, height);
        gameMap = new GameMapContainer(dimension);
        initialize();
        controller = new GameControl(gameMap);
        viewFrame = new ViewFrame(gameMap, dimension);
        viewFrame.setVisible(true);
        viewFrame.addKeyListener(controller);
    }

    //inicializáljuk a pálya kezdő elemeit
    private void initialize(){
        gameMap.addPlayerRobot(new PlayerRobot(new Point(900, 600), 10, 225,
                new KeyMap(KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN,
                        KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD,
                        KeyEvent.VK_MINUS)));
        gameMap.addPlayerRobot(new PlayerRobot(new Point(100, 100), 10, 45,
                new KeyMap(KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S,
                        KeyEvent.VK_0, KeyEvent.VK_1,
                        KeyEvent.VK_2)));


        Random rand = new Random();
        for (int i = 0; i < (5+rand.nextInt(10)); i++) {
            gameMap.addCleanerRobot(new CleanerRobot(
                    new Point(rand.nextInt((int)dimension.getWidth()), rand.nextInt((int)dimension.getHeight())), 10));
            gameMap.addTrap(new Glue(new Point(rand.nextInt((int)dimension.getWidth()),rand.nextInt((int)dimension.getHeight()))));
            gameMap.addTrap(new Oil(new Point(rand.nextInt((int)dimension.getWidth()), rand.nextInt((int)dimension.getHeight()))));
        }

    }

}
