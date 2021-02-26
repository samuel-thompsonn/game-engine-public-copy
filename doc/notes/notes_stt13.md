# Sam Design Notes

## Game API
- First issue: Allowing the entity to control whether it will be dstroyed, and allowing it to create new entities 
    - Possible solution: Have the entities be able to return whether they have been
    destroyed by adding a new method to the API
        - This would also have entities save other entities that can then be accessed
        in the part of the loop where new entities are created.
    - Worse possible solution: Give the entities access to the entities list or to some version of
    it. This has the advantage that adding entities becomes easier.
- Second issue: Allowing entities to have state information so that acceleration can take place
- Third issue: Allowing entities to react to collisions dynamically based on the other entity's
state. i.e., having their downward velocity canceled when they fall onto floor, otherwise having
their sideways velocity canceled.

- added a resource file called inputs
    - the key in the file is the user input 
    - the value is the keyinput for backend 
    - this way there can be multiple inputs that map to the same keyinput for backend
   
   
- Need to figure out if entity is touching the ground before it jumps 

- Notes for Google Dino game:
    - Entities: Dino and Cactus
    - Dino entity: Has jumping for movement (and ducking), has gravity for falling, 
    has interaction with Cactus that causes Dino to stop existing.
    
- Notes from meeting on 4/9/2020:
    - I can make one generic 'updateGameByOneFrame' method in Game so that the view only has to 
    call one thing.
    
- Notes 4/16/2020:
    - I need to do the following things: 
        - Get loading levels to not be buggy with binding
        - Add level end condition support
        - Modify Mario to show off what we have working
        - Add new behaviors as necessary