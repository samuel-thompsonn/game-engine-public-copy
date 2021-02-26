package ooga.game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @author sam thompson
 * Handles keeping track of the entities in a level in OOGA, and keeps track of
 * its part in level sequencing.
 */
public class OogaLevel implements Level{
    private final ObservableList<EntityInternal> myEntities;
    private String myNextLevelId;
    private final String myId;

    /**
     * Constructs the level.
     * @param entities The List of entities that this level will contain.
     * @param id The string identifier of this level.
     */
    public OogaLevel(List<EntityInternal> entities, String id){
        myEntities = FXCollections.observableArrayList(entities);
        myId = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableList<EntityInternal> getEntities() {
        return myEntities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEntity(EntityInternal e) {
        myEntities.removeAll(List.of(e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEntity(EntityInternal e) {
        if (!myEntities.contains(e)) {
            myEntities.add(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nextLevelID() {
        return myNextLevelId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNextLevelID(String nextID) {
        myNextLevelId = nextID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLevelId() {
        return myId;
    }
}
