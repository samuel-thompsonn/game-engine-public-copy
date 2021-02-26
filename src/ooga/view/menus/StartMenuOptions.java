package ooga.view.menus;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chris warren, doherty guirand, caryshindell
 */
public class StartMenuOptions {

    private static final Paint COLORBUTTON = Color.BLACK;
    private static final double Button_Space = 30;
    private final BooleanProperty Display = new SimpleBooleanProperty(false);
    private final BooleanProperty Audio = new SimpleBooleanProperty(false);
    private final BooleanProperty Reset = new SimpleBooleanProperty(false);
    private final BooleanProperty GoBack = new SimpleBooleanProperty(false);
    final Map<BooleanProperty, String> buttonPropertiesAndNames = new HashMap<>() {{
        put(Display, "Display");
        put(Audio, "Audio");
        put(Reset, "Reset");
        put(GoBack, "Go Back");
    }};
    private static final int SLIDERMINNUM = 0;
    private static final int SLIDERMAXNUM = 100;
    private static final int SLIDERUNIT = 10;

    private Button myButton;


    /**
     * Makes button based on the statusproperty and then sets it to the opposite after it is clicked
     * @param statusProperty gets the status property of the audio button
     * @param text sets the text of the button
     * @return makes the appropriate button
     */
    private Button makeButton(BooleanProperty statusProperty, String text) {
        myButton = new Button(text);
        myButton.setTextFill(COLORBUTTON);
        myButton.setOnAction(e -> statusProperty.setValue(!statusProperty.getValue()));
        return myButton;
    }

    /**
     * Gets buttons
     * @return the  button
     */
    private Button getButton() {
        return myButton;
    }

    /**
     * Gets the vbox of all the buttons and highlights the buttons based on mouse position
     */
    public StartMenuOptions() {
        VBox buttonVBox = new VBox(Button_Space);
        for (Map.Entry<BooleanProperty, String> buttonPropertyAndName : buttonPropertiesAndNames.entrySet()) {
            buttonVBox.getChildren().add(makeButton(buttonPropertyAndName.getKey(), buttonPropertyAndName.getValue()));
            myButton.setOnMouseEntered(e -> myButton.resize(myButton.getWidth() * 1.25, myButton.getHeight() * 1.25));
            myButton.setOnMouseExited(e -> myButton.resize(myButton.getWidth() * 1.5, myButton.getHeight() * 1.5));
        }
    }

    /**
     * Creates a slider for volume
     * @return a slider that controls music volume
     */
    public Slider volumeControl() {
        Slider myVolumeSlider = new Slider();
        myVolumeSlider.setMin(SLIDERMINNUM);
        myVolumeSlider.setMax(SLIDERMAXNUM);
        myVolumeSlider.setValue(1);
        myVolumeSlider.setShowTickLabels(true);
        myVolumeSlider.setShowTickMarks(true);
        myVolumeSlider.setMajorTickUnit(SLIDERUNIT);
        myVolumeSlider.setBlockIncrement(SLIDERUNIT);

        //myVolumeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
//                animation.setRate((Double) new_val);
        //});
        return myVolumeSlider;
    }

    public BooleanProperty BackProperty() {
        return GoBack;
    }

    public BooleanProperty DisplayProperty() {
        return Display;
    }

    public BooleanProperty AudioProperty() {
        return Audio;
    }
}
