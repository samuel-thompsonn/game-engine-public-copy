## Description

Entities created at runtime always act as if they are stationary
with respect to the camera.

## Expected Behavior

When a Mario question block makes a mushroom or a fire flower, the 
created item doesn't move with the camera, and its collisions match up
with how its sprite looks onscreen.

## Current Behavior

When a Mario question block makes a mushroom or a fire flower, the 
created item appears to move with the camera, but its collisions act as if it has not
moved with the camera.

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Open the program and select any existing profile.
 2. In the games menu, select Super Mario Bros.
 3. Use Mario to hit a question mark block from underneath so that it makes an item.
 4. Move mario enough to scroll the screen left or right.

## Failure Logs

When a print statement prints a newly created fire flower's 
NonStationaryProperty, it is always 0.0 while the fire flower exists.

## Hypothesis for Fixing the Bug

* Describe a test that you think will verify this bug.
    
    See ``tests/ooga/data/ImageEntityBugTest.java``:
    Instantiate an arbitrary ImageEntityDefinition. Use its ``makeInstanceAt``
    method to make an ImageEntity. Check that the ImageEntity's 'stationary' property
    is the same as the ImageEntityDefinition's 'stationary' property. Do this for both 
    possible 'stationary' properties that the ImageEntityDefinition could have. Note that
    the 'stationary' property for when the definition has FALSE should be 1.0, and it should 
    be 0.0 when the definition has TRUE.
   
* Describe a code fix you believe will solve this issue.
    Modify ``ImageEntityDefinition.makeInstanceAt`` by adding a new line
    at line 39 and making it call newEntity's ``makeNonStationaryProperty`` with the 
    argument being the ImageEntityDefinition's "stationary" instance variable.
    