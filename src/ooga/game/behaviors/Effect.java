package ooga.game.behaviors;

import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;

/**
 * @author caryshindell, sam thompson
 * Determines how an entity reacts to in-game user input of any kind.
 * Dependencies: entity internal, game internal
 * Example: In Super Mario Bros, Certain actions like pressing right or left could
 * cause mario's velocity to change.
 * Relies on having access to the entities and game involved.
 */
public interface Effect {

  /**
   * Handles reaction to controls. Requires the ControlsBehavior to have a reference to the
   * instance that uses it in order to have an effect on that instance.
   * @param subject The entity that owns this controls behavior. This is the entity that should
   *                be modified primarily.
   * @param otherEntity The other entity involved in the event. Expects null if the effect doesn't
   *                    modify or check values for a second entity. This entity should not be modified.
   * @param elapsedTime The time elapsed since the last game frame.
   * @param variables The Map of Game variables mapping variable names to their values. Should be
   *                  modifiable.
   * @param game A reference to a GameInternal interface that provides game-related functionality
   *             (like switching levels).
   */
  void doEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game);

  /**
   * attempts to convert the given data into a double. If it can't, tries to get a value from game variables, then from entity variables.
   *  If that doesn't work, uses default value.
   * @param data string given by XMLGameDataReader
   * @param subject entity the effect is taking place on
   * @param variables game variables
   * @param defaultValue what to return if no match is found
   * @return the parsed value
   */
  default double parseData(String data, EntityInternal subject, Map<String, String> variables, double defaultValue){
    double parsedData;
    String finalValue = doVariableSubstitutions(data, subject, variables);
    try{
      parsedData = Double.parseDouble(finalValue);
    } catch (NumberFormatException e){
      parsedData = defaultValue;
    }
    return parsedData;
  }

  /**
   * tries to get a value from game variables, then from entity variables.
   *  If that doesn't work, returns the original value.
   * @param data string given by XMLGameDataReader
   * @param subject entity the effect is taking place on
   * @param variables game variables
   * @return the parsed value
   */
  static String doVariableSubstitutions(String data, EntityInternal subject, Map<String, String> variables){
    String finalValue = data;
    if(variables.containsKey(data)){
      finalValue = variables.get(data);
    } else if(subject.getVariable(data) != null){
      finalValue = subject.getVariable(data);
    }
    return finalValue;
  }
}
