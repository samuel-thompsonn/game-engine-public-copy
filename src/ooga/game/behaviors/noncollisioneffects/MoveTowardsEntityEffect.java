package ooga.game.behaviors.noncollisioneffects;

import static ooga.game.behaviors.EffectUtil.getDotProduct;
import static ooga.game.behaviors.EffectUtil.getMagnitude;

import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson
 * Causes the subject to move toward otherEntity.
 * Requires that there be another entity specified besides the subject
 * when this is executed.
 */
public class MoveTowardsEntityEffect extends TimeDelayedEffect {

  public static final double LARGE_DOUBLE_VALUE = 100000000.0;
  public static final double DEFAULT_ACCELERATION = 0.0;
  String myAcceleration;
  String myMaxSpeed;

  public MoveTowardsEntityEffect(List<String> args) {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    myAcceleration = args.get(0);
    myMaxSpeed = args.get(1);
  }

  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime,
      Map<String, String> variables, GameInternal game) {
    double maxSpeed = parseData(myMaxSpeed,subject,variables, LARGE_DOUBLE_VALUE);
    double xDiff =  otherEntity.getPosition().get(0) - subject.getPosition().get(0);
    double yDiff = otherEntity.getPosition().get(1) - subject.getPosition().get(1);
    if (getDotProduct(List.of(xDiff,yDiff),subject.getVelocity()) > Math.pow(maxSpeed,2)) {
      return;
    }
    double magnitude = getMagnitude(List.of(xDiff,yDiff));
    double acceleration = parseData(myAcceleration,subject,variables, DEFAULT_ACCELERATION);
    subject.changeVelocity((xDiff / magnitude) * acceleration, (yDiff / magnitude) * acceleration);
  }
}
