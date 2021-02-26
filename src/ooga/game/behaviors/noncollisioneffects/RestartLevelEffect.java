package ooga.game.behaviors.noncollisioneffects;

import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson
 * Restarts the game's current level by reloading it.
 */
@SuppressWarnings("unused")
public class RestartLevelEffect extends TimeDelayedEffect {

  public RestartLevelEffect(List<String> args) {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    //has no arguments.
  }

  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime,
      Map<String, String> variables, GameInternal game) {
    game.restartLevel();
  }
}
