package ooga.view.menus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.OogaDataException;
import ooga.data.Thumbnail;
import ooga.view.ViewProfile;


import java.util.List;
import java.util.ResourceBundle;

/**
 * @author chris warren, doherty guirand, caryshindell
 */
public class GameMenu extends ScrollMenu{

  public static final String PROFILE_VIEW_TITLE = "ProfileViewTitle";
  private final StringProperty optionSelected = new SimpleStringProperty();
  private final String ERROR_MESSAGE = languageResources.getString("ThumbnailError");
  private static final String GAME_MENU_TITLE_KEY = "GameMenuTitle";

  /**
   * A screen that allows the user to select a game to be played
   * @param profile - ViewProfile of profile selected by user
   * @param backButton - button that allows user to go back to previous screen
   */
  public GameMenu(ResourceBundle languageResources, ViewProfile profile, Node backButton){
    super(languageResources, GAME_MENU_TITLE_KEY);
    try {
      List<Thumbnail> thumbnails = myGameDataReader.getThumbnails();
      addImages(thumbnails);
    } catch (OogaDataException e){
      showError(e.getMessage());
    }
    this.setCenter(addProfileDataAndBackButton(profile,backButton));
  }

  /**
   * User is able to choose a game, Visualizer listens to this property to know which game as been selected
   * @return String of name of game selected
   */
  public StringProperty selectedProperty() {
    return optionSelected;
  }


  /**
   * Adds the images as games that are thumbnails which once selected, opens a screen of the game
   * @param thumbnails list of thumbnails for the game
   */
  private void addImages(List<Thumbnail> thumbnails){
    for (Thumbnail thumbnail : thumbnails) {
      ImageView optionImage = new ImageView(thumbnail.getImageFile());
      resizeImage(optionImage, 1);
      Button gameButton = makeButton(optionImage,thumbnail.getDescription());
      gameButton.setOnAction(e -> setOptionSelected(thumbnail.getTitle()));
      myHBox.getChildren().add(gameButton);
    }
  }

  /**
   * Adds the Profile data back button
   * @param profile current profile selected
   * @param backButton node for the back button
   * @return an HBox with both the profile and the back button
   */
  private Node addProfileDataAndBackButton(ViewProfile profile, Node backButton){
    HBox bottomBox = new HBox();
    if(profile != null) bottomBox.getChildren().add(setProfileData(profile));
    bottomBox.getChildren().add(backButton);
    return bottomBox;
  }

  /**
   * Gets the profile Data and displayes it
   * @param profile the current profile being viewed
   * @return the node that displays the Profile data
   */
  private Node setProfileData(ViewProfile profile){
    VBox vBox = new VBox();
    vBox.setOnMouseClicked(e-> showProfile(profile));
    String name = profile.getProfileName();
    Text text = new Text(name);
    ImageView imageView = new ImageView(profile.getProfilePath());
    vBox.getChildren().add(imageView);
    vBox.getChildren().add(text);
    return vBox;
  }

  /**
   * Shows the profile in a new stage
   * @param profile current profile being selected
   */
  private void showProfile(ViewProfile profile){
    Stage stage  = new Stage();
    Scene scene = new Scene(profile.getPane());
    stage.setTitle(languageResources.getString(PROFILE_VIEW_TITLE));
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Sets the option selected
   */
  private void setOptionSelected(String optionSelected) {
    this.optionSelected.set(null);
    this.optionSelected.set(optionSelected);
  }

  /**
   * Shows error if appropriate action happens
   * @param message message specific to the error
   */
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(ERROR_MESSAGE);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
