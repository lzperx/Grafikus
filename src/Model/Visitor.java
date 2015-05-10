package Model;// Model.Visitor interfész a csapdákhoz


import Model.CleanerRobot;
import Model.PlayerRobot;

public interface Visitor {

    public void accept(PlayerRobot robot);
    public void accept(CleanerRobot robot);

}
