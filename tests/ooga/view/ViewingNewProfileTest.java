package ooga.view;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ooga.OogaDataException;
import ooga.data.OogaProfile;
import ooga.data.gamedatareaders.GameDataReaderExternal;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.gamerecorders.XMLGameRecorder;
import ooga.data.profiledatareaders.ProfileReaderExternal;
import ooga.data.profiledatareaders.XMLProfileReader;
import ooga.view.menus.GameMenu;
import ooga.view.menus.LoadMenu;
import ooga.view.menus.ProfileMenu;
import ooga.view.menus.ScrollMenu;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ViewingNewProfileTest{

    private final ResourceBundle GAME_LANGUAGE = ResourceBundle.getBundle("ooga/view/Resources/languages.English");
    private final ObjectProperty<ViewProfile> profileSelected = new SimpleObjectProperty<>();
    private List<ViewProfile> myProfiles = new ArrayList<>();
    public static final String DEFAULT_IMAGE_PATH = "ooga/view/Resources/profilephotos/defaultphoto.jpg";
    private final String addNewProfilePhoto = "ooga/view/Resources/profilephotos/Makenewprofile.png";
    private static final String ADD_PROFILE = "Add a New Profile";
    private static final String SUBMIT = "Submit";
    private static final String ERROR_MESSAGE = "Could Not Create New Profile";
    private static final String PROFILE_MENU_TITLE_KEY = "ProfileMenuTitle";
    private XMLProfileReader myProfileReader = new XMLProfileReader();
    private ViewProfile newViewProfile = new ViewProfile(ResourceBundle.getBundle("ooga/view/Resources/languages.English"), "Test", "/Users/dohertyguirand/Spring2020/CS308/Finalimages/download-2.jpg");
    private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
    private final double WINDOW_WIDTH = Double.parseDouble(myResources.getString("windowWidth"));
    private final double IMAGE_HEIGHT = Double.parseDouble(myResources.getString("gameImageHeight"));
    private final double IMAGE_WIDTH = Double.parseDouble(myResources.getString("gameImageWidth"));
    private final double IMAGE_RESIZE_FACTOR = Double.parseDouble(myResources.getString("gameImageResizeFactor"));
    private final String SCROLL_PANE_STYLE = myResources.getString("scrollpanecss");
    private final String HBOX_STYLE = myResources.getString("hboxcss");
    private final double SCROLLBAR_Y = Double.parseDouble(myResources.getString("scrollbarY"));
    private final double HBOX_SPACING = Double.parseDouble(myResources.getString("hboxspacing"));
    private final double HBOX_Y_LAYOUT = Double.parseDouble(myResources.getString("hboxy"));
    private final String TITLE_STYLE = myResources.getString("titlecss");
    private static final double TITLE_FONT_SIZE = 65;
    protected HBox myHBox;



    public ObjectProperty<ViewProfile> profileSelected(){ return profileSelected; }



    private void makeViewProfiles() {
        List<OogaProfile> oogaProfiles;
        try {
            oogaProfiles = myProfileReader.getProfiles();
        }catch (OogaDataException e){
            return;
        }
        for (OogaProfile oogaProfile : oogaProfiles) {
            ViewProfile viewProfile = new ViewProfile(GAME_LANGUAGE,oogaProfile.getProfileName(),oogaProfile.getProfilePhotoPath(), oogaProfile.getStats());
            myProfiles.add(viewProfile);
        }
    }

    private void addProfileImages() {
        if (myProfiles != null) {
            for (ViewProfile profile : myProfiles) {
                Button button = makeButton(new ImageView(profile.getProfilePath()), profile.getProfileName());
                button.setOnAction(e -> setProfileSelected(profile));
                myHBox.getChildren().add(button);
            }
        }
        ImageView defaultImage = new ImageView(addNewProfilePhoto);
        Button button = makeButton(defaultImage,ADD_PROFILE);
        button.setOnAction(e-> showNewProfileScreen());
        myHBox.getChildren().add(button);
    }

    /**
     * Creates a button that once clicked shows a new screen with the new profile included
     */
    private void showNewProfileScreen(){
        Button submit = new Button(SUBMIT);
        ViewProfile profile = new ViewProfile(GAME_LANGUAGE,submit);
        Stage stage = new Stage();
        submit.setOnAction(e-> {
            stage.hide();
            addNewProfile(profile);
        });
        stage.setScene(new Scene(profile.getPane(),0,0));
        stage.show();
    }


    private void addNewProfile(ViewProfile profile){
        try {
            File photoFile = profile.getFileChosen();
            if(photoFile != null){
                myProfileReader.addNewProfile(profile.getProfileName(),profile.getFileChosen());
                myProfiles.add(profile);
                Button button = makeButton(new ImageView(profile.getProfilePath()), profile.getProfileName());
                button.setOnAction(e -> setProfileSelected(profile));
                myHBox.getChildren().add(button);
            }
        } catch (OogaDataException e) {
            showError(ERROR_MESSAGE);
        }catch (IllegalArgumentException d){
            profile.setProfilePhotoPath(DEFAULT_IMAGE_PATH);
            Button button = makeButton(new ImageView(DEFAULT_IMAGE_PATH), profile.getProfileName());
            button.setOnAction(e -> setProfileSelected(profile));
            myHBox.getChildren().add(button);
            //BUG TO BE FIXED LATER
        }
    }

    /**
     * Sets the profile to the one selected
     * @param profile - profile parameter
     */
    private void setProfileSelected(ViewProfile profile){
        this.profileSelected.set(null);
        this.profileSelected.set(profile);
    }


    /**
     * Shows the appropriate error
     * @param message - messaged based on apporpirate error
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_MESSAGE);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets profiles to spefified lists of viewProfiles. Used for testing
     * @param viewProfiles list of viewProfiles
     */
    @Deprecated
    public void setMyProfiles(List<ViewProfile> viewProfiles){

        myProfiles = viewProfiles;
        addProfileImages();
    }

    protected Button makeButton(ImageView image, String description){
        resizeImage(image,1);
        Button button = new Button(null,image);
        button.setOnMouseEntered(e -> resizeImage(image, IMAGE_RESIZE_FACTOR));
        button.setOnMouseExited(e -> resizeImage(image, 1));
        button.setTooltip(new Tooltip(description));
        return button;
    }

    /**
     * scales the image by desired resizeFactor
     * @param image - ImageView to be resized
     * @param resizeFactor - double that specified the scaling factor
     */
    protected void resizeImage(ImageView image, double resizeFactor) {
        image.setFitWidth(IMAGE_WIDTH *resizeFactor);
        image.setFitHeight(IMAGE_HEIGHT *resizeFactor);
    }

    /**
     * creates horizontal scroller
     * @return returns a node that holds all the thumbnails that can scroll based on addition of content
     */
    protected Node horizontalScroller() {
        myHBox = new HBox();
        myHBox.setStyle(HBOX_STYLE);
        myHBox.setLayoutY(HBOX_Y_LAYOUT);
        myHBox.setSpacing(HBOX_SPACING);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle(SCROLL_PANE_STYLE);
        scrollPane.setLayoutY(SCROLLBAR_Y);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(WINDOW_WIDTH);
        scrollPane.setContent(myHBox);
        return scrollPane;
    }

    @Test
    public void newProfileDoesNotHaveDefaultImage(){
        addNewProfile(newViewProfile);
        for(ViewProfile profile:myProfiles){
            assertNotEquals(profile.getProfilePath(), DEFAULT_IMAGE_PATH);
        }
    }
}
