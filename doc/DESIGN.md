# DESIGN document
* names of all people who worked on the project

Names: Cary Shindell (css57), Sam Thompson (stt13), Doherty Guirand (dg211), Chris Warren (ccw43), Braeden Ward (bmw54)

* each person's role in developing the project

css57 - setting up basic view/visualizer functionality (communication w back end and
updating display, camera), updating and refactoring data reader, conditional behaviors/effects
and collision in back end, fireboy and watergirl game.

stt13 - Game external & internal API, game loop implementation, Entity API and implementation, input processing,
some game file reader and game API refactoring, some effects, helped set up conditional
behaviors, Mario game.

dg211 - View menus and instantiating a game (selecting a profile, a game, and a past save). 
Profile API and implementation in view. Camera shifting. VVVVV game.

ccw43 - View API, setting up profile and selecting a profile in profile API, Game specific behaviors, flappy bird,
dino game, doodle jump

bmw54 - DataReader; The methods that involved writing to or reading from data on the computer. This included reading and writing xml files as well as navigating and creating directories.


* what are the project's design goals, specifically what kinds of new features did you want to make easy to add

    * New scrolling platformer games with unique enemies, items, player controls, camera effects, visuals, etc.
    * New controls schemes that map input to standardized codes
    * New effects that can change in-game entities, variables, or levels.
    * New algorithms for detecting collisions in-game
    * To a lesser extent, different game loop by swapping out concrete Game classes. Also to a lesser extent, different types of game file to represent the same game information in different ways (Using files other than XML, or other XML tags) by swapping out data reading concrete classes.
* describe the high-level design of your project, focusing on the purpose and interaction of the core classes

    The Visualizer, which handles showing information to the user and communicating UI interaction to the rest of the program, interacts with the ProfileReaderExternal to get the user profiles, then communicates with GameDataReaderExternal to find out the possible game files that the user can select. The Visualizer then makes a ViewerGame to handle showing a game to the user by translating Entities into ViewEntities. The Game uses GameDataReaderExternal to translate a game file into Levels, and the Visualizer/ViewerGame asks the Game to update itself and react to user input. 
    The Game takes the Level's Entities (as EntityInternal) and gives them the information to do their behaviors based on variables and entity conditions. The Entities pass this information to behaviors, which pass it to their conditions so they know whether to execute, and the behavior passes information and pointers to itself and the GameInternal so that a behavior's Effects can execute.
    When the user tells the program to save a game, the GameRecorder uses the Game to create a file representing the current level and variable states.
    
* what assumptions or decisions were made to simplify your project's design, especially those that affected adding required features

    * It was assumed that some physics things might be universal, such as friction.
    * We assumed that everything would be 2D.
    * We assumed that the depth of entities could be in the order that they were listed in the file, such that two entities wouldn't need to have the same depth.
    * We assumed that only one level would need to be loaded and running at once.
    * We assumed that for dark mode we just needed to change the overall colors, rather than having a distinct image for each game entity.
    * We assumed that data files would be in xml format but set up the data reader APIs to make it easy to add data readers for other file types.
    * We assumed that the user profiles would want to keep track of score and that games would thus use a Score variable to keep track of whatever formation they wanted attached to player profiles.
    * We assumed that no games would expect certain sound effects or music to play.
    * This isn't a hard assumption, but the APIs would have to change slightly in order to support sound effects
    * We assumed that game designers would not have to change the names of certain entity variables, such as XPos, XVelocity, width. We also assumed that designers would recognize "TerrainID", another hard-coded variable name which is used directly in behavior effects.
    * We assumed that the user would not want to use their inputs to control multiple games at once, that all inputs would be buttons or mouse click (no interpretation of analogue like controllers/bumpers, and no interpretation of mouse movement or mouse button releases).
    * We assumed that the game designer would be responsible for accommodating multiple languages. All of the non-game specific text can be put to another language. The text entities that are created by each game cannot. 


* describe, in detail, how to add new features to your project, especially ones you were not able to complete by the deadline
    * How to add a new collision detection algorithm
        * Create a concrete class implementing CollisionDetector.
        * Implement ``getSupportedDirections`` to return the possible collision directions that this could detect.
        * Implement ``getCollisionDirection`` to return the direction of the collision (or null for no collision) when given two entities, using your new fancy algorithm.
        * Modify ``ViewerGame.setGame()`` to instantiate your new collision detector when creating its OogaGame.
    * How to add a new control keyboard input type
        * Add the new keyboard input type to the keyboard.properties file 
        (such as P = PunchKey)
        * Use new keyboard input type (PunchKey) in game data file
    * How to add a new Effect
        * Create a new class for the effect that either (a) implements Effect or (b) extends TimeDelayedEffect.
        * If it implements Effect, implement the doEffect method to acheive the desired effect using the tools provided by EntityInternal on the subject or target entity, GameInternal, or the modifiable map of game variables.
        * If it extends TimeDelayedEffect, do the same for ``doTimeDelayedEffect``.
        * Also implement processArgs() to set instance variables related to the effect when it is initialized (such as max speed).
        * Add an entry to src/ooga/game/behaviors/resources/effectdefaults.properties listing the number of arguments that are required for the effect to execute.
        * Add entries to src/ooga/data/resources/entityconstants.properties that maps the name of the class to its nicknames that can be used by game designers (for reflection).
    * How to add a different type of DataReader (that uses another file type).
        * First, create a new interface that implements the ``DataReader`` interface. See ``XMLDataReader`` as an example.
        * GameDataReader: create
    * How to add the ability to save and load files
        * The ability is there but there is a small problem. When saving a file, the ``GameRecorderExternal`` loops through the entities stored in the Level and stores their name, location, and variables in a new file
        * The problem comes when there are text entities, which have no name and must be stored differently. These are stored instead as image entities with no name. 
        * When the saved level is loaded, if there are text entities, the loading fails to load the entity with no name and ``GameDataReaderExternal`` throws an error which ``Game`` catches and responds by loading a new game instead.
        * To fix this problem you would just need to add the ability for ``GameRecorderExternal`` to ask each ``Entity`` if they are an image entity or a text entity. If they are a text entity, it should save them accordingly.