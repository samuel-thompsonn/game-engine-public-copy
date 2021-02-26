package ooga.game.controls;

import java.util.ResourceBundle;

/**
 * @author sam thompson
 * Concretely handles using a keyboard-type controls mapping.
 * Doesn't handle certain other schemes (like analogue in controllers.)
 */
public class KeyboardControls implements ControlsInterpreter {

  public static final String UNKNOWN_INPUT = "UNKNOWN";

  private final ResourceBundle myResources;

  /**
   * @param inputFilePath The file location of the properties file containing
   *                      mappings from raw inputs to keys.
   */
  public KeyboardControls(String inputFilePath) {
    myResources = ResourceBundle.getBundle(inputFilePath);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String translateInput(String input) {
    if (!myResources.containsKey(input)) {
      return UNKNOWN_INPUT;
    }
    return myResources.getString(input);
  }
}
