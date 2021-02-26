package ooga.game.behaviors.noncollisioneffects;

import java.util.List;
import java.util.Map;
import java.util.Random;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Effect;
import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author sam thompson
 * Sets a given variable with a random Double value within a given range.
 * Example: An enemy might jump by a random amount every few seconds.
 */
@SuppressWarnings("unused")
public class SetVariableRandomlyEffect extends TimeDelayedEffect {

  private String variableName;
  private String randomRangeMin;
  private String randomRangeMax;

  public SetVariableRandomlyEffect(List<String> args) throws IndexOutOfBoundsException {
    super(args);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void processArgs(List<String> args) {
    variableName = args.get(0);
    randomRangeMin = args.get(1);
    randomRangeMax = args.get(2);
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
    double randomMinValue = parseData(randomRangeMin, subject, variables, 0.0);
    double randomMaxValue = parseData(randomRangeMax, subject, variables, 1.0);

    double variableValue = (new Random().nextDouble()) * (randomMaxValue-randomMinValue);

    if (variables.containsKey(variableName)) {
      variables.put(variableName, Effect.doVariableSubstitutions(Double.toString(variableValue), subject, variables));
    }
    if(subject.getVariable(variableName) != null){
      subject.addVariable(variableName, Double.toString(variableValue));
    }
  }
}
