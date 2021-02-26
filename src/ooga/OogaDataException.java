package ooga;

/**
 * @author sam thompson
 * Represents every exception that occurs in the outward-facing XMLGameDataReader interface.
 * Contains an immutable, readable String field so that the program can display meaningful
 * information to the user when something fails.
 */
public class OogaDataException extends Exception {
  private final String myMessage;

  /**
   * @param message The message that the OogaDataException should carry. Should have information
   *                about what went wrong, like "Couldn't find level file".
   */
  public OogaDataException(String message) {
    myMessage = message;
  }

  /**
   * @return The message that was put into the exception when it was created.
   */
  public String getMessage() {
    return myMessage;
  }
}
