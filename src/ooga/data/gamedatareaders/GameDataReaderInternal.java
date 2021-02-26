package ooga.data.gamedatareaders;

import ooga.OogaDataException;
import ooga.game.behaviors.Action;
import ooga.game.behaviors.Effect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.lang.Class.forName;

/**
 * @author caryshindell
 * this interface essentially houses all game data reader methods that are not specific to a certain file type (e.g. xml)
 * Assumptions: there are action classes and effects classes set up so that the reflection here will work
 * Dependencies: action, effect
 */
public interface GameDataReaderInternal extends GameDataReaderExternal {

  /**
   * part of the data reader internal api, but depends on exact type of data reader
   * @param givenGameName
   * @return
   * @throws OogaDataException
   */
  List<List<String>> getBasicGameInfo(String givenGameName) throws OogaDataException;

  /**
   * Give a Game a list of level ID's in the order that they're listed in the .xml files
   * @param gameName the name of the Game at the start of the .xml file
   * @return A list of strings, the ID's of the Level written in the game file;
   * @throws OogaDataException if the String given isn't a directory or the cooresponding file is not properly formatted
   */
  @Override
  default List<String> getLevelIDs(String gameName) throws OogaDataException{
    return getBasicGameInfo(gameName).get(0);
  }

  /**
   * @param gameName the name of the Game at the start of the .xml file
   * @return A map of variable names [Strings] to their initial values [Strings]
   * @throws OogaDataException if the String given isn't a directory or the cooresponding file is not properly formatted
   */
  @Override
  default Map<String, String> getVariableMap(String gameName) throws OogaDataException{
    Map<String, String> varMap = new HashMap<>();
    List<List<String>> basicGameInfo = getBasicGameInfo(gameName);
    List<String> varList = basicGameInfo.get(1);
    List<String> varValues = basicGameInfo.get(2);
    for (int i=0; i<varList.size(); i++){
      if(i<varValues.size()){
        varMap.put(varList.get(i), varValues.get(i));
      }else{
        varMap.put(varList.get(i), "");
      }
    }
    return varMap;
  }

  default Action makeAction(String actionType, List<String> args, List<Effect> effects) throws OogaDataException {
    String effectClassName = myActionsResources.getString(actionType);
    try {
      Class<?> cls = forName(PATH_TO_CLASSES + effectClassName);
      Constructor<?> cons = cls.getConstructor(List.class, List.class);
      return (Action)cons.newInstance(args, effects);
    } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e){
      throw new OogaDataException(actionType + myEnglishResources.getString("NoSuchActionException"));
    } catch(InvocationTargetException e){ // this should be OogaDataException but it won't work because reflection is used
      throw new OogaDataException(actionType + myEnglishResources.getString("ActionArgException"));
    }
  }

  default Effect makeBasicEffect(String[] effect) throws OogaDataException {
    String effectName = effect[0];
    try {
      String effectClassName = myEffectsResources.getString(effectName);
      Class<?> cls = forName(PATH_TO_CLASSES + effectClassName);
      Constructor<?> cons = cls.getConstructor(List.class);
      return (Effect)cons.newInstance(Arrays.asList(effect).subList(1, effect.length));
    } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | MissingResourceException e) {
      throw new OogaDataException(effectName + myEnglishResources.getString("NoSuchEffectException"));
    } catch(InvocationTargetException e){ // this should be OogaDataException but it won't work because reflection is used
      throw new OogaDataException(effectName + myEnglishResources.getString("EffectArgumentsException"));
    }
  }
}
