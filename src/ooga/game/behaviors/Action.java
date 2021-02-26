package ooga.game.behaviors;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell, sam thompson
 * This abstraction is what happens after a behavior passes its condition checks. The action type determines how the
 * other entity(s) is determined, if any, and then executes the list of effects on each entity/other entity pair.
 * Dependencies: effect, entity internal
 */
public abstract class Action {

  final List<Effect> myEffects;

  /**
   * @param effects The effects that this action executes when it finds its targets.
   */
  public Action(List<Effect> effects){
    myEffects = effects;
  }

  /**
   * Executes all of the effects that this Action has responsibility for, on all of the valid targets
   * that this behavior finds.
   * @param elapsedTime The time elapsed since the last game step.
   * @param subject The Entity that owns this behavior and is affected by it.
   * @param variables The Map of Game variables, mapping variable names to String values.
   * @param collisionInfo Maps EntityInternals with the entities that they collided with
   *                     this frame and the String identifying the direction.
   * @param gameInternal The Game utility class that provides access to game level data
   *                     like the list of entities.
   */
  public void doAction(double elapsedTime, EntityInternal subject, Map<String, String> variables,
                       Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal) {
    List<EntityInternal> otherEntities = findOtherEntities(subject,variables,collisionInfo,gameInternal);
    for (EntityInternal e : otherEntities) {
      doEffects(elapsedTime,subject,e,variables,gameInternal);
    }
  }

  /**
   * Finds the possible target entities of the action besides the subject, which is known as the
   * "otherEntity" by Effects.
   * @param subject The EntityInternal that owns the behavior with this effect.
   * @param variables The Map of Game variables, mapping variable names to String values.
   * @param collisionInfo Maps EntityInternals with the entities that they collided with
   *                      this frame and the String identifying the direction.
   * @param gameInternal The Game utility class that provides access to game level data
   *                     like the list of entities.
   * @return A List of all possible otherEntities that the action could be targeting.
   */
  public abstract List<EntityInternal> findOtherEntities(EntityInternal subject, Map<String, String> variables,
                                                 Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal);

  /**
   * Executes all of the effects that this Action has responsibility for, on one target otherEntity.
   * @param elapsedTime The time elapsed since the last game step.
   * @param subject The Entity that owns this behavior and is affected by it.
   * @param otherEntity The Entity that is involved in the effect that is not the owner of the behavior.
   * @param variables The Map of Game variables, mapping variable names to String values.
   * @param gameInternal The Game utility class that provides access to game level data
   *                     like the list of entities.
   */
  protected void doEffects(double elapsedTime, EntityInternal subject, EntityInternal otherEntity, Map<String, String> variables, GameInternal gameInternal){
    for(Effect effect : myEffects){
      effect.doEffect(subject, otherEntity, elapsedTime, variables, gameInternal);
    }
  }
}
