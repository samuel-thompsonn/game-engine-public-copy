package ooga.data.gamerecorders;

import ooga.Entity;
import ooga.OogaDataException;
import ooga.data.XMLDataReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import ooga.game.Level;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author braeden ward, cary shindell, doherty guirand, sam thompson
 */
public class XMLGameRecorder implements XMLDataReader, GameRecorderInternal {

  @Override
  public List<List<String>> getGameSaves(String userName, String gameName) throws OogaDataException {
    Document doc = getDocForUserName(userName);
    List<List<String>> gameSaveInfo = new ArrayList<>();
    for (int i = 0; i < doc.getElementsByTagName(myDataResources.getString("UserFileGameTag")).getLength(); i++) {
      Element gameElement = (Element) doc.getElementsByTagName(myDataResources.getString("UserFileGameTag")).item(i);
      String foundGameName = gameElement.getElementsByTagName(myDataResources.getString("UserFileGameNameTag")).item(0).getTextContent();
      if (foundGameName.equals(gameName)) {
        String date = getFirstElementByTag(gameElement, "UserFileSaveDateTag", myDataResources.getString("UserFileSaveMissingDates"));
        String loadFilePath = getFirstElementByTag(gameElement, "UserFileSaveFilePathTag", myDataResources.getString("UserFileSaveMissingFilePaths"));
        File levelFile = new File(loadFilePath);
        Document levelDoc = getDocument(levelFile);
        checkKeyExists(levelDoc, "SaveFileLevelTag", myDataResources.getString("SaveFileMissingLevel"));
        Element savedLevelElement = (Element) levelDoc.getElementsByTagName(myDataResources.getString("SaveFileLevelTag")).item(0);
        String id = getFirstElementByTag(savedLevelElement, "LevelIDTag", myDataResources.getString("SaveFileLevelMissingID"));
        gameSaveInfo.add(List.of(id, date));
      }
    }
    return gameSaveInfo;
  }

  @Override
  public void saveLevel(String userName, String gameName, Level level, Map<String, String> variables) {
    String userDirectory = DEFAULT_USERS_FILE+"/"+userName;
    File userDirectoryFile = new File(userDirectory);
    String savesDirectory = DEFAULT_USERS_FILE+"/"+userName + "/saves";
    if(!List.of(userDirectoryFile.listFiles()).contains(new File(savesDirectory))){
      File savesDirectoryFile = new File(savesDirectory);
      savesDirectoryFile.mkdir();
    }
    String saveFilepath = savesDirectory + "/"+ gameName + System.currentTimeMillis()+"-save.xml";
    // create a new .xml file with name gameName + "-save.xml"
    try {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element root = document.createElement("Save");
      document.appendChild(root);
      Element nameElement = document.createElement("Name");
      nameElement.appendChild(document.createTextNode(gameName));
      root.appendChild(nameElement);
      Element variablesElement = document.createElement("Variables");
      root.appendChild(variablesElement);
      //add all the current values of variables
      for (String variableName : variables.keySet()){
        Element singleVariable = document.createElement("Variable");
        variablesElement.appendChild(singleVariable);
        Element varNameElement = document.createElement("Name");
        varNameElement.appendChild(document.createTextNode(variableName));
        Element varValueElement = document.createElement("StartValue");
        varValueElement.appendChild(document.createTextNode(variables.get(variableName)));
        singleVariable.appendChild(varNameElement);
        singleVariable.appendChild(varValueElement);
      }
      Element currentLevelElement = document.createElement("CurrentLevel");
      root.appendChild(currentLevelElement);
      Element IDElement = document.createElement("ID");
      currentLevelElement.appendChild(IDElement);
      IDElement.appendChild(document.createTextNode(level.getLevelId()));
      Element ImageInstancesElement = document.createElement("ImageEntityInstances");
      currentLevelElement.appendChild(ImageInstancesElement);
      for(Entity e : level.getEntities()){
        Element singleImageInstanceElement = document.createElement("ImageEntityInstance");
        ImageInstancesElement.appendChild(singleImageInstanceElement);
        Element entNameElement = document.createElement("Name");
        entNameElement.appendChild(document.createTextNode(e.getName()));
        Element entXPosElement = document.createElement("XPos");
        entXPosElement.appendChild(document.createTextNode(e.getPosition().get(0).toString()));
        Element entYPosElement = document.createElement("YPos");
        entYPosElement.appendChild(document.createTextNode(e.getPosition().get(1).toString()));
        singleImageInstanceElement.appendChild(entNameElement);
        singleImageInstanceElement.appendChild(entXPosElement);
        singleImageInstanceElement.appendChild(entYPosElement);
        Element varNamesElement = document.createElement("VariableNames");
        singleImageInstanceElement.appendChild(varNamesElement);
        Element varValuesElement = document.createElement("VariableValues");
        singleImageInstanceElement.appendChild(varValuesElement);
        for(String var : e.getVariables().keySet()){
          varNamesElement.setTextContent(varNamesElement.getTextContent() + " " + var);
          varValuesElement.setTextContent(varValuesElement.getTextContent() + " " + e.getVariables().get(var));
        }
      }
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new File(saveFilepath));
      transformer.transform(domSource, streamResult);
    } catch (ParserConfigurationException | TransformerException ignored) {
      //Don't save the level if it can't be made into a valid file.
    }
    saveUserFile(userName, gameName, saveFilepath);
  }

  private void saveUserFile(String userName, String gameName, String filepath) {
    // go to user file
    try {
      Document userDoc = getDocForUserName(userName);
      // go to saved game states
      Element saveGameStatesElement = (Element) userDoc.getElementsByTagName("SavedGameStates").item(0);
      // create a new save at that location with the the correct information
      Element newGameElement = userDoc.createElement("Game");
      saveGameStatesElement.appendChild(newGameElement);
      Element newGameNameElement = userDoc.createElement("Name");
      newGameNameElement.setTextContent(gameName);
      newGameElement.appendChild(newGameNameElement);
      Element newGameSaveElement = userDoc.createElement("Save");
      newGameElement.appendChild(newGameSaveElement);
      Element newSaveDateElement = userDoc.createElement("Date");
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy  HH:mm:ss");
      newSaveDateElement.setTextContent(formatter.format(cal.getTime()));
      newGameSaveElement.appendChild(newSaveDateElement);
      Element newSaveFileElement = userDoc.createElement("StateFilePath");
      newSaveFileElement.setTextContent(filepath);
      newGameSaveElement.appendChild(newSaveFileElement);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(userDoc);
      StreamResult streamResult = new StreamResult(new File("data/users/"+userName+"/"+userName+".xml"));
      transformer.transform(domSource, streamResult);
    } catch (OogaDataException | TransformerConfigurationException ignored) {
      //If we can't make a document out of it, don't make the document
    } catch (TransformerException e) {
      e.printStackTrace();
    }
  }

  /**
   * Looks through the user files and finds the path of the save file by the requested user at the requested date
   *
   * @param UserName : Name of the user who made the save file
   *                 if the requested username isn't listed in the file or doesn't have a save at the given time
   * @param Date
   */
  @Override
  public String getLevelFilePath(String UserName, String Date) {return null;}
}
