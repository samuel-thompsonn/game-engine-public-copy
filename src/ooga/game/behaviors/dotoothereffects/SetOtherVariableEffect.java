package ooga.game.behaviors.dotoothereffects;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.noncollisioneffects.SetVariableEffect;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell
 * This is the same as SetVariableEffect but it executes it on other entity instead of subject.
 * We were thinking of having more effects like this, but instead we decided it would probably be sufficient to just
 * have variable determined actions automaticaly execute their effects on other entity. However we kept this here
 * in case we wanted to ever use it.
 */
@SuppressWarnings("unused")
public class SetOtherVariableEffect extends SetVariableEffect {
  public SetOtherVariableEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    super.doTimeDelayedEffect(otherEntity, subject, elapsedTime, variables, game);
  }
}
