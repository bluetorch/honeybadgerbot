/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.bluetorch.robowiki.gun;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author bruceh
 */
public class RandomGun extends Gun {

    private long fireTime = 0;
    private AdvancedRobot r;

    public RandomGun(AdvancedRobot r) {
        super(r);
        this.r = r;
        r.out.println("Random Gun");
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
        if (fireTime == r.getTime() && r.getGunTurnRemaining() == 0) {
            bulletFired(r.setFireBullet(bulletPower));
        }
        fireTime = r.getTime() + 1;
        double targetAngle = r.getHeadingRadians() + e.getBearingRadians();

        double escapeAngle = Math.asin(8 / Rules.getBulletSpeed(bulletPower));
        double randomAimOffset = -escapeAngle + Math.random() * 2 * escapeAngle;

        double headOnTargeting = targetAngle - r.getGunHeadingRadians();
        r.setTurnGunRightRadians(Utils.normalRelativeAngle(headOnTargeting + randomAimOffset));
    }
}
