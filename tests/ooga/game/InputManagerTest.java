package ooga.game;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ooga.game.controls.InputManager;
import ooga.game.controls.inputmanagers.OogaInputManager;
import org.junit.jupiter.api.Test;

public class InputManagerTest {

  @Test
  void testKeyPressed() {
    InputManager manager = new OogaInputManager();
    final double NUM_ASSERTS = 10;
    List<String> generatedStrings = new ArrayList<>();
    for (int i = 0; i < NUM_ASSERTS; i++) {
      byte[] array = new byte[i];
      new Random().nextBytes(array);
      String generatedString = new String(array, StandardCharsets.UTF_8);
      generatedStrings.add(generatedString);
      manager.keyPressed(generatedString);
      assertEquals(generatedStrings,manager.getPressedKeys());
    }
  }

  @Test
  void testUpdate() {
    InputManager manager = new OogaInputManager();
    final double NUM_ASSERTS = 10;
    List<String> generatedStrings = new ArrayList<>();
    for (int i = 0; i < NUM_ASSERTS; i++) {
      byte[] array = new byte[i];
      new Random().nextBytes(array);
      String generatedString = new String(array, StandardCharsets.UTF_8);
      List<String> lastGeneratedString = List.of(generatedString);
      generatedStrings.add(generatedString);
      manager.keyPressed(generatedString);
      assertArrayEquals(generatedStrings.toArray(),manager.getActiveKeys().toArray());
      assertEquals(lastGeneratedString,manager.getPressedKeys());
      manager.update();
    }
  }

  @Test
  void testKeyReleased() {
    InputManager manager = new OogaInputManager();
    final double NUM_ASSERTS = 10;
    List<String> generatedStrings = new ArrayList<>();
    for (int i = 0; i < NUM_ASSERTS; i++) {
      byte[] array = new byte[i];
      new Random().nextBytes(array);
      String generatedString = new String(array, StandardCharsets.UTF_8);
      generatedStrings.add(generatedString);
      manager.keyPressed(generatedString);
    }
    for (int i = 0; i < generatedStrings.size(); i ++) {
      String s = generatedStrings.get(i);
      generatedStrings.remove(s);
      manager.keyReleased(s);
      assertEquals(generatedStrings,manager.getActiveKeys());
    }
  }

}
