package ooga.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ooga.Entity;
import ooga.OogaDataException;
import ooga.UserInputListener;
import ooga.data.gamerecorders.GameRecorderExternal;
import ooga.data.entities.ImageEntityDefinition;
import ooga.data.gamedatareaders.GameDataReaderExternal;
import ooga.game.collisiondetection.CollisionDetector;
import ooga.game.controls.ControlsInterpreter;
import ooga.game.controls.InputManager;
import ooga.game.controls.inputmanagers.OogaInputManager;

/**
 * @author sam thompson, caryshindell, doherty guirand, braeden ward, chris warren
 * Handles running an OOGA game, with any sort of input type, collision detector, and
 * data type. Also handles the external side of the game to visualizers and the
 * internal side of the game to entities and their behaviors.
 */
public class OogaGame implements Game, UserInputListener, GameInternal {

  public static final String CLICKED_ON_CODE = "ClickedOn";
  public static final String KEY_ACTIVE_REQUIREMENT = "KeyActive";
  public static final String KEY_PRESSED_REQUIREMENT = "KeyPressed";
  private List<String> myLevelIds;
  private Level currentLevel;
  private final String myName;
  private final GameDataReaderExternal myGameDataReader;
  private final GameRecorderExternal myGameRecorder;
  private final CollisionDetector myCollisionDetector;
  private final ControlsInterpreter myControlsInterpreter;
  private final InputManager myInputManager = new OogaInputManager();
  private Map<String, String> myVariables;
  private ObservableList<Entity> myEntities;
  private List<EntityInternal> myEntitiesInternal;
  private final List<EntityInternal> myNewCreatedEntities = new ArrayList<>();
  Map<String, ImageEntityDefinition> myEntityDefinitions;
  private final List<DoubleProperty> cameraShiftProperties = List.of(new SimpleDoubleProperty(), new SimpleDoubleProperty());
  private String myProfileName;

  /**
   * Constructs an OogaGame instance.
   * @param gameName The name of the game, agreeing with the name in the target game file.
   * @param gameDataReaderExternal The GameDataReaderExternal that this game will use to
   *                               read its game file.
   * @param detector The CollisionDetector that this game will use to check for collisions.
   * @param controls The ControlsInterpreter that this game will use to map raw inputs
   *                 to standardized key codes.
   * @param profileName The name of the user profile running the game.
   * @param gameRecorderExternal The name of the game recorder that will be used to make saves.
   * @param date The date of the save to load, or an arbitrary date if no game should be loaded.
   * @throws OogaDataException if it is impossible to load the game. Provides details of
   * the error.
   */
  public OogaGame(String gameName, GameDataReaderExternal gameDataReaderExternal,  CollisionDetector detector,
                  ControlsInterpreter controls, String profileName, GameRecorderExternal gameRecorderExternal,
                  String date) throws OogaDataException {
    myGameDataReader = gameDataReaderExternal;
    myGameRecorder = gameRecorderExternal;
    myName = gameName;
    myLevelIds = myGameDataReader.getLevelIDs(gameName);
    myCollisionDetector = detector;
    myControlsInterpreter = controls;
    myProfileName = profileName;
    myEntities = FXCollections.observableArrayList(new ArrayList<>());
    myEntitiesInternal = new ArrayList<>();
    myEntityDefinitions = myGameDataReader.getImageEntityMap(gameName);
    initVariableMap(gameName);
    try {
      loadGameLevel(myGameDataReader.loadSavedLevel(myProfileName, date));
      System.out.println("GOT HERE");
    } catch (OogaDataException e) {
      loadGameLevel(myGameDataReader.loadNewLevel(myName, myLevelIds.get(0)));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ObservableList<Entity> getEntities() {
    return myEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doGameStep(double elapsedTime) {
    doEntityFrameUpdates(elapsedTime);
    doEntityBehaviors(elapsedTime);
    doEntityCleanup();
    executeEntityMovement(elapsedTime);
    doEntityCreation();
    myInputManager.update();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCurrentLevelId() {
    return currentLevel.getLevelId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactToMouseClick(double mouseX, double mouseY) {
    myInputManager.mouseClicked(mouseX,mouseY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactToKeyPress(String keyName) {
    String inputType = myControlsInterpreter.translateInput(keyName);
    myInputManager.keyPressed(inputType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactToKeyRelease(String keyName) {
    String inputType = myControlsInterpreter.translateInput(keyName);
    myInputManager.keyReleased(inputType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactToGameSave() {
    myGameRecorder.saveLevel(myProfileName,myName,currentLevel, myVariables);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createEntity(String type, List<Double> position) {
    ImageEntityDefinition definition = myEntityDefinitions.get(type);
    createEntity(type,position,definition.getWidth(),definition.getHeight());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createEntity(String type, List<Double> position, double width, double height) {
    EntityInternal created = myEntityDefinitions.get(type).makeInstanceAt(position.get(0),position.get(1));
    created.setHeight(height);
    created.setWidth(width);
    myNewCreatedEntities.add(created);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityInternal getEntityWithId(String id) {
    for (EntityInternal e : myEntitiesInternal) {
      String entityId = e.getEntityID();
      if (entityId != null && entityId.equals(id)) {
        return e;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EntityInternal> getEntitiesWithName(String name) {
    List<EntityInternal> entitiesWithName = new ArrayList<>();
    for (EntityInternal e : myEntitiesInternal) {
      if (e.getName().equals(name)) {
        entitiesWithName.add(e);
      }
    }
    return entitiesWithName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void goToLevel(String levelID) {
    try {
      loadGameLevel(myGameDataReader.loadNewLevel(myName,levelID));
      setCameraShiftValues(0,0);
    }
    catch (OogaDataException ignored) {
      //To preserve the pristine gameplay experience, we do nothing (rather than crash).
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void goToNextLevel() {
    goToLevel(currentLevel.nextLevelID());
    setCameraShiftValues(0,0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void restartLevel() {
    goToLevel(currentLevel.getLevelId());
    setCameraShiftValues(0,0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCameraShiftProperties(List<DoubleProperty> properties){
    for(int i = 0; i < cameraShiftProperties.size(); i ++){
      cameraShiftProperties.get(i).bindBidirectional(properties.get(i));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCameraShiftValues(double xValue, double yValue){
    cameraShiftProperties.get(0).set(xValue);
    cameraShiftProperties.get(1).set(yValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Double> getCameraShiftValues() {
    return List.of(cameraShiftProperties.get(0).getValue(), cameraShiftProperties.get(1).getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getVariables() {
    return new HashMap<>(myVariables);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVariable(String var, String value) {
    myVariables.put(var,value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EntityInternal> getInternalEntities() {
    return new ArrayList<>(myEntitiesInternal);
  }

  private void initVariableMap(String gameName) throws OogaDataException {
    myVariables = new HashMap<>();
    for (String key : myGameDataReader.getVariableMap(gameName).keySet()){
      myVariables.put(key, myGameDataReader.getVariableMap(gameName).get(key));
    }
  }

  private void loadGameLevel(Level level) {
    clearEntities();
    addAllEntities(level.getEntities());
    currentLevel = level;
  }

  private void addAllEntities(List<EntityInternal> entities) {
    for (EntityInternal e : entities) {
      addEntity(e);
    }
  }

  private void clearEntities() {
    myEntities.clear();
    myEntitiesInternal.clear();
  }

  private void findEntityCollisions(double elapsedTime, Entity entity,
      Map<String, List<EntityInternal>> collisionsByDirection) {
    for(String direction : myCollisionDetector.getSupportedDirections()){
      collisionsByDirection.put(direction, new ArrayList<>());
    }
    for(EntityInternal collidingWith : currentLevel.getEntities()){
      if(entity == (collidingWith)) {
        continue;
      }
      String collisionDirection = myCollisionDetector.getCollisionDirection(entity, collidingWith, elapsedTime);
      if (collisionDirection != null){
        collisionsByDirection.get(collisionDirection).add(collidingWith);
      }
    }
  }

  private void doEntityFrameUpdates(double elapsedTime) {
    for (EntityInternal entity : currentLevel.getEntities()) {
      entity.blockInAllDirections(false);
      entity.updateSelf(elapsedTime);
      entity.reactToVariables(myVariables);
    }
  }

  private void doEntityBehaviors(double elapsedTime) {
    List<String> activeKeys = myInputManager.getActiveKeys();
    List<String> pressedKeys = myInputManager.getPressedKeys();
    Map<String,String> allInputs = new HashMap<>();
    for (String key : activeKeys) {
      allInputs.put(key, KEY_ACTIVE_REQUIREMENT);
    }
    for (String key : pressedKeys) {
      allInputs.put(key, KEY_PRESSED_REQUIREMENT);
    }
    Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo = findDirectionalCollisions(elapsedTime);
    for (EntityInternal entity : currentLevel.getEntities()) {
      Map<String,String> entityInputs = findEntityInputs(allInputs, entity);
      entity.doConditionalBehaviors(elapsedTime, entityInputs, myVariables, collisionInfo, this);
    }
  }

  private Map<String, String> findEntityInputs(Map<String, String> allInputs, Entity entity) {
    Map<String,String> entityInputs = allInputs;
    for (List<Double> clickPos : myInputManager.getMouseClickPos()) {
      if (myCollisionDetector.entityAtPoint(entity, clickPos.get(0), clickPos.get(1))) {
        entityInputs = new HashMap<>(allInputs);
        entityInputs.put(CLICKED_ON_CODE,KEY_PRESSED_REQUIREMENT);
        break;
      }
    }
    return entityInputs;
  }

  private void executeEntityMovement(double elapsedTime) {
    for (EntityInternal e : currentLevel.getEntities()) {
      e.executeMovement(elapsedTime);
    }
  }

  private void doEntityCreation() {
    for (EntityInternal created : myNewCreatedEntities) {
      addEntity(created);
      currentLevel.addEntity(created);
    }
    myNewCreatedEntities.clear();
  }

  private void addEntity(EntityInternal created) {
    myEntities.add(created);
    myEntitiesInternal.add(created);
  }

  private void doEntityCleanup() {
    List<EntityInternal> destroyedEntities = new ArrayList<>();
    for (EntityInternal e : currentLevel.getEntities()) {
      if (e.isDestroyed()) {
        destroyedEntities.add(e);
      }
    }
    for (EntityInternal destroyed : destroyedEntities) {
      if (destroyed.isDestroyed()) {
        currentLevel.removeEntity(destroyed);
        removeEntity(destroyed);
      }
    }
  }

  private void removeEntity(EntityInternal destroyed) {
    myEntities.remove(destroyed);
    myEntitiesInternal.remove(destroyed);
  }

  private Map<EntityInternal, Map<String, List<EntityInternal>>> findDirectionalCollisions(double elapsedTime) {
    Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo = new HashMap<>();
    for(EntityInternal entity : currentLevel.getEntities()){
      Map<String, List<EntityInternal>> collisionsByDirection = new HashMap<>();
      findEntityCollisions(elapsedTime, entity, collisionsByDirection);
      collisionInfo.put(entity, collisionsByDirection);
    }
    return collisionInfo;
  }
}
