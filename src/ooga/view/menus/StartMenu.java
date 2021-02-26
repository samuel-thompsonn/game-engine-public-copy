package ooga.view.menus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.OogaDataException;
import ooga.data.Thumbnail;
import ooga.view.ViewProfile;
import java.util.List;
import java.util.ResourceBundle;

@Deprecated
public class StartMenu extends ScrollMenu{

  private final StringProperty optionSelected = new SimpleStringProperty();


  public StartMenu(ViewProfile profile, Node backButton){
    super(ResourceBundle.getBundle("ooga/view/Resources/languages.French"),"select a game");
    try {
      List<Thumbnail> thumbnails = myGameDataReader.getThumbnails();
      addImages(thumbnails);
    } catch (OogaDataException ignored){
      //TODO: actually show an error message
    }
  }

  private void addImages(List<Thumbnail> thumbnails){
    for (Thumbnail thumbnail : thumbnails) {
      ImageView optionImage = new ImageView(thumbnail.getImageFile());
      resizeImage(optionImage, 1);
      Button gameButton = makeButton(optionImage,thumbnail.getDescription());
      gameButton.setOnAction(e -> setOptionSelected(thumbnail.getTitle()));
      myHBox.getChildren().add(gameButton);
    }
  }


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
  public StringProperty selectedProperty() {
    return optionSelected;
  }

  private void showProfile(ViewProfile profile){
    Stage stage  = new Stage();
    Scene scene = new Scene(profile.getPane());
    stage.setTitle(profile.getProfileName() + "'s Profile");
    stage.setScene(scene);
    stage.show();
  }

  public void setOptionSelected(String optionSelected) {
    this.optionSelected.set(null);
    this.optionSelected.set(optionSelected);
  }
}
