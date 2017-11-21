package us.bluetorch.robowiki.radar;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class Infinity extends Radar {

  public Infinity(AdvancedRobot robot) {
    super(robot);
  }

  @Override
  public void run() {
    robot.turnRadarRightRadians(Double.POSITIVE_INFINITY);
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent e, int enemyCount) {
    robot.setTurnRadarLeftRadians(robot.getRadarTurnRemainingRadians());
  }

  @Override
  public void onRobotDeath(RobotDeathEvent e) {

  }
}
