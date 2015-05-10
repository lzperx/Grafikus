import java.io.*;

import Control.NewGame;
import Model.Resources;

public class Main {


    public static void main(String args[]) throws IOException, InterruptedException {


        while (true) {
            try {
                if ((args.length == 1) && (args[0].equals("hszk")))
                    Resources.isHSZK = true;
                Resources.load(); //erőforrások betöltése

            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            Resources.gridEnabled = false; //grid megjelenítése
            Resources.timeLeft = 1000; //a játék hossza
            NewGame g = new NewGame(1024, 768);
            g.controller.RoundManager();
            while (!Resources.gameEnd) {
                Thread.sleep(30);
                g.controller.RoundManager();
                g.viewFrame.viewPanel.repaint();
                Resources.timeLeft--;

            }

            //megkérdezi, hogy újra kezdjük-e
            Thread.sleep(2000);
            if (!g.viewFrame.viewPanel.retryDialogBox())
                System.exit(0);
            else {
                Resources.gameEnd = false;
                g.viewFrame.dispose();
            }


        }

    }

}


