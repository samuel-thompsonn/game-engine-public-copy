# Use Cases

### Names & NetIDs
Sam Thompson (stt13)
Chris Warren (ccw43)
Braeden Ward (bmw54)
Cary Shindell (css57)
Doherty Guirand (dg211)

### stt13 Use Cases

1. The user starts the program, and a list of game thumbnails appears onscreen.

    When the program starts, the Engine instantiates a DataReader, and it tells the DataReader what folder to look inside for thumbnails using ``getThumbnails``. The DataReader then returns a list of images, which are passed to the view, which uses internal methods to draw the images onscreen.
    
2. The program attempts to load a game, but the game doesn't have any levels defined.

    The main Engine calls ``loadGame`` on its owned DataReader using a specified filepath. The DataReader examines the game file and throws a ``DataReaderException`` describing that there are no defined levels. The Engine handles the exception by telling its View to display the error message text from the DataReader, and no game is loaded.
    
3. The user pauses a currently active game by pressing Escape.

    The View detects the input and notifies its list of ViewListeners that the Pause key has been pressed. The Engine uses ``TellEntities`` to tell the view to display its pause menu options. It also pauses its timeline, which causes it to stop executing ``doUpdateLoop`` and ``doCollisionLoop``.
    
4. In Super Mario Bros, small Mario touches a mushroom, which causes him to become big Mario.

    During the ``doCollisionLoop`` method, the Game identifies that the two entities are colliding, and it notifies them and gives them each the name of the other entity. Each responds, based on its list of mappings from entity names to behaviors. In response to touching a Mario entity, the mushroom is removed from the game. In response to touching a Mushroom, the Mario is removed, and a BigMario entity appears in its place.
    
5. In the pause menu, the user clicks the Exit button to quit without saving.

    The UI part of the View notifies all of its UserInputListeners that QuitButton has been pressed, activating the ``handleQuit()`` method on the Engine (which extends UserInputListener). The engine sets its current game to none, causing it to fall back to its main menu state (which is itself an object made up of entities, with its own properties, but is not a Game). The Engine binds information to the View about the main menu rather than about a game.
    
6. The user uses a 'Load Game' button in the main menu, and selects a valid game file for Doodle Jump from a USB drive.

    The user input component of the front end handles it the same way as when a thumbnail is clicked. The EventHandler for the file loading button is the same as for thumbnails, so the View notifies its UserInputListeners by calling its ``handleGameLoaded`` method with the filepath of the game. Engine tells its DataReader to ``loadGame``, sets its currently active game to the return value, and begins its Timeline.
    
7. While a game is paused, the user selects the 'Save Game' button, and the game state is saved to a file.

    A method in ``userInputListener`` implemented by Engine handles the event (as detected by the front end) by handing its currently active Game to its DataReader and calling ``saveGame``. It uses a filepath that is stored in a resource/properties file. The DataReader returns nothing, which means that the game state was valid and correctly saved, since no exception was thrown.
    
8. In Mario, Small Mario runs into a koopa, causing the number of lives to decrease, Mario to respawn at the start of the level, and the Camera to go to where Mario is.

    The Game's ``doCollisionLoop`` method identifies the collision and its type and tells the SmallMario and Koopa entities about each others' names and calls ``handleCollision``. The Koopa has no reaction, and small Mario reacts by changing position to the level's start. (TO BE RESOLVED: HOW DOES LEVEL-ORIENTED DATA GET MODIFIED BY ENTITY-ORIENTED EVENTS?)
    
### dg211 Use Cases

1. In Mario, a goomba moves forward.

    The Game's, ``doUpdateLoop`` method will update each entity by using its ``updateSelf`` method. 

2. The user clicks a thumbnail, and a game is launched.

    Each thumbnail is tied to a specific game in the DataReader. The selected thumbnail is sent to the DataReader and its ``loadGame`` method is called with the proper filename that contains the contents of the game.

3. The user pauses the game, then unpauses.
The View receives the input and notifies the ViewListeners that the Pause key has been pressed. The Engine uses ``TellEntities`` to tell the view to display its pause menu options. It also pauses its timeline, which causes it to stop executing ``doUpdateLoop`` and ``doCollisionLoop``. When the user unpauses, the View receives the input and notifies the ViewListeners that the Resume key has been pressed. The Engine uses``TellEntities`` to remove the pause menu and reveals the most recent updates. The timeline is played, and the ``doUpdateLoop`` and ``doCollisionLoop`` methods are told to start again. 
4. The user presses the 'up' key to jump.
The View receives the input and notifies the ViewListeners that the 'up' key has been pressed. Inside the Engine's ``doUpdateLoop`` each Entity will call ``reactToControls`` and ``updateSelf``.  The entities that are set to react to the 'up' key will react. For example, only Mario will jump and not the goombas. 
5. The user tries to run off the screen. For example, Mario uses two walls to jump out of the top of the screen. 
During each Entity's ``updateSelf`` method, the Entity will see that it has made it off of the screen and will force the Entity to return. So in this example, Mario will fall.
6. Both Watergirl and Fireboy touch fire. 
This is an example of two entities having different reactions to the same Entity. In the Engine's ``doCollisionLoop`` the Game will see that two entities are colliding, twice. For each collision, (Fireboy/fire and Watergirl/fire) it will notify both Entities and gives them each the name of the other Entity. Therefore, Fireboy will know that it's colliding with fire and fire will know it's colliding with Fireboy. The same will be true for the Watergirl/fire collision. Each Entity will then respond based on its prescribes reaction to its colliding Entity. In this case, the fire will not react to colliding with Fireboy nor Watergirl. Fireboy will not react to colliding with fire. Watergirl will react to colliding with fire because inside the Watergirl Entity there is a negative behavior mapped to fire. 
7. The leading character (for example Mario) takes two of the same powerup at once. 
Each Entity will have mappings that prescribe certain reactions to collisions with other Entities. When the character takes the first powerup. The old Entity will be removed and the powerup version of that Entity will take its place. The rules of each game will decide a second powerup will affect gameplay or not by changing the mapping that prescribes the reactions to collisions. For example, say Mario colliding with two flowers at once allows for him to be hit by an enemy twice before losing a life. Then when Mario collides with the first flower, the regular Mario Entity will be replaced by the Big Mario Entity. The Big Mario will have a prescribed reaction to mushrooms that will increase the number of times it can be hit by an enemy before it turns to regular Mario again. Therefore, when Big Mario collides with the second flower, Big Mario's enemy tolerance will have increased.  
8. A two-player game is selected.
The View will contain ViewListeners that will react to user input. The ViewListeners will include KeyListeners so that the View knows when keys such as 'space' or 'right' have been pressed. This means that each of the keys could be connected to a certain interaction in the game. Each Entity will contain a mapping that enumerates the keys to which it is supposed to react and what reaction is required. Therefore, one player can use the 'up', 'right', 'left', 'down' keys while the second player can use keys such as 'w',' a', 's', and 'd'. The keys that each player will react to will be enumerated in the data file that contains the contents of the game.

### bmw54 Use Cases
1. An enemy launches a projectile at the player.
    Each entity has access to a list of entities in the level in the form of an interface with restricted abilities. The entity can either add an entity to the list, or delete itself. In the game document, the enemy designed to create projectiles would be given the ID of the entity it is creating. As part of its ``updateSelf``, it would create a copy of this entity and add it to the list. This new entity could behave in many ways. We would likely have a certain AI that just acts like a bullet, moving forward until it encounters an obsticle.
    
2. The user tries to load a game file which doesn't have an image for one of its enemies.
    When the user tries to load the game, the ``DataReader`` begins going through the file and creating the game. This includes creating all of the enemies. When the ``DataReader`` is given the the name of an image file that doesn't exist in the game's folder, the ``DataReader`` would throw a ``DataReaderException`` describing that the image file wasn't given correctly, and the user is returned to the main menu. No game is loaded.

3. A fireball hits an enemy, destroying it.
    When described in the game file, each entity is given a list of entities that it can be destroyed by. In its ``updateSelf``, entities will be able to check if they are colliding with any of these entities. In that case, the entity removes itself from the list of entities in the game.

4. The player tries to walk to the left when there is a wall in the way
    The ``updateSelf`` method of the player AI will check the player's next state and will only allow the player to move if there is no collision with terrain in that direction. This is also how the player stops falling when they land on the ground.

5. The game includes entities that react to the user input besides the player. For example, a door that opens whenever you press the jump button.
    All entities will be assigned an AI that responds to input from the controller. Most of them will likely be assigned an AI that does nothing in response to player input, but others will be able to move or change.
    
6. The game has many player entities that all run and jump with the input from the user.
    As mentioned above, every entity is assigned a way to react to user input. One of these AIs will lay out how the player moves and jumps. Having a second player is as easy as creating two copies of this entity in the level, both with the given AI. 

7. The game has terrain that moves. For example a playform that moves back and forth.
    This terrain would be an entity whos collision AI holds the player in place on the platform so it doesn't slide out from under them.

8. A level has multiple ways of ending, for example, two exit doors, either of which ends the level.
    To handle complicated end conditions, we plan to allow the game designer to specify some simple AND/OR logic in the game file when setting the rules for ending the level. They would be choosing from a palete of possible end conditions and can choose to say "x AND (y OR z)." Our plan is to make a simple parser which checks for this and creates a level with teh correct level end conditions. In this case, the designer could specify that the level ends when the player collides with entity "door1" OR collides with entity "door2."

### css57 Use Cases

1. The player touches something like a flag pole which ends the level and needs an animation
for the player sliding down it. Entities will have set interactions and there will be a EndCondition
class to define when the level ends.

2. The user presses the up arrow.
    The view sees that the up arrow has been pressed so it notifies the input listener and
    updates it to tell it "up" was the input. The Game is then notified there was an "up"
    input so it consults the resource files and runs the method that corresponds to up input.
    
3. The player collects a coin item.
    The Game updates 2 entities: the coin entity, so that it is no longer displayed, and the
    coin counter. The property bindings in the View update the display of these entities accordingly.
    
4. The player moves left and a new entity appears on screen.
    The Game fetches the new entity from the DataReader and adds it to the list of existing entities.
    The property binding on the list size in the View is activated and the new entity is created
    and displayed in the View. This may require an API add entity method, and a property binding to
    an entity object to be added to the entity list.
    
5. A player entity moves right.
    The player entity position property is updated in the backend. The binding in the front end,
    which is one of many automatically bound properties, is updated and the entity is redrawn.
    
6. The game is lost.
    A game over message and final score is displayed and all the level and entity info is cleared.
    Eventually return to the main menu.
    
7. The user clicks on an entity.
    Nothing happens to the entity in the front end. The user input is transmitted to the back end
    as normal, and it includes the mouse position. The back end checks to see which entities
    if any are at the mouse position and executes entities' onClicked methods.
    
8. The player entity warps from one side of the screen across to the other.
    A collision is detected between the player entity and the background/wall/camera entity.
    The interaction is defined in the data files such that the player position changes to the other
    side of the screen.
    
### ccw43 Use Cases
1. The player wants to save the current progress of the game and come back later. The user pauses the game with a keystroke, and chooses rhe option to save. The engine saves the level on which the user was at. When coming back, the user selects the game and then selects the load game option, bringing them back to the point.

2. User completes the game. The screen congratulates the user and brings them back to the home screen.

3. The user wants to enter cheat codes. We have an options button on the home screen which cheat codes are a selection. 
Player is prompted to enter a keystroke that can give  a user infinite lives.

4. Player presses both the left and right button at the same time. The player should stand still and not move in either direction.

5. The game allows the user to go off the screen and come in on the opposite side. The algortihm must recognize that the 
player is off the screen and return him going in the same direction on the opposite side of the screen

6. The user hovers over the game that they are about to play in the home screen. The screen should slightly increase in 
size as if it is highlighted and play a clip of the game int he background of the square.

7. The game has a checkpoint within the level. This allows for users to die past that checkpoint and respawn their character 
at the place where the checkpoint is while resetting the level up to that point.

8. In a game, the player lands on top of an entity. The user would not get injured or lose a life but the entity would get destroyed.

