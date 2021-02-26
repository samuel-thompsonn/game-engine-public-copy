package ooga.game.behaviors.noncollisioneffects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ooga.Entity;
import ooga.OogaDataException;
import ooga.data.entities.ImageEntity;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.game.EntityInternal;
import ooga.game.Game;
import ooga.game.GameInternal;
import ooga.game.OogaGame;
import ooga.game.behaviors.BehaviorInstance;
import ooga.game.behaviors.ConditionalBehavior;
import ooga.game.behaviors.Effect;
import ooga.game.behaviors.actions.IndependentAction;
import ooga.game.collisiondetection.DirectionalCollisionDetector;
import ooga.game.controls.KeyboardControls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonCollisionEffectTest {

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
        new KeyboardControls(KEYBOARD_INPUT_FILE),"",new XMLGameRecorder(),"");
    myGame = game;
    myGameInternal = game;
  }

  @Test
  void testChangePositionEffect() {
    List<String> args = List.of("10.0","10.0","+");
    Entity affected = createBehaviorWithEffect(new ChangePositionEffect(args));
    List<Double> startPos = affected.getPosition();
    assertEquals(startPos,affected.getPosition());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(List.of(startPos.get(0)+10.0,startPos.get(1)+10.0),affected.getPosition());
  }

  @Test
  void testChangeVariableEffect() {
    List<String> args = List.of("Lives","+","5.0");
    createBehaviorWithEffect(new ChangeVariableEffect(args));
    double startingLives = Double.parseDouble(myGameInternal.getVariables().get(args.get(0)));
    assertEquals(startingLives, Double.parseDouble(myGameInternal.getVariables().get(args.get(0))));
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(startingLives + 5, Double.parseDouble(myGameInternal.getVariables().get(args.get(0))));
  }

  @Test
  void testChangeVelocity() {
    List<String> args = List.of("1.0","10.0","+","100.0");
    Entity affected = createBehaviorWithEffect(new ChangeVelocityEffect(args));
    assertEquals(List.of(0.0,0.0),affected.getVelocity());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(List.of(1.0,10.0),affected.getVelocity());
  }

  @Test
  void testCreateEntityCustom() {
    List<String> args = List.of("Mushroom","0.0","0.0","5000.0","4000.0");
    createBehaviorWithEffect(new CreateEntityCustomEffect(args));
    double startingEntities = myGame.getEntities().size();
    assertEquals(startingEntities,myGame.getEntities().size());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(startingEntities+1,myGame.getEntities().size());
    Entity newEntity = myGameInternal.getEntitiesWithName("Mushroom").get(0);
    assertEquals(4000.0,newEntity.getHeight());
    assertEquals(5000.0,newEntity.getWidth());
  }

  @Test
  void testCreateEntityRelative() {
    List<String> args = List.of("SmallMario","0.0","0.0");
    createBehaviorWithEffect(new CreateEntityRelativeEffect(args));
    double startingEntities = myGame.getEntities().size();
    assertEquals(startingEntities,myGame.getEntities().size());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(startingEntities+1,myGame.getEntities().size());
  }

  @Test
  void testDestroySelfEffect() {
    Entity destructible = createBehaviorWithEffect(new DestroySelfEffect(new ArrayList<>()));
    assertTrue(myGame.getEntities().contains(destructible));
    myGame.doGameStep(ELAPSED_TIME);
    assertFalse(myGame.getEntities().contains(destructible));
  }

  @Test
  void testGotoLevel() {
    createBehaviorWithEffect(new GotoLevelEffect(List.of(TARGET_LEVEL)));
    assertEquals("1",myGame.getCurrentLevelId());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(TARGET_LEVEL,myGame.getCurrentLevelId());
  }

  @Test
  void testGotoInvalidLevel() {
    createBehaviorWithEffect(new GotoLevelEffect(List.of(INVALID_LEVEL_NAME)));
    assertEquals("1",myGame.getCurrentLevelId());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(INVALID_LEVEL_NAME,myGame.getCurrentLevelId());
  }

  @Test
  void testMoveTowardsEntityEffect() {
    List<String> args = List.of("10.0","100.0");
    EntityInternal a = createDummyEntity("a",0.0,0.0);
    EntityInternal b = createDummyEntity("b",100.0,0.0);
    Effect testEffect = new MoveTowardsEntityEffect(args);
    testEffect.doEffect(a,b,1.0,new HashMap<>(), myGameInternal);
    assertEquals(List.of(10.0,0.0),a.getVelocity());
    testEffect.doEffect(a,b,1.0,new HashMap<>(), myGameInternal);
    assertEquals(List.of(20.0,0.0),a.getVelocity());
    assertEquals(List.of(0.0,0.0),b.getVelocity());
  }

  @Test
  void testNextLevel() {
    createBehaviorWithEffect(new NextLevelEffect(new ArrayList<>()));
    assertEquals("1",myGame.getCurrentLevelId());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals("2",myGame.getCurrentLevelId());
  }

  @Test
  void testResize() {
    List<String> args = List.of("100.0","110.0");
    Entity resized = createBehaviorWithEffect(new ResizeEffect(args));
    double startWidth = resized.getWidth();
    double startHeight = resized.getHeight();
    assertEquals(startWidth,resized.getWidth());
    assertEquals(startHeight,resized.getHeight());
    myGame.doGameStep(ELAPSED_TIME);
    assertEquals(100.0,resized.getWidth());
    assertEquals(110.0,resized.getHeight());
  }

  @Test
  void testRestartLevel() {
    Entity restarter = createBehaviorWithEffect(new RestartLevelEffect(new ArrayList<>()));
    assertTrue(myGame.getEntities().contains(restarter));
    myGame.doGameStep(ELAPSED_TIME);
    assertFalse(myGame.getEntities().contains(restarter));
  }

  @Test
  void testSetImage() {

  }

  @Test
  void testSetPosition() {
    List<String> args = List.of("100.0","400.0");
    Effect reposition = new SetPositionEffect(args);
    EntityInternal subj = createDummyEntity("subject",0.0,0.0);
    assertEquals(List.of(0.0,0.0),subj.getPosition());
    reposition.doEffect(subj,null,ELAPSED_TIME,new HashMap<>(),myGameInternal);
    assertEquals(List.of(100.0,400.0),subj.getPosition());
  }

  @Test
  void testSetVariable() {
    List<String> argsFirst = List.of("var1","100.0");
    EntityInternal subj = createDummyEntity("subject",0.0,0.0);
    Effect varSetter = new SetVariableEffect(argsFirst);
    myGameInternal.setVariable("var1","0.0");
    assertEquals("0.0",myGameInternal.getVariables().get("var1"));
    varSetter.doEffect(subj,null,ELAPSED_TIME,myGameInternal.getVariables(),myGameInternal);
    assertEquals("100.0",myGameInternal.getVariables().get("var1"));
  }

  @Test
  void testSetVelocity() {
    List<String> args = List.of("10.0","111.0");
    EntityInternal subj = createDummyEntity("subj",0.0,0.0);
    assertEquals(List.of(0.0,0.0),subj.getVelocity());
    Effect velocitySetter = new SetVelocityEffect(args);
    velocitySetter.doEffect(subj,null,ELAPSED_TIME,new HashMap<>(),myGameInternal);
    assertEquals(List.of(10.0,111.0),subj.getVelocity());
  }

  @Test
  void testEffectConstructor() {
    try {
      Effect invalidArgs = new ChangePositionEffect(new ArrayList<>());
      fail();
    } catch (IndexOutOfBoundsException e) {
      //Throwing an exception causes this test to pass.
    }
    try {
      Effect invalidArgs = new ResizeEffect(new ArrayList<>());
      fail();
    } catch (IndexOutOfBoundsException e) {
      //Throwing an exception causes this test to pass.
    }
    try {
      Effect invalidArgs = new SetPositionEffect(new ArrayList<>());
      fail();
    } catch (IndexOutOfBoundsException e) {
      //Throwing an exception causes this test to pass.
    }
  }

  private EntityInternal createBehaviorWithEffect(Effect effect) {
    IndependentAction action = new IndependentAction(new ArrayList<>(),List.of(effect));
    ConditionalBehavior behavior = new BehaviorInstance(new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), List.of(action));
    myGameInternal.getInternalEntities().get(0).setConditionalBehaviors(List.of(behavior));
    return myGameInternal.getInternalEntities().get(0);
  }

  private EntityInternal createDummyEntity(String name, double xPos, double yPos) {
    return new ImageEntity(name,"",xPos,yPos,1.0,1.0);
  }

}