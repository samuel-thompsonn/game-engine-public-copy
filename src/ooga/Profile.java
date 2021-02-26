package ooga;

import java.util.Map;

/**
 * @author doherty guirand
 *
 */
public interface Profile {

    //Can change stats to be another structure that hold more data points such as (kills,deaths,coins). Probably create a stats object

    /**
     * Sets the users profile photo
     * @param photoPath A String that represents the File Path to make an ImageView
     */
    void setProfilePhoto(String photoPath);

    /**
     * Changes the users Profile Name.
     * @param name A String representing the new Profile name
     */
    void setProfileName(String name);

    /**
     * Hard resets the players statistics
     * @param stats A Map that contains a String as the Value and Integer as the Key. The String is the Name of the Game. The Integer is the player's highest score.
     */
    void setStats(Map<String,Integer> stats);

    /**
     * Gets the players stats
     * @return A Map of type String,Integer that maps the name of the game to that player's highest score
     */
    Map<String, Integer> getStats();

    /**
     * Updates the statistics of a specific game to be reflected in player's profile
     * @param gameName A String representing the name of the game
     * @param highScore An Integer representing the new highscore
     */
    void updateStats(String gameName, Integer highScore);

    /**
     * Returns the player's highest score of the specified game
     * @param gameName A String representing the the game of which you want the player's score
     * @return An int representing that player's highest score
     */
    int getGameStats(String gameName);

    /**
     * Returns The player's profile name
     * @return A String
     */
    String getProfileName();

    /**
     *
     * @return the path to the profile photo
     */
    String getProfilePhotoPath();


}
