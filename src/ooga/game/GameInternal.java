package ooga.game;

import java.util.List;

import java.util.Map;

/**
 * @author sam thompson, doherty guirand, caryshindell
 */
public interface GameInternal {

  /**
   * Creates an entity at the specified position of the specified type,
   * if there is an entity defined with that name.
   * The Entity is not active in-game until next frame.
   * @param type The String identifying the type of entity to create.
   * @param position The position that the new entity instance will have.
   */
  void createEntity(String type, List<Double> position);

  /**
   * @see GameInternal#createEntity(String, List, double, double)
   * @param width The width of the instance created.
   * @param height The height of the instance created.
   */
  void createEntity(String type, List<Double> position, double width, double height);

  /**
   * @param id The ID of the entity to search for.
   * @return An arbitrary EntityInternal that has the specified ID,
   * or null if there are none.
   */
  EntityInternal getEntityWithId(String id);

  /**
   * @param name The Name of entities to search for.
   * @return Every in-game EntityInternal with the specified name.
   */
  List<EntityInternal> getEntitiesWithName(String name);

  /**
   * Sets the current level to be the level in the game with the specified ID,
   * if there is one with that ID.
   * @param levelID The String ID of the level to go to.
   */
  void goToLevel(String levelID);

  /**
   * Sets the current level to be the active level's listed next level.
   * Does nothing if the current level has no next level set.
   */
  void goToNextLevel();

  /**
   * Reloads the current level and removes all current level state.
   * Does nothing if it can't find the level to load (i.e., if the level was deleted/moved)
   */
  void restartLevel();

  /**
   * @param xShift The amount to shift the camera's X coordinate in the next step.
   * @param yShift The amount to shift the camera's Y coordinate in the next step.
   */
  void setCameraShiftValues(double xShift, double yShift);

  /**
   * @return The List of Doubles representing the amount by which the
   * camera will be shifted next game step.
   */
  List<Double> getCameraShiftValues();

  /**
   * @return A copy of the map of game variables for viewing.
   */
  Map<String,String> getVariables();

  /**
   * Sets the provided variable to the desired value. Creates the variable if it doesn't exist.
   * @param var The name of the variable to set.
   * @param value The String to set the variable equal to.
   */
  void setVariable(String var, String value);

  /**
   * @return A list of the entities that are currently in play.
   */
  List<EntityInternal> getInternalEntities();
}
