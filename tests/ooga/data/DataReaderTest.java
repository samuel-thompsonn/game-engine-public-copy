
package ooga.data;

import ooga.Entity;
import ooga.OogaDataException;
import ooga.data.entities.ImageEntity;
import ooga.data.entities.ImageEntityDefinition;
import ooga.data.entities.OogaEntity;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.GameRecorderExternal;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.data.profiledatareaders.XMLProfileReader;
import ooga.game.EntityInternal;
import ooga.game.Level;
import ooga.data.OogaProfile;
import ooga.game.OogaLevel;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSException;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a class that just tests XMLGameDataReader and makes sure it is working correctly.
 * @author braedenward
 */
public class DataReaderTest {

//    private XMLGameDataReader testDataReader = new XMLGameDataReader() {
//    };
//    private XMLProfileReader profileDataReader = new XMLProfileReader() {
//    };
//    private List<String> GAME_NAMES = new ArrayList<>(List.of(
//        "Doodle Jump", "Chrome Dino", "Super Mario Bros", "Fireboy and Watergirl", "Flappy Bird",
//        "VVVVVV"));
//
//    @Test
//    public void testGetThumbnails() throws OogaDataException {
//        List<Thumbnail> thumbnailList = testDataReader.getThumbnails();
//        for (Thumbnail t : thumbnailList) {
//            System.out.println(String
//                .format("Title: %s \nDescription: %s \nImage: %s\n", t.getTitle(),
//                    t.getDescription(), t.getImageFile()));
//        }
//    }
//
//    @Test
//    public void testGetBasicGameInfo() {
//        for (String gameName : GAME_NAMES) {
//            List<List<String>> stringList = null;
//            try {
//                stringList = testDataReader.getBasicGameInfo(gameName);
//            } catch (OogaDataException e) {
//                // TODO: Fix this, Braeden
//                e.printStackTrace();
//                fail();
//            }
//            System.out
//                .println("List of Level IDs recieved for " + gameName + ": " + stringList + "\n");
//        }
//    }
//
//    @Test
//    public void testLoadNewLevel() {
//        boolean testPassed = true;
//        // for every game load and display every level
//        for (String gameName : GAME_NAMES) {
//            System.out.println("Game: " + gameName);
//            List<String> idList = null;
//            try {
//                idList = testDataReader.getLevelIDs(gameName);
//            } catch (OogaDataException e) {
//                // if there's an error finding the game, recognize it and move on to the next game
//                e.printStackTrace();
//                testPassed = false;
//                continue;
//            }
//
//            // for every level, load it and print every entity
//            for (String id : idList) {
//                System.out.println("\tLevel: " + id);
//                Level createdLevel = null;
//                try {
//                    createdLevel = testDataReader.loadNewLevel(gameName, id);
//                } catch (OogaDataException e) {
//                    // if there's an error loading the level, recognize it and move on to the next level
//                    e.printStackTrace();
//                    testPassed = false;
//                    continue;
//                }
//                // for every entity, neatly display its information (in a very Clue-esque way)
//                for (Entity e : createdLevel.getEntities()) {
//                    System.out
//                        .println(String.format("\t\tEntity %s at position %s with variables %s",
//                            e.getName(), e.getPosition().toString(), e.getVariables().toString()));
//                }
//                System.out.print("\n");
//            }
//            System.out.print("\n");
//        }
//        assertTrue(testPassed);
//    }
//
//    @Test
//
//    public void testGetGameFilePaths() {
//       // List<String> pathList = testDataReader.getGameFilePaths();
//       // System.out.println(pathList);
//    }
//
//    @Test
//
//    public void testGetEntityMap() {
//        for (String gameName : GAME_NAMES) {
//            Map<String, ImageEntityDefinition> retMap = null;
//            try {
//                //TODO: Add getEntityMap
//                retMap = testDataReader.getImageEntityMap(gameName);
//            } catch (OogaDataException e) {
//                e.printStackTrace();
//                fail();
//            }
//            for (String key : retMap.keySet()) {
//                Entity e = retMap.get(key).makeInstanceAt(0.0, 0.0);
//                System.out.print("Name: " + key + "   ");
//                System.out.print("Height: " + e.getHeight() + "   ");
//                System.out.println("Width: " + e.getWidth());
//            }
//            System.out.println(retMap);
//        }
//    }
//
//    @Test
//    public void testAddNewProfile() throws OogaDataException {
//        OogaProfile testProfile = new OogaProfile("Doug Rattman", "data/users/Rattman.jpeg");
//        profileDataReader.addNewProfile(testProfile);
//    }
//
//    @Test
//    public void testGetProfiles() {
////        List<OogaProfile>  profiles = testDataReader.getProfiles();
////        System.out.println(profiles.size() + " Profiles:");
////        for (OogaProfile profile : profiles){
////package ooga.data;
////
////import ooga.Entity;
////import ooga.OogaDataException;
////import ooga.view.OggaProfile;
////import org.junit.jupiter.api.Test;
////
////import java.util.ArrayList;
////import java.util.List;
////import java.util.Map;
////
////import static org.junit.jupiter.api.Assertions.*;
////
/////**
//// * This is a class that just tests XMLGameDataReader and makes sure it is working correctly.
//// * @author braedenward
//// */
////public class DataReaderTest {
////    private OogaDataReader testDataReader = new OogaDataReader();
////    //private String GAME_NAME = "Chrome Dino";
////    private String GAME_NAME = "Super Mario Bros";
//////    private String GAME_NAME = "Fireboy and Watergirl";
////
////    private ArrayList<String> ID_LIST  = new ArrayList<>(List.of("1"));
////
////    @Test
////    public void testGetThumbnails(){
////        List<Thumbnail> thumbnailList = testDataReader.getThumbnails();
////        for (Thumbnail t : thumbnailList) {
////            System.out.println(String.format("Title: %s \nDescription: %s \nImage: %s\n", t.getTitle(), t.getDescription(), t.getImageFile()));
////        }
////    }
////    @Test
////    public void testGetBasicGameInfo(){
////        List<List<String>> stringList = null;
////        try {
////            stringList = testDataReader.getBasicGameInfo(GAME_NAME);
////        } catch (OogaDataException e) {
////            // TODO: Fix this, Braeden
////            System.out.println("Test Failed");
////            e.printStackTrace();
////        }
////        System.out.println("List of Level IDs recieved for " + GAME_NAME + ": " + stringList + "\n");
////        assertEquals(ID_LIST, stringList);
////    }
////
////    @Test
////    public void testLoadLevel(){
////        boolean testPassed = true;
////        for(String id : ID_LIST){
////            try {
////                testDataReader.loadLevel(GAME_NAME, id);
////            } catch (OogaDataException e) {
////                // TODO: Fix this, Braeden
////                testPassed = false;
////                e.printStackTrace();
////            }
////        }
////        assertTrue(testPassed);
////    }
////
////    @Test
////    public void testGetGameFilePaths(){
////        List<String> pathList = testDataReader.getGameFilePaths();
////        System.out.println(pathList);
////    }
////
////    @Test
////    public void testGetEntityMap(){
////        Map<String, ImageEntityDefinition> retMap = null;
////        try {
////            //TODO: Add getEntityMap
////            retMap = testDataReader.getImageEntityMap(GAME_NAME);
//////            retMap = null;
////        } catch (OogaDataException e) {
////            e.printStackTrace();
////            fail();
////        }
////        for(String key : retMap.keySet()){
////            Entity e = retMap.get(key).makeInstanceAt(0.0,0.0);
////            System.out.print("Name: "+ key + "   ");
////            System.out.print("Height: " + e.getHeight()+"   ");
////            System.out.println("Width: " + e.getWidth());
////        }
////        System.out.println(retMap);
////    }
////
////    @Test
////    public void testGetProfiles(){
////        List<OggaProfile>  profiles = testDataReader.getProfiles();
////        System.out.println(profiles.size() + " Profiles:");
////        for (OggaProfile profile : profiles){
//
////            assertNotNull(profile.getProfileName(), "Profile name is null");
////            assertNotNull(profile.getProfilePhotoPath(), "Profile photo is null");
////            System.out.println(String.format("Name %s  Image: %s", profile.getProfileName(), profile.getProfilePhotoPath()));
////        }
//
//    }
//
//    @Test
//    public void testLoadSavedLevel() throws OogaDataException {
//        String testName = "Braeden";
//        String testDate = "4/24/20";
////        Level returnedLevel = testDataReader.loadSavedLevel(testName, testDate);
////        System.out.println(String.format("Level: %s", returnedLevel.getLevelId()));
////        for (Entity e : returnedLevel.getEntities()){
////            System.out.println(String.format("\tEntity %s at position %s with variables %s",
////                    e.getName(), e.getPosition().toString(), e.getVariables().toString()));
////        }
//    }
//
//    @Test
//    public void testGetGameSaves() {
//        boolean testPassed = true;
//        for (String game : GAME_NAMES) {
//            System.out.println("Game: " + game);
////            try{
//            String testName = "Braeden";
////                List<List<String>> result = testDataReader.getGameSaves(testName, game);
////                System.out.println(result);
////            }catch (OogaDataException e){
////                e.printStackTrace();
////                testPassed = false;
////            }
////            }
//            assertTrue(testPassed);
//        }
//    }

//    @Test
//    public void testGetGameSaves() {
//        boolean testPassed = true;
//        for (String game : GAME_NAMES) {
//            System.out.println("Game: " + game);
////            try{
//            String testName = "Braeden";
////                List<List<String>> result = testDataReader.getGameSaves(testName, game);
////                System.out.println(result);
////            }catch (OogaDataException e){
////                e.printStackTrace();
////                testPassed = false;
////            }
////            }
//            assertTrue(testPassed);
//        }
//    }

    @Test
    public void testSaveLevel(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        System.out.println(formatter.format(cal.getTime()));

        List<EntityInternal> entityList = new ArrayList<>();
        EntityInternal entity = new ImageEntity("testName", "testImage", 0.0, 0.1, 400, 500){};
        Map<String, String> entityVars = new HashMap<>();
        entityVars.put("And he's singin'", "wayo wayo wayo wayo");
        entityVars.put("ooAAAAooo", "WAAyo Wayo");
        entity.setVariables(entityVars);

        entityList.add(entity);

        Level testLevel = new OogaLevel(entityList, "What's up Gamers");
        XMLGameRecorder testGameRecorder = new XMLGameRecorder();
        Map<String, String > vars = new HashMap<>();
        vars.put("Lives", "2");
        vars.put("Deaths", "30");
        testGameRecorder.saveLevel("Doherty", "Super Mario Bros", testLevel, vars);
    }
}

