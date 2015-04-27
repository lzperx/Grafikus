import java.awt.*;
import java.util.Random;

/* Az új játék felépítéséért felelős objektum.
   Az inicializálás is itt történik meg.
 */
public class NewGame {
    private Dimension dimension;
    public GameMapContainer gameMap;
    public GameControl controller;


    public NewGame(int weight, int height) {
        dimension = new Dimension(weight, height);
        gameMap = new GameMapContainer(dimension);
        controller = new GameControl(gameMap);
    }

}
