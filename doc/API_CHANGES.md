# API Changes

## Data
- Added method ``getPauseButtonImage`` in DataReader to fetch an Image object representing
the pause button
- 4/7/20 Removed ``getPauseButtonImage`` in DataReader because view is going to do that on its own
- 4/7/20 Changed ``getGameFiles`` to ``getGameFilePaths`` in DataReader to make the purpose more explicit
- 4/7/20 Changed ``getThumbnails`` in DataReader to return a list of Thumbnails instead of Strings
- 4/7/20 Added ``getBasicGameInfo`` in DataReader which takes a game's name and give it basic info. More description in the API.
- DataReader getThumbnails no longer takes String arg
- 4/8/20 Changed ``loadGame`` to ``loadLevel`` in DataReader according to a change we made in a meeting to how 
DataReader loads and reports information. Changed its description and return type accordingly.
- 4/8/20 Changed ``loadLevel`` so that it takes a Game name and a level ID instead of a file path.
- 4/9/20 Added ``getEntityMap`` so that the Game can ask for the map of entity definitions needed to
create new Entity instances.
- 4/14/20 Added ``getProfiles`` so View can ask for a list of the existing users
- 4/14/20 Changed the return type of ``getProfiles`` to use the same class as View
- 4/15/20 Changed return type of ``getBasicGameInfo`` to be list of list of strings, to allow
it to return variables in addition to level IDs
- 4/21/20 Added ``addNewProfile``
- 4/21/20 Replaced ``getBasicGameInfo`` with two new methods: ``getLevelIDs`` and ``getVariableMap``. Keeping 
``getBasicGameInfo`` as a private method.
- 4/24/20 Change    ``addNewProfile`` signiture from taking in an oogaProfile to taking in a string containing the profile name and a file containing the photo because
view shouldn't know about oogaProfiles

## Game API
- getEntities now returns an observable list instead of list
- added method ``makeUserInputListener`` to create a listener that will react to inputs
- note that a lot of the methods in UserInputListener are unnecessary, like ReactToGameSelect,
since the game will always know what game it is running and is not responsible for choosing game
- added method ``reactToPauseButton``
- UserInputListener method probably shouldn't take a String input, as the Game should be
responsible for determining where to save
- now has a doGameStep method that view calls during its step method
- 4/12/2020: Added time as a param to collision loop (but will probably removed 
collision loop from public side of GAME).
- 4/14/2020: Removed ``doUpdateLoop`` from external game interface, since from the outside
one need only call ``doGameStep``.
- 4/14/2020: Removed ``doCollisionLoop`` from external game interface for the same reason
- 4/14/2020: Removed ``handleUserInput``, since that's part of the UserInputListener interface of
the game.
- 4/16/2020 added ``doConditionalBehaviors`` to entity (and some resulting changes related to
conditional behaviors, mainly internal game API and new behavior interfaces and classes)
- 4/22/2020 changed game variables from map with double values to map with string values 
- 4/22/2020 changed game variables from map with double values to map with string values
-4/23/2020 Added CameraProperty methods ``setCameraShiftProperty(List<DoubleProperty> property)``  , ``public void setCameraShiftValue(double xShift, double yShift)``
, ``getCameraShiftProperties()``, `` List<Double> getCameraShiftValues()``
- 4/24/2020: Removed ``reactToGameQuit`` since closing a game means getting rid of that object.
                                  
                                        
                                          
- Changed ``loadLevel`` to ``loadNewLevel`` and added ``loadSavedLevel``
- Added ``getGameSaves`` which gives information about all of the saves for a given user and game


### Entity
- Added ``move`` method so that entities can move in their movement behavior.
- Added ``getPosition`` method so that a JUnit test could check whether ``updateSelf`` was working for
behaviors that change the entity's position.
- Added ``setCollisionBehaviors`` method so that an entity can know its behavior when colliding
with another entity, and have that behavior swapped out at runtime.
- Added ``destroySelf`` so that behaviors can cause entities to be removed from the level.
- Added ``setPosition`` so that behaviors with access to the entity can teleport it around the level.
- Added several properties including activeInView which determines whether the entity will be
displayed
- Added ``getHeight`` and ``getWidth`` so that collision detection can do its job 
under the assumption of square collisions.
- Added ``Entity`` abstract class methods to ``EntityAPI``, so that we can hopefully unite the two.
The motivation was that View relies on Entities, but it asks the Game interface to get entities
as a list, and I don't want Game to rely on implementation details of EntityAPI (even though Entity
is abstract). The ultimate solution is to make a distinction between the 'front-facing' part of Entity
and the rest.
- Added ``getName`` so that collisions can use the name of the entity.
- Added ``getVelocity`` so that the Entity knows it 
- 4/10/2020: Removed ``moveByVelocity`` since it's better to assume that velocity is
applied internally each frame.
- 4/12/2020: Added ``executeMovement`` to serve as ``moveByVelocity`` since it's
better when actual movement happens after all logic.
- 4/14/2020: Added ``reactToControlsPressed`` to have different types of control reactions
to inputs (reacting to a press is better for jumping).
- 4/14/2020: Added ``createEntity`` and ``popCreatedEntities`` so that new
entities can be created by behaviors.
- 4/15/2020: Added ``reactToVariables`` so that entities can react to variable values each frame.
- 4/16/2020: Added ``hasCollision`` so that the collision loop can avoid being hung up on nonexistant
collisions and ignoring real ones.
-4/18/20 Set ``hasCollision`` to always return true because it was messing up conditional collision
behaviors
-4/19/20 Added ``blockInDirection`` to change the value of an entity's blocked status map. Essentially
this allows/disallows the entity from moving in that direction
-4/21/20 Added myVariables to entity with add and get methods
-4/22/20 Added ``getEntityID`` to give the variable value mapped to entity variable "ID"
-4/22/20 Added method to get a map of all an entity's variables.
- 4/25/2020 Split Entity into internal and external parts so that internal side has more
access.
- 4/25/2020 Removed ``reactToPauseButton`` from UserInputListener since it is never implemented.

### Level
- Added ``removeEntity`` so that the game can remove destroyed entities.
The alternative would be internal 'garbage collection' inside level, but that would also require
at least one new method.
- Added ``addEntity`` so that the main game loop can involve entities being instantiated.
- Added ``setNextLevelID`` so that they can point to a next level.
- 4/22/2020: Probably decided to get rid of level end conditions as a level-driven behavior. Instead it will be
driven by entities.
### ControlsBehavior
- Modified ``reactToControls()`` to take in the subject entity as a parameter.


### MovementBehavior
- Added ``setTarget`` method as a way to resolve an issue where an Entity needed to know 
movement behavior and a movement behavior needed to know what entity it was modifying.
- Removed ``setTarget`` 
- Added a second parameter "Entity subject"  in ``doMovementUpdate`` method as another solution to how the MovementBehavior knows which Entity it
is updating 

### CollisionBehavior
- 4/9/2020: Modified ``handleCollision`` to take an entity as a paramter, so that
it can have an effect on whatever entity calls it. Possibly could change to EntityInternal.
Also could change to take two entities, since two are involved in a collision.

### Profile
- 4/10/20 added Profile API to start implementing player profiles
- 4/10/20 added  the following methods
    ``setProfilePhoto(String photoPath)``
    ``setProfileName(String name)``
    ``setStats(Map<String,Integer> stats)``
    ``getStats()``
    ``updateStats(String gameName, Integer highScore)``
    ``getGameStats(String gameName)``
    ``getProfileName()``
    
-4/12/20 added ``getProfilePhotoPath()`` that returns a string with the filePath to the photo

### CollisionDetector
- 4/12/2020: Added detection for sideways and vertical collisions.


