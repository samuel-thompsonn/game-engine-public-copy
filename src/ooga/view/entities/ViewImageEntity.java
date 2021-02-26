package ooga.view.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ooga.Entity;
import ooga.data.entities.ImageEntity;
import ooga.view.entities.ViewEntity;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author caryshindell
 * This is the view's knowledge of an entity that holds an image. It does not extend ImageView because of technical
 * problems that creates with property bindings.
 * The position, size, and image properties are all bound to the back end entity's properties. Image is updated whenever
 * the file path changes.
 * Note: color effect is used to set dark mode. There is a y offset in order to create a deadzone for the pause and dark mode buttons.
 * Dependencies: ViewEntity, ImageEntity, entity
 */
public class ViewImageEntity implements ViewEntity {

  private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
  private final double Y_OFFSET = Double.parseDouble(myResources.getString("pauseButtonSize"));
  private final ImageView imageView = new ImageView();


  public ViewImageEntity(ImageEntity entity, ObjectProperty<Effect> colorEffect, List<DoubleProperty> cameraShift){
    bindImageProperty(entity.imageLocationProperty(), colorEffect);
    bindGenericProperties(entity, cameraShift);
    bindSizeProperties(entity);
  }

  /**
   * Binds the x and y position properties to be incremented by the camera shift
   * Multiplies the camera shift by the entity's stationary property, so entities can be marked to move/not move with camera
   * @param entity
   */
  public void bindGenericProperties(Entity entity, List<DoubleProperty> cameraShift) {
    imageView.layoutXProperty().bind(entity.xProperty().add(entity.nonStationaryProperty().multiply(cameraShift.get(0))));
    imageView.layoutYProperty().bind(entity.yProperty().add(new SimpleDoubleProperty(Y_OFFSET).add(entity.nonStationaryProperty().multiply(cameraShift.get(1)))));
    // add more properties here if needed
  }

  public Node getNode() {
    return imageView;
  }


  private void bindImageProperty(StringProperty location, ObjectProperty<Effect> colorEffect){
    imageView.setImage(new Image(location.getValue()));
    location.addListener((o, oldVal, newVal) -> {
      imageView.setImage(new Image(newVal));
      imageView.setEffect(colorEffect.getValue());
    });
    colorEffect.addListener((o, oldVal, newVal) -> imageView.setEffect(newVal));
  }

  private void bindSizeProperties(ImageEntity entity){
    imageView.fitHeightProperty().bind(entity.heightProperty());
    imageView.fitWidthProperty().bind(entity.widthProperty());
  }
}
