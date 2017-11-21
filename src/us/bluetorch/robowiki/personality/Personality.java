/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.bluetorch.robowiki.personality;

import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

/**
 *
 * @author bruceholt
 */
public interface Personality {

    public void run();

    public void onScannedRobot(ScannedRobotEvent e);

    public void onHitByBullet(HitByBulletEvent e);

    public void onRobotDeath(RobotDeathEvent e);
    
    public void stop();
}
