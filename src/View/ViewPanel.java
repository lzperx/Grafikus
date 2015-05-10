package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
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
        drawScores(g2d);
        drawTraps(g2d);
        drawCleanerRobots(g2d);
        drawPlayerRobots(g2d);
        if(Resources.gameEnd)
            g2d.drawImage(Resources.WinnerImage, 200,20,null);

        //System.out.println("repainting...");
    }

    private void drawBackground(Graphics2D g2d){

        g2d.drawImage(Resources.BackgroundImage,0,0,null);
        if (Resources.gridEnabled) {
            for (int i = 0; i < 20; i++) {
                g2d.drawLine(i * 100, 0, i * 100, 2000);
                g2d.drawLine(0, i * 100, 2000, i * 100);
            }
        }
    }

    private void drawScores(Graphics2D g2d){
        double distance;
        int loc = 1;
        String text;
        Color c = Color.RED;
        for (PlayerRobot r : gameMapContainer.getPlayerRobots()) { //robotok score-jának kiírása
            distance = r.distance;
            text = r.name + "'s score: " + String.valueOf((int)distance);
            if(r.name.toString().contains("2")) {
                c = Color.BLUE;
                drawTextWithShadow(g2d, text, 20, loc * 30, c);
                c = Color.RED;
            }
            else {
                drawTextWithShadow(g2d, text, 20, loc * 30, c);
            }
            loc++;
        }

        text = "Time left: " + Resources.timeLeft; //játékból hátralévő idő
        drawTextWithShadow(g2d,text,20, loc*30, c);
        loc++;

        if(Resources.gameEnd) //győztes
        {
            text = "Winner: " + Resources.winner;
            drawTextWithShadow(g2d, text, 20, loc*30, c);
            loc++;
        }
    }

    private void drawTextWithShadow(Graphics2D g2d, String text, int x, int y, Color c)
    {
        Font font = new Font("SansSerif", Font.BOLD, 20);
        g2d.setColor(c);
        g2d.setFont(font);
        TextLayout textLayout = new TextLayout(text, font, g2d.getFontRenderContext());
        g2d.setPaint(new Color(0, 0, 0));
        textLayout.draw(g2d, x + 2, y + 2);
        g2d.setPaint(c);
        textLayout.draw(g2d, x, y);
        g2d.drawString(text, x, y);
    }

    private void drawPlayerRobots(Graphics2D g2d){
        PlayerRobot r = null;
        //egyik robot
        try {
            r = gameMapContainer.getPlayerRobots().get(0);
            g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
            g2d.drawImage(Resources.PlayerRobot1Image, (int) r.getLocation().getX() - (Resources.PlayerRobot1Image.getWidth() / 2), (int) r.getLocation().getY() - (Resources.PlayerRobot1Image.getHeight() / 2), null);
            //mivel a rajzlapot forgattuk el, vissza kell csinálni
            g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
        }catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
        //másik robot
        try {
            r = gameMapContainer.getPlayerRobots().get(1);
            g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY());
            g2d.drawImage(Resources.PlayerRobot2Image, (int) r.getLocation().getX() - (Resources.PlayerRobot2Image.getWidth() / 2), (int) r.getLocation().getY() - (Resources.PlayerRobot2Image.getHeight() / 2), null);
            g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    private void drawCleanerRobots(Graphics2D g2d){
        for(CleanerRobot r : gameMapContainer.getCleanerRobots()) {
            g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
            g2d.drawImage(Resources.CleanerRobotImage, (int) r.getLocation().getX() - (Resources.CleanerRobotImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.CleanerRobotImage.getHeight() / 2), null);
            //mivel a rajzlapot forgattuk el, vissza kell csinálni
            //System.out.println("drawing: " + r.toString() + "\nat loc: " + r.getLocation() + "\nrotation: " + r.angle + "\n");
            g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
        }
    }

    private void drawTraps(Graphics2D g2d){
        for(Trap r : gameMapContainer.getTraps()) {
            if(r.toString().contains("Oil")) {
                g2d.drawImage(TrapText(Resources.OilImage, String.valueOf(r.getTimeToLive())), (int) r.getLocation().getX() - (Resources.OilImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.OilImage.getWidth() / 2), null);
                //System.out.println(r.toString());
            }
            else
            {
                g2d.drawImage(TrapText(Resources.GlueImage, String.valueOf(r.getTimeToLive())), (int) r.getLocation().getX()- (Resources.GlueImage.getWidth() / 2), (int) r.getLocation().getY()  - (Resources.GlueImage.getWidth() / 2), null);
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
