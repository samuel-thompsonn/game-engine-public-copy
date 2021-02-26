package ooga.view.entities;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import ooga.Entity;

import java.util.List;

/**
 * @author caryshindell
 * This abstraction exists to encapsulate what a view knows about entities and what it needs to update them (although
 * updates are mainly handled through property bindings).
 * It is not an abstract class because it is really just meant to represent the methods view entities have in common,
 * there is not really any shared state.
 * Dependencies: entity
 */
public interface ViewEntity {

  /**
   * Binds the x and y position properties to be incremented by the camera shift
   * @param entity the entity
   */
  void bindGenericProperties(Entity entity, List<DoubleProperty> cameraShift);

  Node getNode();
}
