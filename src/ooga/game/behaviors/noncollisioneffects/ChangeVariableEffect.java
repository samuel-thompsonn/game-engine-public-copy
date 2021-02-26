package ooga.game.behaviors.noncollisioneffects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Effect;
import ooga.game.behaviors.ExpressionEvaluator;
import ooga.game.behaviors.TimeDelayedEffect;

import javax.script.ScriptException;

/**
 * @author caryshindell, sam thompson
 * This effect changes the value of a variable based on its existing value, and the operator and value given.
 * Assumptions: this is called through reflection. Note that it can substitute in variable values for the operator
 * and increment, but not the variable name. It can change either entity variable or game variable.
 * Dependencies: TimeDelayedEffect, entity internal, ExpressionEvaluator
 * Example: ChangeVariable score + 1.0 should add one to the game variable score
 */
@SuppressWarnings("unused")
public class ChangeVariableEffect extends TimeDelayedEffect {

  private String variableName;
  private String operatorData;
  private String changeValueData;

  public ChangeVariableEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    int index = 0;
    variableName = args.get(index++);
    operatorData = args.get(index++);
    changeValueData = args.get(index);
  }

  /**
   * Performs the effect
   * @param subject     The entity that owns this. This is the entity that should be modified.
   * @param otherEntity entity we are "interacting with" in this effect
   * @param elapsedTime time between steps in ms
   * @param variables   game variables
   * @param game        game instance
   */
  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    //in the variable map, increment variableName by variableValue
    double changeValue = parseData(changeValueData, subject, variables, 1.0);
    String operator = Effect.doVariableSubstitutions(operatorData, subject, variables);
    if (variables.containsKey(variableName)) {
      try {
        variables.put(variableName, evaluateOperation(variables.get(variableName), changeValue, operator));
        return;
      }catch (NumberFormatException ignored){
        //if this doesn't work, then look elsewhere for the var definition.
      }
    }
    String entityVariableValue = subject.getVariable(variableName);
    if(entityVariableValue != null){
      try {
        subject.addVariable(variableName, evaluateOperation(entityVariableValue, changeValue, operator));
      } catch (NumberFormatException ignored){
        //If the operation is invalid, don't do anything.
      }
    }
  }

  private String evaluateOperation(String varValue, double changeValue, String operator) throws NumberFormatException {
    String formattedVarValue = BigDecimal.valueOf(Double.parseDouble(varValue)).toPlainString();
    return String.valueOf(ExpressionEvaluator.eval(formattedVarValue + operator + BigDecimal.valueOf(changeValue).toPlainString()));
  }
}
