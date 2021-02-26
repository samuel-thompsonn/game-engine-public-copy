package ooga.game;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import ooga.OogaDataException;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.game.collisiondetection.DirectionalCollisionDetector;
import ooga.game.controls.KeyboardControls;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class getCameraShiftValuesTest {
    @Test
    public void CameraTest() throws OogaDataException {
        OogaGame myGame = new OogaGame("VVVVVV", new XMLGameDataReader(), new DirectionalCollisionDetector(), new KeyboardControls("ooga/game/controls/inputs/keyboard"), "test", new XMLGameRecorder(), "test");
        double expectedXCameraShift = 10;
        double expectedYCameraShift = 0;
        myGame.setCameraShiftProperties(List.of(new SimpleDoubleProperty(), new SimpleDoubleProperty()));
        myGame.setCameraShiftValues(expectedXCameraShift,expectedYCameraShift);
        assertEquals(expectedXCameraShift, myGame.getCameraShiftValues().get(0), 0);
        assertEquals(expectedYCameraShift, myGame.getCameraShiftValues().get(1), 0);
    }
}
