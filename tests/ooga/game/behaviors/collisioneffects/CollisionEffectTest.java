package ooga.game.behaviors.collisioneffects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ooga.OogaDataException;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.game.EntityInternal;
import ooga.game.Game;
import ooga.game.GameInternal;
import ooga.game.OogaGame;
import ooga.game.behaviors.BehaviorInstance;
import ooga.game.behaviors.ConditionalBehavior;
import ooga.game.behaviors.actions.CollisionDeterminedAction;
import ooga.game.collisiondetection.DirectionalCollisionDetector;
import ooga.game.controls.KeyboardControls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author caryshindell
 */
class CollisionEffectTest {

  public static final String GAME_NAME = "Super Mario Bros";
  public static final double ELAPSED_TIME = 1.0;
  public static final String KEYBOARD_INPUT_FILE = "ooga/game/controls/inputs/keyboard";

  private Game myGame;
  private GameInternal myGameInternal;

  @BeforeEach
  void setUp() throws OogaDataException {
    OogaGame game = new OogaGame(GAME_NAME, new XMLGameDataReader(){},new DirectionalCollisionDetector(),
            new KeyboardControls(KEYBOARD_INPUT_FILE),"",new XMLGameRecorder(),"");
    myGame = game;
    myGameInternal = game;
  }

  @Test
  void testRunIntoTerrainLeftEffect(){
    List<String> args = List.of();
    myGameInternal.createEntity("SmallMario", List.of(9999.0,50.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal entity1 = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    myGameInternal.createEntity("Brick", List.of(9900.0,0.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal terrainEntity = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    CollisionDeterminedAction action = new CollisionDeterminedAction(List.of("Brick", "Left"),List.of(new RunIntoTerrainLeftEffect(args)));
    ConditionalBehavior behavior = new BehaviorInstance(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), List.of(action));
    entity1.setConditionalBehaviors(List.of(behavior));
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(terrainEntity.getPosition().get(0)+terrainEntity.getWidth(), entity1.getPosition().get(0));
  }

  @Test
  void testRunIntoTerrainRightEffect(){
    List<String> args = List.of();
    myGameInternal.createEntity("SmallMario", List.of(9900.0,50.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal entity1 = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    myGameInternal.createEntity("Brick", List.of(9999.0,0.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal terrainEntity = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    CollisionDeterminedAction action = new CollisionDeterminedAction(List.of("Brick", "Right"),List.of(new RunIntoTerrainRightEffect(args)));
    ConditionalBehavior behavior = new BehaviorInstance(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), List.of(action));
    entity1.setConditionalBehaviors(List.of(behavior));
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(terrainEntity.getPosition().get(0), entity1.getPosition().get(0)+entity1.getWidth());
  }

  @Test
  void testRunIntoTerrainUpEffect(){
    List<String> args = List.of();
    myGameInternal.createEntity("SmallMario", List.of(9950.0,90.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal entity1 = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    myGameInternal.createEntity("Brick", List.of(9900.0,0.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal terrainEntity = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    CollisionDeterminedAction action = new CollisionDeterminedAction(List.of("Brick", "Up"),List.of(new RunIntoTerrainUpEffect(args)));
    ConditionalBehavior behavior = new BehaviorInstance(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), List.of(action));
    entity1.setConditionalBehaviors(List.of(behavior));
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(terrainEntity.getPosition().get(1)+terrainEntity.getHeight(), entity1.getPosition().get(1)-0.07);
  }

  @Test
  void testRunIntoTerrainDownEffect(){
    List<String> args = List.of();
    myGameInternal.createEntity("SmallMario", List.of(9950.0,0.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal entity1 = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    myGameInternal.createEntity("Brick", List.of(9900.0,90.0), 100.0, 100.0);
    myGame.doGameStep(ELAPSED_TIME);
    EntityInternal terrainEntity = myGameInternal.getInternalEntities().get(myGameInternal.getInternalEntities().size()-1);
    CollisionDeterminedAction action = new CollisionDeterminedAction(List.of("Brick", "Down"),List.of(new RunIntoTerrainDownEffect(args)));
    ConditionalBehavior behavior = new BehaviorInstance(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), List.of(action));
    entity1.setConditionalBehaviors(List.of(behavior));
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(terrainEntity.getPosition().get(1), entity1.getPosition().get(1)+entity1.getHeight());
  }
}