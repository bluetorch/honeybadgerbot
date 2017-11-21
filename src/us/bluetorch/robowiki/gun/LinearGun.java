/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.bluetorch.robowiki.gun;

import java.awt.geom.Point2D;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author bruceh
 */
public class LinearGun extends Gun {

    private AdvancedRobot r;

    public LinearGun(AdvancedRobot r) {
        super(r);
        this.r = r;
        r.out.println("Linear Gun");

    }

    @Override
    public void initRound() {
    }

    @Override
    public void execute() {
    }

    @Override
    public void update(ScannedRobotEvent e) {
        double bulletPower = Math.min(Math.min(1.9, r.getEnergy() / 10.0), e.getEnergy());
        double myX = r.getX();
        double myY = r.getY();
        double absoluteBearing = r.getHeadingRadians() + e.getBearingRadians();
        double enemyX = r.getX() + e.getDistance() * Math.sin(absoluteBearing);
        double enemyY = r.getY() + e.getDistance() * Math.cos(absoluteBearing);
        double enemyHeading = e.getHeadingRadians();
        double enemyVelocity = e.getVelocity();


        double deltaTime = 0;
        double battleFieldHeight = r.getBattleFieldHeight(),
                battleFieldWidth = r.getBattleFieldWidth();
        double predictedX = enemyX, predictedY = enemyY;
        while ((++deltaTime) * (20.0 - 3.0 * bulletPower)
                < Point2D.Double.distance(myX, myY, predictedX, predictedY)) {
            predictedX += Math.sin(enemyHeading) * enemyVelocity;
            predictedY += Math.cos(enemyHeading) * enemyVelocity;
            if (predictedX < 18.0
                    || predictedY < 18.0
                    || predictedX > battleFieldWidth - 18.0
                    || predictedY > battleFieldHeight - 18.0) {
                predictedX = Math.min(Math.max(18.0, predictedX),
                        battleFieldWidth - 18.0);
                predictedY = Math.min(Math.max(18.0, predictedY),
                        battleFieldHeight - 18.0);
                break;
            }
        }
        double theta = Utils.normalAbsoluteAngle(Math.atan2(
                predictedX - r.getX(), predictedY - r.getY()));
        r.setTurnGunRightRadians(Utils.normalRelativeAngle(theta - r.getGunHeadingRadians()));
        bulletFired(r.fireBullet(bulletPower));
    }
}
