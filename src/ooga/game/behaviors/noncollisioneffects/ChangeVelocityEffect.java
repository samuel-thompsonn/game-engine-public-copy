package ooga.game.behaviors.noncollisioneffects;

import static ooga.game.behaviors.EffectUtil.getDotProduct;
import static ooga.game.behaviors.EffectUtil.getMagnitude;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Effect;
import ooga.game.behaviors.ExpressionEvaluator;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson, caryshindell
 * Changes the velocity of the subject by a specified amount.
 */
public class ChangeVelocityEffect extends TimeDelayedEffect {

  public static final double DEFAULT_ACCELERATION = 0.0;
  private String xAccelerationPerFrameData;
  private String yAccelerationPerFrameData;
  private String operatorData;
  private String myMaxSpeedData;
  private static final double MAX_SPEED_DEFAULT = 1000.0;

  public ChangeVelocityEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    int index = 0;
    xAccelerationPerFrameData = args.get(index++);
    yAccelerationPerFrameData = args.get(index++);
    operatorData = args.get(index++);
    myMaxSpeedData = args.get(index);
  }

  /**
   * Performs the effect
   * @param subject     The entity that owns this. This is the entity that should be modified.
   * @param otherEntity
   * @param elapsedTime
   * @param variables
   * @param game
   */
  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    double myMaxSpeed = parseData(myMaxSpeedData, subject, variables, MAX_SPEED_DEFAULT);
    double xAccel = parseData(xAccelerationPerFrameData,subject,variables, DEFAULT_ACCELERATION);
    double yAccel = parseData(yAccelerationPerFrameData,subject,variables,DEFAULT_ACCELERATION);
    String operator = Effect.doVariableSubstitutions(operatorData, subject, variables);
    double accelMagnitude = getMagnitude(List.of(xAccel,yAccel));
    List<Double> accelVectorNormalized = List.of(xAccel / accelMagnitude, yAccel / accelMagnitude);
    if (getDotProduct(subject.getVelocity(),accelVectorNormalized) < myMaxSpeed) {
      String formattedXVelocity = BigDecimal.valueOf(subject.getVelocity().get(0)).toPlainString();
      String formattedYVelocity = BigDecimal.valueOf(subject.getVelocity().get(1)).toPlainString();
      double newX = ExpressionEvaluator.eval(formattedXVelocity+ operator + parseData(xAccelerationPerFrameData, subject, variables, 0.0));
      double newY = ExpressionEvaluator.eval(formattedYVelocity+ operator + parseData(yAccelerationPerFrameData, subject, variables, 0.0));
      subject.setVelocity(newX, newY);
    }
  }
}
