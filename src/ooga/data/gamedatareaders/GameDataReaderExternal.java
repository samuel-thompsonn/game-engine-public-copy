package ooga.data.gamedatareaders;

import ooga.OogaDataException;
import ooga.data.Thumbnail;
import ooga.data.entities.ImageEntityDefinition;
import ooga.game.Level;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author sam thompson, braeden ward, caryshindell
 * Handles the interaction with game data files, including interpretation and writing.
 *  Forms the external side of game data interaction so that the rest of the program doesn't
 *  need to know how we're storing games.
 */
public interface GameDataReaderExternal {

  String PATH_TO_CLASSES = "ooga.game.behaviors.";
  String LIBRARY_FILE_PATH = "data/games-library";   //the path to the folder in which is held every folder for every game that will be displayed and run
  String ENGLISH_PROPERTIES_LOCATION = "ooga/data/resources/english";
  String EFFECTS_PROPERTIES_LOCATION = "ooga/data/resources/effects";
  String ACTIONS_PROPERTIES_LOCATION = "ooga/data/resources/actions";
  String COMPARATORS_PROPERTIES_LOCATION = "ooga/data/resources/comparators";
  ResourceBundle myEffectsResources = ResourceBundle.getBundle(EFFECTS_PROPERTIES_LOCATION);
  ResourceBundle myActionsResources = ResourceBundle.getBundle(ACTIONS_PROPERTIES_LOCATION);
  ResourceBundle myComparatorsResources = ResourceBundle.getBundle(COMPARATORS_PROPERTIES_LOCATION);
  ResourceBundle myEnglishResources = ResourceBundle.getBundle(ENGLISH_PROPERTIES_LOCATION);
  String SLASH = myEnglishResources.getString("Slash");

  /**
   * Returns a list of thumbnails for all the available games.
   * Returns an empty list if there are no files containing thumbnails.
   *
   * @return The list of thumbnails of games.
   */
  List<Thumbnail> getThumbnails() throws OogaDataException;

  /**
   * Give a Game a list of level ID's in the order that they're listed in the .xml files
   * @param gameName the name of the Game at the start of the .xml file
   * @return A list of strings, the ID's of the Level written in the game file;
   * @throws OogaDataException if the String given isn't a directory or the cooresponding file is not properly formatted
   */
  List<String> getLevelIDs(String gameName) throws OogaDataException;

  /**
   * @param gameName the name of the Game at the start of the .xml file
   * @return A map of variable names [Strings] to their initial values [Strings]
   * @throws OogaDataException if the String given isn't a directory or the cooresponding file is not properly formatted
   */
  Map<String, String> getVariableMap(String gameName) throws OogaDataException;

  /**
   * @param givenGameName The name of the game
   * @param givenLevelID  The ID of the level the game is asking for
   * @return A fully loaded Level that is runnable by the game and represents the level in the
   * data file.
   * @throws OogaDataException If the given name isn't in the library or the ID is not in the game.
   */
  Level loadNewLevel(String givenGameName, String givenLevelID) throws OogaDataException;

  /**
   * @param gameName: the name of the game for which a map is being requested
   * @return A map of all the entities described in a game file of the given name.
   * It maps from the entities' names to their definitions.
   */
  Map<String, ImageEntityDefinition> getImageEntityMap(String gameName) throws OogaDataException;
  Level loadSavedLevel(String UserName, String Date) throws OogaDataException;
}
