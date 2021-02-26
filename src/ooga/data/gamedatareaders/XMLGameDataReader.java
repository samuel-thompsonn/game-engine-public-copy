package ooga.data.gamedatareaders;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import ooga.OogaDataException;
import ooga.data.Thumbnail;
import ooga.data.XMLDataReader;
import ooga.data.entities.ImageEntityDefinition;
import ooga.data.entities.TextEntity;
import ooga.game.EntityInternal;
import ooga.game.Level;
import ooga.game.OogaLevel;
import ooga.game.behaviors.*;
import ooga.game.behaviors.comparators.VariableComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static java.lang.Class.forName;

/**
 * @author braeden ward, caryshindell, sam thompson
 * XML specific data reader
 */

public class XMLGameDataReader implements GameDataReaderInternal, XMLDataReader {

  /**
   * Returns a list of thumbnails for all the available games.
   * Returns an empty list if there are no files containing thumbnails.
   * @return The list of thumbnails of games.
   */
  @Override
  public List<Thumbnail> getThumbnails() throws OogaDataException{
    ArrayList<Thumbnail> thumbnailList = new ArrayList<>();
    for (File gameFile : getAllFiles(LIBRARY_FILE_PATH)){
      File fXmlFile = new File(String.valueOf(gameFile));
      Document doc = getDocument(fXmlFile);
      String gameTitle = getGameName(doc);
      String gameDescription = getFirstElementByTag(doc, "DescriptionTag", myDataResources.getString("GameDescriptionException"));
      String gameThumbnailImageName = getFirstElementByTag(doc, "ThumbnailTag", myDataResources.getString("ThumbnailException"));
      String fullImagePath = myDataResources.getString("PathPrefix") + gameFile.getParentFile() + SLASH + gameThumbnailImageName;
      Thumbnail newThumbnail = new Thumbnail(fullImagePath, gameTitle, gameDescription);
      thumbnailList.add(newThumbnail);
    }
    return thumbnailList;
  }

  /**
   * @param givenGameName The name of the game
   * @param givenLevelID  The ID of the level the game is asking for
   * @return A fully loaded Level that is runnable by the game and represents the level in the
   * data file.
   * @throws OogaDataException If the given name isn't in the library or the ID is not in the game.
   */
  @Override

  public Level loadNewLevel(String givenGameName, String givenLevelID) throws OogaDataException{
    List<EntityInternal> initialEntities = new ArrayList<>();
    File gameFile = findGame(givenGameName);
    Map<String, ImageEntityDefinition> entityMap = getImageEntityMap(givenGameName);
    String nextLevelID = null;
    Document doc = getDocument(gameFile);
    NodeList levelNodes = doc.getElementsByTagName(myDataResources.getString("LevelTag"));
    for (int i = 0; i < levelNodes.getLength(); i++) {
      Element level = (Element) levelNodes.item(i);
      String levelID = getFirstElementByTag(level, "LevelIDTag", myDataResources.getString("MissingIDException"));
      if(levelID.equals(givenLevelID)){
        nextLevelID = getFirstElementByTag(level, "NextLevelTag", String.format(myDataResources.getString("NextLevelException"), levelID));
        initialEntities = getInitialEntities(entityMap, level);
        break;
      }
    }
    OogaLevel oogaLevel = new OogaLevel(initialEntities, givenLevelID);
    oogaLevel.setNextLevelID(nextLevelID);
    return oogaLevel;
  }

  /**
   * @param gameName: the name of the game for which a map is being requested
   * @return A map of all the entities described in a game file of the given name.
   * It maps from the entities' names to their definitions.
   */
  @Override
  public Map<String, ImageEntityDefinition> getImageEntityMap(String gameName) throws OogaDataException{
    Map<String, ImageEntityDefinition> imageEntityMap = new HashMap<>();
    File gameFile = findGame(gameName);
    try {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(gameFile);
      NodeList entityNodes = doc.getElementsByTagName(myDataResources.getString("ImageEntityTag"));
      for (int i = 0; i < entityNodes.getLength(); i++) {
        Node currentEntity = entityNodes.item(i);
        Element entityElement = (Element) currentEntity;
        String newName = getFirstElementByTag(entityElement, "EntityNameTag", myDataResources.getString("IEDMissingNameException"));
        ImageEntityDefinition newIED = createImageEntityDefinition(entityElement, gameFile.getParentFile().getName());
        newIED.setVariables(getEntityVariables(entityElement));
        imageEntityMap.put(newName, newIED);
      }
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new OogaDataException("");
    }
    return imageEntityMap;
  }

  /**
   * part of the data reader internal api
   * @param givenGameName
   * @return
   * @throws OogaDataException
   */
  @Override
  public List<List<String>> getBasicGameInfo(String givenGameName) throws OogaDataException {
    List<List<String>> basicGameInfo = List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    File gameFile = findGame(givenGameName);
    Document doc = getDocument(gameFile);
    String[] outerTagNames = new String[] {myDataResources.getString("LevelTag"),
            myDataResources.getString("GameVariableTag"), myDataResources.getString("GameVariableTag")};
    String[] innerTagNames = new String[] {"LevelIDTag", "VariableNameTag", "GameVariableStartValueTag"};
    for(int j=0; j<outerTagNames.length; j++){
      NodeList outerList = doc.getElementsByTagName(outerTagNames[j]);
      // add all elements to the corresponding list
      for (int i = 0; i < outerList.getLength(); i++) {
        Node currentNode = outerList.item(i);
        Element nodeAsElement = (Element) currentNode;
        String newItem = getFirstElementByTag(nodeAsElement, innerTagNames[j], myDataResources.getString("GameInfoException"));
        basicGameInfo.get(j).add(newItem);
      }
    }
    return basicGameInfo;
  }

  @Override
  public Level loadSavedLevel(String UserName, String Date) throws OogaDataException {
    String levelFilePath = getLevelFilePath(UserName, Date);
    return loadLevelAtPath(levelFilePath);
  }

  private List<EntityInternal> getInitialEntities(Map<String, ImageEntityDefinition> entityMap, Element level) throws OogaDataException {
    List<EntityInternal> initialEntities = new ArrayList<>(getImageEntities(entityMap, level));
    initialEntities.addAll(getTextEntities(level));
    return initialEntities;
  }

  private List<EntityInternal> getTextEntities(Element level) throws OogaDataException {
    List<EntityInternal> textEntities = new ArrayList<>();
    NodeList textEntityNodes = level.getElementsByTagName("TextEntityInstance");
    for (int j = 0; j < textEntityNodes.getLength(); j++) {
      Node currentEntity = textEntityNodes.item(j);
      Element entityElement = (Element) currentEntity;
      String text = getFirstElementByTag(entityElement, "TextTag", myDataResources.getString("MissingTextException"));
      String font = getFirstElementByTag(entityElement, "FontTag", myDataResources.getString("MissingFontException"));
      String[] parameterNames = new String[] {"XPosTag", "YPosTag","WidthTag", "HeightTag"};
      List<Double> parameterValues = constructEntity(entityElement, text, parameterNames);
      int index = 0;
      EntityInternal entity = new TextEntity(text, font, parameterValues.get(index++), parameterValues.get(index++),
              parameterValues.get(index++),  parameterValues.get(index));
      textEntities.add(setAdditionalEntityState(entityElement, entity, false));
    }
    return textEntities;
  }

  private List<EntityInternal> getImageEntities(Map<String, ImageEntityDefinition> entityMap, Element level) throws OogaDataException {
    List<EntityInternal> imageEntities = new ArrayList<>();
    NodeList imageEntityNodes = level.getElementsByTagName(myDataResources.getString("ImageEntityInstanceTag"));
    // for each, save a copy of the specified instance at the specified place
    for (int j = 0; j < imageEntityNodes.getLength(); j++) {
      Node currentEntity = imageEntityNodes.item(j);
      Element entityElement = (Element) currentEntity;

      String entityName = getFirstElementByTag(entityElement, "EntityNameTag", String.format(myDataResources.getString("EntityNameException"),
              level.getElementsByTagName(myDataResources.getString("LevelIDTag"))));
      if(!entityMap.containsKey(entityName)) throw new OogaDataException(myDataResources.getString("UnknownEntityException") + entityName);
      String[] parameterNames = new String[] {"XPosTag","YPosTag"};
      List<Double> parameterValues = constructEntity(entityElement, entityName, parameterNames);
      int[] rowsColsAndGaps = getRowsColsAndGaps(entityElement);
      ImageEntityDefinition imageEntityDefinition = entityMap.get(entityName);
      double xPos;
      double yPos = parameterValues.get(1);
      for(int row=0; row<rowsColsAndGaps[0]; row++){
        xPos = parameterValues.get(0);
        for(int col=0;col<rowsColsAndGaps[1];col++){
          EntityInternal entity = entityMap.get(entityName).makeInstanceAt(xPos,yPos);
          imageEntities.add(setAdditionalEntityState(entityElement, entity, imageEntityDefinition.getStationary()));
          xPos += imageEntityDefinition.getWidth()+rowsColsAndGaps[2];
        }
        yPos += imageEntityDefinition.getHeight()+rowsColsAndGaps[3];
      }
    }
    return imageEntities;
  }

  private EntityInternal setAdditionalEntityState(Element entityElement, EntityInternal entity, boolean stationary) throws OogaDataException {
    entity.setPropertyVariableDependencies(getEntityVariableDependencies(entityElement));
    entity.setVariables(getEntityVariables(entityElement));
    entity.makeNonStationaryProperty(isStationary(entityElement, stationary));
    return entity;
  }

  /**
   * @param loadFilePath the path to a saved level
   * @return the loaded level based on the save file
   */
  private Level loadLevelAtPath(String loadFilePath) throws OogaDataException {
    //get the name of the game
    File levelFile = new File(loadFilePath);
    Document doc = getDocument(levelFile);
    String gameName = getGameName(doc);
    Map<String, ImageEntityDefinition> imageEntityMap = getImageEntityMap(gameName);
    checkKeyExists(doc, "SaveFileLevelTag","SaveFileMissingLevel");
    Element savedLevelElement = (Element) doc.getElementsByTagName(myDataResources.getString("SaveFileLevelTag")).item(0);
    List<EntityInternal> entities = getInitialEntities(imageEntityMap, savedLevelElement);
    String id = getFirstElementByTag(savedLevelElement,"LevelIDTag", "SaveFileLevelMissingID");
    return new OogaLevel(entities, id);
  }

  /**
   * Looks through the user files and finds the path of the save file by the requested user at the requested date
   * @param UserName: Name of the user who made the save file
   * @param Date: The date this save file was made
   * @return The path to the requested save file
   * @throws OogaDataException If the user file can't be parsed, has no saves, has no usernames, or
   * if the requested username isn't listed in the file or doesn't have a save at the given time
   */
  private String getLevelFilePath(String UserName, String Date) throws OogaDataException {
    Document userDoc = getDocForUserName(UserName);// find where the save file is stored
    checkKeyExists(userDoc, "UserFileSaveDateTag", "UserFileHasNoSaves");
    for(int i=0; i<userDoc.getElementsByTagName(myDataResources.getString("UserFileSaveDateTag")).getLength(); i++){
      String loadedDate = userDoc.getElementsByTagName(myDataResources.getString("UserFileSaveDateTag")).item(i).getTextContent();
      if(!loadedDate.equals(Date)) continue;
      return userDoc.getElementsByTagName(myDataResources.getString("UserFileSaveFilePathTag")).item(i).getTextContent();
    }
    throw new OogaDataException("User has no save at the given date");
  }

  private Map<String, String> getEntityVariables(Element entityElement) throws OogaDataException {
    Map<String, String> variableMap = new HashMap<>();
    NodeList nameNodes = entityElement.getElementsByTagName(myDataResources.getString("VariableNamesTag"));
    NodeList valueNodes = entityElement.getElementsByTagName(myDataResources.getString("VariableValuesTag"));
    if(valueNodes.getLength() > 0 && nameNodes.getLength() > 0) {
      String[] variableNames = nameNodes.item(0).getTextContent().split(" ");
      String[] variableValues = valueNodes.item(0).getTextContent().split(" ");
      if(variableNames.length != variableValues.length){
        throw new OogaDataException(myDataResources.getString("EntityVariablesLengthException"));
      }
      for(int i=0; i<variableNames.length; i++){
        variableMap.put(variableNames[i], variableValues[i]);
      }
    }
    else if(valueNodes.getLength() > 0 || nameNodes.getLength() > 0){
      throw new OogaDataException(myDataResources.getString("EntityVariablesOneMissingException"));
    }
    return variableMap;
  }

  /**
   * gets the rows and columns fields of this entity, each defaults to 1 if not specified. Gaps default to 0.
   * @param entityElement element in the xml of this entity
   * @return array of rows, columns, x gap, y gap
   * @throws OogaDataException if either field is not parsable to an int
   */
  private int[] getRowsColsAndGaps(Element entityElement) throws OogaDataException {
    int[] rowsColsAndGap = new int[]{1, 1, 0, 0};
    String[] keys = new String[]{myDataResources.getString("RowsTag"),
            myDataResources.getString("ColumnsTag"), myDataResources.getString("XGapTag"),
            myDataResources.getString("YGapTag")};
    for(int i=0; i<rowsColsAndGap.length; i++) {
      NodeList nodes = entityElement.getElementsByTagName(keys[i]);
      if (nodes.getLength() > 0) {
        try {
          rowsColsAndGap[i] = Integer.parseInt(nodes.item(0).getTextContent());
        } catch(NumberFormatException e){
          throw new OogaDataException(myDataResources.getString("RowColException"));
        }
      }
    }
    return rowsColsAndGap;
  }

  private List<Double> constructEntity(Element entityElement, String entityName, String[] parameterNames) throws OogaDataException {
    List<Double> parameterValues = new ArrayList<>();
    for(String parameterName : parameterNames){
      try {
        parameterValues.add(Double.parseDouble(getFirstElementByTag(entityElement, parameterName, entityName + myDataResources.getString("EntityParameterException"))));
      } catch (IndexOutOfBoundsException | NumberFormatException e){
        throw new OogaDataException(String.format(myDataResources.getString("EntityFormatException"), entityName));
      }
    }
    return parameterValues;
  }

  private Map<String, String> getEntityVariableDependencies(Element entityElement) throws OogaDataException {
    Map<String, String> dependencyMap = new HashMap<>();
    NodeList dependencyList = entityElement.getElementsByTagName(myDataResources.getString("PropertyVariableDependencyTag"));
    for(int i=0; i<dependencyList.getLength(); i++){
      Element dependencyElement = (Element)dependencyList.item(i);
      String variableName = getFirstElementByTag(dependencyElement, "VariableNameTag2", myDataResources.getString("PVDVariableMissingException"));
      String propertyName = getFirstElementByTag(dependencyElement, "PropertyNameTag", myDataResources.getString("PVDPropertyMissingException"));
      dependencyMap.put(variableName, propertyName);
    }
    return dependencyMap;
  }

  /**
   * Read the .xml file and create a new EntityDefinition as it describes
   * @param entityElement the Node describing the requested Entity
   * @param gameDirectory
   * @return
   */
  private ImageEntityDefinition createImageEntityDefinition(Element entityElement, String gameDirectory) throws OogaDataException {
    String name = getFirstElementByTag(entityElement, "EntityNameTag", String.format(myDataResources.getString("EntityMissingDataException"), "name"));
    double height = Double.parseDouble(getFirstElementByTag(entityElement, "HeightTag", String.format(myDataResources.getString("EntityMissingDataException"), "height")));
    double width = Double.parseDouble(getFirstElementByTag(entityElement, "WidthTag", String.format(myDataResources.getString("EntityMissingDataException"), "width")));
    String imagePath = myDataResources.getString("PathPrefix") + LIBRARY_FILE_PATH + SLASH + gameDirectory + SLASH +
            getFirstElementByTag(entityElement, "ImageTag", String.format(myDataResources.getString("EntityMissingDataException"), "image"));
    boolean stationary = isStationary(entityElement, false);

    List<ConditionalBehavior> behaviors = new ArrayList<>();
    NodeList nodeList = entityElement.getElementsByTagName(myDataResources.getString("BehaviorTag"));
    for (int i=0; i<nodeList.getLength(); i++){
      Element behaviorElement = (Element) nodeList.item(i);
      Map<String, List<String>> inputConditions;
      Map<List<String>, String> requiredCollisionConditions = new HashMap<>();
      Map<List<String>, String> bannedCollisionConditions = new HashMap<>();
      addCollisionConditions(requiredCollisionConditions, behaviorElement.getElementsByTagName(myDataResources.getString("RequiredCollisionConditionTag")), name);
      addCollisionConditions(bannedCollisionConditions, behaviorElement.getElementsByTagName(myDataResources.getString("BannedCollisionConditionTag")), name);
      inputConditions = getInputConditions(behaviorElement.getElementsByTagName(myDataResources.getString("InputConditionTag")),
              myDataResources.getString("KeyTag"), myDataResources.getString("InputRequirementTag"));
      List<VariableCondition> gameVariableConditions = getGameVariableConditions(behaviorElement.getElementsByTagName(
              myDataResources.getString("GameVariableConditionTag")));
      Map<String,List<VariableCondition>> entityVarConditions = getEntityVariableConditions(behaviorElement.getElementsByTagName(
              myDataResources.getString("EntityVariableConditionTag")));
      behaviors.add(new BehaviorInstance(gameVariableConditions,entityVarConditions,inputConditions,
              requiredCollisionConditions,bannedCollisionConditions,getActions(behaviorElement)));
    }

    ImageEntityDefinition imageEntityDefinition = new ImageEntityDefinition(name, height, width, imagePath, behaviors);
    imageEntityDefinition.setStationary(stationary);
    return imageEntityDefinition;
  }

  private boolean isStationary(Element entityElement, boolean defaultValue) {
    return Boolean.parseBoolean(getFirstElementOrDefault(entityElement,"StationaryTag", String.valueOf(defaultValue)));
  }

  private Map<String, List<String>> getInputConditions(NodeList conditionNodes, String keyName, String valueName)
          throws OogaDataException {
    Map<String,List<String>> conditionMap = new HashMap<>();
    for(int j=0; j<conditionNodes.getLength(); j++){
      String name = ((Element)conditionNodes.item(j)).getElementsByTagName(keyName).item(0).getTextContent();
      Element requirementElement = (Element)(conditionNodes.item(j));
      conditionMap.putIfAbsent(name,new ArrayList<>());
      NodeList requirementList = requirementElement.getElementsByTagName(valueName);
      if (requirementList.getLength() == 0) conditionMap.get(name).add(myDataResources.getString("Any"));
      else {
        addKeyRequirements(conditionMap, name, requirementList);
      }
    }
    return conditionMap;
  }

  private void addKeyRequirements(Map<String, List<String>> conditionMap, String name,
      NodeList requirementList) throws OogaDataException {
    for (int k = 0; k < requirementList.getLength(); k ++) {
      String requirement = requirementList.item(k).getTextContent();
      try {
        conditionMap.get(name).add(myDataResources.getString(requirement));
      } catch (Exception e) {
        throw new OogaDataException(String.format(myDataResources.getString("InvalidInputRequirementException"),requirement,name));
      }
    }
  }

  private List<VariableCondition> getGameVariableConditions(NodeList conditions)
          throws OogaDataException {
    List<VariableCondition> variableConditions = new ArrayList<>();
    for (int i = 0; i < conditions.getLength(); i ++) {
      Element variableConditionElement = (Element) conditions.item(i);
      String name = getFirstElementByTag(variableConditionElement, "VariableNameTag2", myDataResources.getString("MissingVariableNameException"));
      String requiredValue = getFirstElementByTag(variableConditionElement, "RequiredValueTag", myDataResources.getString("MissingVariableValueException"));
      VariableComparator comparator = getComparator(variableConditionElement);
      variableConditions.add(new OogaVariableCondition(name,comparator,requiredValue));
    }
    return variableConditions;
  }

  private void addCollisionConditions(Map<List<String>, String> collisionConditionsMap, NodeList collisionConditionNodes,
                                      String entityName) throws OogaDataException {
    for(int i=0; i<collisionConditionNodes.getLength(); i++){
      Element collisionConditionElement = (Element)collisionConditionNodes.item(i);
      String entity1Info = getFirstElementOrDefault(collisionConditionElement, "Entity1Tag", entityName);
      String entity2Info = getFirstElementByTag(collisionConditionElement, "Entity2Tag", myDataResources.getString("MissingEntity2Exception") + entityName);
      String direction = getFirstElementByTag(collisionConditionElement, "DirectionTag", myDataResources.getString("MissingDirectionException") + entityName);
      collisionConditionsMap.put(List.of(entity1Info, entity2Info), direction);
    }
  }

  private List<Action> getActions(Element behaviorElement) throws OogaDataException {
    List<Action> actions = new ArrayList<>();
    String[] actionTypes = new String[]{myDataResources.getString("CollisionDeterminedTag"),
            myDataResources.getString("NameDeterminedTag"), myDataResources.getString("VariableDeterminedTag"),
            myDataResources.getString("IndependentTag")};
    for(String actionType: actionTypes) {
      NodeList actionNodes = behaviorElement.getElementsByTagName(actionType);
      for(int i=0; i<actionNodes.getLength(); i++){
        NodeList argsNodes = ((Element) actionNodes.item(i)).getElementsByTagName(myDataResources.getString("ArgsTag"));
        List<String> args = new ArrayList<>();
        if(argsNodes.getLength() > 0) args = Arrays.asList(argsNodes.item(0).getTextContent().split(" "));
        NodeList effectNodes = behaviorElement.getElementsByTagName(myDataResources.getString("EffectTag"));
        List<Effect> effects = new ArrayList<>();
        for (int j = 0; j < effectNodes.getLength(); j++) {
          String[] effectStrings = effectNodes.item(j).getTextContent().split(" ");
          Effect effect = makeBasicEffect(effectStrings);
          effects.add(effect);
        }
        actions.add(makeAction(actionType, args, effects));
      }
    }
    return actions;
  }

  private Map<String,List<VariableCondition>> getEntityVariableConditions(NodeList variableConditionNodes) throws OogaDataException{
    Map<String,List<VariableCondition>> entityVariableConditions = new HashMap<>();
    for(int j=0; j<variableConditionNodes.getLength(); j++){
      Element variableConditionElement = (Element) variableConditionNodes.item(j);
      String entityInfo = getFirstElementOrDefault(variableConditionElement, "EntityNameOrIDTag", BehaviorInstance.SELF_IDENTIFIER);
      entityVariableConditions.putIfAbsent(entityInfo, new ArrayList<>());
      entityVariableConditions.get(entityInfo).add(getEntityVariableCondition(variableConditionElement));
    }
    return entityVariableConditions;
  }

  private VariableCondition getEntityVariableCondition(Element variableConditionElement) throws OogaDataException {
    String name = getFirstElementByTag(variableConditionElement, "VariableNameTag2", myDataResources.getString("MissingVariableNameException"));
    String requiredValue = getFirstElementByTag(variableConditionElement, "RequiredValueTag", myDataResources.getString("MissingVariableValueException"));
    VariableComparator comparator = getComparator(variableConditionElement);
    return new OogaVariableCondition(name,comparator,requiredValue);
  }

  private VariableComparator getComparator(Element variableConditionElement)
          throws OogaDataException {
    String comparatorClassName = myComparatorsResources.getString("Equals");
    NodeList comparatorType = variableConditionElement.getElementsByTagName(myDataResources.getString("ComparisonTag"));
    if (comparatorType.getLength() != 0) {
      comparatorClassName = myComparatorsResources.getString(comparatorType.item(0).getTextContent());
    }
    try {
      Class<?> cls = forName(PATH_TO_CLASSES + comparatorClassName);
      Constructor<?> cons = cls.getConstructor();
      return (VariableComparator)cons.newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |InstantiationException e ) {
      throw new OogaDataException(String.format(myDataResources.getString("ComparatorException"), comparatorClassName));
    }
  }

  private File findGame(String givenGameName) throws OogaDataException {
    List<File> gameFiles = getAllFiles(LIBRARY_FILE_PATH);
    for(File f : gameFiles) {
      // check if this game file is the correct game file
      // create a new document to parse
      File fXmlFile = new File(String.valueOf(f));
      Document doc = getDocument(fXmlFile);
      String gameTitle = getFirstElementByTag(doc, "GameNameTag", givenGameName + myDataResources.getString("MissingGameException"));
      if (gameTitle.equals(givenGameName)) return f;
    }
    throw new OogaDataException(myDataResources.getString("GameNotFoundException"));
  }

  private String getGameName(Document doc) throws OogaDataException {
    return getFirstElementByTag(doc, "GameNameTag", myDataResources.getString("MissingGameException"));
  }
}
