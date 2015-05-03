import java.awt.*;
import java.lang.Math;


/*A teljes játékirányításért felelős objektum.*/

public class GameControl {

    /*   Referencia a játékelemeket tároló objektumra,
     *   így tudjuk hogy milyen robotok vannak, és mi hol van a pályán.
     *   Ezt a konstruktorban kell átadni.
    */
    private GameMapContainer gameMapContainer;

    //A konstruktorban inicializálunk mindent, létrehozzuk a robotokat, a kezdő elemeket
    public GameControl(GameMapContainer gameMapContainer) {
        this.gameMapContainer = gameMapContainer;

        /*
        //todo itt kell inicializálni a kezdő pályaelemeket
        gameMapContainer.addPlayerRobot(new PlayerRobot(new Point(, , 10, )));
        gameMapContainer.addCleanerRobot(new CleanerRobot(new Point(, , 10)));

        gameMapContainer.addTrap(new Glue(new Point(, )));
        gameMapContainer.addTrap(new Oil(new Point(, )));
        */
    }


    //Ez a fő metódusunk: körök kezelése (kisrobotok, nagyrobotok, foltok szárítása)
    public void RoundManager() {

        //minden kör elején lefuttatjuk a kisrobotokat
        for (CleanerRobot cleanerRobot : gameMapContainer.getCleanerRobots()) {
            //beállítjuk a legközelebbi folt felé
            cleanerRobot.angle = setAngleofCleanerRobot(cleanerRobot);
            //mozgatjuk a robotot, ha épp takarít, akkor a takarítási idő csökken
            cleanerRobot.Jump();
            //üztökést detektálunk: frissülnek az adatok
            Collision(cleanerRobot);
        }

        //lekezeljük a pálya robotjait sorban
        for (PlayerRobot actualRobot : gameMapContainer.getPlayerRobots()) {
            //todo itt kell kezelni a gombokat az allábbiak szerint
            /*
                actualRobot.Speed();
                actualRobot.TurnRight();
                actualRobot.TurnLeft();
                if (actualRobot.PutOil())
                    gameMapContainer.addTrap(new Oil(actualRobot.getLocation()));
                if (actualRobot.PutGlue())
                    gameMapContainer.addTrap(new Glue(actualRobot.getLocation()));
               */

            //a gombok után futtatjuk az aktuális robotra a változásokat
            actualRobot.Jump();
            //ütközést detektálunk
            Collision(actualRobot);
        }

        //a kör végén lekezeljük külön a csapdákat (szárítás: törlés, ha kiszáradt)
        removeOldTraps();

    }


    //A nagyrobot ütközéseit kezeljük itt le
    private void Collision(PlayerRobot C3PO) {

        //Csapdákkal való ütközés lekezelése
        for (Trap itsATrap : gameMapContainer.getTraps()) {

            if (C3PO.getLocation().distance(itsATrap.getLocation()) < (C3PO.getHitbox() + itsATrap.getHitbox())) {
                itsATrap.accept(C3PO);
            }
        }

        //kisrobotokkal való ütközés lekezelése
        for (CleanerRobot c : gameMapContainer.getCleanerRobots()) {
            if (C3PO.getLocation().distance(c.getLocation()) < (C3PO.getHitbox() + c.getHitbox())) {

                c.accept(C3PO);
                gameMapContainer.addTrap(new Oil(c.getLocation()));
                gameMapContainer.removeCleanerRobot(c);
                break;
            }
        }


        //Nagyrobotokkal való ütközés
        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {

            if (C3PO != R2D2) {
                if (C3PO.getLocation().distance(R2D2.getLocation()) < (C3PO.getHitbox() + R2D2.getHitbox())) {
                    R2D2.accept(C3PO);

                    //ezt a törlést nem tehettük meg az accept metódusban, hisz a két robot nem láthatja egymást,
                    //de a GameControl látja őket, itt történik a törlés hívás ténylegesen
                    if (C3PO.speed > R2D2.speed) {
                        gameMapContainer.removePlayerRobot(R2D2);

                        //ha tehát R2D2 halt meg, akkor visszatérünk rögtön, mert különben
                        //exception-t dob, hisz a for ciklusunk 2-ig számolt, de mi töröltük az 1.-t az 1. körben,
                        //így a 2. robot (ami most már így az 1. a listában) nem lesz található
                        break;
                    } else {
                        gameMapContainer.removePlayerRobot(C3PO);
                        break;
                    }

                }
            }
        }

    }

    //A kisrobot ütközéseit kezeljük itt le
    private void Collision(CleanerRobot C3PO) {

        //Csapdákkal való ütközés lekezelése

        for (Trap trap : gameMapContainer.getTraps()) {
            if (trap.getLocation() == GetMinDistanceTrapLocation(C3PO)) {
                Trap closest = trap;
                if (C3PO.getLocation().distance(closest.getLocation()) < (closest.getHitbox())) {
                    C3PO.location = closest.getLocation();

                    //ha már nem takarít, akkor kilépünk ebből a for ciklusból,
                    //mert különben Exception-t kapunk, mert a gameMapContainer.getTraps().size() változott
                    if (!CleaningTrap(C3PO)) break;
                }
            }
        }
        //kisrobotokkal való ütközés lekezelése
        for (CleanerRobot R2D2 : gameMapContainer.getCleanerRobots()) {
            if (C3PO != R2D2)
                if (C3PO.getLocation().distance(R2D2.getLocation()) < (C3PO.getHitbox() + R2D2.getHitbox()))
                    R2D2.accept(C3PO);
        }

        //Nagyrobotokkal való ütközés
        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {
            if (C3PO.getLocation().distance(R2D2.getLocation()) < (C3PO.getHitbox() + R2D2.getHitbox())) {
                R2D2.accept(C3PO);
            }

        }

    }


    /*Új cleaning by Jánoky*/
    //Takarít a paraméterben kapott robot
    //megkeresi az a foltot, amin áll és takarít vagy befejezi a takarítást=törli a foltot
    private boolean CleaningTrap(CleanerRobot cleaner) {
        Trap cleanupThis = null;

        for (Trap trap : gameMapContainer.getTraps()) {
            if (trap.getLocation() == GetMinDistanceTrapLocation(cleaner)) {
                cleanupThis = trap;
            }
        }

        if (cleanupThis != null) {
            if (cleaner.cleaningcount == cleaner.TimeOfCleaning) {
                gameMapContainer.removeTrap(cleanupThis);
                cleaner.cleaningcount = 0;
                cleaner.isCleaning = false;
                return false;
            } else {
                cleaner.isCleaning = true;
                cleanupThis.accept(cleaner);
                return true;
            }
        }

        //elméletileg sosem jut idáig, mert a cleanupThis mindig valid lesz,
        //hisz azért vagyunk ebben a metódusban, mert tudjuk, hogy állunk valamin
        return false;
    }

    //visszatér az átadott robot új szögével
    private double setAngleofCleanerRobot(CleanerRobot robot) {

        double angle = 0;
        Point trapLocation = GetMinDistanceTrapLocation(robot);

        if (trapLocation.getX() < 0) {
            System.out.println("Nincsen csapda a palyan.");
            //ekkor baktatunk a sajat szögünkkel tovább
            return robot.angle;
        } else {
            double x = robot.getLocation().getX() - trapLocation.getX();
            double y = robot.getLocation().getY() - trapLocation.getY();
            double atmero = robot.getLocation().distance(trapLocation);

            if (x > 0 && y >= 0)
                angle = 180 + Math.toDegrees(Math.asin(y / atmero));//jó
            if (x >= 0 && y < 0)
                angle = 180 + Math.toDegrees(Math.asin(y / atmero));//jó
            if (x <= 0 && y > 0)
                angle = 360 - Math.toDegrees(Math.asin(y / atmero));//jó
            if (x < 0 && y <= 0)
                angle = (-1) * Math.toDegrees(Math.asin(y / atmero));//jó

            return Math.round(angle);
        }


    }

    //visszaadja a legközelebbi folt koordinátáit
    private Point GetMinDistanceTrapLocation(CleanerRobot robot) {
        double minValue = 10000;
        int minTrapIndex = -1;
        for (Trap trap : gameMapContainer.getTraps()) {
            if (robot.getLocation().distance(trap.getLocation()) < minValue) {
                minValue = robot.getLocation().distance(trap.getLocation());
                minTrapIndex = gameMapContainer.getTraps().indexOf(trap);
            }
        }
        return minTrapIndex == (-1) ? new Point(-1, -1) : gameMapContainer.getTraps().get(minTrapIndex).getLocation();
    }

    //szárít az olajon, és törli az száraz ill elkopott csapdákat
    //minden kör végén kell meghívni
    public void removeOldTraps() {
        for (Trap csapda : gameMapContainer.getTraps()) {
            csapda.dry();
            if (csapda.getTimeToLive() <= 0) {
                gameMapContainer.removeTrap(csapda);
                break;
            }
        }
    }
}
