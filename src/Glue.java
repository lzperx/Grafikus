import java.awt.*;

// Ragacs csapda, melybe ha a robot belelép, megfelezi a sebességét
public class Glue extends Trap {

    // a ragacs életét mutatja, ráugrásoknként egyel csökken.
    // Miután 4 robot ráugrott, a ragacs eltűnik a pályáról.
    private int timeToLive = 4;
    public Glue(Point location) {
        super(location);
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

            R2D2.visit(this);
            timeToLive--;        // csökkentjük a ragacs életét, mert ráugrott egy robot
    }
    @Override
    public void accept(CleanerRobot R2D2) {
        R2D2.visit(this);
    }

}
