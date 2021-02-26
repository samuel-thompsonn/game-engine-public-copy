## Description

Summary of the feature's bug (without describing implementation details)

Entity variables that are supposed to be updated automatically (XPos, YPos, XVelocity, YVelocity, Width, Height) are
not getting added to the entity variables at all.

## Expected Behavior

Describe the behavior you expect

I expect that when I set another variable like score to the value of an entity's Width, it would produce something 
like score=50 (if entity width was 50).

## Current Behavior

Describe the actual behavior

The variable is being set to the string "Width" meaning the entity does not have variable Width.

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Give sonic an action with effect SetVariable Lives Width.
 1. Lives display will not change (since the string Width can't be formatted as a number).

## Failure Logs

Include any relevant print statements or stack traces.

N/A

## Hypothesis for Fixing the Bug

Describe the test you think will verify this bug and the code fix you believe will solve this issue.

I have pretty much already described the test. I created it in entity test, it's called automaticVariablesTest.
The fix is simple: ImageEntity overrides the updateAutomaticVariables method but does not call the super method or
add Image to its variables. Also, the method in OogaEntity is using the actual DoubleProperty for some of them instead
of the double value.