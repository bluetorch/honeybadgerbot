package us.bluetorch.robowiki.radar;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class Spinning extends Radar {

  public Spinning(AdvancedRobot robot) {
    super(robot);
  }

  @Override
  public void run() {

    while(true) {
      robot.turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent e, int enemyCount) {
    //Do nothing
  }

  @Override
  public void onRobotDeath(RobotDeathEvent e) {
    //Do Nothing
  }
}
