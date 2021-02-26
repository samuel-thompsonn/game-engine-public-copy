//package ooga.view;
//
//
//
//import static org.junit.Assert.assertTrue;
//
//import javafx.geometry.Point2D;
//import javafx.scene.Group;
//import javafx.scene.control.Button;
//import ooga.view.menus.PauseMenu;
//import org.junit.Before;
//import org.testfx.framework.junit.ApplicationTest;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.junit.Test;
//import java.util.ResourceBundle;
//
//public class PauseTest extends ApplicationTest {
//    PauseMenu pauseMenu;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        ResourceBundle GAME_LANGUAGE = ResourceBundle.getBundle("ooga/view/Resources/languages.French");
//        pauseMenu = new PauseMenu(GAME_LANGUAGE);
//        stage.setScene(pauseMenu.getScene());
//        stage.show();
//        stage.toFront();
//    }
//
//    @Test
//    public void testPauseInput(){
//      pauseMenu.setResumed(true);
//      assertTrue(pauseMenu.resumedProperty().getValue());
//    }
//}
