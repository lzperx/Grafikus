import java.awt.*;


public abstract class Trap extends GameElements{


    public Trap(Point location) {
        super(location, 10);
    }
    public abstract int getTimeToLive();

    // dry függvény az olaj száradásához... ragacsnál nem csinál semmit
    public abstract void dry ();
}
