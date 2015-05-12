package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.nio.Buffer;


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
        drawBullets(g2d);
        drawCleanerRobots(g2d);
        drawPlayerRobots(g2d);
        drawAmountOfTraps(g2d);
        if (Resources.gameEnd) {
            g2d.drawImage(Resources.WinnerImage,
                    ((int) gameMapContainer.getResolution().getWidth() / 2) - (Resources.WinnerImage.getWidth() / 2),
                    ((int) gameMapContainer.getResolution().getHeight() / 2) - (Resources.WinnerImage.getHeight() / 2),
                    null);
        }


    }

    public boolean retryDialogBox() {
        int reply = JOptionPane.showConfirmDialog(this, "Would you like to start again?", "You should stay!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Resources.LZImage);
        if (reply == JOptionPane.YES_OPTION)
            return true;
        return false;
    }

    private void drawBackground(Graphics2D g2d) {

        g2d.drawImage(Resources.BackgroundImage, 0, 0, null);
        if (Resources.gridEnabled) {
            for (int i = 0; i < 20; i++) {
                g2d.drawLine(i * 100, 0, i * 100, 2000);
                g2d.drawLine(0, i * 100, 2000, i * 100);
            }
        }
    }


    private void drawAmountOfTraps(Graphics2D g2d) {
        int loc = 1;
        String textOil, textGlue;
        Color color = Color.RED;
        for (PlayerRobot r : gameMapContainer.getPlayerRobots()) { //robotok foltmennyiségének kiírása
            textOil = r.name + "'s amount of oil: " + String.valueOf(r.ammountofOil) + " packs";
            textGlue = r.name + "'s amount of glue: " + String.valueOf(r.ammountofGlue) + " packs";

            drawTextWithShadow(g2d, textOil, 700, loc * 30, color);
            drawTextWithShadow(g2d, textGlue, 700, loc * 30 + 30, color);
            loc = 3;
            color = Color.BLUE;

        }

    }

    private void drawScores(Graphics2D g2d) {
        double distance;
        int loc = 1;
        String text;
        Color c = Color.RED;
        for (PlayerRobot r : gameMapContainer.getPlayerRobots()) { //robotok score-jának kiírása
            distance = r.distance;
            text = r.name + "'s score: " + String.valueOf((int) distance);
            if (r.name.toString().contains("2")) {
                c = Color.CYAN;
                drawTextWithShadow(g2d, text, 20, loc * 30, c);
                c = Color.RED;
            } else {
                drawTextWithShadow(g2d, text, 20, loc * 30, c);
            }
            loc++;
        }

        text = "Time left: " + Resources.timeLeft; //játékból hátralévő idő
        drawTextWithShadow(g2d, text, 20, loc * 30, c);
        loc++;

        if (Resources.gameEnd) //győztes
        {
            text = "Winner: " + Resources.winner;
            drawTextWithShadow(g2d, text, 20, loc * 30, c);
            loc++;
        }
    }

    private void drawTextWithShadow(Graphics2D g2d, String text, int x, int y, Color c) {
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

    private void drawPlayerRobots(Graphics2D g2d) {
        try {
            for (PlayerRobot r : gameMapContainer.getPlayerRobots()) {
                g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
                BufferedImage img;
                if (r.name.toString().contains("2")) {
                    img = Resources.PlayerRobot2Image;
                } else {
                    img = Resources.PlayerRobot1Image;
                }
                g2d.drawImage(img, (int) r.getLocation().getX() - (img.getWidth() / 2), (int) r.getLocation().getY() - (img.getHeight() / 2), null);
                g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void drawCleanerRobots(Graphics2D g2d) {
        for (CleanerRobot r : gameMapContainer.getCleanerRobots()) {
            g2d.rotate(Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
            g2d.drawImage(Resources.CleanerRobotImage, (int) r.getLocation().getX() - (Resources.CleanerRobotImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.CleanerRobotImage.getHeight() / 2), null);
            //mivel a rajzlapot forgattuk el, vissza kell csinálni
            //System.out.println("drawing: " + r.toString() + "\nat loc: " + r.getLocation() + "\nrotation: " + r.angle + "\n");
            g2d.rotate(-Math.toRadians(r.angle), r.getLocation().getX(), r.getLocation().getY()); //forgat
        }
    }

    private void drawTraps(Graphics2D g2d) {
        for (Trap r : gameMapContainer.getTraps()) {
            if (r.toString().contains("Oil")) {
                g2d.drawImage(TrapText(Resources.OilImage, String.valueOf(r.getTimeToLive())), (int) r.getLocation().getX() - (Resources.OilImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.OilImage.getWidth() / 2), null);
                //System.out.println(r.toString());
            } else {
                g2d.drawImage(TrapText(Resources.GlueImage, String.valueOf(r.getTimeToLive())), (int) r.getLocation().getX() - (Resources.GlueImage.getWidth() / 2), (int) r.getLocation().getY() - (Resources.GlueImage.getWidth() / 2), null);
            }
        }
    }

    private void drawBullets(Graphics2D g2d) {
        if (gameMapContainer.getBullets() != null) {
            for (Bullet bullet : gameMapContainer.getBullets()) {
                g2d.setColor(Color.GREEN);
                g2d.fillOval((int) bullet.getLocation().getX(), (int) bullet.getLocation().getY(), 5, 5);
            }
        }
    }

    protected BufferedImage TrapText(BufferedImage image, String text) { //feliratozzuk a csapdákat

        ColorModel cm = image.getColorModel(); //lemásoljuk a képet, hogy ne írjunk rá az eredetire
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        BufferedImage copied = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        Graphics2D gO = copied.createGraphics(); //feliratozzuk a másolt képet
        gO.setColor(Color.red);
        gO.setFont(new Font("SansSerif", Font.BOLD, 12));
        gO.drawString(text, 20, 30);
        return copied;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}
