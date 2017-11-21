package us.bluetorch.robowiki.radar;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.util.LinkedHashMap;

public class Oldest extends Radar {

  static LinkedHashMap<String, Double> enemyHashMap;
  static double scanDir;
  static Object sought;

  public Oldest(AdvancedRobot robot) {
    super(robot);
  }

  @Override
  public void run() {
    scanDir = 1;
    enemyHashMap = new LinkedHashMap<>(5, 2, true);

    while(true) {
      robot.setTurnRadarRightRadians(scanDir * Double.POSITIVE_INFINITY);
      robot.scan();
    }
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent e, int enemyCount) {
    String name = e.getName();
    LinkedHashMap<String, Double> ehm = enemyHashMap;
    ehm.put(name, robot.getHeadingRadians() + e.getBearingRadians());

    if ((name == sought || sought == null) && ehm.size() == robot.getOthers()) {
      scanDir = Utils.normalRelativeAngle(ehm.values().iterator().next() - robot.getRadarHeadingRadians());
      sought = ehm.keySet().iterator().next();
    }
  }

  @Override
  public void onRobotDeath(RobotDeathEvent e) {
    enemyHashMap.remove(e.getName());
    sought = null;
  }
}