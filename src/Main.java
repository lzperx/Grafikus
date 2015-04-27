import java.io.*;

public class Main {

    static Shell shell = new Shell();

    private enum ProcessingEnum {
        create, startgame, exitgame
    }

    public static void main(String args[]) throws IOException {

        String[] sor = null;
        int i = 0;

      /*  while (bemenet[i++] != null) {
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
            
        }
        */

        //Robot kirajzolás tesztelése -- András
        Point test = new Point(200,300);
        JFrame gameFrame = new JFrame("Phoebe");

        GameFrame frame = new GameFrame(test, "testrobot");
        gameFrame.setSize(1024, 768);
        frame.setRotation(Math.toRadians(80));

        gameFrame.add(frame);
        frame.repaint();
        gameFrame.setVisible(true);
        System.out.println(frame.getRobotLocation());
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Idáig

    }
}


