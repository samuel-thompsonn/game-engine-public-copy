package ooga.game.behaviors.noncollisioneffects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.script.ScriptException;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Effect;
import ooga.game.behaviors.ExpressionEvaluator;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson
 * Changes the given variable of the subject by an amount
 * in a specified range. Detects what operator is used when changing
 * the variable (+,-,*,/)
 */
@SuppressWarnings("unused")
public class ChangeVariableRandomlyEffect extends TimeDelayedEffect {

  private String variableName;
  private String operatorData;
  private String randomRangeMin;
  private String randomRangeMax;

  /**
   * @param args The first arg is the String name of the ariable.
   *             The second arg is the operator used (+,-,*,/)
   *             The third arg is the minimum value to produce.
   *             The fourth arg is the maximum value to produce.
   * @throws IndexOutOfBoundsException
   */
  public ChangeVariableRandomlyEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    variableName = args.get(0);
    operatorData = args.get(1);
    randomRangeMin = args.get(2);
    randomRangeMax = args.get(3);
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
    double randomMinValue = parseData(randomRangeMin, subject, variables, 0.0);
    double randomMaxValue = parseData(randomRangeMax, subject, variables, 1.0);
    double changeValueData = (new Random().nextDouble()) * (randomMaxValue-randomMinValue);
    String operator = Effect.doVariableSubstitutions(operatorData, subject, variables);
    if (variables.containsKey(variableName)) {
      modifyGameVariable(variables, changeValueData, operator);
    }
    else {
      modifyEntityVariable(subject, changeValueData, operator);
    }
  }

  private void modifyEntityVariable(EntityInternal subject, double changeValueData, String operator) {
    String entityVariableValue = subject.getVariable(variableName);
    if(entityVariableValue != null){
      try {
        subject.addVariable(variableName, evaluateOperation(entityVariableValue, changeValueData, operator));
      } catch (NumberFormatException ignored){
        //If the change can't happen, don't change the variable.
      }
    }
  }

  private void modifyGameVariable(Map<String, String> variables, double changeValueData,
      String operator) {
    try {
      variables.put(variableName, evaluateOperation(variables.get(variableName), changeValueData, operator));
    }catch (NumberFormatException ignored){
      //If the change can't happen, don't change the variable.
    }
  }

  private String evaluateOperation(String varValue, double changeValue, String operator) throws NumberFormatException {
    String formattedVarValue = BigDecimal.valueOf(Double.parseDouble(varValue)).toPlainString();
    return String.valueOf(ExpressionEvaluator.eval(formattedVarValue + operator + changeValue));
  }
}
