/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.bluetorch.robowiki.gun;

import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.ScannedRobotEvent;

/**
 *
 * @author bruceh
 */
public abstract class Gun {
    private AdvancedRobot robot;
    private long bulletsFired = 0;
    private long bulletsHit = 0;
    private double powerFired = 0D;
    private double powerHit = 0D;
    

    public Gun(AdvancedRobot robot) {
        this.robot = robot;
    }
    
    public void bulletFired(Bullet b) {
        if (b != null) {
            bulletsFired++;
            powerFired += b.getPower();
        }
    }

    public void update(BulletHitEvent e) {
        bulletsHit++;
        powerHit += e.getBullet().getPower();
    }

    public void cleanUpRound() {
        getRobot().out.println("Bullets fired/hit: " + getBulletsFired() + "/" + getBulletsHit()
                + " (" + Math.round(getBulletsHit() * 1000D / getBulletsFired())/10D + "%)");
        getRobot().out.println("Power fired/hit: " + (int)getPowerFired() + "/" + (int)getPowerHit()
                + " (" + Math.round(getPowerHit() * 1000D / getPowerFired())/10D + "%)");
    }
    
    public abstract void execute();
    public abstract void update(ScannedRobotEvent e);
    public abstract void initRound();

    /**
     * @return the robot
     */
    public AdvancedRobot getRobot() {
        return robot;
    }

    /**
     * @return the bulletsFired
     */
    public long getBulletsFired() {
        return bulletsFired;
    }

    /**
     * @return the bulletsHit
     */
    public long getBulletsHit() {
        return bulletsHit;
    }

    /**
     * @return the powerFired
     */
    public double getPowerFired() {
        return powerFired;
    }

    /**
     * @return the powerHit
     */
    public double getPowerHit() {
        return powerHit;
    }
    
}
