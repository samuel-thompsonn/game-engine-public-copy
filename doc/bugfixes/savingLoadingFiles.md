## Description

Summary of the feature's bug (without describing implementation details)

When a file was saved, this bug was preventing the user from seeing the file 
as an option when they reopened the game, so they were never able to try to load it.

Both the process for saving and loading were implemented, but there was a small problem
at the time this project was submitted that prevented the user from ever being able to 
access their saves, thus making those features invisible.

## Expected Behavior

Describe the behavior you expect

The user pauses a game and presses Save. The back end then creates a new .xml file storing 
the current state of the game and then adds the path to that file and the current date and time
to the SavedGameStates section on the user's file. 

Then, when the user returns to the home screen and selects the game, between "New Game" and "Back" 
there is a button with the saved level's ID and the date and time it was saved. Pressing
this button loads the level exactly as it was saved on that day, and the game continues from
that frame.

## Current Behavior

Describe the actual behavior

The user can save the level as described, but after the saved file is made, ``XMLGameRecorder.saveUserFile()`` 
isn't correctly adding the filepath and date information to the user's file, so when the 
user returns to the main menu and then clicks on the game, and the front end asks the back end
when saves the user has, the back end doesn't find the save listed in the user file and reports that 
the user has no saves.

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

Open a game. Press pause. Press Save. Close the game. Return to the main menu. Press on the same
game again. Observe whether the option appears to load the save made seconds ago.

## Failure Logs

Include any relevant print statements or stack traces.

NA

## Hypothesis for Fixing the Bug

Describe the test you think will verify this bug and the code fix you believe will solve this issue.

Adding the same 5 lines to the end of ``XMLGameRecorder.saveUserFile()`` as there are at the and 
of ``XMLGameRecorder.saveLevel()``. After creating the proper elements for the xml file, these 5 lines
actually create these changes in the specified directory. ``saveUserFile()`` doesn't have those 5 lines,
so none of the Elements that it adds are actually being put onto the user's file. Adding those 5 lines and 
changing the parameters slightly to suit this situation should change that.

To test this, just try to save a game and then access the save as described above.
