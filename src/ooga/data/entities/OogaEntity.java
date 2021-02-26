package ooga.data.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import ooga.game.GameInternal;
import ooga.game.behaviors.ConditionalBehavior;
import ooga.Entity;
import ooga.game.EntityInternal;

/**
 * @author caryshindell, sam thompson, doherty guirand
 * This is the implementation of entity on the data side. It is essentially an object that defines how it is modified
 * by the back end and interpreted by the front end.
 * Assumptions: An entity can be blocked in 4 directions: left, right, up, down
 * Dependencies: entity internal, behavior interface
 * Example: an image or text entity
 */
public abstract class OogaEntity implements Entity, EntityInternal {

  public static final String CONSTANTS_FILEPATH = "ooga/data/resources/entityconstants";
  public static final String FRICTION_CONST_LABEL = "Friction";
  public static final double DEFAULT_FRICTION = 30.0 / 1000.0;
  private final BooleanProperty activeInView = new SimpleBooleanProperty(true);
  protected final DoubleProperty xPos = new SimpleDoubleProperty();
  protected final DoubleProperty yPos = new SimpleDoubleProperty();
  protected final DoubleProperty width = new SimpleDoubleProperty();
  protected final DoubleProperty height = new SimpleDoubleProperty();
  protected final DoubleProperty nonStationaryProperty = new SimpleDoubleProperty(0);
  protected String myName;
  private double frictionAcceleration;
  protected Map<String, String> propertyVariableDependencies = new HashMap<>();
  protected final Map<String, Consumer<String>> propertyUpdaters = new HashMap<>(){{
    put("XPos", variableValue -> xPos.set(Double.parseDouble(variableValue)));
    put("YPos", variableValue -> yPos.set(Double.parseDouble(variableValue)));
    put("Width", variableValue -> width.set(Double.parseDouble(variableValue)));
    put("Height", variableValue -> height.set(Double.parseDouble(variableValue)));
  }};

  private List<Double> myVelocity;
  private List<ConditionalBehavior> myConditionalBehaviors;
  private boolean isDestroyed;
  private static final String[] directions = new String[]{"Up", "Down", "Left", "Right"};
  private final Map<String, Boolean> blockedMovements = new HashMap<>();
  protected final Map<String, String> myVariables = new HashMap<>();

  public OogaEntity(double xPos, double yPos, double width, double height) {
    ResourceBundle constants = ResourceBundle.getBundle(CONSTANTS_FILEPATH);
    initConstant(constants,FRICTION_CONST_LABEL,DEFAULT_FRICTION);
    myVelocity = List.of(0.,0.);
    this.xPos.set(xPos);
    this.yPos.set(yPos);
    this.width.set(width);
    this.height.set(height);
    myConditionalBehaviors = new ArrayList<>();
    myName = "";
    for(String direction : directions){
      blockedMovements.put(direction, false);
    }
  }

  private void initConstant(ResourceBundle constants, String label, Double fallback) {
    try {
      this.frictionAcceleration = Double.parseDouble(constants.getString(label));
    } catch (NumberFormatException e) {
      this.frictionAcceleration = fallback;
    }
  }

  @Override
  public String getName() { return myName; }

  @Override
  public DoubleProperty xProperty() { return xPos; }

  @Override
  public DoubleProperty yProperty() { return yPos; }

  @Override
  public BooleanProperty activeInViewProperty() { return activeInView; }

  @Override
  public void setActiveInView(boolean activeInView) { this.activeInView.set(activeInView); }

  public DoubleProperty widthProperty(){ return width; }

  public DoubleProperty heightProperty(){ return height; }

  @Override
  public double getWidth() { return width.get(); }

  @Override
  public double getHeight() { return height.get(); }

  @Override
  public void setWidth(double width) { this.width.set(width); }

  @Override
  public void setHeight(double height) { this.height.set(height); }

  @Override
  public void setPosition(List<Double> newPosition) {
    xPos.set(newPosition.get(0));
    yPos.set(newPosition.get(1));
  }

  @Override
  public void updateSelf(double elapsedTime) {
    applyFrictionHorizontal();
    applyFrictionVertical();
  }

  private void applyFrictionHorizontal() {
    if (Math.abs(myVelocity.get(0)) < frictionAcceleration) {
      setVelocity(0,getVelocity().get(1));
    }
    else {
      changeVelocity(frictionAcceleration * -1 * Math.signum(myVelocity.get(0)),0);
    }
  }

  private void applyFrictionVertical() {

    if (Math.abs(myVelocity.get(1)) < frictionAcceleration) {
      setVelocity(getVelocity().get(0),0);
    }
    else {
      changeVelocity(0, frictionAcceleration * -1 * Math.signum(myVelocity.get(1)));
    }
  }


  @Override
  public void executeMovement(double elapsedTime) {
    double[] directionalVelocities = new double[]{-myVelocity.get(1), myVelocity.get(1), -myVelocity.get(0), myVelocity.get(0)};
    int[] velocityIndexes = new int[]{1, 1, 0, 0};
    for(int i=0; i<directions.length; i++){
      double[] newVelocity = new double[]{myVelocity.get(0), myVelocity.get(1)};
      newVelocity[velocityIndexes[i]] = 0.0;
      if(blockedMovements.get(directions[i]) && directionalVelocities[i] > 0) {
        setVelocity(newVelocity[0], newVelocity[1]);
      }
    }
    changePosition(myVelocity,elapsedTime);
  }

  /**
   * assigns the conditional behaviors of this entity
   *
   * @param conditionalBehaviors list of conditional behaviors
   */
  @Override
  public void setConditionalBehaviors(List<ConditionalBehavior> conditionalBehaviors) {
    myConditionalBehaviors = new ArrayList<>(conditionalBehaviors);
  }

  @Override
  public List<Double> getPosition() {
    return List.of(xPos.get(), yPos.get());
  }

  @Override
  public List<Double> getVelocity() {
    return new ArrayList<>(myVelocity);
  }

  @Override
  public boolean isDestroyed() {
    return isDestroyed;
  }

  @Override
  public void destroySelf() {
    isDestroyed = true;
  }

  private void changePosition(List<Double> velocity, double elapsedTime) {
    xPos.set(xPos.get() + (velocity.get(0) * elapsedTime));
    yPos.set(yPos.get() + (velocity.get(1) * elapsedTime));
  }

  @Override
  public void changeVelocity(double xChange, double yChange) {
    setVelocity(myVelocity.get(0) + xChange, myVelocity.get(1) + yChange);
  }

  @Override
  public void setVelocity(double xVelocity, double yVelocity) {
    myVelocity = List.of(xVelocity, yVelocity);
  }

  @Override
  public void reactToVariables(Map<String, String> variables) {
    //TODO: make this work for entity variables?
    for (Map.Entry<String, String> varNameEntry : variables.entrySet()) {
      if (propertyVariableDependencies.containsKey(varNameEntry.getKey())) {
        String propertyName = propertyVariableDependencies.get(varNameEntry.getKey());
        updateProperty(propertyName, varNameEntry.getValue());
      }
    }
    updateAutomaticEntityVariables();
  }

  private void updateProperty(String propertyName, String variableValue) {
    if (propertyUpdaters.containsKey(propertyName)) {
      try{
        propertyUpdaters.get(propertyName).accept(variableValue);
      } catch (NumberFormatException e){
        //can't set dependency; continue.
      }
      }
  }

  /**
   * automatically create/set entity variables containing basic entity information, allowing it to be used in conditions and effects
   */
  private void updateAutomaticEntityVariables() {
    myVariables.put("XVelocity", String.valueOf(myVelocity.get(0)));
    myVariables.put("YVelocity", String.valueOf(myVelocity.get(1)));
    myVariables.put("XPos", String.valueOf(this.xPos.get()));
    myVariables.put("YPos", String.valueOf(this.yPos.get()));
    myVariables.put("Width", String.valueOf(this.width.get()));
    myVariables.put("Height", String.valueOf(this.height.get()));
  }

  @Override
  public String getEntityID(){
    return getVariable("ID");
  }

  @Override
  public Map<String, String> getVariables() {
    return new HashMap<>(myVariables);
  }

  @Override
  public void setPropertyVariableDependencies(Map<String, String> propertyVariableDependencies){
    this.propertyVariableDependencies = propertyVariableDependencies;
  }

  /**
   * Execute the do method on each of this entity's conditional behaviors, which will check the conditions and execute the
   * assigned behavior if true
   */
  @Override
  public void doConditionalBehaviors(double elapsedTime, Map<String, String> inputs, Map<String, String> variables,
                                     Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal) {
    for (ConditionalBehavior conditionalBehavior : myConditionalBehaviors) {
      conditionalBehavior.doConditionalUpdate(elapsedTime, this, variables, inputs, collisionInfo, gameInternal);
    }
  }

  /**
   * change the value in this entity's blockedMovements map to the specified value
   * @param direction up, down, left, or right
   * @param isBlocked true if the entity is blocked in the direction, otherwise false
   */
  @Override
  public void blockInDirection(String direction, boolean isBlocked){
    blockedMovements.put(direction, isBlocked);
  }

  /**
   * change every value in this entity's blockedMovements map to the specified value
   * @param isBlocked true if the entity is blocked in the direction, otherwise false
   */
  @Override
  public void blockInAllDirections(boolean isBlocked){
    blockedMovements.replaceAll((d, v) -> isBlocked);
  }

  /**
   * Adds (or sets) a variable to this entity's variable map
   * @param name name of the variable
   * @param value value of the variable
   */
  @Override
  public void addVariable(String name, String value){ myVariables.put(name, value); }

  /**
   * returns the value of entity variable mapped to name
   * @param name key
   * @return value
   */
  @Override
  public String getVariable(String name){ return myVariables.get(name); }

  /**
   * add all variables to the specified map to this entity's variable map
   *
   * @param variables map of variable names to values
   */
  @Override
  public void setVariables(Map<String, String> variables) { myVariables.putAll(variables); }

  /**
   * creates the double property nonStationaryProperty for the entity. 1 if false, 0 if true.
   *
   * @param stationary whether or not the entity is stationary with respect to camera moves
   */
  @Override
  public void makeNonStationaryProperty(boolean stationary) {
    if (stationary) nonStationaryProperty.set(0);
    else nonStationaryProperty.set(1);
  }

  @Override
  public DoubleProperty nonStationaryProperty(){
    return nonStationaryProperty;
  }
}
