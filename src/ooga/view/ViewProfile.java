package ooga.view;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author doherty guirand
 */
public class ViewProfile{
    public static final int ZERO_INDEX = 0;
    public static final int FIRST_INDEX = 1;
    public static final String CLICK_TO_ADD_NAME = "Click_To_Add_Name";
    public static final String DEFAULT_IMAGE_PATH = "ooga/view/Resources/profilephotos/defaultphoto.jpg";
    private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
    private final double WINDOW_HEIGHT = Double.parseDouble(myResources.getString("windowHeight"));
    private final double WINDOW_WIDTH = Double.parseDouble(myResources.getString("windowWidth"));
    private static final String STYLESHEET = "ooga/view/Resources/PlayerProfile.css";
    private static final String GRID_PANE_STYLE = ".grid-pane";
    private String profilePhotoPath;
    private String profileName;
    private Map<String, Integer> myStats;
    private final List<Node> extraNodes = new ArrayList<>();
    private File fileChosen;

    /**
     * View's version of a profile, stores a photo, name, and stats, allows user to add a new profile
     * and update an existing profile, can support some user interaction such as drag and drop photo to replace existing photo,
     * double clicking on photo, and changing profile name by clicking on name
     * @param name - String to be used as profile name
     * @param imagePath - path to a photo to be used as profile photo -- if not an actual path, profile photo is set to default
     */
    public ViewProfile(ResourceBundle resourceBundle, String name, String imagePath){
        profileName = name;
        profilePhotoPath = imagePath;
        verifyPhotoPath();
        myStats = new HashMap<>();
    }
    /**
     * This constructor is used it the profile already as stats, used in ViewerGame when making viewProfiles from backend Profiles
     * @param name - String to be used as profile name
     * @param imagePath - path to a photo to be used a profile photo -- if not an actual path, profile photo is set to default
     * @param stats - Map of String, Integer that stores users game statistics
     */
    public ViewProfile(ResourceBundle resourceBundle,String name, String imagePath, Map<String, Integer> stats){
        this(resourceBundle,name,imagePath);
        myStats = stats;
    }
    /**
     * This constructor is used when user is creating a new profile, used by profileMenu
     * @param submitButton - button that tells profileMenu that the user is ready to submit name and photo
     * added. Name and photo saved in data
     */
    public ViewProfile(ResourceBundle resourceBundle,Node submitButton){
      this(resourceBundle, CLICK_TO_ADD_NAME, DEFAULT_IMAGE_PATH);
      extraNodes.add(submitButton);
    }

    /**
     *
     * @return Return's profilename
     */
    public String getProfileName(){return profileName;}

    /**
     * returns path to profilephoto
     * @return
     */
    public String getProfilePath(){return profilePhotoPath;}


    /**
     * Creates and returns a pane that displays the profile's name, photo and stats
     * @return Pane
     */
    public Pane getPane(){
        BorderPane pane = new BorderPane();
        pane.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        pane.setTop(setNameAndPhoto());
        pane.setCenter(setStatsBox());
        HBox hBox = new HBox();
        for(Node node : extraNodes){
            hBox.getChildren().add(node);
        }
        pane.setBottom(hBox);
        pane.getStylesheets().add(STYLESHEET);
        return pane;
    }

    private void verifyPhotoPath(){
        try{
            ImageView example = new ImageView(profilePhotoPath);
        }
        catch (IllegalArgumentException | NullPointerException e){
            profilePhotoPath = DEFAULT_IMAGE_PATH;
        }
    }

    private VBox setNameAndPhoto(){
        VBox nameAndPhoto = new VBox();
        nameAndPhoto.getChildren().add(new ImageView(profilePhotoPath));
        nameAndPhoto.getChildren().add(setNameText());
        nameAndPhoto.setOnDragEntered(this::handleDroppedPhoto);
        nameAndPhoto.setOnDragDropped(this::handleDroppedPhoto);
        nameAndPhoto.setOnMouseClicked(e->{ if(e.getClickCount() == 2){ handleChosenPhoto();}});
        return nameAndPhoto;
    }

    private void handleChosenPhoto(){
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File fileChosen = fileChooser.showOpenDialog(stage);
        if(fileChosen != null) setNewProfilePhoto(fileChosen);
    }
    private void handleDroppedPhoto(DragEvent e){
        Dragboard db = e.getDragboard();
        List<File> files = db.getFiles();
        for(File file: files) {
            setNewProfilePhoto(file);
        }
    }

    private void setNewProfilePhoto(File filepath){
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(filepath);
            String path = "src/ooga/view/Resources/profilephotos/" +System.currentTimeMillis()+".jpg";
            File newFile = new File(path);
            ImageIO.write(bufferedImage,"jpg",newFile);
            fileChosen = filepath;
            profilePhotoPath = path;
        } catch (IOException e) {
            profilePhotoPath = DEFAULT_IMAGE_PATH;
            fileChosen = new File(DEFAULT_IMAGE_PATH);
        }
    }

    private void handleExitPressed(TextArea textArea, HBox hBox, KeyEvent e){
        if(e.getCode() ==  KeyCode.ENTER){
            hideTextArea(textArea,hBox);
        }
    }

    private void hideTextArea(TextArea textArea,HBox hBox){
        if(!textArea.getText().equals("\n")){
            profileName = textArea.getText().replace("\n", "");
        }
        Text text = new Text(profileName);
        hBox.getChildren().clear();
        hBox.getChildren().add(text);
    }
    private void replaceWithTextArea(HBox hBox,TextArea textArea){
        hBox.getChildren().clear();
        hBox.getChildren().add(textArea);
    }


    private GridPane setNameText(){
        Text nameHeader = new Text("Name:");
        GridPane gridPane = new GridPane();
        Text name = new Text(profileName);
        HBox hBox = new HBox();
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(25);
        textArea.setPrefWidth(200);
        hBox.setOnMouseClicked(e->replaceWithTextArea(hBox,textArea));
        textArea.setOnKeyPressed(e->handleExitPressed(textArea,hBox,e));
        //myPane.setOnMouseClicked(m->handleMouseClicked(m, textArea,hBox));
        hBox.getChildren().add(name);
        gridPane.setStyle(GRID_PANE_STYLE);
        gridPane.add(nameHeader,0,0);
        gridPane.add(hBox,1,0);
        return gridPane;
    }



    private GridPane setStatsBox(){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50);
        int row = 0;
        for(String game: myStats.keySet()){
            Integer stat = myStats.get(game);
            Text gameName = new Text(game);
            Text statText = new Text(stat.toString());
            gridPane.add(gameName, ZERO_INDEX,row);
            gridPane.add(statText, FIRST_INDEX,row);
            row++;
        }
        gridPane.setStyle(GRID_PANE_STYLE);
        return gridPane;
    }

    public File getFileChosen(){return fileChosen;}

    public void setProfilePhotoPath(String path){profilePhotoPath = path;}



}
