package Model;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

    /*
        A pályán lévő használni kívánt képek és egyéb erőforrások
        betöltésére hivatott osztály
     */
public class Resources {
    public static BufferedImage BackgroundImage;
    public static BufferedImage PlayerRobot1Image;
    public static BufferedImage PlayerRobot2Image;
    public static BufferedImage CleanerRobotImage;
    public static BufferedImage OilImage;
    public static BufferedImage GlueImage;
    public static BufferedImage WinnerImage;
    public static ImageIcon LZImage;
    public static boolean gridEnabled = true;
    public static int timeLeft = 0; // a játékból hátralévő idő
    public static boolean gameEnd = false;
    public static String winner = "Error";
    public static int maxSpeed = 9;
    public static Clip winnerSound;
    public static Clip startSound;
    public static Clip hogyneSound;
    public static Clip collisionSound;
    public static Clip crashSound;


        //hszk gépein úgy fut, ha isHSZK = true;
    public static boolean isHSZK = false;
    /**
     * A betöltés.
     *
     * @throws IOException
     */
    public static void load() throws IOException, LineUnavailableException, UnsupportedAudioFileException { //ezt a függvényt illik a program indításakor meghívni,
    // memóriába tölti az erőforrásokat
    // - és időben jelez ha nincsenek meg.
    	if(isHSZK){
    	BackgroundImage = ImageIO.read(new File("../pic/bg.jpg"));
        PlayerRobot1Image = ImageIO.read(new File("../pic/testrobot.png"));
        PlayerRobot2Image = ImageIO.read(new File("../pic/testrobot2.png"));
        CleanerRobotImage = ImageIO.read(new File("../pic/kisrobot.png"));
        OilImage = ImageIO.read(new File("../pic/oil.png"));
        GlueImage = ImageIO.read(new File("../pic/glue.png"));
        WinnerImage = ImageIO.read(new File("../pic/winner.jpg"));
        LZImage = new ImageIcon("../pic/lz.jpg");
        winnerSound = AudioSystem.getClip();
        winnerSound.open(AudioSystem.getAudioInputStream((new File("../Sounds/yourwinner.wav"))));
        startSound = AudioSystem.getClip();
        startSound.open(AudioSystem.getAudioInputStream((new File("../Sounds/start.wav"))));
        hogyneSound = AudioSystem.getClip();
        hogyneSound.open(AudioSystem.getAudioInputStream((new File("../Sounds/LZ_hogyne.wav"))));
        collisionSound = AudioSystem.getClip();
        collisionSound.open(AudioSystem.getAudioInputStream((new File("../Sounds/collision.wav"))));
        crashSound = AudioSystem.getClip();
        crashSound.open(AudioSystem.getAudioInputStream((new File("../Sounds/crash.wav"))));
    	}
    	else{
    	BackgroundImage = ImageIO.read(new File("pic/bg.jpg"));
        PlayerRobot1Image = ImageIO.read(new File("pic/testrobot.png"));
        PlayerRobot2Image = ImageIO.read(new File("pic/testrobot2.png"));
        CleanerRobotImage = ImageIO.read(new File("pic/kisrobot.png"));
        OilImage = ImageIO.read(new File("pic/oil.png"));
        GlueImage = ImageIO.read(new File("pic/glue.png"));
        WinnerImage = ImageIO.read(new File("pic/winner.jpg"));
        LZImage = new ImageIcon("pic/lz.jpg");
        winnerSound = AudioSystem.getClip();
        winnerSound.open(AudioSystem.getAudioInputStream((new File("Sounds/yourwinner.wav"))));
        startSound = AudioSystem.getClip();
        startSound.open(AudioSystem.getAudioInputStream((new File("Sounds/start.wav"))));
        hogyneSound = AudioSystem.getClip();
        hogyneSound.open(AudioSystem.getAudioInputStream((new File("Sounds/LZ_hogyne.wav"))));
        collisionSound = AudioSystem.getClip();
        collisionSound.open(AudioSystem.getAudioInputStream((new File("Sounds/collision.wav"))));
        crashSound = AudioSystem.getClip();
        crashSound.open(AudioSystem.getAudioInputStream((new File("Sounds/crash.wav"))));
    	}
        
    }
}
