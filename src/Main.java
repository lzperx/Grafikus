import java.io.*;

import Control.NewGame;
import Model.Resources;

public class Main{



    public static void main(String args[]) throws IOException, InterruptedException {

        try{
            Resources.load(); //erőforrások betöltése
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
        }
        Resources.gridEnabled = false; //grid megjelenítése
        Resources.timeLeft = 500; //a játék hossza
        NewGame g = new NewGame(1400,800);
        g.controller.RoundManager();
        while (Resources.timeLeft > 0){
            Thread.sleep(100);
            g.controller.RoundManager();
            g.viewFrame.viewPanel.repaint();
            Resources.timeLeft--;
        }

    }

}


