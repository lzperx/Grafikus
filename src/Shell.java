import java.awt.*;


public class Shell {

    //glob�lis v�ltoz�k a kiirat�shoz
    public static String[] kimenet = new String[10000];
    public static int outdb = 0;  //kimeneti sor sz�ml�l�
    public static int round = 0;
    public static int robotdb = 0;
    public static int RobotSorszam = 0; //1-t�l indexelj�k
    private  enum CreateManagerEnum{
        map, robot, clrobot, glue, oil
    }
    private enum RoundManagerEnum{
        speed, right, left, putoil, putglue
    }
    NewGame game;



    public void CreateManager(String[] sor) {
        CreateManagerEnum enumCreate = CreateManagerEnum.valueOf(sor[1]);
        switch (enumCreate) {
            case map:
                game = new NewGame(Integer.parseInt(sor[2]), Integer.parseInt(sor[3]));
                break;
            case robot:
                game.gameMap.addPlayerRobot(
                        new PlayerRobot(
                                new Point(Integer.parseInt(sor[2]), Integer.parseInt(sor[3])),
                                10,
                                Integer.parseInt(sor[4])));
                break;
            case clrobot:
                game.gameMap.addCleanerRobot(
                        new CleanerRobot(new Point(Integer.parseInt(sor[2]), Integer.parseInt(sor[3])), 10));
                break;
            case glue:
                game.gameMap.addTrap(new Glue(new Point(Integer.parseInt(sor[2]), Integer.parseInt(sor[3]))));
                break;
            case oil:
                game.gameMap.addTrap(new Oil(new Point(Integer.parseInt(sor[2]), Integer.parseInt(sor[3]))));
                break;
        }
    }


    //RoundManager: a controllMinions funkci�j�t t�lti be a protoban
    public void RoundManager(String[] sor) {

        robotdb=game.gameMap.getPlayerRobots().size();
        RobotSorszam++;
        RobotSorszam= (RobotSorszam>robotdb)? RobotSorszam=1: RobotSorszam;

        PlayerRobot robot = game.gameMap.getPlayerRobots().get(RobotSorszam-1);


        if (RobotSorszam  == 1) {
            kimenet[++outdb] = "Round" + (++round); //k�r�k sz�ma
            game.controller.ControlCleanerRobots();

        }

        RoundManagerEnum enumCreate = RoundManagerEnum.valueOf(sor[0]);
        switch (enumCreate) {
            case speed:
                robot.Speed(Integer.parseInt(sor[1]));
                break;
            case right:
                robot.TurnRight(Integer.parseInt(sor[1]));
                break;
            case left:
                robot.TurnLeft(Integer.parseInt(sor[1]));
                break;
            case putoil:
                    if(robot.PutOil())
                    game.gameMap.addTrap(new Oil(robot.getLocation()));
                break;
            case putglue:
                if(robot.PutGlue())
                    game.gameMap.addTrap(new Glue(robot.getLocation()));
                break;
        }


        game.controller.ControlPlayerRobot(robot);

        if (RobotSorszam==robotdb) {
            game.controller.removeOldTraps();
        }



    }

}
