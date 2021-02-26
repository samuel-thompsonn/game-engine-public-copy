package ooga.game.collisiondetection;

import java.util.Collection;
import ooga.Entity;

/**
 * @author sam thompson, caryshindell
 * Detects collisions between entities. Can likely be static.
 * May use JavaFX algorithms, so should be encapsulated from the back end.
 */
public interface CollisionDetector {

  /**
   * Figure out if the entities are colliding and return the direction that a is colliding with b (e.g. a,b -> right)
   * @param a entity a
   * @param b entity b
   * @param elapsedTime time passed in ms
   * @return direction of collision, or null if no collision
   */
  String getCollisionDirection(Entity a, Entity b, double elapsedTime);

  /**
   * @return The String identifiers of directions that this collision detector can detect.
   */
  Collection<String> getSupportedDirections();

  /**
   * Determines whether an entity is intersecting with a specific point onscreen
   * @param e The Entity to check for intersection with the point.
   * @param xPos The x position of the point to check.
   * @param yPos The y position of the point to check.
   * @return True if the entity is intersecting the point
   */
  boolean entityAtPoint(Entity e, double xPos, double yPos);
}
