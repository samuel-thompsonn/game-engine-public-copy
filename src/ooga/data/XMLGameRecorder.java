package ooga.data;

import ooga.OogaDataException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface XMLGameRecorder extends GameRecorder, XMLDataReader, GameRecorderInternal {

  String DEFAULT_USERS_FILE = "data/users";

  @Override
  default List<List<String>> getGameSaves(String userName, String gameName) throws OogaDataException {
    Document doc = getDocForUserName(userName);
    List<List<String>> gameSaveInfo = new ArrayList<>();
    // loop through all of the games saves in the user file and find the right games
    for (int i = 0; i < doc.getElementsByTagName(myDataResources.getString("UserFileGameTag")).getLength(); i++) {
      Element gameElement = (Element) doc.getElementsByTagName(myDataResources.getString("UserFileGameTag")).item(i);
      try {
        checkKeyExists(gameElement, myDataResources.getString("UserFileGameNameTag"), "");
      } catch (OogaDataException e) {
        // meant to be empty
        // skip games that don't have names
        continue;
      }
      String foundGameName = gameElement.getElementsByTagName(myDataResources.getString("UserFileGameNameTag")).item(0).getTextContent();
      if (foundGameName.equals(gameName)) {
        // add the Level ID and date to the list
        checkKeyExists(gameElement, myDataResources.getString("UserFileSaveDateTag"), myDataResources.getString("UserFileSaveMissingDates"));
        checkKeyExists(gameElement, myDataResources.getString("UserFileSaveFilePathTag"), myDataResources.getString("UserFileSaveMissingFilePaths"));

        String date = gameElement.getElementsByTagName(myDataResources.getString("UserFileSaveDateTag")).item(0).getTextContent();

        // get the level ID from the level file
        String loadFilePath = gameElement.getElementsByTagName(myDataResources.getString(myDataResources.getString("UserFileSaveFilePathTag"))).item(0).getTextContent();
        File levelFile = new File(loadFilePath);
        Document levelDoc = getDocument(levelFile, myDataResources.getString("DocumentParseException"));

        checkKeyExists(levelDoc, myDataResources.getString("SaveFileLevelTag"), myDataResources.getString("SaveFileMissingLevel"));
        Element savedLevelElement = (Element) levelDoc.getElementsByTagName(myDataResources.getString("SaveFileLevelTag")).item(0);

        checkKeyExists(savedLevelElement, myDataResources.getString("LevelIDTag"), myDataResources.getString("SaveFileLevelMissingID"));
        String id = savedLevelElement.getElementsByTagName(myDataResources.getString("LevelIDTag")).item(0).getTextContent();

        ArrayList<String> entry = new ArrayList<>(List.of(id, date));
        gameSaveInfo.add(entry);
      }
    }
    return gameSaveInfo;
  }

  /**
   * @param UserName the name of the user whose document we need
   * @return The Document for the user with the given username
   * @throws OogaDataException if the document has no user with that username
   */
  @Override
  default Document getDocForUserName(String UserName) throws OogaDataException {
    for (File userFile : getAllFiles(DEFAULT_USERS_FILE)) {
      try{
        // create a new document to parse
        File fXmlFile = new File(String.valueOf(userFile));
        Document doc = getDocument(fXmlFile, myDataResources.getString("DocumentParseException"));
        // get the name at the top of the file
        checkKeyExists(doc, "Name", "User file missing username");
        String loadedName = doc.getElementsByTagName("Name").item(0).getTextContent();

        if (loadedName.equals(UserName)) return doc;
      } catch (OogaDataException e) {
        // this field is meant to be empty
        // right now we're just looking for a user,
        // it's not a problem if one of the other documents is improperly formatted
      }
    }
    throw new OogaDataException(myDataResources.getString("UserFolderMissingRequestedUsername"));
  }

  Document getDocument(File fXmlFile, String documentParseException);
}
