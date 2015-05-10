package Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/* A játékban a pélyán találhtó objektumok összességének tárolóosztálya.
    Itt tároljuk a robotokat és a csapdákat.
 */

public class GameMapContainer {

    // Pálya felbontása
    private Dimension resolution;
    private List<CleanerRobot> cleanerRobots;
    private List<PlayerRobot> playerRobots;
    private List<Trap> traps;


    //Létrehozzuk a megfelelő tárolókat és játék elemeket
    public GameMapContainer(Dimension resolution) {

        this.resolution = resolution;
        playerRobots = new ArrayList<PlayerRobot>();
        cleanerRobots = new ArrayList<CleanerRobot>();
        traps = new ArrayList<Trap>();
       System.out.println( "Sikeres letrehozas: map[" + resolution.width + "," + resolution.height + "]");
    }

    // getterek
    public List<PlayerRobot> getPlayerRobots() {
        return playerRobots;
    }

    public List<CleanerRobot> getCleanerRobots() {
        return cleanerRobots;
    }

    public List<Trap> getTraps() {
        return traps;
    }


    //nagyrobot hozzáadása
    public void addPlayerRobot(PlayerRobot playerRobot) {

            //IDEIGLENES, proto miatt el kell nevezni őket
            //azért itt mert itt látom a listát, ahol majd lesz benne
            playerRobot.name=playerRobots.size()+1;

        playerRobots.add(playerRobot);
       System.out.println( "Sikeres letrehozas: robot[" +
                playerRobot.getLocation().getX() + " ," + playerRobot.getLocation().getY() +", "+
                playerRobot.angle+" ,"+playerRobot.speed+"]");


    }

    //kisrobot hozzáadása
    public void addCleanerRobot(CleanerRobot cleanerRobot) {

        //IDEIGLENES, proto miatt el kell nevezni őket
        //azért itt mert itt látom a listát, ahol majd lesz benne
        cleanerRobot.name=cleanerRobots.size()+1;

        cleanerRobots.add(cleanerRobot);
       System.out.println( "Sikeres letrehozas: clrobot[" +
                cleanerRobot.getLocation().getX() + "," + cleanerRobot.getLocation().getY() + "]");
    }

    //trap hozzáadása
    public void addTrap(Trap trap) {

       System.out.println( "Sikeres letrehozas: trap[" + trap.getLocation().getX() + "," + trap.getLocation().getY() + "]");
        traps.add(trap);
    }

    public void removePlayerRobot(PlayerRobot playerRobot){

       System.out.println( "    Robot"+playerRobot.name+"[" + playerRobot.getLocation().getX() +
                "," + playerRobot.getLocation().getY() + "] megsemmisult!");
        playerRobots.remove(playerRobot);
    }
    public void removeCleanerRobot(CleanerRobot cleanerRobot){

       System.out.println( "    KisRobot"+cleanerRobot.name+"[" + cleanerRobot.getLocation().getX() +
                "," + cleanerRobot.getLocation().getY() +"] megsemmisult!");
        cleanerRobots.remove(cleanerRobot);
    }
    public void removeTrap(Trap trap){
       System.out.println( "    trap[" + trap.getLocation().getX() + "," + trap.getLocation().getY() + "] megsemmisult!");
        traps.remove(trap);

    }
}
