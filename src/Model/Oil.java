package Model;

import java.awt.*;


// Olaj csapda, melybe a robot ha belelép, nem tud irányt változtatni.
public class Oil extends Trap {

    // az olaj 4 kör/ugrás után felszárad, ugrásonként csökkentjük.
    private int timeToLive = 200;

    public Oil(Point location) {
        super(location);
        busy=false;
    }


    @Override
    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public void dry() {
    timeToLive--;
    }


    // visitor
    @Override
    public void accept(PlayerRobot R2D2) {
        R2D2.visit(this);

    }
    @Override
    public void accept(CleanerRobot R2D2) {
        R2D2.visit(this);
    }
}
