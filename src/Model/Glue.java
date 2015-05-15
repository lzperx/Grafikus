package Model;

import java.awt.*;

// Ragacs csapda, melybe ha a robot belelép, megfelezi a sebességét
public class Glue extends Trap {

    // a ragacs életét mutatja, ráugrásoknként egyel csökken.
    // Miután 4 robot ráugrott, a ragacs eltűnik a pályáról.
    private int timeToLive = 4;
    public Glue(Point location) {
        super(location);
        busy=false;
    }

    @Override
    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public void dry() {
        //nem csinálunk semmit, a ragacs mnem szárad fel
    }


    // visitor
    @Override
    public void accept(PlayerRobot R2D2) {


        // csökkentjük a ragacs életét, mert ráugrott egy robot,
        // de csak ha érvényes ugrás volt (gluetime ==0)
        if(R2D2.glueTime==0)
            timeToLive--;
            R2D2.visit(this);

    }
    @Override
    public void accept(CleanerRobot R2D2) {
        R2D2.visit(this);
    }

}
