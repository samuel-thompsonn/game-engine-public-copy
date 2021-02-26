package ooga.data;

import ooga.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author doherty guirand, Chris Warren
 */
public class OogaProfile implements Profile {
    private String myProfilePhotoPath;
    private String myName;
    private Map<String, Integer> myHighestScores;

    /**
     * An oogaProfile is data's version of a profile. It can store and update statistics, a profile name and photo.
     * This constructor is used when there is no exisiting stats for this profile. Such as in view when creating a new profile.
     * @param profilename - string to be used as profile name
     * @param imagePath - string to that represents path to a photo
     */
    public OogaProfile(String profilename, String imagePath){
        myName = profilename;
        myProfilePhotoPath = imagePath;
        myHighestScores = new HashMap<>();
    }

    /**
     * This constructor Creates an oogaProfile with existing stats such as oogaDataReader when creating oogaProfiles from data
     * @param profileName - String to be used as profile name
     * @param imagePath - String that is the path to a photo
     * @param stats - a Map that stores the user's stats
     */
    public OogaProfile(String profileName, String imagePath, Map<String, Integer> stats){
        this(profileName,imagePath);
        myHighestScores = stats;
    }

    /**
     * sets the profilephotopath to new path
     * @param photoPath A String that represents the File Path to make an ImageView
     */
    @Override
    public void setProfilePhoto(String photoPath){
        myProfilePhotoPath = photoPath;
    }

    /**
     * sets the profile name to string given
     * @param name A String representing the new Profile name
     */
    @Override
    public void setProfileName(String name) {
        myName = name;
    }

    /**
     * @param stats A Map that contains a String as the Value and Integer as the Key.
     *   The String is the Name of the Game. The Integer is the player's highest score.
     */
    @Override
    public void setStats(Map<String,Integer> stats) {
        myHighestScores = stats;
    }

    /**
     *
     * @return Map that contains games names and their highest scores
     */
    @Override
    public Map<String,Integer> getStats() {
        return myHighestScores;
    }

    /**
     * @param gameName A String representing the name of the game
     * @param highScore An Integer representing the new highscore
     */
    @Override
    public void updateStats(String gameName, Integer highScore) {
        myHighestScores.putIfAbsent(gameName,0);
        myHighestScores.put(gameName,highScore);
    }

    /**
     * @param gameName A String representing the the game of which you want the player's score
     * @return the highest score of specified game
     */
    @Override
    public int getGameStats(String gameName) {
        return myHighestScores.get(gameName);
    }

    /**
     * @return returns profile's name
     */
    @Override
    public String getProfileName() {
        return myName;
    }

    /*
     * @returns profile's photo path
     */
    @Override
    public  String getProfilePhotoPath(){
        return myProfilePhotoPath;
    }

}
