package ooga.game.behaviors.noncollisioneffects;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell
 * This shifts the camera by a specified x and y (which can be varaibles).
 * Assumptions/Notes: this should be coupled with the ability to make entities stationary/nonstationary.
 * Dependencies: time delayed effect, entity internal, game internal
 */
@SuppressWarnings("unused")
public class ShiftCameraEffect extends TimeDelayedEffect {

  private String cameraShiftXData;
  private String cameraShiftYData;

  public ShiftCameraEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  /**
   * Processes the String arguments given in the data file into values used by this effect.
   *
   * @param args The String arguments given for this effect in the data file.
   */
  @Override
  public void processArgs(List<String> args) {
    cameraShiftXData = args.get(0);
    cameraShiftYData = args.get(1);
  }

  /**
   * Performs the effect
   *  @param subject     The entity that owns this. This is the entity that should be modified.
   * @param otherEntity entity we are "interacting with" in this effect
   * @param elapsedTime time between steps in ms
   * @param variables   game variables
   * @param game        game instance
   */
  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    double xShift = game.getCameraShiftValues().get(0)+parseData(cameraShiftXData, subject, variables, 0.0);
    double yShift = game.getCameraShiftValues().get(1)+parseData(cameraShiftYData, subject, variables, 0.0);
    game.setCameraShiftValues(xShift, yShift);
  }
}
