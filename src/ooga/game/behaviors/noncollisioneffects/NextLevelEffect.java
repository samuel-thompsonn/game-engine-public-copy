package ooga.game.behaviors.noncollisioneffects;

import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson
 * Tells the game to load the next level and set that as its active level.
 */
@SuppressWarnings("unused")
public class NextLevelEffect extends TimeDelayedEffect {

  public NextLevelEffect(List<String> args) {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    //has no arguments
  }

  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime,
                                     Map<String, String> variables, GameInternal game) {
    game.goToNextLevel();
  }
}
