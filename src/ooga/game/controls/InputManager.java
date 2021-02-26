package ooga.game.controls;

import java.util.List;

/**
 * @author sam thompson
 * Keeps track of and handles logic related to inputs of controls.
 * Determine which inputs have which status, and where the mouse was clicked.
 */
public interface InputManager {

  /**
   * Registers a keypress to the manager.
   * @param key The standardized String identifier of the key pressed
   */
  void keyPressed(String key);

  /**
   * Registers a key release to the manager.
   * @param key The standardized String identifier of the key pressed
   */
  void keyReleased(String key);

  /**
   * @return The inputs that are active this frame
   */
  List<String> getActiveKeys();

  /**
   * @return The inputs that have been pressed this frame.
   * ('Pressed' means that the input was activated this frame).
   */
  List<String> getPressedKeys();

  /**
   * Registers a mouse click with the manager.
   * @param mouseX The X position of the mouse click, in game coordinates.
   * @param mouseY The Y position of the mouse click, in game coordinates.
   */
  void mouseClicked(double mouseX, double mouseY);

  /**
   * @return The list of all points where the mouse clicked this frame.
   */
  List<List<Double>> getMouseClickPos();

  /**
   * Tells the input manager that a game step has passed.
   * This is required to keep track of Pressed vs Active inputs.
   */
  void update();
}
