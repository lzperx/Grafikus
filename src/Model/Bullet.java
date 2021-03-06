package Model;


import java.awt.*;

public class Bullet extends GameElements {

    private double angle;
    private double speed;

    public Bullet(Point location, double angle, double speed) {
        super(location,5);
        this.location=new Point(
                (int) ((Resources.PlayerRobot1Image.getWidth()/2) * Math.cos(Math.toRadians(angle))) + (int) location.getX(),
                (int) ((Resources.PlayerRobot1Image.getWidth()/2) * Math.sin(Math.toRadians(angle))) + (int) location.getY());
        this.angle = angle;
        this.speed = 10+Math.abs((speed));
    }

    public void next() {

        location = new Point(
                (int) (speed * Math.cos(Math.toRadians(angle))) + (int) location.getX(),
                (int) (speed * Math.sin(Math.toRadians(angle))) + (int) location.getY());

    }

    public double getAngle(){return  angle;}

    @Override
    public void accept(PlayerRobot robot) {
        robot.gotHit(this);
    }

    @Override
    public void accept(CleanerRobot robot) {
        robot.gotHit(this);
    }
}
