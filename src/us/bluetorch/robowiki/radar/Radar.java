package us.bluetorch.robowiki.radar;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public abstract class Radar {
  AdvancedRobot robot;

  public Radar(AdvancedRobot robot) {
    this.robot = robot;
  }

  public abstract void run();

  public abstract void onScannedRobot(ScannedRobotEvent e, int enemyCount);

  public abstract void onRobotDeath(RobotDeathEvent e);


}
