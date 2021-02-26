package ooga.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import ooga.OogaDataException;
import ooga.data.OogaProfile;
import ooga.data.profiledatareaders.XMLProfileReader;
import ooga.game.Game;
import ooga.view.menus.GameMenu;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class nullProfileTest {
    private final ResourceBundle GAME_LANGUAGE = ResourceBundle.getBundle("ooga/view/Resources/languages.French");
    String profileNameSelected;
    private ViewProfile defaultViewProfile = new ViewProfile(GAME_LANGUAGE, "Test", "testing");
    XMLProfileReader myProfileReader = new XMLProfileReader();
    public ArrayList<ViewProfile> myProfiles = new ArrayList<>();

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

    @Test
    void testIncorrectObject(){

    }

}
