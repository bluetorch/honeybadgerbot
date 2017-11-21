/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.bluetorch.robowiki.gun;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

/**
 *
 * @author bruceh
 */
public class HeadOnGun extends Gun {
    private long fireTime = 0;
    private AdvancedRobot r;
    
    public HeadOnGun(AdvancedRobot r) {
        super(r);
        this.r = r;
        r.out.println("HeadOn Gun");
    }
    
    @Override
    public void execute() {
    }
    
    @Override
    public void initRound() {
    }

    @Override
    public void update(ScannedRobotEvent e) {
        double bulletPower = Math.min(Math.min(1.9, r.getEnergy()/10.0), e.getEnergy());
        if (fireTime == r.getTime() && r.getGunTurnRemaining() == 0) {
            bulletFired(r.setFireBullet(bulletPower));
        }
        fireTime = r.getTime() + 1;
        double absoluteBearing = r.getHeadingRadians() + e.getBearingRadians();
        r.setTurnGunRightRadians(
                robocode.util.Utils.normalRelativeAngle(absoluteBearing
                - r.getGunHeadingRadians()));
    }
}
