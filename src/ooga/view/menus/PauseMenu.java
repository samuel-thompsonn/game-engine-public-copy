package ooga.view.menus;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

/**
 * @author caryshindell, doherty guirand
 * This class represents the menu that appears in the UI when the user clicks the pause button.
 * It uses property bindings to communicate back to ViewerGame what buttons were pressed.
 * Dependencies: menu package
 */
public class PauseMenu extends OptionMenu {
  private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
  private final String ICON_STYLE = myResources.getString("iconcss");
  private static final String PAUSE_MENU_TITLE = "GamePaused";
  private static final double ICON_SIZE = 50;

  private final BooleanProperty resumed = new SimpleBooleanProperty(true);
  private final BooleanProperty quit = new SimpleBooleanProperty(false);
  private final BooleanProperty save = new SimpleBooleanProperty(false);
  private final Map<BooleanProperty, String> buttonPropertiesAndNames;


  /**
   * Menu that appears when game has been paused
   */
  public PauseMenu(ResourceBundle languageresources){
    super(languageresources,languageresources.getString(PAUSE_MENU_TITLE));
    buttonPropertiesAndNames = new HashMap<>(){{
      put(resumed, languageresources.getString("Play"));
      put(quit, languageresources.getString("Quit"));
      put(save, languageresources.getString("Save"));
    }};
    this.setLeft(setMenuItems(createButtons()));
  }

  /**
   * used by ViewerGame to tell if user has selected to resume game
   * @return true if resumed false if not
   */
  public BooleanProperty resumedProperty() {
    return resumed;
  }

  /**
   * Sets resumed Property
   * @param resumed - a boolean that states if game is in play
   */
  public void setResumed(boolean resumed) {
    this.resumed.set(resumed);
  }

  /**
   * Used by ViewerGame to tell if game as been exited by user
   * @return true if exited, false if not
   */
  public BooleanProperty quitProperty() {
    return quit;
  }

  /**
   * Used by ViewGame to tell is game as been saved by user
   * @return true if saved
   */
  public BooleanProperty saveProperty() {
    return save;
  }


  private Node makeButton(BooleanProperty statusProperty, String text){
    Button button = new Button(text);
    ImageView icon = new ImageView(new Image(myResources.getString(languageResources.getString(text))));
    icon.setStyle(ICON_STYLE);
    icon.setFitHeight(ICON_SIZE);
    icon.setFitWidth(ICON_SIZE);
    button.setGraphic(icon);
    button.setOnAction(e -> statusProperty.setValue(!statusProperty.getValue()));
    return button;
  }


  private List<Node> createButtons() {
    List<Node> list = new ArrayList<>();
    for(Map.Entry<BooleanProperty, String> buttonPropertyAndName : buttonPropertiesAndNames.entrySet()){
      list.add(makeButton(buttonPropertyAndName.getKey(), buttonPropertyAndName.getValue()));
    }
    return list;
  }
}
