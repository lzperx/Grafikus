package Model;

import java.awt.*;

public class CleanerRobot extends GameElements {
    public int name; //CSAK INEIGLENESEN a protoban

    protected Point nextPosition;   //A robot ahova ugrani fog legközelebb
    public int speed=3;               // A robot aktuális sebessége
    public double angle=0;         //A robot aktuális szöge fokban

    public boolean isCleaning=false; //igaz, ha éppen takarít
    public int TimeOfCleaning=50;    //ennyi ideig(körig) takarítja a foltot TODO 3
    public int cleaningcount = 0;    // ennyi kört tisztított már a TimeofCleaning-ből

    public CleanerRobot(Point location, int hitbox) {
        super(location, hitbox);
    }

    public void Jump() {
        if(!isCleaning){
            nextPosition = new Point(
                    (int) (speed * Math.cos(Math.toRadians(angle))) + (int) location.getX(),
                    (int) (speed * Math.sin(Math.toRadians(angle))) + (int) location.getY());

            location = nextPosition;
        }

    }


    /*****************************************************************************************************************/
    /*****                                         VISITOR PATTERN                                               *****/
    /*****************************************************************************************************************/


    /* A robotok csapdával való érintkezését oldja meg,
        minden csapda saját magát adja át a visit metódusnak,
        így a megfelelő kód fut le.
     */
    void visit(Oil oil){

        cleaningcount++;

    }

    void visit(Glue glue){

        cleaningcount++;

    }

    void visit(CleanerRobot cleanerRobot){
        angle-=180;
        angle =(angle)%360;
        if (angle<0) angle+=360;
        Jump();
    }

    void visit(PlayerRobot playerRobot){
        angle -= 180;
        Jump();
    }

    // visitor
    @Override
    public void accept(PlayerRobot R2D2) {
        //a kisrobot megsemmisül, mert ütközött egy nagyrobottal
       System.out.println( "    Robot" + R2D2.name + " es KisRobot"+name+" utkoztek!");
        //konkrétan a Gamecontrolban hívódik meg a gameMapContainer.removePlayerRobot() függvény,
        //azért nem itt, mert a robot nem láthatja a többi robotot

    }
    @Override
    public void accept(CleanerRobot R2D2) {
       System.out.println( "    KisRobot" + R2D2.name + " es KisRobot"+name+" utkoztek!");

        R2D2.visit(this);
        angle-=180;
        angle =(angle)%360;
        if (angle<0) angle+=360;
        Jump();

    }

}