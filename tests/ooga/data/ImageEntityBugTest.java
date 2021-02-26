package ooga.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import ooga.data.entities.ImageEntityDefinition;
import ooga.game.EntityInternal;
import org.junit.jupiter.api.Test;

public class ImageEntityBugTest {

  @Test
  void testCreatedStationary() {
    ImageEntityDefinition definition = new ImageEntityDefinition("FireFlower",50.0,50.0,"",new ArrayList<>());
    definition.setStationary(false);
    EntityInternal newEntity = definition.makeInstanceAt(0.0,0.0);
    assertEquals(1.0,newEntity.nonStationaryProperty().getValue(),0.3);
  }

  @Test
  void testCreatedNonStationary() {
    ImageEntityDefinition definition = new ImageEntityDefinition("FireFlower",50.0,50.0,"",new ArrayList<>());
    definition.setStationary(true);
    EntityInternal newEntity = definition.makeInstanceAt(0.0,0.0);
    assertEquals(0.0,newEntity.nonStationaryProperty().getValue(),0.3);
  }

}
