import java.io.*;

public class Main {

    static Shell shell = new Shell();

    private enum ProcessingEnum {
        create, startgame, exitgame
    }

    public static void main(String args[]) throws IOException {

        String[] sor = null;
        int i = 0;

        while (bemenet[i++] != null) {
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
    }
}

