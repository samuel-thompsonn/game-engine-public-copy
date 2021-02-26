package ooga.view.menus;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ooga.data.gamedatareaders.GameDataReaderExternal;
import ooga.data.profiledatareaders.ProfileReaderExternal;
import ooga.data.gamedatareaders.XMLGameDataReader;
import ooga.data.profiledatareaders.XMLProfileReader;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author chris warren, caryshindell, doherty guirand
 */
public abstract class ScrollMenu extends BorderPane {

    protected final ProfileReaderExternal myProfileReader;
    protected final GameDataReaderExternal myGameDataReader;
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
    protected final ResourceBundle languageResources;

    /**
     * This type of menu has a horizontal scrollPane that allows users to scroll through a list of options.
     * Styled by the css file specified in constants
     */
    protected ScrollMenu(ResourceBundle languageresources, String titleKey){
        languageResources = languageresources;
        myProfileReader = new XMLProfileReader() {};
        myGameDataReader = new XMLGameDataReader() {};
        double windowHeight = Double.parseDouble(myResources.getString("windowHeight"));
        this.setWidth(WINDOW_WIDTH);
        this.setHeight(windowHeight);
        this.setTop(makeTitle(titleKey));
        this.setBottom(horizontalScroller());
        String SCROLLBAR_CSS_LOCATION = myResources.getString("scrollBarCSSLocation");
        this.getStylesheets().add(SCROLLBAR_CSS_LOCATION);
    }

    /**
     * Created buttons that have an image, buttons enlarge when mouse hovers over, tooltip appears when mouse hovers over
     * @param image - image for button
     * @param description - description that appears in tooltip
     * @return - the Button created, must be Button (as opposed to Node) because subclasses decide the button's action
     */
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

    /**
     *
     * @param titleKey - represents the string that corresponds to the title
     * @return Hbox that contains the title
     */
    private HBox makeTitle(String titleKey){
        String title;
        try {
            title = languageResources.getString(titleKey);
        }catch (MissingResourceException e){
            title = titleKey;
        }
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        Text text = new Text(title);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle(TITLE_STYLE);
        text.setFont(Font.font(TITLE_FONT_SIZE));
        text.setFill(Color.WHITE);
        hbox.getChildren().add(text);
        return hbox;
    }
}
