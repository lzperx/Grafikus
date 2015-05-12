package Model;
import javax.sound.sampled.*;
import java.io.File;

/**
 * Created by andri_000 on 2015.05.12..
 */
public class Sound {

    private Clip clip;

    public static Sound winnerSound = new Sound("yourwinner.wav");
    public static Sound startSound = new Sound("start.wav");
    public static Sound hogyneSound = new Sound("LZ_hogyne.wav");
    public static Sound collisionSound = new Sound("collision.wav");
    public static Sound crashSound = new Sound("crash.wav");
    public static Sound bulletSound = new Sound("lasergun.wav");

    public Sound(String fileName) {
        if (fileName != "") {
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("Sounds/" + fileName));
                clip = AudioSystem.getClip();
                clip.open(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void play (){
        try {
            if (clip != null){
                new Thread(){
                    public void run (){
                        synchronized (clip){
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop (){
        if (clip == null) return;
        clip.stop();
    }

    public void loop(){
        try {
            if(clip != null){
                new Thread(){
                    public void run(){
                        synchronized (clip){
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isActive(){
        return clip.isActive();
    }

}
