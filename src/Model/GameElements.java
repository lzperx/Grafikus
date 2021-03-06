package Model;

import java.awt.*;

/* Absztrakt alaposztály, a játékon belüli minden objektum belőle származik.
   Tárolja az adott elem koordinátáit a pályán,
   valamint az adott objektum kiterjedését(méretét)
 */
public abstract class GameElements implements Visitor {
    public Point location;
    protected int hitbox;

    public GameElements(Point location, int hitbox) {
        this.location = location;
        this.hitbox = hitbox;
    }


    public Point getLocation() {
        return location;
    }

    public int getHitbox() {
        return hitbox;
    }

}
