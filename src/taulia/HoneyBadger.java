package taulia;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
import us.bluetorch.robowiki.gun.*;
import us.bluetorch.robowiki.radar.*;
import us.bluetorch.robowiki.personality.*;

import java.awt.*;

public class HoneyBadger extends AdvancedRobot {

  static Gun gun;
  static Radar radar;
  static Personality personality;

  @Override
  public void run() {
    setColors(Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE);
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    if (gun == null) {
      gun = new HeadOnGun(this);
    }
    gun.initRound();

    if (personality == null) {
      personality = new AntiGravity(this);
    }
    personality.run();

    if (radar == null) {
      radar = new Oldest(this);
    }
    radar.run();

  }


  @Override
  public void onStatus(StatusEvent e) {
    if (gun != null) {
      gun.execute();
    }
  }

  @Override
  public void onRobotDeath(RobotDeathEvent e) {
    if (getOthers() == 0) {
      gun.cleanUpRound();
    }
    radar.onRobotDeath(e);
    personality.onRobotDeath(e);
  }

  @Override
  public void onBulletHit(BulletHitEvent e) {
    gun.update(e);
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent e) {
    // Make sure you stay on a single target or this will be unpredictable.
    gun.update(e);

    personality.onScannedRobot(e);

    radar.onScannedRobot(e, getOthers());

  }

  @Override
  public void onDeath(DeathEvent e) {
    gun.cleanUpRound();
  }

  @Override
  public void onHitByBullet(HitByBulletEvent e) {
    personality.onHitByBullet(e);
  }

}
