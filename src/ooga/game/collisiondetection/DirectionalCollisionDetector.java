package ooga.game.collisiondetection;

import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.shape.Rectangle;
import ooga.Entity;

/**
 * @author caryshindell, sam thompson
 * This is an implementation of a collision detector that uses 4 directions, right left up down.
 * It uses the positions of the entity edges to see which direction the collision is likely in, so it will likely produce
 * some bugs when entities are moving very quickly (especially if both are moving), or frame rate is low.
 * Note that it ignores corner collisions which makes sense since a corner collision doesn't block vertically or horizontally.
 * Dependencies: collision detector, entity
 */
public class DirectionalCollisionDetector implements CollisionDetector {

  public static final String RESOURCE_PATH = "ooga/game/collisiondetection/resources/collisiontypes";
  public static final ResourceBundle directionCodes = ResourceBundle.getBundle(RESOURCE_PATH);

  public static final String RIGHT_CODE = directionCodes.getString("RightCode");
  public static final String LEFT_CODE = directionCodes.getString("LeftCode");
  public static final String DOWN_CODE = directionCodes.getString("DownCode");
  public static final String UP_CODE = directionCodes.getString("UpCode");

  /**
   * Figure out if the entities are colliding and return the direction that a is colliding with b (e.g. a,b -> right)
   * @param a entity a
   * @param b entity b
   * @param elapsedTime time passed in ms
   * @return direction of collision, or null if no collision
   */
  @SuppressWarnings("SuspiciousNameCombination")
  @Override
  public String getCollisionDirection(Entity a, Entity b, double elapsedTime) {
    Rectangle aShape = makeShapeFromEntity(a, a.getVelocity().get(0) * elapsedTime,a.getVelocity().get(1) * elapsedTime);
    Rectangle bShape = makeShapeFromEntity(b, b.getVelocity().get(0) * elapsedTime,b.getVelocity().get(1) * elapsedTime);
    if(aShape.getBoundsInParent().intersects(bShape.getBoundsInParent())) {
      // scenario 1.1: a is left of b and below b
      // scenario 1.2: a is left of b and above b
      // scenario 2.1: a is right of b and below b
      // scenario 2.2: a is right of b and above b
      Rectangle leftShape = aShape;
      Rectangle rightShape = bShape;
      if (aShape.getX() > bShape.getX()) {
        leftShape = bShape;
        rightShape = aShape;
      }
      Rectangle topShape = leftShape;
      Rectangle bottomShape = rightShape;
      if (leftShape.getY() > bottomShape.getY()) {
        topShape = rightShape;
        bottomShape = leftShape;
      }
      double leftShapeRightEdge = leftShape.getX() + leftShape.getWidth();
      double rightShapeLeftEdge = rightShape.getX();
      double topShapeBottomEdge = topShape.getY() + topShape.getHeight();
      double bottomShapeTopEdge = bottomShape.getY();
      // ignore corner collisions
      if(leftShapeRightEdge - rightShapeLeftEdge == bottomShapeTopEdge - topShapeBottomEdge) return null;
      boolean isHorizontal = leftShapeRightEdge - rightShapeLeftEdge < -(bottomShapeTopEdge - topShapeBottomEdge);
      if(isHorizontal){
        if(leftShape == aShape){
          return RIGHT_CODE;
        }
        return LEFT_CODE;
      }
      if(topShape == aShape){
        return DOWN_CODE;
      }
      return UP_CODE;
    }
    return null;
  }

  @Override
  public Collection<String> getSupportedDirections() {
    return List.of(RIGHT_CODE,LEFT_CODE,DOWN_CODE,UP_CODE);
  }

  @Override
  public boolean entityAtPoint(Entity e, double xPos, double yPos) {
    Rectangle eShape = makeShapeFromEntity(e,0.0,0.0);
    Rectangle point = new Rectangle(xPos,yPos,1.0,1.0);
    return eShape.intersects(point.getBoundsInParent());
  }

  private Rectangle makeShapeFromEntity(Entity e, double xChange, double yChange) {
    double xPos = e.getPosition().get(0) + xChange;
    double yPos = e.getPosition().get(1) + yChange;
    return new Rectangle(xPos, yPos, e.getWidth(), e.getHeight());
  }
}
