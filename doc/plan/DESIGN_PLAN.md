# Design Plan

### Names & NetIDs
Sam Thompson (stt13)
Chris Warren (ccw43)
Braeden Ward (bmw54)
Cary Shindell (css57)
Doherty Guirand (dg211)



## Introduction
In this document we are discussing what we plan on including doing for our final CS308 project. This includes a description of our planned design, as well as a list of games that are scrolling platformer games that we will implement. The design goals for this project are to be as general as possible, so that any game can be played without changing code within. This would mean that the engine would have to be the most flexible part, where it can read in multiple datat files and configure the game in order for someone to play it immediately. 

## Overview
The program will have APIs for:
- Engine
    - Runs the 'model' of the game, takes in the player UI interaction and outputs information about the game state that is needed to (a) draw the info onscreen, (b) store save data?
    - Controls
        - In the main game loop, the engine lets every fknow all the user input every frame, and lets them react.
        - The engine subscribes to the user input from whatever UI-containing object it instantiates (a Visualizer object).
            - One option: The engine is constantly asked to react to user input with a ``reactToInput()`` method that has ALL the inputs as parameters (maybe as a map with entries like "Left","Right",etc.). It could also be done one method at a time, but this makes the interface very volatile.
    - Entities
        - Represents anything in the foreground that is not HUD and not terrain. Like spikes, enemies, the player, mushrooms
        - Knows its own size, location, other internal info.
        - Ideally, doesn't know the image that is used to draw it, but that info is combined with it in the front end.
        - Must react to what is happening in the game THIS frame.
            - Knows whether it's colliding with the player, or any other important collisions.
        - behaviors must know how to add and remove entities from the level.
    - CollisionBehavior
        - In the file, each entity is given a list of other entity names that are mapped to a specific behavior upon collision.
        - The FileReader probably wants to map a name of an entity to a LIST of behaviors, for flexibility.
        - Example: A Koopa's map would have MarioFireBall -> DeathBehavior
    - Player interaction behavior
        - We can make this a subset of CollisionBehavior
        - Represents how an entity reacts to COLLIDING with the player. Could be a mushroom that applies a powerup, could be an enemy that applies damage, could alternate between.
        - Entities that are terrain, like bricks or water or quicksand can all have their full effect here. (Example): When you hit the ground, you stop moving vertically, when you are in water, an upward force is applied.
        - Some interactions need to be able 
    - Entity movement behavior
        - There a bunch of different movement classes, each with a different AI (movement pattern, reaction to game data).
        - AI is defined in Java
        - Whenever an entity is enumerated in a file, it has to choose which one of these AIs to use.
    - Control Reaction Behavior
        - Describes how the entity will react to user inputs.
        - If an entity is defined without this behavior specified, it defaults to having no reaction.
        - EXAMPLE: Have class called GreenKoopa that walks on the ground, bounces off walls, and drops off of edges. The file specifies "GreenKoopa" within the definition of the enemy. If any parameters are needed (speed, position, etc.), those can be specified by the file
    - Level
        - Contains/owns entities, terrain, a background, maybe music, other internal data.
        - It starts and it ends. It tells the engine to go to another level. 
        - Maybe owns a list of subclasses of a LevelEndCondition abstract class, whose job is to define what the level is looking for so that it can end.
            - Example: PlayerEntityCondition Checks for a specific interaction between an entity and the player.
            - Example: If a boss is killed, end the level.
- Data
    - DataReader
        - When given a folder to check, it immediately checks for every game file, and figures out its thumnail, name, description, so that they can be shown in the main menu of the program.
        - BUT it doesn't load any file fully until asked.
        - Given a specific filename/file, can produce a Game that knows all the internal info about data, entities, levels, anything else specific to one game.
        - Also can use the SAME file to take in an entity name and output the image that uses it.
            - This is separate because the back end doesn't need to know what image file is used to represent an enemy (and some views don't, like the text view.) 
    - Type of data file used for storing values related to games: Sam votes that we use XML
    - Defines a menu of enemies, which are each XML items/entries, and gives them a number of properties, like speed, health, movement AI, player interaction.
    - Defines a list of named levels, and populates them with XML items that match the name of defined entities. For example, thousands of <name="brick", x="100" y="100">.
    - Recorder
        - Saves things like current level, items, health, etc. to files that can be read in a generic way and applied to the game.
        - text displayed in the GUI (e.g., to localize it or display it in a different language)
        - graphical icons used in game 
        - point values of game objectives 
        - number of levels, their starting configurations, and the order in which they are played
        - keys used for interaction **
    - Entity
        - Should define which of its properties are related to UI display
        - Shouldn't have any javafx here (only references to images, etc)
- Front End (draws things)
    - Objectives: Draw lots of images, including background, terrain, and entities.
    - What data does it need?
        - All HUD data, like number of lives.
        - All SPRITES/Entities, their locations, their sizes, their images
            - All players, enemies, items, projectiles, particles, terrain.
        - All tiles (might be entities)
        - The background
        - The viewport
    - Has data about entities, background, HUD bound to backend values, so that it can redraw whenever something changes.
    - Asks the data loader for images, asks the back end for entities and their locations.
        - It can ask an entity (or whatever it gets back from the engine) about its image, location, size, etc., and it controls the JavaFX root, so that it can draw an image there.
        - It should have a list of entities that is bound to a list of entities in the Game so that
        new entities are added in view whenever they are created in Game
            - View will have a method that binds each UI property of an entity
        - Entity will be part of Data but View will likely have its own version/subclass
    - Drawable (can just be an imageView)
        - Has a framerate of its animation, has many images, has a size, position, angle.
        - Reacts to time passing and its corresponding back end entity changing.
    - User Input
        - The engine, which initializes the front end, also signs up to be notified of and react to user input.
        - This should be part of some class which handles all bindings/interactions between Game and View
- Where is ``main()`` located?
    - The program is booted from View (not engine, because of property bindings)
    - View will make a DataReader, ask the data reader to read a file based on what the user wants to play 
    - View will make a Game and create property bindings and give it the DataReader instance



## Design Details
- Gravity
    - While most platforming games have a single gravity acceleration downward, some choose to have a gravity that can change in magnitude or direction, so we will choose to have a vector representing gravity which can be changed as the game goes on or can remain constant.
- Camera
    - Can follow the player, can center on a room, defines the size and location of things that get drawn onscreen.
- user inputs: engine will pass in a listener object to viewer constructor. That listener will be activated whenever a user input is detected in the front end, and will return to the backend a string/event (and possibly mouse position, etc) about what the input was. That string will map to a method in a resource file to define how that input is handled in the back end.

### UML Diagrams
#### Planning Day 1
![UML Diagram from day 1](https://i.imgur.com/FaA0g0R.png)

#### Planning Day 2
![UML Diagram from day 2](https://i.imgur.com/LpdMzVH.jpg)


## Example Games
### Key Examples
* Super Mario
    * Classic platformer, has one controllable character, large levels, many enemies, projectiles, powerups, collectible coins, score, multiple level win conditions, limited lives, moving but fixed size cameras.
    * Entity-to-entity interactions that don't involve the player, such as shells and fireballs hitting enemies.
    * if player saves and quites, person starts from beginning of level
    * This platformer defines major goals of our design.
* Doodle Jump
    * Different camera behavior. Vertical as opposed to horizontal
    * If the player goes off the screen to the right, they appear on the left
    * Player's high scores are shown during the game (Stretch goal)
    * platforms that can be moved by the user
    * The user can fire projectiles by tapping or clicking anywhere
* Fireboy and Watergirl
    - Two players. Each reacts differently to entities
    - Complicated ending requirements. In order to complete a level, all of the coins must be picked up and then both players must make it to the end of the level.

### Examples of Other Platformers
* Zelda
* Bounce Ball
* Super meat boy
* [VVVVVV](https://www.youtube.com/watch?v=sf06P-_1lkU)
* (flappy bird?)
* Sonic the Hedgehog
* Jetpack Joyride

### Things that all platformer games have / generalizations:
- A defined data-driven map, so maybe 'level' files?
    - Tiles of terrain on a GRID (Slopes are a stretch goal :) )
- 2d graphics sprites. images for backgrounds
- One or more movable player characters
- Controls (by controller or keyboard or anything)
- A scrolling camera that follows the player. Constantly having the player in the center or swapping from room to room.
- levels with set beginnings and endings with a main menu between them
- entities, including the player sprite, enemy sprites. Can be removable, can be invincible.
- player state (lives, health, score)


## Design Considerations
- Is there a controller that owns Engines and front ends?
    - Is the engine the controller? Does it own instances of games? 
    - The advantage of having a Game that stores all of the active info about a game, is that we know that getting rid of that Game object will get rid of all the data currently in it, so we don't need to worry about data persisting.
    - The engine can instantiate Game objects, based on the output from DataReader objects, and it can instantiate Views.
- How do you have non-player entities interact with each other?
    - Example: Fireballs interact with collisions with enemies by killing the enemies. How do you put that in a generic collision loop?
- Property bindings for front end display of data?
    - The front end API has methods for binding everything necessary, ideally so that you can bind a different level to draw, since the active level in the game can change.
- Level class
- Objects that affect the player when they're in them (e.g. water, sand)
    - Or deal with in collisions?
- Gravity
- Items (coins, rings) and win conditions
- Should terrain and entities be the same thing?
- How to load or create a level?
- Diificulty levels for enemies - different AIs/algorithms or just changing enemy parameters?
    - Changing parameters is probably more scalable
- Split up AI into targeting and movement?
- Ending a level is a unique entity property?
    - Better: have the level define the end condition, each level has a EndCondition class
    - Mixing and matching level end conditions
    - Have to define valid possible end conditions for config file to choose from (e.g. a certain number of items obtained, boss killed, player position, time, etc)
    - Engine class should cons!
tantly check for end conditions
- Entities that cause specific animations
    - This is related to interactions
- Algorithmically generated levels, like in Doodle Jump
    - Would be very hard to mix with the fact that an individual game describes the catalogue of entities.
    - You would want level creation to be abstracted, so that there are mutliple types of level building, each being its own object.
        - One object is a ManualLevelBuilder, which causes the DataReader to expect a list of named XML Elements.
        - Another object is ProceduralLevelBuilder, and there could be several subclasses, and they would expect input like what the ground is, what enemies can be placed, how large the level is (could be infinite?).
- How can entities impact each other without colliding? 
    - They want access to a list of all the other entities on the level, and they might even want the power to modify this list in any way they want.
- Details about saving the game to a file (probably a later feature?)
- Details about giving entities access to level- or game-based data, so that entity interactions can change how many lives are left.

## Use Cases (TO BE MOVED TO ``USE_CASES.md``)

### Potential Use Cases
- The program starts up, and a sequence of thumbnails is displayed onscreen.
- The user clicks a thumbnail, and a game is launched.
- Errors occur in reading a thumbnail.
- Errors occur in reading a game file.
- The user pauses the game, then unpauses.
- The user pauses the game, and exits back to the menu without saving.
- While the game is paused, the user saves the game.
- The user launches a game and loads a save file for that game, all at once.
- The user manually selects a game to load from an attached USB drive, rather than clicking on one of the thumbnails (optional).
- The user presses the 'up' key to jump.
- In Mario, a goomba moves forward.
- A fireball hits a goomba, destroying it.
- Mario collects a powerup, changing his sprite.
- A level ends due to all enemies being destroyed, and the next level starts.

### stt13
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
### dg211
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

### bmw54
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
    
### ccw43
1. The player wants to save the current progress of the game and come back later. The user pauses the game with a keystroke, and chooses rhe option to save. The engine saves the level on which the user was at. When coming back, the user selects the game and then selects the load game option, bringing them back to the point.

2. User completes the game. The screen congratulates the user and brings them back to the home screen.

3. The user wants to enter cheat codes. We have an options button on the home screen which cheat codes are a selection. Player is prompted to enter a keystroke that can give  a user infinite lives.
4. Player presses both the left and right button at the same time. The player should stand still and not move in eithr direction.
5. The game allows the user to go off the screen and come in on the opposite side. The algortihm must recognize that the player is off the screen and return him going in the same direction on the opposite side of the screen
6. The user hovers over the game that they are about to play in the home screen. The screen should slightly increase in size as if it is highlighted and play a clip of the game int he background of the square.
7. The game has a checkpoint within the level. This allows for users to die past that checkpoint and respawn their character at the place where the checkpoint is while resetting the level up to that point 
