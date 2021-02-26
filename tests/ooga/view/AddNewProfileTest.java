package ooga.view;


import ooga.OogaDataException;
import ooga.data.profiledatareaders.XMLProfileReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class AddNewProfileTest {
    /**
     * @author doherty guirand
     * @purpose to test if we are able to add new profiles without throwing an oogaDataException
     * as long as userName is unique
     */
    XMLProfileReader profileReader = new XMLProfileReader();
    String exampleName = "Test" + System.currentTimeMillis();
    File photoFile = new File("src/ooga/view/Resources/alien.jpg");

    @Test
    public void testAddingUniqueProfile() {
        try {
            profileReader.addNewProfile(exampleName, photoFile);
        } catch (OogaDataException ex) {
            fail("Should not have thrown an exception");
        }
    }
}
