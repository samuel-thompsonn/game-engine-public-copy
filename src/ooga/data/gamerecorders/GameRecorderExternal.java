package ooga.data.gamerecorders;

import ooga.OogaDataException;
import ooga.game.Level;

import java.util.List;
import java.util.Map;

/**
 * @author braeden ward, caryshindell
 * Handles saving the state of currently active games, or restoring save games.
 *
 */
public interface GameRecorderExternal {
  /**
   * Saves the current state of the game so it can easily be loaded from where the player
   * left off.
   * @param gameName the name of the game the level applies to
   * @param level The filepath at which to save the game.
   * @param userName the name of the user asking for the save
   */
  void saveLevel(String userName, String gameName, Level level, Map<String, String> variables);

  /**
   * returns information about all of the saves for a given game and user name
   * @param userName the name of the user who made the save file
   * @param gameName the name of the game for which the save was made
   * @return a list of Level ID and date pairs i.e.:
   * {{"Level 1", "1/1/20 12:30:45"},
   * {"Level 3", "1/1/25 1:13:65"}}
   * returns an empty list if the user has no saves for this game
   * @throws OogaDataException if the given username isn't listed as a user in the users file
   */
  List<List<String>> getGameSaves(String userName, String gameName) throws OogaDataException;

}
