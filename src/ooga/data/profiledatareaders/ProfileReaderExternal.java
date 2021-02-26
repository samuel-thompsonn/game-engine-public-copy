package ooga.data.profiledatareaders;

import ooga.OogaDataException;
import ooga.data.OogaProfile;

import java.io.File;
import java.util.List;

/**
 * @author caryshindell, braeden ward
 * This is the external api of a profile data reader. It deals with reading user profile data.
 * Dependencies: OogaProfile
 */
public interface ProfileReaderExternal {

  /**
   * writes a new profile to data
   * @param newProfileName - the name of the new profile
   * @param photoFile - File containing the new profile photo
   * @throws OogaDataException - if creating a new profile breaks such as file not being found
   */
  void addNewProfile(String newProfileName, File photoFile) throws OogaDataException;

  /**
   * @return A list of Profiles according to the data stored in the Users folder. Returns an empty list if there are no
   * existing profiles
   */
  List<OogaProfile> getProfiles() throws OogaDataException;
}
