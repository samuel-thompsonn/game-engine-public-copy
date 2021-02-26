package ooga.view.menus;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author doherty guirand
 */
public abstract class OptionMenu extends BorderPane {
    private final ResourceBundle myResources = ResourceBundle.getBundle("ooga/view/Resources.config");
    private final double WINDOW_HEIGHT = Double.parseDouble(myResources.getString("windowHeight"));
    private final double WINDOW_WIDTH = Double.parseDouble(myResources.getString("windowWidth"));
    private static final String STYLESHEET = "ooga/view/Resources/OptionMenu.css";
    private final String SCROLL_PANE_STYLE = myResources.getString("scrollpanecss");
    private final String TITLE_STYLE = myResources.getString("titlecss");
    private static final double TITLE_FONT_SIZE = 70;
    private static final double SPACING = 30;
    protected final ResourceBundle languageResources;

    /**
     * This type of menu has a vertial scrollpane that allows user to select from a list of options
     * Styled by css file specified in constants
     * @param title does not use language resource because title of game
     */
    public OptionMenu(ResourceBundle languageresources, String title){
        languageResources = languageresources;
        this.getStylesheets().add(STYLESHEET);
        this.setWidth(WINDOW_WIDTH);
        this.setHeight(WINDOW_HEIGHT);
        this.setTop(makeMenuTitle(title));
        this.setCenterShape(true);
    }

    /**
     * Adds menu items to vertical scrollpane,
     * @param menuItems a list of Menu items to be added to menu
     * @return - a Node that contains all of the menu items
     */
    protected Node setMenuItems(List<Node> menuItems){
        VBox buttonVBox = new VBox(SPACING);
        buttonVBox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(WINDOW_WIDTH);
        scrollPane.setStyle(SCROLL_PANE_STYLE);
        scrollPane.setContent(buttonVBox);
        scrollPane.setFitToWidth(true);
        for(Node item: menuItems){
            buttonVBox.getChildren().add(item);
        }
        return scrollPane;
    }

    private HBox makeMenuTitle(String title){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        Text text = new Text(title);
        text.setStyle(TITLE_STYLE);
        text.setFont(Font.font(TITLE_FONT_SIZE));
        text.setFill(Color.WHITE);
        hbox.getChildren().add(text);
        return hbox;
    }
}
