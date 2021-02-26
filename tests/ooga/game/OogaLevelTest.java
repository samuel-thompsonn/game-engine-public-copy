package ooga.game;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ooga.data.entities.ImageEntity;
import org.junit.jupiter.api.Test;

public class OogaLevelTest {

  public static final int NUM_ASSERTS = 10;

  private Level myLevel;


  @Test
  void testRemoveEntity() {
    EntityInternal removable = createDummyEntity("placeholder",0.0,0.0);
    Level level = new OogaLevel(List.of(removable),"1");
    assertEquals(1,level.getEntities().size());
    level.removeEntity(removable);
    assertEquals(0,level.getEntities().size());
  }

  @Test
  void testAddEntity() {
    EntityInternal addable = createDummyEntity("placeholder",0.0,0.0);
    Level level = new OogaLevel(new ArrayList<>(),"1");
    assertEquals(0,level.getEntities().size());
    level.addEntity(addable);
    assertEquals(1,level.getEntities().size());
    assertEquals(addable.getName(),level.getEntities().get(0).getName());
    for (int i = 0; i < NUM_ASSERTS-1; i ++) {
      level.addEntity(createDummyEntity("placeholder2",0.0,0.0));
      assertEquals(i+2,level.getEntities().size());
    }
    assertEquals(NUM_ASSERTS,level.getEntities().size());
  }

  @Test
  void testLevelId() {
    Level level = new OogaLevel(new ArrayList<>(),"1");
    byte [] rand = new byte[10];
    new Random().nextBytes(rand);
    String randString = new String(rand, StandardCharsets.UTF_8);
    level.setNextLevelID(randString);
    assertEquals(randString,level.nextLevelID());
  }

  private EntityInternal createDummyEntity(String name, double xPos, double yPos) {
    return new ImageEntity(name,"",xPos,yPos,1.0,1.0);
  }
}
