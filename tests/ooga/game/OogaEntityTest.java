package ooga.game;

import ooga.OogaDataException;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.game.behaviors.Action;
import ooga.game.behaviors.BehaviorInstance;
import ooga.game.behaviors.ConditionalBehavior;
import ooga.game.behaviors.Effect;
import ooga.game.behaviors.actions.CollisionDeterminedAction;
import ooga.game.behaviors.actions.IndependentAction;
import ooga.game.behaviors.collisioneffects.RunIntoTerrainLeftEffect;
import ooga.game.behaviors.noncollisioneffects.SetVariableEffect;
import ooga.game.collisiondetection.DirectionalCollisionDetector;
import ooga.game.controls.KeyboardControls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author caryshindell
 * Tests the entity implementation
 */
public class OogaEntityTest {

  public static final String GAME_NAME = "Sonic the Hedgehog";
  public static final double ELAPSED_TIME = 1.0;
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
  void testAutomaticVariables(){
    myGameInternal.createEntity("Coin", List.of(9999.0,50.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal entity1 = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    Effect setVariableEffect = new SetVariableEffect(List.of("Lives", "Width"));
    Action action = new IndependentAction(List.of(), List.of(setVariableEffect));
    ConditionalBehavior behavior = new BehaviorInstance(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), List.of(action));
    entity1.setConditionalBehaviors(List.of(behavior));
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(myGameInternal.getVariables().get("Lives"), String.valueOf(entity1.getWidth()));
  }


}
