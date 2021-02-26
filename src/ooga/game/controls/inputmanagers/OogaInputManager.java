package ooga.game.controls.inputmanagers;

import java.util.ArrayList;
import java.util.List;
import ooga.game.controls.InputManager;

/**
 * @author sam thompson
 * Concretly handles keeping track of inputs. Works intuitively,
 * so that a key is pressed only if the manager has been notified of
 * a press since the last update.
 */
public class OogaInputManager implements InputManager {

  private final List<String> myActiveKeys;
  private final List<String> myPressedKeys;
  private final List<List<Double>> mouseClickedPos;

  /**
   * Default constructor. Initializes basic instance variables.
   */
  public OogaInputManager() {
    myActiveKeys = new ArrayList<>();
    myPressedKeys = new ArrayList<>();
    mouseClickedPos = new ArrayList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keyPressed(String key) {
    if (!myActiveKeys.contains(key)) {
      myActiveKeys.add(key);
      if (!myPressedKeys.contains(key)) {
        myPressedKeys.add(key);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keyReleased(String key) {
    myActiveKeys.remove(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getActiveKeys() {
    return new ArrayList<>(myActiveKeys);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getPressedKeys() {
    return new ArrayList<>(myPressedKeys);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseClicked(double mouseX, double mouseY) {
    mouseClickedPos.add(List.of(mouseX,mouseY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<List<Double>> getMouseClickPos() {
    return new ArrayList<>(mouseClickedPos);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update() {
    myPressedKeys.clear();
    mouseClickedPos.clear();
  }
}
