## Description

Adding a new profile does not add a new directory.

## Expected Behavior

That adding a new profile would create a new folder under data/users folder

## Current Behavior

A new folder is not being added 

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

 1. Running the program
 1. Choosing to add a new profile
 1. Clicking submit button after adding a name/photo

## Failure Logs

java.io.FileNotFoundException: data/users/Herdy/Herdyphoto (No such file or directory)
	at java.base/java.io.RandomAccessFile.open0(Native Method)
	at java.base/java.io.RandomAccessFile.open(RandomAccessFile.java:345)
	at java.base/java.io.RandomAccessFile.<init>(RandomAccessFile.java:259)
	at java.base/java.io.RandomAccessFile.<init>(RandomAccessFile.java:214)
	at java.desktop/javax.imageio.stream.FileImageOutputStream.<init>(FileImageOutputStream.java:69)
	at java.desktop/com.sun.imageio.spi.FileImageOutputStreamSpi.createOutputStreamInstance(FileImageOutputStreamSpi.java:55)
	at java.desktop/javax.imageio.ImageIO.createImageOutputStream(ImageIO.java:419)
	at java.desktop/javax.imageio.ImageIO.write(ImageIO.java:1549)
	at ooga.data.profiledatareaders.XMLProfileReader.addNewProfile(XMLProfileReader.java:52)
	at ooga.view.menus.ProfileMenu.addNewProfile(ProfileMenu.java:97)
	at ooga.view.menus.ProfileMenu.lambda$showNewProfileScreen$2(ProfileMenu.java:86)

## Hypothesis for Fixing the Bug
Fixing code to create the directory. It somehow got lost in merges. 
