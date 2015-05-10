package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Balázs on 2015.05.10..
 */
public class Resources {
    public static BufferedImage BackgroundImage;
    public static BufferedImage PlayerRobot1Image;
    public static BufferedImage PlayerRobot2Image;
    public static BufferedImage CleanerRobotImage;
    public static BufferedImage OilImage;
    public static BufferedImage GlueImage;

    /**
     * A betöltés.
     *
     * @throws IOException
     */
    public static void load() throws IOException { //ezt a függvényt illik a program indításakor meghívni,
    // memóriába tölti az erőforrásokat
    // - és időben jelez ha nincsenek meg.
        BackgroundImage = ImageIO.read(new File("pic/bg.jpg"));
        PlayerRobot1Image = ImageIO.read(new File("pic/testrobot.png"));
        PlayerRobot2Image = ImageIO.read(new File("pic/testrobot.png"));
        CleanerRobotImage = ImageIO.read(new File("pic/kisrobot.png"));
        OilImage = ImageIO.read(new File("pic/oil.png"));
        GlueImage = ImageIO.read(new File("pic/glue.png"));
    }
}
