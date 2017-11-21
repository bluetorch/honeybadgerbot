/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.bluetorch.robowiki.personality;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author bruceholt
 */
public class Bloodlust implements Personality {

    static final double WALL_MARGIN = 17;
    static double oldHeading;		// the previous heading of the selected opponent
    static double fieldWidth;		// the width of the battlefield
    static double fieldHeight;	// the heigth of the battlefield
    static Rectangle2D.Double fireField;	// the shootable battlefield
    static Point2D.Double myNextPos = new Point2D.Double();	// my next position
    private AdvancedRobot r;
    private String target;

    public Bloodlust(AdvancedRobot r) {
        this.r = r;
        // First set the statistics
        fieldWidth = r.getBattleFieldWidth() - WALL_MARGIN;
        fieldHeight = r.getBattleFieldHeight() - WALL_MARGIN;
        fireField = new Rectangle2D.Double(WALL_MARGIN, WALL_MARGIN, fieldWidth - WALL_MARGIN, fieldHeight - WALL_MARGIN);
    }

    public void run() {
        r.turnRadarRightRadians(Double.POSITIVE_INFINITY);
        r.scan();
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (target == null) {
            target = e.getName();
        } else if (target.equals(e.getName())) {
            double power = 3D;		// the power of the bullet
            double bearingFromGun;
            double turnAngle;
            double tmpbearing = r.getHeadingRadians() + e.getBearingRadians();

            // Lock target with radar
            r.setTurnRadarRightRadians(2.2 * Utils.normalRelativeAngle(tmpbearing - r.getRadarHeadingRadians()));

            // perform iterative circular targeting
            Point2D.Double point = new Point2D.Double();
            long deltahittime;
            double head, chead, speed, bspeed;
            double tmpx, tmpy;

            myNextPos.setLocation(r.getX() + Math.sin(r.getHeadingRadians()) * r.getVelocity(), r.getY() + Math.cos(r.getHeadingRadians()) * r.getVelocity());	// Next position (approx)

            tmpx = r.getX() + Math.sin(tmpbearing) * e.getDistance();
            tmpy = r.getY() + Math.cos(tmpbearing) * e.getDistance();
            head = e.getHeadingRadians();
            chead = head - oldHeading;
            oldHeading = head;
            speed = e.getVelocity();
            point.setLocation(tmpx, tmpy);
            deltahittime = -1;				// necessary to shoot to enemies NEXT position
            do {
                tmpx += Math.sin(head) * speed;
                tmpy += Math.cos(head) * speed;
                head += chead;
                deltahittime++;
                if ((!fireField.contains(tmpx, tmpy)) && (deltahittime > 0)) {
                    point.setLocation(warpPoint(tmpx, tmpy));
                    bspeed = (point.distance(myNextPos) - 18) / deltahittime;
                    if (bspeed < 19.7) {
                        power = (20 - bspeed) / 3.0;
                    } else {
                        power = 0.1;
                    }
                    break;
                }
                point.setLocation(tmpx, tmpy);
            } while ((int) Math.round((point.distance(myNextPos) - 18) / (20 - (3 * power))) > deltahittime);

            point.setLocation(warpPoint(tmpx, tmpy));
            tmpbearing = ((Math.PI / 2) - Math.atan2(point.y - myNextPos.getY(), point.x - myNextPos.getX()));
            bearingFromGun = Utils.normalRelativeAngle(tmpbearing - r.getGunHeadingRadians());
            r.setTurnGunRightRadians(bearingFromGun);
            if ((e.getDistance() <= 350) && (power > 0.0) && (r.getGunHeat() == 0.0) && (Math.abs(bearingFromGun) < 0.35)) {
                r.setFire(power);
            }
            turnAngle = Utils.normalRelativeAngle(tmpbearing - r.getHeadingRadians());
            r.setTurnRightRadians(Math.atan(Math.tan(turnAngle)));
            r.setAhead(Double.POSITIVE_INFINITY * Math.cos(turnAngle));
        }
    }

    public Point2D.Double warpPoint(double x, double y) {
        Point2D.Double pos = new Point2D.Double();

        pos.x = Math.min(fieldWidth - WALL_MARGIN, Math.max(2 * WALL_MARGIN, x));
        pos.y = Math.min(fieldHeight - WALL_MARGIN, Math.max(2 * WALL_MARGIN, y));
        return pos;
    }

    public void onHitByBullet(HitByBulletEvent e) {
        
    }

    public void onRobotDeath(RobotDeathEvent e) {
        
    }

    public void stop() {
        
    }
}
