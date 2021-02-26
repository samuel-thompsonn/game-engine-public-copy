package ooga.game.behaviors;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;

/**
 * @author caryshindell, sam thompson
 * This is the concrete implementation of a behavior. It essentially is "if these conditions are true do these actions"
 * Note: you can use the special key SELF to specify the subject entity as the first entity in a collision condition
 * Dependencies: entity internal, game internal, action, conditional behavior
 * Example: fireboy has behavior "if game variables red diamonds is 1 and bluedimaonds is 1 and I am colliding with terrain
 * in downward direction and I am colliding with red door in any direction and user is pressing down key and watergirl is
 * colliding with blue door in any direction, then: do action(s) with effect(s) go to next level.
 */
public class BehaviorInstance implements ConditionalBehavior {

  public static final String ANY_DIRECTION = "ANY";
  public static final String SELF_IDENTIFIER = "SELF";
  public static final String ANY_KEY_REQUIREMENT = "KeyAny";
  public static final String KEY_INACTIVE_REQUIREMENT = "KeyInactive";
  final Map<String, List<String>> inputConditions;
  final Map<List<String>, String> requiredCollisionConditions;
  final Map<List<String>, String> bannedCollisionConditions;
  final List<VariableCondition> gameVarConditions;
  final Map<String,List<VariableCondition>> entityVarConditions;
  final List<Action> actions;

  /**
   * @param gameVariableConditions
   * @param entityVariableConditions
   * @param inputConditions
   * @param requiredCollisionConditions conditions that must be true Map<List<String>, String> [entity 1 info, entity 2 info] : direction (or "ANY")
   *   entity info can be id or name, method will check for either
   * @param bannedCollisionConditions conditions that must be false (see above)
   * @param actions
   */
  public BehaviorInstance(List<VariableCondition> gameVariableConditions,
      Map<String, List<VariableCondition>> entityVariableConditions,
      Map<String, List<String>> inputConditions,
      Map<List<String>, String> requiredCollisionConditions,
      Map<List<String>, String> bannedCollisionConditions, List<Action> actions){
    this.inputConditions = inputConditions;
    this.gameVarConditions = gameVariableConditions;
    this.entityVarConditions = entityVariableConditions;
    this.requiredCollisionConditions = requiredCollisionConditions;
    this.bannedCollisionConditions = bannedCollisionConditions;
    this.actions = actions;
  }

  /**
   * So we want a new type of behavior, conditional behavior, which has a map of conditionals for each thing it is possible
   * to depend on (inputs, collisions, variables). It also has a list of behaviors it will execute if the conditionals are true.
   * Example: Fireboy can only go through door, ending the level, if he is colliding with door and touching ground,
   * variable red diamonds collected is 5 (or perhaps a dynamic value?),
   * and user is pressing up key
   * Method call for this example would look like: new ConditionalBehavior(inputs=(up:true), collisions=(door:true, ground:true),
   * variables=(red diamonds: 5), behaviors=(EndLevel))
   * @param elapsedTime time passed in ms
   * @param subject     entity that owns this behavior
   * @param variables   map of game/level variables
   * @param inputs      all registered key inputs at this frame
   * @param collisionInfo Map of maps, direction name : map of collisions for that direction. map of collisions is entity : list of entities
   * @param gameInternal what game this is run from
   */
  @Override
  public void doConditionalUpdate(double elapsedTime, EntityInternal subject, Map<String, String> variables, Map<String, String> inputs,
                                  Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal) {
    // TODO: add ability for entity instances to have additional behaviors?
    if (checkGameVariableConditions(subject,variables)
    &&  checkEntityVariableConditions(subject,gameInternal,variables)
    &&  checkInputConditions(inputs)
    && allCollisionConditionsSatisfied(collisionInfo, requiredCollisionConditions, true, subject, gameInternal)
    && allCollisionConditionsSatisfied(collisionInfo, bannedCollisionConditions, false, subject, gameInternal)) {
      doActions(elapsedTime, subject, variables, collisionInfo, gameInternal);
    }
  }

  private boolean checkInputConditions(Map<String, String> inputs) {
    for(Entry<String, List<String>> inputCondition : inputConditions.entrySet()){
      if (!inputConditionSatisfied(inputs, inputCondition)) {
        return false;
      }
    }
    return true;
  }

  private boolean inputConditionSatisfied(Map<String, String> inputs,
      Entry<String, List<String>> inputCondition) {
    for (String inputType : inputCondition.getValue()) {
      String keyState = inputs.get(inputCondition.getKey());
      if (keyRequirementSatisfied(inputType, keyState)) {
        return true;
      }
    }
    return false;
  }

  private boolean keyRequirementSatisfied(String inputType, String keyState) {
    return inputType.equals(keyState) ||
        keyAnySatisfied(inputType, keyState) ||
        keyInactiveSatisfied(inputType, keyState);
  }

  private boolean keyAnySatisfied(String inputType, String keyState) {
    return inputType.equals(ANY_KEY_REQUIREMENT) && keyState != null;
  }

  private boolean keyInactiveSatisfied(String inputType, String keyState) {
    return inputType.equals(KEY_INACTIVE_REQUIREMENT) && keyState == null;
  }


  private boolean checkGameVariableConditions(EntityInternal subject, Map<String, String> gameVariables) {
    return checkVariableConditionsList(subject, gameVariables, gameVarConditions, gameVariables);
  }

  private boolean checkVariableConditionsList(EntityInternal subject, Map<String, String> gameVariables,
      List<VariableCondition> varConditions,
      Map<String, String> targetEntityVars) {
    for (VariableCondition condition : varConditions) {
      if (!condition.isSatisfied(subject,gameVariables,targetEntityVars)) {
        return false;
      }
    }
    return true;
  }

  private boolean checkEntityVariableConditions(EntityInternal subject, GameInternal gameInternal,
      Map<String, String> gameVariables) {
    for (Entry<String ,List<VariableCondition>> conditionEntry : entityVarConditions.entrySet()) {
      EntityInternal identified = identifyEntityVariableSubject(subject,gameInternal,conditionEntry.getKey());
      if (identified == null) {
        return false;
      }
      if (!checkVariableConditionsList(subject,gameVariables,conditionEntry.getValue(), identified.getVariables())) {
        return false;
      }
    }
    return true;
  }

  private EntityInternal identifyEntityVariableSubject(EntityInternal subject, GameInternal gameInternal, String label) {
    if (label.equals(SELF_IDENTIFIER)) {
      return subject;
    }
    else {
      return otherEntitySubject(subject, gameInternal, label);
    }
  }

  private EntityInternal otherEntitySubject(EntityInternal subject, GameInternal gameInternal, String label) {
    String subjectVariable = subject.getVariable(label);
    if (subjectVariable != null) {
      EntityInternal e = gameInternal.getEntityWithId(subject.getVariable(label));
      if (e != null) {
        return e;
      }
      List<EntityInternal> entitiesWithName = gameInternal.getEntitiesWithName(subjectVariable);
      if (entitiesWithName.size() > 0) {
        return entitiesWithName.get(0);
      }
    }
    EntityInternal e = gameInternal.getEntityWithId(label);
    if (e != null) {
      return e;
    }
    List<EntityInternal> entitiesWithName = gameInternal.getEntitiesWithName(label);
    if (!entitiesWithName.isEmpty()) {
      return entitiesWithName.get(0);
    }
    return null;
  }

  private boolean allCollisionConditionsSatisfied(Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo,
                                                  Map<List<String>, String> collisionConditions, boolean required,
                                                  EntityInternal subject, GameInternal gameInternal) {
    for(Map.Entry<List<String>, String> collisionConditionEntry : collisionConditions.entrySet()){
      String entity1Info = collisionConditionEntry.getKey().get(0);
      String entity2Info = collisionConditionEntry.getKey().get(1);
      String direction = collisionConditionEntry.getValue();
      if(checkCollisionCondition(collisionInfo, entity1Info, entity2Info, direction, subject, gameInternal) != required) return false;
    }
    return true;
  }

  private boolean checkCollisionCondition(Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, String entity1Info,
                                          String entity2Info, String direction, EntityInternal subject, GameInternal gameInternal) {
    EntityInternal entity1 = identifyEntityVariableSubject(subject, gameInternal, entity1Info);
    if(collisionInfo.containsKey(entity1)) {
      return checkCollisionConditionForEntity(collisionInfo, entity2Info, direction, entity1);
    }
    return false;
  }

  private boolean checkCollisionConditionForEntity(Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo,
                                                   String entity2Info, String direction, EntityInternal entity) {
    if(direction.equals(ANY_DIRECTION)){
      for(String possibleDirection : collisionInfo.get(entity).keySet()){
        if(hasCollisionInDirection(collisionInfo, entity2Info, possibleDirection, entity)) return true;
      }
    } else return hasCollisionInDirection(collisionInfo, entity2Info, direction, entity);
    return false;
  }

  private boolean hasCollisionInDirection(Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo,
                                          String entity2Info, String direction, EntityInternal entity) {
    List<EntityInternal> collidingWithInDirection = collisionInfo.get(entity).get(direction);
    for(EntityInternal collidingEntity : collidingWithInDirection){
      if(entityMatches(entity2Info, collidingEntity)){
        return true;
      }
    }
    return false;
  }

  /**
   * collision/terrain specific!
   * @param entity1Info
   * @param entity
   * @return
   */
  private boolean entityMatches(String entity1Info, EntityInternal entity) {
    return entity.getName().equals(entity1Info) ||
            (entity.getVariable("TerrainID") != null && entity.getVariable("TerrainID").equals(entity1Info));
  }

  /**
   * executes the actions owned by this behavior
   * @param elapsedTime time in ms
   * @param subject entity that owns this conditional behavior
   * @param variables map of variables in the game/level
   * @param collisionInfo current collision info
   * @param gameInternal what game this is run from
   */
  @Override
  public void doActions(double elapsedTime, EntityInternal subject, Map<String, String> variables,
                        Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo, GameInternal gameInternal) {
    for(Action action : actions){
      action.doAction(elapsedTime, subject, variables, collisionInfo, gameInternal);
    }
  }
}
