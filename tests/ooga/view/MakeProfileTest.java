package ooga.view;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.File;

public class MakeProfileTest extends Application {

    private GridPane myGridpane;
    private Scene myScene;
    protected Pane myPane;
    protected Group myRoot;
    private final FileChooser myFileChooser = new FileChooser();
    private final ImageView mv = new ImageView();
    private Stage myStage;

    public void start(Stage primaryStage) throws Exception {
        myGridpane = createRegistrationFormPane();
        addUIControls(myGridpane);
        configureFileChooser();
        myScene = new Scene(myGridpane);
        primaryStage.setScene(myScene);
        primaryStage.show();
        myStage = primaryStage;
    }
    private GridPane createRegistrationFormPane() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Make your profile");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Name Label
        Label nameLabel = new Label("Username : ");
        gridPane.add(nameLabel, 0, 1);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1, 1);

        // Add file chooser

        // Add Name Label
        Label picLabel = new Label("Select picture : ");
        gridPane.add(picLabel, 0, 2);

        Button browse = new Button("Browse");
        gridPane.add(browse, 1,2);
        browse.setOnAction((event) -> {
            File file = myFileChooser.showOpenDialog(myStage);
            if (file != null) { // only proceed, if file was chosen
                Image img = new Image(file.toURI().toString());
                mv.setImage(img);
            }
        });

        // Add Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));
    }

    private void configureFileChooser() {
        myFileChooser.setTitle("View Pictures");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        myFileChooser.getExtensionFilters().add(extFilter);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
