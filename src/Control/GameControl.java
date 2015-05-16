package Control;

import Model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Math;


/*A teljes játékirányításért felelős objektum.*/

public class GameControl implements KeyListener {

    /*   Referencia a játékelemeket tároló objektumra,
     *   így tudjuk hogy milyen robotok vannak, és mi hol van a pályán.
     *   Ezt a konstruktorban kell átadni.
    */
    private GameMapContainer gameMapContainer;

    //Ezek a mi globális mértékegységeink a gyorsításnál, fordulásnál
    //minden gomblenyomásnál ennyit fog változni a sebesség/elfordulás
    public static final double speedChange = 0.3;
    public static final int angleChange = 10;

    public GameControl(GameMapContainer gameMapContainer) {
        this.gameMapContainer = gameMapContainer;

    }


    //Ez a fő metódusunk: körök kezelése (kisrobotok, nagyrobotok, foltok szárítása)
    public void RoundManager() {
        if (gameMapContainer.getPlayerRobots().size() == 1) {
            Resources.winner = gameMapContainer.getPlayerRobots().get(0).name;
            Resources.gameEnd = true;
        }

        if (Resources.timeLeft == 1) {
            Resources.gameEnd = true;
            PlayerRobot winner = gameMapContainer.getPlayerRobots().get(0);
            for (PlayerRobot r : gameMapContainer.getPlayerRobots()) {
                if (r.distance > winner.distance)
                    winner = r;
            }
            Resources.winner = winner.name;

        }

        if (gameMapContainer.getBullets() != null) {
            for (Bullet bullet : gameMapContainer.getBullets()) {
                bullet.next();
                if (isOutOfMap(bullet)) {
                    gameMapContainer.removeBullet(bullet);
                    break;
                }

            }
        }


        //minden kör elején lefuttatjuk a kisrobotokat
        for (CleanerRobot cleanerRobot : gameMapContainer.getCleanerRobots()) {
            //beállítjuk a legközelebbi folt felé, ha éppen nem takarít
            if (!cleanerRobot.isCleaning)
                cleanerRobot.angle = setAngleofCleanerRobot(cleanerRobot);
            //mozgatjuk a robotot, ha épp takarít, akkor a takarítási idő csökken
            cleanerRobot.Jump();
            endlessScreen(cleanerRobot);

            //üztökést detektálunk: frissülnek az adatok
            //ha megsemmisült valamelyik, akkor kilépünk a ciklusból, mert hibát fog dobni
            if (Collision(cleanerRobot))
                break;
        }

        //lekezeljük a pálya robotjait sorban
        try {

            for (PlayerRobot actualRobot : gameMapContainer.getPlayerRobots()) {
                //A gombkezelést a keyListener interfészen keresztül nézzük
                //és ott változtatjuk a játékos robotok értékeit.
                //a gombok segítségével beállított változásokat futtatjuk az aktuális robotra: Jump()
                actualRobot.Jump();

                //ütközést detektálunk,
                //ha megsemmisült valamelyik, akkor kilépünk a ciklusból, mert hibát fog dobni
                if (Collision(actualRobot))
                    break;

                /*
                if (isOutOfMap(actualRobot))
                    gameMapContainer.removePlayerRobot(actualRobot);
                    */
                //törlés helyett, a másik oldalon jön ki a robot
                endlessScreen(actualRobot);

            }
        } catch (Exception ex) {
            Resources.gameEnd = true;
        }

        //a kör végén lekezeljük külön a csapdákat (szárítás: törlés, ha kiszáradt)
        removeOldTraps();
        pollKey();

    }

    //ha az okjektum lemegy a pályáról, akkor true-val tér vissza
    private boolean isOutOfMap(GameElements object) {
        return object.getLocation().getX() > gameMapContainer.getResolution().getWidth() || object.getLocation().getX() < 0 ||
                object.getLocation().getY() > gameMapContainer.getResolution().getHeight() || object.getLocation().getY() < 0;
    }

    //az átadott objektum koordinátái a screen másik felére válzotnak, ha túlmentünk az egyik oldalon
    private void endlessScreen(GameElements object) {
        if (object.getLocation().getX() > gameMapContainer.getResolution().getWidth()) {
            object.getLocation().setLocation(0, object.getLocation().getY());
        }
        if (object.getLocation().getX() < 0) {
            object.getLocation().setLocation(gameMapContainer.getResolution().getWidth(), object.getLocation().getY());
        }
        if (object.getLocation().getY() > gameMapContainer.getResolution().getHeight()) {
            object.getLocation().setLocation(object.getLocation().getX(), 0);
        }
        if (object.getLocation().getY() < 0) {
            object.getLocation().setLocation(object.getLocation().getX(), gameMapContainer.getResolution().getHeight());
        }
    }

    //A nagyrobot ütközéseit kezeljük itt le
    private boolean Collision(PlayerRobot C3PO) {

        //Csapdákkal való ütközés lekezelése
        for (Trap itsATrap : gameMapContainer.getTraps()) {
            if (C3PO.getLocation().distance(itsATrap.getLocation()) < (C3PO.getHitbox() + itsATrap.getHitbox())) {
                itsATrap.accept(C3PO);
            } else C3PO.state = PlayerRobot.robotState.NORMAL;
        }

        //kisrobotokkal való ütközés lekezelése
        for (CleanerRobot c : gameMapContainer.getCleanerRobots()) {
            if (C3PO.getLocation().distance(c.getLocation()) < (C3PO.getHitbox() + c.getHitbox())) {

                c.accept(C3PO);
                gameMapContainer.addTrap(new Oil(c.getLocation()));
                Sound.splashSound.play();
                gameMapContainer.removeCleanerRobot(c);
                break;
            }
        }

        //golyókkal való ütközés
        for (Bullet bullet : gameMapContainer.getBullets()) {
            if (C3PO.getLocation().distance(bullet.getLocation()) < (C3PO.getHitbox() + bullet.getHitbox())) {
                bullet.accept(C3PO);
                gameMapContainer.removeBullet(bullet);
                break;
            }
        }

        //Nagyrobotokkal való ütközés
        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {

            if (C3PO != R2D2) {
                if (C3PO.getLocation().distance(R2D2.getLocation()) < (C3PO.getHitbox() + R2D2.getHitbox())) {
                    R2D2.accept(C3PO);

                    //ezt a törlést nem tehettük meg az accept metódusban, hisz a két robot nem láthatja egymást,
                    //de a Control.GameControl látja őket, itt történik a törlés hívás ténylegesen
                    if (C3PO.speed > R2D2.speed) {
                        Sound.crashSound.play();
                        gameMapContainer.removePlayerRobot(R2D2);

                        //ha tehát R2D2 halt meg, akkor visszatérünk rögtön, mert különben
                        //exception-t dob, hisz a for ciklusunk 2-ig számolt, de mi töröltük az 1.-t az 1. körben,
                        //így a 2. robot (ami most már így az 1. a listában) nem lesz található

                    } else {
                        Sound.crashSound.play();
                        gameMapContainer.removePlayerRobot(C3PO);

                    }
                    return true;
                }
            }
        }
        return false;
    }

    //A kisrobot ütközéseit kezeljük itt le
    private boolean Collision(CleanerRobot C3PO) {

        //Csapdákkal való ütközés lekezelése
        for (Trap trap : gameMapContainer.getTraps()) {
            if (C3PO.getLocation().distance(trap.getLocation()) < (trap.getHitbox())) {
                C3PO.location = trap.getLocation();

                //ha már nem takarít, akkor kilépünk ebből a for ciklusból,
                //mert különben Exception-t kapunk, mert a gameMapContainer.getTraps().size() változott
                if (!CleaningTrap(C3PO, trap)) {
                    break;
                }

            }
        }

        //kisrobotokkal való ütközés lekezelése
        for (CleanerRobot R2D2 : gameMapContainer.getCleanerRobots()) {
            if (C3PO != R2D2)
                if (C3PO.getLocation().distance(R2D2.getLocation()) < (C3PO.getHitbox() + R2D2.getHitbox())) {
                    R2D2.accept(C3PO);
                }

        }

        //golyókkal való ütközés
        for (Bullet bullet : gameMapContainer.getBullets()) {
            if (C3PO.getLocation().distance(bullet.getLocation()) < (C3PO.getHitbox() + bullet.getHitbox())) {
                bullet.accept(C3PO);
                gameMapContainer.removeBullet(bullet);
                gameMapContainer.removeCleanerRobot(C3PO);
                return true;
            }
        }

        //Nagyrobotokkal való ütközés
        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {
            if (C3PO.getLocation().distance(R2D2.getLocation()) < (C3PO.getHitbox() + R2D2.getHitbox())) {
                Sound.collisionSound.play();
                R2D2.accept(C3PO);
            }

        }
        return false;
    }


    //Takarít a paraméterben kapott robot
    //megkeresi az a foltot, amin áll és takarít vagy befejezi a takarítást=törli a foltot
    private boolean CleaningTrap(CleanerRobot cleaner, Trap trap) {
        if (cleaner.cleaningcount == cleaner.TimeOfCleaning || trap.getTimeToLive() == 0) {
            trap.busy = false;
            gameMapContainer.removeTrap(trap);
            cleaner.cleaningcount = 0;
            cleaner.isCleaning = false;
            return false;
        } else {
            trap.busy = true;
            cleaner.isCleaning = true;
            trap.accept(cleaner);
            return true;
        }
    }

    //visszatér az átadott robot új szögével fokban
    private double setAngleofCleanerRobot(CleanerRobot robot) {

        double angle = 0;
        Point trapLocation = GetMinDistanceTrapLocation(robot);

        if (trapLocation.getX() < 0) {
            //System.out.println("Nincsen csapda a palyan.");
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

    //visszaadja a legközelebbi SZABAD folt koordinátáit
    private Point GetMinDistanceTrapLocation(CleanerRobot robot) {
        double minValue = 10000;
        int minTrapIndex = -1;
        for (Trap trap : gameMapContainer.getTraps()) {

            //ha a folt nem foglalat, akkor nézem
            if (!trap.busy)
                if ((robot.getLocation().distance(trap.getLocation()) < minValue)) {
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
            if (csapda.getTimeToLive() <= 0 && !csapda.busy) {
                gameMapContainer.removeTrap(csapda);
                break;
            }
        }
    }

    public void pollKey() {
        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {

            if (R2D2.keys.left)
                R2D2.TurnLeft(angleChange);
            if (R2D2.keys.up)
                R2D2.Speed(speedChange);
            if (R2D2.keys.right)
                R2D2.TurnRight(angleChange);
            if (R2D2.keys.down)
                R2D2.Speed(-speedChange);
        }
    }

    //keylistener-hez tartozó megvalósítandó metódus
    //ha lenyomták az adott gombot, akkor hajtódnak végre az adott változások
    @Override
    public void keyTyped(KeyEvent e) {


        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {

            if (e.getKeyCode() == R2D2.keys.getLeftKey())
                R2D2.TurnLeft(angleChange);
            if (e.getKeyCode() == R2D2.keys.getUpKey())
                R2D2.Speed(speedChange);
            if (e.getKeyCode() == R2D2.keys.getRightKey())
                R2D2.TurnRight(angleChange);
            if (e.getKeyCode() == R2D2.keys.getDownKey())
                R2D2.Speed(-speedChange);

        }

    }


    //keylistener-hez tartozó megvalósítandó metódus
    //nekünk most itt nem kell használnunk
    @Override
    public void keyReleased(KeyEvent e) {

        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {

            if (e.getKeyCode() == R2D2.keys.getLeftKey())
                R2D2.keys.left = false;
            if (e.getKeyCode() == R2D2.keys.getUpKey())
                R2D2.keys.up = false;
            if (e.getKeyCode() == R2D2.keys.getRightKey())
                R2D2.keys.right = false;
            if (e.getKeyCode() == R2D2.keys.getDownKey())
                R2D2.keys.down = false;
            if (e.getKeyCode() == R2D2.keys.getOilKey()) {

                if (R2D2.ammountofOil > 0)
                    gameMapContainer.addTrap(new Oil(R2D2.getLocation()));
                R2D2.PutOil();
            }

            if (e.getKeyCode() == R2D2.keys.getGlueKey()) {

                if (R2D2.ammountofGlue > 0)
                    gameMapContainer.addTrap(new Glue(R2D2.getLocation()));
                R2D2.PutGlue();
            }

            if (e.getKeyCode() == R2D2.keys.getGunKey()) {
                gameMapContainer.addBullet(new Bullet(R2D2.getLocation(), R2D2.angle, R2D2.speed));
                Sound.bulletSound.play();
            }
        }
    }

    //keylistener-hez tartozó megvalósítandó metódus
    //nekünk most itt nem kell használnunk
    @Override
    public void keyPressed(KeyEvent e) {


        for (PlayerRobot R2D2 : gameMapContainer.getPlayerRobots()) {

            if (e.getKeyCode() == R2D2.keys.getLeftKey())
                R2D2.keys.left = true;
            if (e.getKeyCode() == R2D2.keys.getUpKey())
                R2D2.keys.up = true;
            if (e.getKeyCode() == R2D2.keys.getRightKey())
                R2D2.keys.right = true;
            if (e.getKeyCode() == R2D2.keys.getDownKey())
                R2D2.keys.down = true;
        }
    }


}
