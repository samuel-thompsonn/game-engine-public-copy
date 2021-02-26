package ooga.game;

import java.util.List;
import java.util.Map;
import ooga.Entity;
import ooga.game.behaviors.ConditionalBehavior;

/**
 * @author sam thompson
 * The side of an entity that behaviors can see. Provides tools to modify the entity,
 * destroy it, and move it around. Separates the outward-facing Entity interface from the
 * inward-facing utilities.
 */
public interface EntityInternal extends Entity {

  /**
   * @param newPosition The new position for the entity to have in the level.
   */
  void setPosition(List<Double> newPosition);

  /**
   * Marks this entity for removal by the next frame, and prevents it from taking further actions.
   */
  void destroySelf();

  /**
   * Changes the velocity of the entity by a specified value.
   * Movement is generally not done until after all velocity changes in a frame.
   * @param xChange The x-value of the change in velocity (additive).
   * @param yChange The y-value of the change in velocity (additive).
   */
  void changeVelocity(double xChange, double yChange);

  /**
   * Sets the velocity of the entity to the specified value.
   * @param xVelocity The x-value of the new velocity.
   * @param yVelocity The y-value of the new velocity.
   */
  void setVelocity(double xVelocity, double yVelocity);

  /**
   * Handles updates that happen every frame regardless of context. Can still have logic.
   * Example: An enemy might move right every frame.
   * @param elapsedTime The time elapsed since the last frame. Generally is in milliseconds, but
   *                    this depends on implementation.
   */
  void updateSelf(double elapsedTime);

  /**
   * Actually moves the entity in space by its velocity. Should happen after all movement and
   * collision logic in a given frame.
   * @param elapsedTime The time elapsed since the last frame. Generally is in milliseconds, but
   *                    this depends on implementation.
   */
  void executeMovement(double elapsedTime);


  /**
   * Sets the variable with the specified name to the specified value.
   * @param variableName The name of the variable to modify or add.
   * @param value The value to set the variable to.
   */
  void addVariable(String variableName, String value);

  /**
   * @param variableName The name of the variable to look up.
   * @return The value stored by this entity in a variable with the given name. Returns null
   * if nothing is found.
   */
  String getVariable(String variableName);

  /**
   * Handles any behavior that depends on the values of game variables.
   * @param variables The Map of game variables, where the key set is the variable names and
   *                  the values are variable values.
   */
  void reactToVariables(Map<String, String> variables);

  /**
   * Add a dependency to the map so that when the variable with the given name changes, the property with the given name is updated
   * @param propertyVariableDependencies A Map where the key set contains names of game variables to keep track of and
   *                                     the value set contains names of properties to update in the entity.
   */
  void setPropertyVariableDependencies(Map<String, String> propertyVariableDependencies);

  /**
   * Executes each of this entity's conditional behaviors, which will check the conditions and
   * execute the assigned Effects if the conditions are met.
   * @param elapsedTime The time elapsed since the last frame. Generally is in milliseconds, but
   *                    this depends on implementation.
   * @param inputs The Map mapping standardized names of inputs (like UpKey) to standardized identifiers
   *               of their status (such as Pressed or Active). If a key is not active, it isn't
   *               in the map.
   * @param variables The game variables, as a modifiable map.
   * @param collisionInfo A Map mapping the name of each entity to the list of entities that it
   *                      is colliding with.
   * @param gameInternal The internal interface of the Game, for game-level utilities/operations.
   */
  void doConditionalBehaviors(double elapsedTime, Map<String, String> inputs, Map<String, String> variables,
      Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal);

  /**
   * Assigns the conditional behaviors of this entity.
   * @param conditionalBehaviors The list of conditional behaviors this entity should execute each frame.
   */
  void setConditionalBehaviors(List<ConditionalBehavior> conditionalBehaviors);

  /**
   * Changes every value in this entity's blockedMovements map to the specified value
   * @param isBlocked true if the entity is blocked in all directions, false if the entity should
   *                  be unblocked in all directions.
   */
  void blockInAllDirections(boolean isBlocked);

  /**
   * Sets the entity's variables to match the specified map
   * @param variables A map of variable names to values
   */
  void setVariables(Map<String, String> variables);


  /**
   * @param width The value to set the entity's width to.
   */
  void setWidth(double width);

  /**
   * @param height The value to set the entity's height to.
   */
  void setHeight(double height);

  /**
   * @param filepath The filepath of the image for this entity to use from now on.
   */
  void setImageLocation(String filepath);

  /**
   * Changes the value in this entity's blockedMovements map to the specified value
   * @param direction up, down, left, or right, as a standardized String.
   * @param blocked true if the entity is blocked in the direction, otherwise false
   */
  void blockInDirection(String direction, boolean blocked);
}
