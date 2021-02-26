## Description

If user does not select a new profilePhoto then there is a printStackTrace

## Expected Behavior

Nothing will happen

## Current Behavior
Nothing happens but there is an IllegalArguementExceptions
## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Create a new Profile
 1. Open filechooser
 3. hit cancel 

## Failure Logs

Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: input == null!
	at java.desktop/javax.imageio.ImageIO.read(ImageIO.java:1305)
	at ooga.view.ViewProfile.setNewProfilePhoto(ViewProfile.java:136)
	at ooga.view.ViewProfile.handleChosenPhoto(ViewProfile.java:123)
	at ooga.view.ViewProfile.lambda$setNameAndPhoto$0(ViewProfile.java:114)
	at javafx.base/com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86)
	at javafx.base/com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:238)
	at javafx.base/com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191)
	at javafx.base/com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)


## Hypothesis for Fixing the Bug
Checking if file is null before proceeding