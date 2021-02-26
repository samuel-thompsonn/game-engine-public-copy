package ooga.data;

import ooga.OogaDataException;
import ooga.game.Game;

import java.util.List;

/**
 * Handles saving the state of currently active games, or restoring save games.
 * Has methods that are also under the XMLGameDataReader title, since this might be a part of the
 * internal data API.
 */
public interface GameRecorder {
  /**
   * Saves the current state of the game so it can easily be loaded from where the player
   * left off.
   * @param filePath The filepath at which to save the game.
   * @throws OogaDataException if the filepath is invalid, or if no game is loaded.
   */
  void saveGameState(String filePath) throws OogaDataException;

  /**
   * Loads a file that contains a saved game state, and returns a runnable game that picks up
   * where it left off.
   * @param filePath The filepath of the file to load.
   * @return A runnable Game that matches with how the game was when saved.
   * @throws OogaDataException if there is no valid file at the given filepath.
   */
  Game loadGameState(String filePath) throws OogaDataException;

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
