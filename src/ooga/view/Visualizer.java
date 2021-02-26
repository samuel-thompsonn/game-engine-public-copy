package ooga.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ooga.OogaDataException;
import ooga.data.gamedatareaders.GameDataReaderExternal;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.view.menus.GameMenu;
import ooga.view.menus.LoadMenu;
import ooga.view.menus.ProfileMenu;

import java.util.ResourceBundle;

/**
 * @author caryshindell, doherty guirand, chris warren
 * THIS IS THE MASTER CONTROLLER. The program is launched from here. It creates the starting menus.
 * Game language is also configurable.
 * This decides on format for reading files.
 * Dependencies: data reader external, menus
 */
public class Visualizer extends Application {
  private final ResourceBundle GAME_LANGUAGE = ResourceBundle.getBundle("ooga/view/Resources/languages.English");
  private final String KEYBOARD_INPUTS_FILE_PATH = "ooga/game/controls/inputs/keyboard";
  private final String ERROR_MESSAGE = GAME_LANGUAGE.getString("ErrorMessage");
  private final String START_MENU_TITLE = GAME_LANGUAGE.getString("StageTitle");
  private final String BACK_BUTTON_TEXT = GAME_LANGUAGE.getString("Back");
  private String profileNameSelected;
  private Stage stage;
  private final GameDataReaderExternal myDataReader = new XMLGameDataReader();
  private final XMLGameRecorder gameRecorder = new XMLGameRecorder();


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    showProfileMenu();
    primaryStage.setTitle(START_MENU_TITLE);
    primaryStage.show();
    primaryStage.setResizable(false);
  }

  private void showProfileMenu() {
    ProfileMenu profileMenu = new ProfileMenu(GAME_LANGUAGE);
    Scene profileMenuScene = new Scene(profileMenu,profileMenu.getWidth(),profileMenu.getHeight());
    profileMenu.profileSelected().addListener((p, poldVal, pnewVal) ->{
      if(pnewVal != null)profileNameSelected = pnewVal.getProfileName();
      showStartMenu(pnewVal, profileMenuScene);
    });
    stage.setScene(profileMenuScene);
  }

  private void showStartMenu(ViewProfile profile, Scene returnScene){
    Button backToProfileMenu = makeBackButton(returnScene);
    GameMenu gameMenu = new GameMenu(GAME_LANGUAGE,profile,backToProfileMenu);
    Scene gameMenuScene = new Scene(gameMenu, gameMenu.getWidth(), gameMenu.getHeight());
    gameMenu.selectedProperty().addListener((o, oldVal, newVal) -> {
      if(newVal != null)showLoadMenu(newVal, gameMenuScene);
    });
    stage.setScene(gameMenuScene);
  }

  private void showLoadMenu(String gameName, Scene returnScene){
    Button backToStartMenu = makeBackButton(returnScene);
    LoadMenu loadMenu = new LoadMenu(GAME_LANGUAGE,gameRecorder,gameName, profileNameSelected,myDataReader, backToStartMenu);
    Scene loadScene = new Scene(loadMenu, loadMenu.getWidth(),loadMenu.getHeight());
    loadMenu.getDateSelected().addListener((d,dold,dnew)-> {
      if(dnew == null) System.out.println("null");
      if(dnew != null) startGame(gameName,profileNameSelected,dnew);
    });
    stage.setScene(loadScene);
  }

  private void startGame(String gameName, String profileName, String date) {
    if (gameName != null) {
      try {
        new ViewerGame(gameName,profileName,date, GAME_LANGUAGE, KEYBOARD_INPUTS_FILE_PATH);
      } catch (OogaDataException e) {
        //Sam added this, because he made it possible for the OogaGame constructor to throw
        // an exception, so that the view can decide what to do when no game is found.
        showError(e.getMessage());
      }
    }
  }
  private Button makeBackButton(Scene backScene){
    Button button = new Button();
    button.setText(BACK_BUTTON_TEXT);
    button.setOnAction(e->stage.setScene(backScene));
    return button;
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(ERROR_MESSAGE);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
