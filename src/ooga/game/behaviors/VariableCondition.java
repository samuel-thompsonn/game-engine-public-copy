package ooga.game.behaviors;

import java.util.Map;

import ooga.game.EntityInternal;

/**
 * @author sam thompson
 * Represents a condition to a behavior's execution that depends on the value of
 * a Game or Entity variable.
 */
public interface VariableCondition {

  /**
   * @param behaviorEntity The EntityInternal that owns this behavior.
   * @param gameVariables The Game's Map from variable names to String values. Doesn't have to
   *                      be mutable.
   * @param subjectVariables The Map of variables of the EntityInternal that owns this behavior.
   * @return True if the condition is satisfied based on the variable values.
   */
  boolean isSatisfied(EntityInternal behaviorEntity, Map<String, String> gameVariables, Map<String, String> subjectVariables);
}
