// Visitor interfész a csapdákhoz


public interface Visitor {

    public void accept(PlayerRobot robot);
    public void accept(CleanerRobot robot);

}
