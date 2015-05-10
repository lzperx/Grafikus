import java.io.*;

import Control.NewGame;
import Model.Resources;
import View.*;

public class Main {


    public static void main(String args[]) throws IOException, InterruptedException {

        String[] sor = null;
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
        /*while (bemenet[i++] != null) {
            sor = bemenet[i - 1].split(" ");
            ProcessingEnum process = ProcessingEnum.valueOf(sor[0]);
            switch (process) {
                case create:
                    shell.CreateManager(sor);
                    break;
                case startgame:
                   System.out.println( "A jatek sikeresen elindult!");
                    break;
                case exitgame:
                   System.out.println( "A jatek sikeresen leallt!");
                    break;
                default:
                    shell.RoundManager(sor);
            }
            
        }*/


        /*//Robot kirajzolás tesztelése -- András
        Point test = new Point(200,300);
        JFrame gameFrame = new JFrame("Phoebe");

        GameElementComponent frame = new GameElementComponent(test, "testrobot");
        gameFrame.setSize(1024, 768);
        frame.setRotation(Math.toRadians(80));

        gameFrame.add(frame);
        frame.repaint();
        gameFrame.setVisible(true);
        //System.out.println(frame.getRobotLocation());
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Idáig*/

    }
}


