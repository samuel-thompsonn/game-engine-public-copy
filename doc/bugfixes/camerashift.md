## Description

The bug did not allow for the camera to shift in our games in the y direction

## Expected Behavior

I expect the behavior for the camera to move in the y direction if coded in the xml file

## Current Behavior

The camera only moves in the positive or negative x direction

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Create a shift entity behavior for any character that moves
 2. Make the shift correspond to the Y values of the characters position
 3. Camera does not move in the Y direction

## Failure Logs

N/A

## Hypothesis for Fixing the Bug

Describe the test you think will verify this bug and the code fix you believe will solve this issue.
A test that sets the camerShiftValues to be nonxero for X and zero for Y. Then it gets the values and tests that they are equal.