package ooga.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.Entity;
import ooga.OogaDataException;
import ooga.UserInputListener;
import ooga.data.entities.ImageEntity;
import ooga.data.entities.TextEntity;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.game.OogaGame;
import ooga.game.controls.KeyboardControls;
import ooga.view.entities.ViewEntity;
import ooga.view.entities.ViewImageEntity;
import ooga.view.entities.ViewTextEntity;
import ooga.view.menus.PauseMenu;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ooga.game.collisiondetection.DirectionalCollisionDetector;

/**
 * @author caryshindell, doherty guirand
 * This class represents the viewer's interpretation of a single game instance. It creates the game in back end and handles
 * the basic UI interactions, including displaying and updating entities, and animation.
 * Note: this is the class that decides on/specifies file format (e.g. xml), collision detector type, and keyboard controls.
 *  Currently we only have on implementation for each of those but if others were added this is where that decision is made.
 *  (Some are also made in Visualizer but they're passed through here...)
 * Dependencies: OogaGame, entity, menus, DirectionalCollisionDetector, data readers, effect
 * Example: mario is selected from start menu in Visualizer. This creates a new ViewerGame for that mario game. Another mario
 * game could still be separately launched.
 */
public class ViewerGame {

  private static final double MILLISECOND_DELAY = 33.33;
  public static final int NORMAL_BUTTON_XPOS = 300;
  public static final int ALIEN_BUTTON_XPOS = 100;
  public static final String NORMAL_MODE = "NormalMode";
  public static final String DARK_MODE = "DarkMode";
  public static final String SET_DARK_MODE = "setDarkMode";
  public static final String SET_NORMAL_MODE = "setNormalMode";
  public static final int START_X = 0;
  public static final String SAVE_DATE_DEFAULT_EMPTY = "";
  private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
  private final String PAUSE_BUTTON_LOCATION = myResources.getString("pauseButtonLocation");
  private final String ALIEN_BUTTON_LOCATION = myResources.getString("alienButtonLocation");
  private final String NORMAL_BUTTON_LOCATION = myResources.getString("normalButtonLocation");
  private final double PAUSE_BUTTON_SIZE = Double.parseDouble(myResources.getString("pauseButtonSize"));
  private final double PAUSE_BUTTON_IMAGE_SIZE = PAUSE_BUTTON_SIZE - 10;
  private final double WINDOW_WIDTH = Double.parseDouble(myResources.getString("windowWidth"));
  private final double WINDOW_HEIGHT = Double.parseDouble(myResources.getString("windowHeight"));
  private final Group myEntityGroup = new Group();
  private Group myRoot;
  private final String myGameName;
  private Scene myGameScene;
  private Stage myGameStage;
  private PauseMenu myPauseMenu;
  private OogaGame myGame;
  private Timeline myAnimation;
  private final ObjectProperty<Effect> colorEffectProperty = new SimpleObjectProperty<>();
  private Scene pauseScene;
  private final String myProfileName;
  private final List<DoubleProperty> cameraShift = new ArrayList<>();
  private Exception currentError = null;
  private final ResourceBundle languageResources;



  /**
   * This class handles the animation and viewing of each game, sets on input listeners
   * @param gameName - name of game to be played
   * @param profileName - name of profile user is on
   * @param saveDate - save date of game being played, an empty string if playing a new game
   * @throws OogaDataException if there is an error in reading game file
   */
  public ViewerGame(String gameName, String profileName, String saveDate, ResourceBundle languageresources, String keyBoardFilePath) throws OogaDataException {
    languageResources = languageresources;
    myGameName = gameName;
    myProfileName = profileName;
    setGame(saveDate, keyBoardFilePath);
    setUpGameStage();
    setCameraProperties();
    setUpGameEntities();
    myRoot.getChildren().addAll(setUpPauseButton(), setUpDarkModeButton(), setUpNormalModeButton());
    colorEffectProperty.set(new ColorAdjust());
    setUpInputListeners(myGame);
  }

  private void setCameraProperties(){
    cameraShift.add(new SimpleDoubleProperty());
    cameraShift.add(new SimpleDoubleProperty());
    myGame.setCameraShiftProperties(cameraShift);
  }

  private void setGame(String saveDate, String keyInputFilePath) throws OogaDataException {
    if (saveDate == null) {
      saveDate = SAVE_DATE_DEFAULT_EMPTY;
    }
    myGame = new OogaGame(myGameName, new XMLGameDataReader() {}, new DirectionalCollisionDetector(), new KeyboardControls(
            keyInputFilePath), myProfileName,new XMLGameRecorder(), saveDate);
  }

  private void setUpGameEntities(){
    ObservableList<Entity> gameEntities = myGame.getEntities();
    // add listener here to handle entities being created/removed
    // this listener should set the "active" property of the entity,
    //    which will trigger a listener that removes it from the group
    gameEntities.addListener((ListChangeListener<Entity>) c -> {
      while (c.next()) {
        if(c.wasAdded() || c.wasRemoved()){
          for (Entity removedItem : c.getRemoved()) {
            removedItem.setActiveInView(false);
          }
          for (Entity addedItem : c.getAddedSubList()) {
            addedItem.setActiveInView(true);
            addToEntityGroup(addedItem);
          }
        }
      }
    });
    for(Entity entity : gameEntities){
      addToEntityGroup(entity);
    }
  }


  private void addToEntityGroup(Entity entity) {
    Node viewEntity = makeViewEntity(entity);
    myEntityGroup.getChildren().add(viewEntity);
    entity.activeInViewProperty().addListener((o, oldVal, newVal) -> {
      if(newVal) myEntityGroup.getChildren().add(viewEntity);
      else myEntityGroup.getChildren().remove(viewEntity);
    });
  }

  private Node makeViewEntity(Entity entity){
    ViewEntity viewEntity;
    if(entity.getEntityType().equals(Entity.imageEntityType)){
      viewEntity = new ViewImageEntity((ImageEntity)entity, colorEffectProperty,cameraShift);
    }
    else if(entity.getEntityType().equals(Entity.textEntityType)){
      viewEntity = new ViewTextEntity((TextEntity)entity,cameraShift);
    } else return null;
    return viewEntity.getNode();
  }

  private Node setUpPauseButton() {
    myPauseMenu = new PauseMenu(languageResources);
    pauseScene = new Scene(myPauseMenu, myGameScene.getWidth(), myGameScene.getHeight());
    return makeButton(getImage(PAUSE_BUTTON_LOCATION, PAUSE_BUTTON_IMAGE_SIZE), null, 0, "pause");
  }

  @SuppressWarnings("unused")
  private void pause() {
    myGameStage.setScene(pauseScene);
    myPauseMenu.setResumed(false);
    myAnimation.stop();
  }

  private Node setUpDarkModeButton() {
    return makeButton(getImage(ALIEN_BUTTON_LOCATION, PAUSE_BUTTON_IMAGE_SIZE), languageResources.getString(DARK_MODE), ALIEN_BUTTON_XPOS, SET_DARK_MODE);
  }

  private Node setUpNormalModeButton(){
    return makeButton(getImage(NORMAL_BUTTON_LOCATION, PAUSE_BUTTON_IMAGE_SIZE), languageResources.getString(NORMAL_MODE), NORMAL_BUTTON_XPOS, SET_NORMAL_MODE);
  }

  @SuppressWarnings("unused")
  private void setNormalMode(){
    colorEffectProperty.set(new ColorAdjust());
    myGameScene.getRoot().setStyle("-fx-base: rgba(255, 255, 255, 255)");
  }

  @SuppressWarnings("unused")
  private void setDarkMode(){
    colorEffectProperty.set(new ColorAdjust(0.5, 0.2, 0.0 ,0.0));
    myGameScene.getRoot().setStyle("-fx-base: rgba(60, 63, 65, 255)");
  }

  private Node makeButton(Node graphic, String text, double xPos, String methodName) {
    Button button = new Button(text);
    button.setGraphic(graphic);
    button.setOnAction(e -> {
      try {
        this.getClass().getDeclaredMethod(methodName).invoke(this);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
      }
    });
    button.setLayoutX(xPos);
    button.setLayoutY(0);
    // note: need the below because buttons consume certain key press events (like arrow keys)
    button.setOnKeyPressed(e -> button.getParent().fireEvent(e));
    return button;
  }

  private ImageView getImage(String location, double size){
    ImageView imageView = new ImageView(location);
    imageView.setFitHeight(size);
    imageView.setFitWidth(size);
    return imageView;
  }

  private void setUpGameStage() {
    myGameStage = new Stage();
    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
      try {
        step();
      } catch (Exception ex) {
        ex.printStackTrace();
        // note that this should ideally never be thrown
        if(currentError == null || !ex.getClass().equals(currentError.getClass())) {
          myAnimation.stop();
          showError(ex.getMessage());
          currentError = ex;
        }
      }
    });
    myAnimation = new Timeline();
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.getKeyFrames().add(frame);
    myAnimation.play();

    myRoot = new Group();
    myRoot.getChildren().add(myEntityGroup);
    myRoot.getChildren().add(new Line(START_X, PAUSE_BUTTON_SIZE, WINDOW_WIDTH, PAUSE_BUTTON_SIZE));
    myGameScene = new Scene(myRoot, WINDOW_WIDTH , WINDOW_HEIGHT);
    myGameStage.setScene(myGameScene);
    myGameStage.setTitle(myGameName);
    myGameStage.show();
  }

  private void step() {
    myGame.doGameStep(myAnimation.getCurrentTime().toMillis());
  }

  @SuppressWarnings("unused")
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(message);
    alert.show();
    alert.setOnCloseRequest(e -> myAnimation.play());
  }

  private void setUpInputListeners(UserInputListener userInputListener) {
    setUpPauseMenuListeners(userInputListener);
    myGameScene.setOnKeyPressed(e -> userInputListener.reactToKeyPress(e.getCode().getName()));
    myGameScene.setOnKeyReleased(e -> userInputListener.reactToKeyRelease(e.getCode().getName()));
    myGameScene.setOnMouseClicked(e -> userInputListener.reactToMouseClick(e.getX(), e.getY()-PAUSE_BUTTON_SIZE));
    // add more input types here as needed, like mouse drag events
  }

  private void setUpPauseMenuListeners(UserInputListener userInputListener) {
    myPauseMenu.resumedProperty().addListener((o, oldVal, newVal) -> {
      if(newVal){
        myGameStage.setScene(myGameScene);
        myAnimation.play();
      }
    });
    myPauseMenu.quitProperty().addListener((o, oldVal, newVal) -> {
      myGameStage.close();
      myAnimation.stop();
      myGame = null;
    });
    myPauseMenu.saveProperty().addListener((o, oldVal, newVal) -> userInputListener.reactToGameSave());
  }
}
