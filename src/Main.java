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
        NewGame g = new NewGame(1400,800);
        g.controller.RoundManager();
        for (int i = 0; i <20000; i++){
            Thread.sleep(100);
            g.controller.RoundManager();
            g.viewFrame.viewPanel.repaint();
        }

    }

}


