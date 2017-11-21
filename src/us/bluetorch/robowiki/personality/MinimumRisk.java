package us.bluetorch.robowiki.personality;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class MinimumRisk implements Personality {

    private AdvancedRobot r;
    static Point2D myLocation, last;
    static EnemyInfo currentTarget;
    private HashMap<String, EnemyInfo> enemies;
    static HashMap stats = new HashMap();
    private boolean isRunning = true;
    

    public MinimumRisk(AdvancedRobot robot) {
        r = robot;
        enemies = new HashMap<String, EnemyInfo>();
        r.setColors(Color.DARK_GRAY, Color.BLACK, Color.BLACK, Color.BLACK, Color.YELLOW);
        r.out.println("Minimum Risk Activated");
    }

    public void run() {
        r.setTurnRadarRight(Double.POSITIVE_INFINITY);
        Point2D next = currentTarget = null;
        do {
            //r.out.print("run ");
            myLocation = new Point2D.Double(r.getX(), r.getY());
            if (currentTarget != null) {
                if (next == null) {
                    next = last = myLocation;
                }
                if (next.distance(myLocation) < 15 || r.getOthers() > 1) {
                    boolean changed = false;
                    double angle = 0;
                    double moveDist = myLocation.distance(currentTarget);
                    moveDist = (r.getOthers() == 1 ? Math.random() * moveDist : Math.min(500, moveDist)) * .5;
                    do {
                        Point2D p;
                        if (inField(p = projectPoint(myLocation, angle, moveDist)) && findRisk(p) < findRisk(next)) {
                            changed = true;
                            next = p;
                        }
                        angle += .1;
                    } while (angle < Math.PI * 2);
                    if (changed) {
                        last = myLocation;
                    }
                }
                double a1, a2;
                r.setTurnRightRadians(a1 = Math.atan(Math.tan(a2 = robocode.util.Utils.normalRelativeAngle(angle(next, myLocation) - r.getHeadingRadians()))));
                r.setAhead(Math.abs(r.getTurnRemainingRadians()) > 1 ? 0 : (a1 == a2 ? 1.0 : -1.0) * next.distance(myLocation));
            }
            r.execute();
        } while (isRunning);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        String name;
        EnemyInfo enemy = enemies.get(name = e.getName());
        if (enemy == null) {
            enemies.put(name, enemy = new EnemyInfo());
        }
        enemy.setLocation(projectPoint(new Point2D.Double(r.getX(), r.getY()), r.getHeadingRadians() + e.getBearingRadians(), e.getDistance()));
        if (myLocation == null) {
            myLocation = new Point2D.Double(r.getX(), r.getY());
        }
        if (currentTarget == null || e.getDistance() < myLocation.distance(currentTarget)) {
            currentTarget = enemy;
        }

    }

    public void onHitByBullet(HitByBulletEvent e) {
        EnemyInfo enemy;
        if ((enemy = enemies.get(e.getName())) != null) {
            enemy.lastHit = r.getTime();
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        //the EnemyInfo objects are volitile and made for a round at a time, so I can sacrifice them when they die:
        if (enemies.remove(e.getName()) == currentTarget) {
            currentTarget = null;
        }
    }

    private double findRisk(Point2D point) {
        double risk = 0;
        Collection<EnemyInfo> enemySet;
        Iterator<EnemyInfo> it = (enemySet = enemies.values()).iterator();
        do {
            EnemyInfo e = it.next();
            double thisrisk = (e.energy + 50) / point.distanceSq(e);
            int closer = 0;
            Iterator<EnemyInfo> it2 = enemySet.iterator();
            do {
                EnemyInfo e2 = it2.next();
                if (e.distance(e2) * .9 > e.distance(point)) {
                    closer++;
                }
            } while (it2.hasNext());
            if (closer <= 1 || e.lastHit > r.getTime() - 200 || e == currentTarget) {
                thisrisk *= 2 + 2 * Math.abs(Math.cos(angle(myLocation, point) - angle(e, myLocation)));
            }
            risk += thisrisk;
        } while (it.hasNext());
        if (r.getOthers() > 1) {
            risk += Math.random() / last.distanceSq(point);
        }
        risk += Math.random() / 5 / myLocation.distanceSq(point);
        return risk;
    }

    public static Point2D projectPoint(Point2D startPoint, double theta, double dist) {
        return new Point2D.Double(startPoint.getX() + dist * Math.sin(theta), startPoint.getY() + dist * Math.cos(theta));
    }

    public static double angle(Point2D point2, Point2D point1) {
        return Math.atan2(point2.getX() - point1.getX(), point2.getY() - point1.getY());
    }

    public boolean inField(Point2D p) {
        return new Rectangle2D.Double(30, 30, r.getBattleFieldWidth() - 60, r.getBattleFieldHeight() - 60).contains(p);
    }

    public void stop() {
        isRunning  = false;
    }

    private class EnemyInfo extends Point2D.Double {

        long lastHit;
        double energy;
        double velocity;
    }
}
