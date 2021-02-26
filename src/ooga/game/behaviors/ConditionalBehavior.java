package ooga.game.behaviors;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell
 * every behavior executes even if it does nothing
 *   So we want a new type of behavior, conditional behavior, which has a map of conditionals for each thing it is possible
 *     to depend on (inputs, collisions, variables). It also has a list of behaviors it will execute if the conditionals are true.
 *   Example: Fireboy can only go through door, ending the level, if he is colliding with door and touching ground,
 *     variable red diamonds collected is 5 (or a dynamic value),
 *     and user is pressing up key
 *   Method call for this example would look like: new ConditionalBehavior(inputs=(up:true), collisions=(door:true, ground:true),
 *    variables=(red diamonds: 5), behaviors=(EndLevel))
 *  Dependencies: entity internal, game internal
 */
public interface ConditionalBehavior {

  /**
   * So we want a new type of behavior, conditional behavior, which has a map of conditionals for each thing it is possible
   * to depend on (inputs, collisions, variables). It also has a list of behaviors it will execute if the conditionals are true.
   * Example: Fireboy can only go through door, ending the level, if he is colliding with door and touching ground,
   * variable red diamonds collected is 5 (or perhaps a dynamic value?),
   * and user is pressing up key
   * Method call for this example would look like: new ConditionalBehavior(inputs=(up:true), collisions=(door:true, ground:true),
   * variables=(red diamonds: 5), behaviors=(EndLevel))
   * @param elapsedTime time passed in ms
   * @param subject     entity that owns this behavior
   * @param variables   map of game/level variables
   * @param inputs      all registered key inputs at this frame
   * @param collisionInfo Map of maps, direction name : map of collisions for that direction. map of collisions is entity : list of entities
   * @param gameInternal what game this is run from
   */
  void doConditionalUpdate(double elapsedTime, EntityInternal subject, Map<String, String> variables, Map<String, String> inputs,
                           Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal);


  /**
   * executes the actions owned by this behavior
   * @param elapsedTime time in ms
   * @param subject entity that owns this conditional behavior
   * @param variables map of variables in the game/level
   * @param collisionInfo current collision info
   * @param gameInternal what game this is run from
   */
  void doActions(double elapsedTime, EntityInternal subject, Map<String, String> variables,
                 Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal);
}
