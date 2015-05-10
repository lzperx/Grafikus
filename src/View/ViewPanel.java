package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;


// az ablakon belül lévő megjelenítő panelt valósítja meg.
// újrarajzolása (repaint()) hívás esetén megkérdezi a gamemapcontainert,
// amire van egy referenciája, hogy mi hol van, majd kirajzolja az összes játékelemet

public class ViewPanel extends JPanel {

    GameMapContainer gameMapContainer; //tartalmaz egy referenciát a gamemaocontainer-re

    protected ViewPanel(GameMapContainer gameMapContainer) //konstruktor
    {
        this.gameMapContainer = gameMapContainer;
    }


    private void doDrawing(Graphics g) {     //ez fut le a repaint() hívásakor.
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        drawTraps(g2d);
        drawCleanerRobots(g2d);
        drawPlayerRobots(g2d);
        System.out.println("repainting...");
    }

    private void drawBackground(Graphics2D g2d){
        g2d.drawImage(Resources.BackgroundImage,0,0,null);
    }

    private void drawPlayerRobots(Graphics2D g2d){
        //egyik robot
        PlayerRobot r = gameMapContainer.getPlayerRobots().get(0);
        g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX() + (Resources.PlayerRobot1Image.getWidth()/2), r.getLocation().getY() + (Resources.PlayerRobot1Image.getHeight()/2)); //forgat
        g2d.drawImage(Resources.PlayerRobot1Image, (int)r.getLocation().getX() - (Resources.PlayerRobot1Image.getWidth()/2), (int)r.getLocation().getY() - (Resources.PlayerRobot1Image.getHeight()/2), null);
        //mivel a rajzlapot forgattuk el, vissza kell csinálni
        g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX() + (Resources.PlayerRobot1Image.getWidth()/2), r.getLocation().getY() + (Resources.PlayerRobot1Image.getHeight()/2)); //forgat
        //másik robot
        r = gameMapContainer.getPlayerRobots().get(1);
        g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX() + (Resources.PlayerRobot1Image.getWidth()/2), r.getLocation().getY() + (Resources.PlayerRobot1Image.getHeight()/2));
        g2d.drawImage(Resources.PlayerRobot1Image, (int)r.getLocation().getX() - (Resources.PlayerRobot1Image.getWidth()/2), (int)r.getLocation().getY() - (Resources.PlayerRobot1Image.getHeight()/2), null);
        g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX() + (Resources.PlayerRobot1Image.getWidth()/2), r.getLocation().getY() + (Resources.PlayerRobot1Image.getHeight()/2)); //forgat
    }

    private void drawCleanerRobots(Graphics2D g2d){
        for(CleanerRobot r : gameMapContainer.getCleanerRobots()) {
            g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX() + (Resources.CleanerRobotImage.getWidth() / 2), r.getLocation().getY() + (Resources.CleanerRobotImage.getHeight() / 2)); //forgat
            g2d.drawImage(Resources.CleanerRobotImage, (int) r.getLocation().getX() - (Resources.CleanerRobotImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.CleanerRobotImage.getHeight() / 2), null);
            //mivel a rajzlapot forgattuk el, vissza kell csinálni
            System.out.println("drawing: " + r.toString() + "\nat loc: " + r.getLocation() + "\nrotation: " + r.angle + "\n");
            g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX() + (Resources.CleanerRobotImage.getWidth() / 2), r.getLocation().getY() + (Resources.CleanerRobotImage.getHeight() / 2)); //forgat
        }
    }

    private void drawTraps(Graphics2D g2d){
        for(Trap r : gameMapContainer.getTraps()) {
            if(r.toString().contains("Oil")) {
                g2d.drawImage(TrapText(Resources.OilImage, String.valueOf(r.getTimeToLive())), (int) r.getLocation().getX() - (Resources.OilImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.OilImage.getHeight() / 2), null);
                //System.out.println(r.toString());
            }
            else
            {
                g2d.drawImage(TrapText(Resources.GlueImage, String.valueOf(r.getTimeToLive())), (int) r.getLocation().getX() - (Resources.GlueImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.GlueImage.getHeight() / 2), null);
            }
        }
    }
    protected BufferedImage TrapText(BufferedImage image, String text){ //feliratozzuk a csapdákat

        ColorModel cm = image.getColorModel(); //lemásoljuk a képet, hogy ne írjunk rá az eredetire
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        BufferedImage copied = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        Graphics2D gO = copied.createGraphics(); //feliratozzuk a másolt képet
        gO.setColor(Color.red);
        gO.setFont(new Font( "SansSerif", Font.BOLD, 12 ));
        gO.drawString(text, 20, 30);
        return copied;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}
