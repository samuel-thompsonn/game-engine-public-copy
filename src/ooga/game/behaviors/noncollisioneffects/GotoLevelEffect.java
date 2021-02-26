package ooga.game.behaviors.noncollisioneffects;

import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson
 * Causes the game to go to the specified level and set that as its
 * active level.
 */
@SuppressWarnings("unused")
public class GotoLevelEffect extends TimeDelayedEffect {

  private String myLevelId;

  public GotoLevelEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    myLevelId = args.get(0);
  }

  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime,
                                     Map<String, String> variables, GameInternal game) {
    game.goToLevel(myLevelId);
  }
}
