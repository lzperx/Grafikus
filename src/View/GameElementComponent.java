package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameElementComponent extends JComponent {

    Point componentLocation = null;
    double rotation = 0;
    BufferedImage testRobot = null;

    /*Beállítja a GameFrame-t,
      hogy a kívánt nevű robottal lehessen játszani.
     */
    public GameElementComponent(Point robot1Location, String name){
        componentLocation = robot1Location;
        testRobot = loadRobot(name);
    }

    /* Betölti a robot képét a program futási mappájában lévő robot mappából.
       A robot képét .png kiterjesztésben kell a robot mappában tárolni.
       A betöltendő robot nevét paraméterként kell átadni a metódusnak
       (ez a file neve)
    */
    private BufferedImage loadRobot(String name){

        try{
            testRobot = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "pic" + File.separator + name + ".png"));
        }catch (Exception e){
            System.out.println("File not found");
            System.out.println(System.getProperty("user.dir") + File.separator + "pic" + File.separator);
        }
        return testRobot;
    }

    
    // setterek
    public void setRotation(double angle){
        rotation = angle;
    }

    public void  setRobotLocation(Point newLocation){
        componentLocation = newLocation;
    }

    // a megfelelő irányba fordított robot kirajzolása
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(rotation,componentLocation.getX() + (testRobot.getWidth()/  2), componentLocation.getY() + (testRobot.getHeight() / 2));
        g2d.drawImage(testRobot,(int)componentLocation.getX(),(int)componentLocation.getY(),null);
    }
}