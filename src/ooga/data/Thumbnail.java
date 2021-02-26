package ooga.data;

/**
 * @author braeden ward
 * This class encapsulates the initial representation of a Game on the home screen. It is everything the XMLGameDataReader needs
 * to give initially including but not limited to the thumbnail image, the game's title, and the game's description.
 */
public class Thumbnail {

    private final String imageFile;
    private final String title;
    private final String description;

    public Thumbnail(String imageFileName, String title, String description){
        imageFile = imageFileName;
        this.title = title;
        this.description = description;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
