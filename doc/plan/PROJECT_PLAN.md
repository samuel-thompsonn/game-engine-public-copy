# Implementation Plan

### Names & NetIDs

Chris Warren (ccw43)
Braeden Ward (bmw54)
Cary Shindell (css57)
Sam Thompson (stt13)
Doherty Guirand (dg211)

### Major APIs
- Engine/Assembler: Instantiates other interfaces and moderates interactions between them. Binds the view to the model, and responds to user input as a listener. Owns ``main()``.
- Game: Handles the generic internal workings of platformers, such as collisions, updating entities, level progression, and higher-level game data like number of lives.
    - Also desires a slightly different API for allowing the user to make levels in a GUI rather than by typing out files.
- Data: Handles the loading of games and game-related data from files, and saving to files.
- View: Handles displaying data from the currently active Game(s) on the screen.
    - Also handles detecting user input and transmitting it to the rest of the program.

### Features

#### Basic
- Ability to play a game based on loading a file specified by the user.
- The differences between any two games is within the game data files (no code that is only for one game).
- Ability for a game to displayed onscreen accurately in real time, with a mobile camera.
- Ability to interact with the game using controls to move and jump.
- The user can switch from game to game without rerunning the program.
- At least 7 (2 + 1 per team member) runnable example scrolling platformers.

#### Complete
- Ability to play, display, and interact with multiple games at once.
- Abiltiy to save the state of a game as a save file that can be loaded.
- An interactive basic level editor (Can be earlier if it saves time to have this for debugging)
- Ability to keep track of high scores or other game-level permanent data and attach it to a player profile, and have multiple player profiles.
- Difficulty systems within games

### Responsibilities

Doherty: Game primary, View secondary
Cary: 
Sam: Game primary, Data secondary.
Braeden: Data primary, Game secondary
Chris: View Primary, Game secondary
