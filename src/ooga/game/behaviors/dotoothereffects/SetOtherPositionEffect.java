package ooga.game.behaviors.dotoothereffects;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.noncollisioneffects.SetPositionEffect;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell
 * This is the same as SetPositionEffect but it executes it on other entity instead of subject.
 * We were thinking of having more effects like this, but instead we decided it would probably be sufficient to just
 * have variable determined actions automaticaly execute their effects on other entity. However we kept this here
 * in case we wanted to ever use it.
 */
@SuppressWarnings("unused")
public class SetOtherPositionEffect extends SetPositionEffect {
  /**
   * Construct the set position effect by setting desiredLocation. Note that it adds strings because it could depend on variables.
   *
   * @param args list of arguments from XMLGameDataReader
   */
  public SetOtherPositionEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    super.doTimeDelayedEffect(otherEntity, subject, elapsedTime, variables, game);
  }
}
