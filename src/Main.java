import java.io.*;

import Control.NewGame;
import Model.Resources;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {


    public static void main(String args[]) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {


        while (true) {

                if ((args.length == 1) && (args[0].equals("hszk")))
                    Resources.isHSZK = true;
                Resources.load(); //erőforrások betöltése

            Resources.gridEnabled = false; //grid megjelenítése
            Resources.timeLeft = 1000; //a játék hossza
            Resources.startSound.start();
            NewGame g = new NewGame(1024, 768);
            g.controller.RoundManager();
            while (!Resources.gameEnd) {
                Thread.sleep(30);
                g.controller.RoundManager();
                g.viewFrame.viewPanel.repaint();
                Resources.timeLeft--;

            }

            //Megnézi, hogy fut e a játék indulásának hangja, és ha igen akkor vár a lejátszással
            while (Resources.startSound.isActive()){
                Thread.sleep(500);
            }
            while (Resources.crashSound.isActive()){
                Thread.sleep(500);
            }
            Resources.winnerSound.start();
            //megkérdezi, hogy újra kezdjük-e
            Thread.sleep(3000);
            if (!g.viewFrame.viewPanel.retryDialogBox())
                System.exit(0);
            else {
                Resources.hogyneSound.start();
                Thread.sleep(2500);
                Resources.gameEnd = false;
                g.viewFrame.dispose();
            }


        }

    }

}


