package ooga.game.controls;

/**
 * @author sam thompson
 * Handles translation from raw keyboard/IO inputs communicated from outside to
 * a standardized set of key codes (like UpKey) for internal use and for reference
 * in game data files.
 */
public interface ControlsInterpreter {

  /**
   * @param input The String representing the raw key/button input.
   * @return The standardized String representing the type of input.
   */
  String translateInput(String input);
}
