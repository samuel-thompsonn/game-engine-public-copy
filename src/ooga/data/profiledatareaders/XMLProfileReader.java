package ooga.data.profiledatareaders;

import ooga.OogaDataException;
import ooga.Profile;
import ooga.data.OogaProfile;
import ooga.data.XMLDataReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author doherty guirand, braeden ward, caryshindell
 */
public class XMLProfileReader implements XMLDataReader, ProfileReaderExternal, ProfileReaderInternal {

  /**
   * writes a new profile to data
   * @param newProfileName - the name of the new profile
   * @param photoFile - File containing the new profile photo
   * @throws OogaDataException - if creating a new profile breaks such as file not being found
   */
  public void addNewProfile(String newProfileName, File photoFile) throws OogaDataException {
    for (OogaProfile op : getProfiles()){
      if(newProfileName.equals(op.getProfileName())) throw new OogaDataException("Username already exists");
    }
    try {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      String directory = DEFAULT_USERS_FILE+"/"+newProfileName;
      String filepath = directory+"/"+newProfileName+".xml";
      File file = new File(directory);
      boolean bool = file.mkdir();
      if(bool){
        System.out.println("Directory created successfully");
      }else{
        System.out.println("Sorry could not create specified directory");
      }

      // root element
      Element root = document.createElement("User");
      document.appendChild(root);

      // name element
      Element nameElement = document.createElement("Name");
      nameElement.appendChild(document.createTextNode(newProfileName));
      root.appendChild(nameElement);


      //copy image into the new user's folder
      Path src = Paths.get(photoFile.getPath());
      String imageName = photoFile.getPath().split("/")[photoFile.getPath().split("/").length-1];
      String copiedProfileImage = directory+"/"+imageName; //
      Path dest = Paths.get(copiedProfileImage);
      Files.copy(src, dest);

      //change the directory stored in the given Profile to point to this new copy of the image

      // Image element
      Element imageElement = document.createElement("Image");
      imageElement.appendChild(document.createTextNode(imageName));
      root.appendChild(imageElement);

      // Saves element
      // Saves are initially empty for new users
      Element saveStateElement = document.createElement("SavedGameStates");
      root.appendChild(saveStateElement);

      // create the xml file
      //transform the DOM Object to an XML File
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new File(filepath));

      transformer.transform(domSource, streamResult);

    } catch (ParserConfigurationException | TransformerException | NullPointerException | IllegalArgumentException | IOException pce) {
      throw new OogaDataException("Cannot Create Profile");
    }
  }

  /**
   * @return A list of Profiles according to the data stored in the Users folder. Returns an empty list if there are no
   * existing profiles
   */

  public List<OogaProfile> getProfiles() throws OogaDataException{
    ArrayList<OogaProfile> profileList = new ArrayList<>();
    for (File userFile : getAllFiles(DEFAULT_USERS_FILE)){
      File fXmlFile = new File(String.valueOf(userFile));
      Document doc = getDocument(fXmlFile);
      String userName = getFirstElementByTag(doc, "ProfileNameTag", myDataResources.getString("MissingUserNameException"));
      String userImage = getFirstElementByTag(doc, "ProfileImageTag", myDataResources.getString("MissingUserImageException"));
      NodeList gameNodes = doc.getElementsByTagName("Game");
      Map<String, Integer> statMap = new HashMap<>();
      for(int i = 0; i < gameNodes.getLength(); i++){
        Element game = (Element) gameNodes.item(i);
        String gameName = getFirstElementByTag(game, "GameNameTag", myDataResources.getString("MissingNameException"));
        NodeList highScores = game.getElementsByTagName(myDataResources.getString("HighScoreTag"));
        int highscore = 0;
        if(highScores.getLength() != 0) {
          highscore = Integer.parseInt(highScores.item(0).getTextContent());
        }
        statMap.put(gameName,highscore);
      }
      String fullImagePath = myDataResources.getString("PathPrefix") + userFile.getParentFile() +
              myDataResources.getString("Slash") + userImage;
      OogaProfile newProfile = new OogaProfile(userName,fullImagePath,statMap);
      profileList.add(newProfile);
    }
    return profileList;
  }
}
