package ooga.view.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ooga.Entity;
import ooga.data.entities.TextEntity;
import ooga.view.entities.ViewEntity;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author caryshindell
 * This is the view's knowledge of an entity that holds text. It does not extend Text because of technical
 * problems that creates with property bindings.
 * The position, size, and text properties are all bound to the back end entity's properties. Image is updated whenever
 * the file path changes.
 * The font is dynamically resized so the text fits the desired boundaries.
 * Note: color effect is used to set dark mode. There is a y offset in order to create a deadzone for the pause and dark mode buttons.
 * Dependencies: ViewEntity, TextEntity, entity
 */
public class ViewTextEntity implements ViewEntity {

  private final Text text = new Text();
  private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
  private final double Y_OFFSET = Double.parseDouble(myResources.getString("pauseButtonSize"));
  private static final double SIGNIFICANT_DIFFERENCE = 5.0;
  private static final double DEFAULT_FONT_SIZE = 12.0;
  private static final double FONT_SIZE_INCREMENT = 0.2;

  public ViewTextEntity(TextEntity entity, List<DoubleProperty> cameraShift) {
    bindTextProperty(entity.textProperty());
    bindWidthProperty(entity.widthProperty());
    bindFontProperties(entity);
    bindGenericProperties(entity, cameraShift);
  }

  public void bindGenericProperties(Entity entity, List<DoubleProperty> cameraShift) {
    text.setTextOrigin(VPos.TOP);
    text.layoutXProperty().bind(entity.xProperty().add(entity.nonStationaryProperty().multiply(cameraShift.get(0))));
    text.layoutYProperty().bind(entity.yProperty().add(new SimpleDoubleProperty(Y_OFFSET)).add(entity.nonStationaryProperty().multiply(cameraShift.get(1))));
    // add more properties here if needed
  }

  public Node getNode() {
    return text;
  }

  private void bindTextProperty(StringProperty string){
    text.textProperty().bind(string);
  }

  private void bindWidthProperty(DoubleProperty width){ text.wrappingWidthProperty().bind(width); }

  private void bindFontProperties(TextEntity entity){
    entity.fontNameProperty().addListener((o, oldVal, newVal) -> resizeFont(entity));
    entity.textProperty().addListener((o, oldVal, newVal) -> resizeFont(entity));
    entity.heightProperty().addListener((o, oldVal, newVal) -> resizeFont(entity));
    entity.widthProperty().addListener((o, oldVal, newVal) -> resizeFont(entity));
    resizeFont(entity);
  }

  /**
   * adjusts the font size to try to fit the text entity within the desired height
   * @param entity the text entity
   */
  private void resizeFont(TextEntity entity){
    Font font = new Font(entity.getFontName(), DEFAULT_FONT_SIZE);
    text.setFont(font);
    double adjustedFontSize = DEFAULT_FONT_SIZE;
    double difference = text.getLayoutBounds().getHeight() - entity.getHeight();
    double previousDifference;
    while(Math.abs(difference) > SIGNIFICANT_DIFFERENCE){
      if(difference > 0) adjustedFontSize -= FONT_SIZE_INCREMENT;
      else adjustedFontSize += FONT_SIZE_INCREMENT;
      font = new Font(entity.getFontName(), adjustedFontSize);
      text.setFont(font);
      previousDifference = difference;
      difference = text.getLayoutBounds().getHeight() - entity.getHeight();
      if(difference < 0 && previousDifference > 0) break;
    }
  }


}
