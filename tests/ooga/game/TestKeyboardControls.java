package ooga.game;

import static org.junit.Assert.assertEquals;

import java.util.List;
import ooga.game.controls.ControlsInterpreter;
import ooga.game.controls.KeyboardControls;
import org.junit.jupiter.api.Test;

public class TestKeyboardControls {

  @Test
  void testTranslateInput() {
    ControlsInterpreter controls = new KeyboardControls("ooga/game/controls/inputs/keyboard");
    List<String> target = List.of("UpKey2","DownKey2","RightKey2","LeftKey2");
    List<String> input = List.of("W","S","D","A");
    for (int i = 0; i < input.size(); i ++) {
      assertEquals(controls.translateInput(input.get(i)),target.get(i));
    }
  }
}
