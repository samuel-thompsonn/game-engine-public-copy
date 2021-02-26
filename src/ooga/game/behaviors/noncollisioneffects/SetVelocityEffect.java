package ooga.game.behaviors.noncollisioneffects;

import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson, caryshindell
 * Sets the velocity of the subject entity to the specified value.
 */
@SuppressWarnings("unused")
public class SetVelocityEffect extends TimeDelayedEffect {

  private String velocityXData;
  private String velocityYData;

  /**
   * @param args The String arguments of this effect.
   */
  public SetVelocityEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    velocityXData = args.get(0);
    velocityYData = args.get(1);
  }



  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    subject.setVelocity(parseData(velocityXData, subject, variables, 0.0),
            parseData(velocityYData, subject, variables, 0.0));
  }
}
