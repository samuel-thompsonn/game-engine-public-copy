package ooga.data.entities;

import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ooga.Entity;

/**
 * @author caryshindell
 * An entity implementation that stores an image
 * Dependencies: entity, OogaEntity
 * Notes: only contains the image file path. ViewImageEntity will have the javafx ImageView
 * Example: mario is an image entity with a string containing his image location
 */
public class ImageEntity extends OogaEntity {

  private StringProperty imageLocation = null;
  private static final String type = Entity.imageEntityType;

  public ImageEntity(String name, String imageFilePath, double xPos, double yPos, double width, double height) {
    super(xPos, yPos, width, height);
    if (imageFilePath != null) {
      imageLocation = new SimpleStringProperty(imageFilePath);
    }
    myName = name;
    propertyUpdaters.put("imageLocation", this::setImageLocation);
  }

  @Override
  public void setImageLocation(String filePath){imageLocation.set(filePath);}

  public StringProperty imageLocationProperty() {
    return imageLocation;
  }

  /**
   * is it an image entity or text entity
   *
   * @return string "image" or "text" or something else if new entity classes are created
   */
  @Override
  public String getEntityType() {
    return type;
  }

  @Override
  public void reactToVariables(Map<String, String> variables) {
    super.reactToVariables(variables);
    myVariables.put("Image", this.imageLocation.get());
  }
}
