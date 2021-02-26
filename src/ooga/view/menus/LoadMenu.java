package ooga.view.menus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import ooga.OogaDataException;
import ooga.data.gamedatareaders.GameDataReaderExternal;
import ooga.data.gamerecorders.GameRecorderExternal;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author doherty guirand, chris warren
 */
public class LoadMenu extends OptionMenu {

    public static final String NEW_GAME = "NewGame";
    public static final String SAVED_GAME_ERROR = "SavedGameError";
    private final StringProperty dateSelected = new SimpleStringProperty();
    private final String ERROR_MESSAGE;
    private final String NEWGAME;
    private List<List<String>> pastSaves;
    /**
     * View where user can decide to load a saved game or a new game
     * @param gamename - name of game selected, used to find past saves
     * @param profilename - user's profile name, used to find saved games
     * @param reader - DataReader used to get past saves from data
     * @param backButton - button that allows user to get back to startmenu screen
     */
    public LoadMenu(ResourceBundle languageResources, GameRecorderExternal gamerecorder, String gamename,
                    String profilename, GameDataReaderExternal reader, Node backButton){
        super(languageResources, gamename);
        try {
            pastSaves = gamerecorder.getGameSaves(profilename,gamename);
        } catch (OogaDataException | MissingResourceException e) {
            //showError(languageResources.getString("SavesErrorMessage"));
            pastSaves = new ArrayList<>(new ArrayList<>());
        }
        NEWGAME = languageResources.getString(NEW_GAME);
        ERROR_MESSAGE = languageResources.getString(SAVED_GAME_ERROR);
        this.setLeft(setMenuItems(createButtons(backButton, reader,profilename,gamename)));
    }

    /**
     * User chooses to either load a saved game or enter a new game
     * @return String Property containing the date of the game selected. If new game, returns an empty string.
     */
    public StringProperty getDateSelected() {
        return dateSelected;
    }

    private Node makeButton(String date, String text){
        Button button = new Button(text);
        button.setOnAction(e-> {
            setDateSelected(button.getText());
        });
        return new Button(text);
    }

    /**
     * Set the date selected
     * @param optionSelected - string representing which date was selected
     */
    private void setDateSelected(String optionSelected) {
        this.dateSelected.set(null);
        this.dateSelected.set(optionSelected);
    }

    /**
     *
     * @param backButton
     * @return creates the buttons for New Game, and load saved game
     */
    private List<Node> createButtons(Node backButton, GameDataReaderExternal dataReader, String profileName, String gameName) {
       List<Node> buttons = new ArrayList<>();
        Button button = new Button(NEWGAME);
        button.setOnAction(e-> setDateSelected("  "));
        buttons.add(button);
        for(List<String> dates: pastSaves){
            Button saved = new Button();
            saved.setText(dates.get(0) + "-" + dates.get(1));
            saved.setOnAction(e-> setDateSelected(dates.get(1)));
            buttons.add(saved);
       //    buttons.add(makeButton(dates.get(1), dates.get(1)));
        }
        buttons.add(backButton);
        return buttons;
    }

    /**
     * Shows an alert for any error message
     * @param message specific error message prvided
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(ERROR_MESSAGE);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
