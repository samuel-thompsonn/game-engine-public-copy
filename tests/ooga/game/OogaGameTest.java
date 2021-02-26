package ooga.game;

import static org.junit.Assert.assertEquals;

import java.util.List;
import ooga.OogaDataException;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.game.collisiondetection.DirectionalCollisionDetector;
import ooga.game.controls.KeyboardControls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OogaGameTest {

  public static final String GAME_NAME = "Super Mario Bros";
  public static final double ELAPSED_TIME = 1.0;
  public static final String TARGET_LEVEL = "2";
  public static final String INVALID_LEVEL_NAME = "INVALIDLEVEL";
  public static final String KEYBOARD_INPUT_FILE = "ooga/game/controls/inputs/keyboard";

  private Game myGame;
  private GameInternal myGameInternal;

  @BeforeEach
  void setUp() throws OogaDataException {
    OogaGame game = new OogaGame(GAME_NAME, new XMLGameDataReader(){},new DirectionalCollisionDetector(),
    new KeyboardControls(KEYBOARD_INPUT_FILE),"", new XMLGameRecorder(),"");
    myGame = game;
    myGameInternal = game;
  }

  @Test
  void testCurrentLevelId() {
    assertEquals("1", myGame.getCurrentLevelId());
  }

  @Test
  void testGoToNextLevel() {
    assertEquals("1", myGame.getCurrentLevelId());
    myGameInternal.goToNextLevel();
    assertEquals("2", myGame.getCurrentLevelId());
  }

  @Test
  void testCreateEntity() {
    double numEntities = myGame.getEntities().size();
    myGameInternal.createEntity("Mushroom", List.of(0.0, 0.0));
    assertEquals(numEntities, myGame.getEntities().size(), 0.5);
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(numEntities + 1, myGame.getEntities().size(), 0.5);
  }
}