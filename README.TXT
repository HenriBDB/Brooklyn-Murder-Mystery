This is a project part of the book: Objects First with Java A Practical Introduction Using BlueJ
Described as following:

-------------------------------------------------------------------------
Project: zuul-better
Authors: Michael Kölling and David J. Barnes

This project is part of the material for the book

   Objects First with Java - A Practical Introduction using BlueJ
   Sixth edition
   David J. Barnes and Michael Kölling
   Pearson Education, 2016
   
This project is a simple framework for an adventure game. In this version,
it has a few rooms and the ability for a player to walk between these rooms.
That's all.

To start this application, create an instance of class "Game" and call its
"play" method.

This project was written as the starting point of a small Java project.

The goal is to extend the game:

  - add items to rooms (items may have weight)
  - add multiple players
  - add commands (pick, drop, examine, read, ...)
  - (anything you can think of, really...)

Read chapter 8 of the book to get a detailed description of the project.
-------------------------------------------------------------------------

My extension consists of:

- A Brooklyn Nine-nine themed murder mystery game
- A transformation of the text-based code into a GUI based one
- Implementation of items and the ability for players to take, store and give them
- A quest system with different quest branches and quests that become available
  only when specific other ones are started:accomplished
- The ability for quests to reward items and request that the player gives items
  to NPCs or goes to specific rooms.
- Interactable NPCs that walk around and can give quests, teleport the player
  or simply talk in quotes from the series.
- A teleporter room.
- A back command to send the player to his previous room.
- The ability for a player to inspect rooms and items.

JavaFX library was used for the implementation of teh GUI.
All Java classes can be found in this repository.

Author: Henri Boistel de Belloy