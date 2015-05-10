package Control;

import Model.GameMapContainer;
import View.ViewFrame;

import java.awt.*;

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
        controller = new GameControl(gameMap);
        viewFrame = new ViewFrame(gameMap, dimension);
        viewFrame.setVisible(true);
        viewFrame.addKeyListener(controller);
    }

}
