package ooga.data;

import ooga.OogaDataException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author caryshindell, braeden ward
 * this is part of internal API. it houses the methods that are common to any type of XML data reader (e.g. XMLGameDataReader)
 */
public interface XMLDataReader extends DataReader {

  String ENGLISH_PROPERTIES_LOCATION = "ooga/data/resources/english";
  ResourceBundle myDataResources = ResourceBundle.getBundle(ENGLISH_PROPERTIES_LOCATION);
  String fileType = "xml";
  String DEFAULT_USERS_FILE = "data/users";

  /**
   * @return the file type of the data reader sub-interface (e.g. xml)
   */
  @Override
  default String getFileType(){return fileType;}

  @Override

  default Document getDocument(File fXmlFile) throws OogaDataException {
    Document doc;
    try {
      doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fXmlFile);
    } catch (SAXException | ParserConfigurationException | IOException e) {

      throw new OogaDataException(myDataResources.getString("DocumentParseException"));
    }
    return doc;
  }
  /**
   * @param UserName the name of the user whose document we need
   * @return The Document for the user with the given username
   * @throws OogaDataException if the document has no user with that username
   */
  @Override
  default Document getDocForUserName(String UserName) throws OogaDataException {
    for (File userFile : getAllFiles(DEFAULT_USERS_FILE)) {
      File fXmlFile = new File(String.valueOf(userFile));
      Document doc = getDocument(fXmlFile);
      String loadedName = getFirstElementByTag(doc, "UserNameTag", myDataResources.getString("MissingUserNameException"));
      if (loadedName.equals(UserName)) return doc;
    }
    throw new OogaDataException(myDataResources.getString("UserFolderMissingRequestedUsername"));
  }

  default String getFirstElementByTag(Element element, String tagKey, String errorMessage) throws OogaDataException {
    checkKeyExists(element, tagKey, errorMessage);
    return element.getElementsByTagName(myDataResources.getString(tagKey)).item(0).getTextContent();
  }

  default String getFirstElementByTag(Document document, String tagKey, String errorMessage) throws OogaDataException {
    checkKeyExists(document, tagKey, errorMessage);
    return document.getElementsByTagName(myDataResources.getString(tagKey)).item(0).getTextContent();
  }

  default void checkKeyExists(Element element, String tagKey, String errorMessage) throws OogaDataException {
    if(element.getElementsByTagName(myDataResources.getString(tagKey)).getLength() == 0)
      throw new OogaDataException(errorMessage);
  }

  default void checkKeyExists(Document document, String key, String errorMessage) throws OogaDataException {
    if(document.getElementsByTagName(myDataResources.getString(key)).getLength() == 0)
      throw new OogaDataException(errorMessage);
  }

  default String getFirstElementOrDefault(Element element, String tagKey, String defaultValue){
    if(element.getElementsByTagName(myDataResources.getString(tagKey)).getLength() == 0) return defaultValue;
    return element.getElementsByTagName(myDataResources.getString(tagKey)).item(0).getTextContent();
  }
}
