## Description

This bug did not allow the new profile to have the proper image on screen.

## Expected Behavior

I expect the button associated with the new profile to have the image that the user chose.
## Current Behavior

The image that shows up is the default image because of an exception being thrown.
## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Create a new profile
 2. Notice that the image associated with the new profile is the default image
 3. Reload the program and notice that the image is the correct image

## Failure Logs

N/A

## Hypothesis for Fixing the Bug
Have the profile menu reload all of the new profiles instead of just adding the new one. 