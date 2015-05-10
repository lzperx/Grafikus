package Model;

import Control.KeyMap;

import java.awt.*;

public class PlayerRobot extends GameElements {

    public String name; //CSAK INEIGLENESEN a protoban


    protected Point nextPosition;   //A robot ahova ugrani fog legközelebb
    public int speed;               // A robot aktuális sebessége
    public double angle;         //A robot aktuális szöge, FOKBAN
    public double distance;    //összesen megtett távolság
    public int ammountofOil;        //robot olaj készlete
    public int ammountofGlue;       //robot ragacs készlete
    public KeyMap keys;   // a robot billentyűzetkiosztása

    public static enum robotState {NORMAL, OILED}

    public robotState state = robotState.NORMAL;     //robot talajhoz viszonyított állapota

        /* robotState= enumeráció a robot aktuális állapotara.
          Ez határozza meg, hogy van-e lehetőség iránymódosításra az aktuális földetéréskor.
          Az olajt paraméterül váró visit metódus ezt állítja be.
       */

    public int glueTime = 0;

    public PlayerRobot(Point location, int hitbox, double angle, KeyMap keys) {
        super(location, hitbox);
        speed = 2;    //kezdősebessége
        distance = 0;
        this.angle = angle;
        ammountofGlue = 3;
        ammountofOil = 3;
        this.keys=keys;
    }

    public void Jump() {

        nextPosition = new Point(
                (int) (speed * Math.cos(Math.toRadians(angle))) + (int) location.getX(),
                (int) (speed * Math.sin(Math.toRadians(angle))) + (int) location.getY());

        distance += nextPosition.distance(location);
        location = nextPosition;

        if(glueTime>0)
        glueTime--;

    }

    public void TurnLeft(int change) {
        angle = (angle - change) % 360;
        //ha mínusz érték, akkor még hozzáadom a 360-hoz, így [0-360] tartományban lesz
        if (angle < 0) angle += 360;
       System.out.println( "Robot" + name + " balra fordulas: " + change);

    }

    public void TurnRight(int change) {
        angle = (angle + change) % 360;
       System.out.println( "Robot" + name + " jobbra fordulas: " + change);

    }

    public void Speed(int change) {
        if (speed > Resources.maxSpeed)
            return;
        if (speed < -Resources.maxSpeed)
            return;
        if (state == robotState.NORMAL){
           System.out.println( "Robot" + name + " sebesseg-valtoztatas: " + change);
            speed += change;
        }


    }

    public boolean PutGlue() {
        if (ammountofGlue > 0) {
            ammountofGlue--;
           System.out.println( "Robot" + name +
                    " lerakott egy ragacsot[X= "+this.getLocation().getX()+" , Y= "+this.getLocation().getY()+"]");
           System.out.println( "Robot" + name + " lerakhat meg [" + ammountofGlue + "] db ragacsot");
            return true;
        }
       System.out.println( "Robot" + name + " lerakhat meg [" + ammountofGlue + "] db ragacsot");
        return false;
    }

    public boolean PutOil() {
        if (ammountofOil > 0) {
            ammountofOil--;
           System.out.println( "Robot" + name +
                    " lerakott egy olajat[X= "+this.getLocation().getX()+" , Y= "+this.getLocation().getY()+"]");
           System.out.println( "Robot" + name + " lerakhat meg [" + ammountofOil + "] db olajat");
            return true;
        }
       System.out.println( "Robot" + name + " lerakhat meg [" + ammountofOil + "] db olajat");
        return false;
    }

    /*****************************************************************************************************************/
    /*****                                         VISITOR PATTERN                                               *****/
    /**
     * *************************************************************************************************************
     */


    /* A robotok csapdával való érintkezését oldja meg,
        minden csapda saját magát adja át a visit metódusnak,
        így a megfelelő kód fut le.
     */
    void visit(Oil oil) {
        state = robotState.OILED;
       System.out.println( "Robot" + name + " olajra lepett!");
    }


    // ha a gluetime 0 csak akkor felezi a robot sebességét,
    // így másodpercenként csak egyszer feleződhet
    // (kb 33 fps el fut a game ezért 33 ra reseteljük)
    void visit(Glue glue) {
        if(glueTime==0) {
        speed /= 2;
        System.out.println("Robot" + name + " ragacsra lepett!");
            glueTime = 33;
        }
    }

    void visit(PlayerRobot playerRobot) {
        //ha ez lefut, akkor azt jelenti, hogy megsemmisült, a Model.GameMapContainer fogja kiírni a halálát
        //konkrétan a Gamecontrolban hívódik meg a gameMapContainer.removePlayerRobot() függvény,
        //azért nem itt, mert a robot nem láthatja a többi robotot

    }

    void visit(CleanerRobot cleanerRobot){

    }
    @Override
    public void accept(PlayerRobot C3PO) {

       System.out.println( "    Robot" + name + " es Robot"+C3PO.name+" utkoztek!");
        if (speed > C3PO.speed)
            C3PO.visit(this);

        //különben azt jelenti, hogy megsemmisült, a Model.GameMapContainer fogja kiírni a halálát




    }

    @Override
    public void accept(CleanerRobot R2D2) {
        //Kisrobot elpattan a Nagyrobottól, mert ő mert neki, nem fordítva
       System.out.println( "    KisRobot" + R2D2.name + " es Robot"+name+" utkoztek!");
        R2D2.visit(this);
    }

}
