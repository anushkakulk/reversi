# Reversi 

This codebase is an implementation of the Reversi game,
complete with a game board, rules keeping, and player management.


## Source Organization

The codebase is organized into the following packages:

- model: contains the core Reversi game model components, including the main game model interfaces
  (ReversiModel and ReadOnlyReversiModel) and their implementation (ReversiGameModel).
  - model Package:
    - ReversiModel: Interface for playing a game of Reversi.
    - ReadOnlyReversiModel: Interface for accessing game state information.
    - ReversiGameModel: The concrete implementation of ReversiModel.
    - Tile: Represents a tile on the game board.
    - ReversiPiece: Enum representing the states of tiles (EMPTY, BLACK, WHITE).
    - GameStatus: Enum representing game status options (PLAYING, WON, STALEMATE).
- view: this package holds components responsible for rendering the game's visual representation.
  - view Package:
    - ReversiView: Interface for viewing a game of Reversi.
    - ReversiTextualView: A class that implements the ReversiView interface to provide a textual 
     representation of the Reversi game.
- player: contains components for making game play decisions, like a Player interface, a (potential) 
concrete player class, and interfaces and classes to work to represent a Player's next move and 
strategy.
  - player Package: 
    - ReversiPlayer: Interface representing a player of Reversi.
    - Pass: Class representing a pass move, a type of move that a player can make.
    - MovePiece: Class representing moving a piece, a type of move that a player can make.
    - IPlayerMoveStrategy: Interface for executing a player move in the model, if valid.
    - IPlayerMove: Interface representing a player move.
    - Player: A class representing a player with a move strategy, Reversi piece, 
      and read only access to the game model (NOT IMPLEMENTED YET)

# Model 

## Key Components

### ReadOnlyReversiModel Interface

Represents a read-only model for a Reversi game, which contains methods to access to game state 
info such as getting what pieces are on the board at a certain position, the current player, 
and game status (won, tied, still playing).

### ReversiModel Interface 

The ReversiModel interface extends the ReadOnlyReversiModel and defines methods for the 
functionality of the game, including methods for mutating the game state by either placing a piece
or passing a turn. 


### ReversiGameModel

The `ReversiGameModel` class implements the ReversiModel interface and provides the actual game
logic. Create instances of the ReversiGameModel (giving it a sideLength of the board to play on)
to actually play/test the game. 

## Key Subcomponents

####  Tile

The ` Tile` class represents a tile with cubic coordinates, which are used to construct the game
board for Reversi. It includes methods for accessing the coordinates, finding neighboring tiles, and
adding direction vectors. We chose tiles with cubic coordinates for the hexagonal board due to their simplicity and
efficiency in representing tile positions. they provide a consistent and symmetrical system
where each tile's position is defined by three integers (q, r, s) with q + r + s = 0,
 which simplifies operations like finding neighboring tiles (which is a key part of Reversi).

#### ReversiPiece Enum

The `ReversiPiece` enum represents the game pieces used in Reversi. It includes three
values: `EMPTY`, `BLACK`, and `WHITE`. These pieces are mapped to a tile to form the gameboard. 

#### GameStatus Enum

The `GameStatus` enum represents the state options of Reversi. It includes three
values: `PLAYING`, `WON`, and `STALEMATE`.



## Getting Started

To start, create an instance of `ReversiGameModel` by giving the
side length of the hexagonal game board in the constructor. The game will initialize with the
starting positions
of pieces, and you can make moves by calling the `move` method (which automatically switches to the
next player's turn after the move)
and pass your turn using the `pass` method.

## Model Example

Here's an example of how to create and play the model:

```
// makes a game with a hexagonal board of side length 6
ReversiModel model = new ReversiGameModel(6); 

 model.move(-1, -1, 2); // move made by black
 
 model.move(-2, -1, 3); // move made by white

 model.pass(); // black just passed, its white's turn again now

if (game.isGameOver()) {
    ReversiPiece winner = game.getWinner();
    System.out.println("The winner is: " + winner);
}
```

# View  

### ReversiView Interface

The ReversiView interface represents the primary view interface for viewing a game of Reversi, using
a method to render the model's board.


### ReversiTextualView Class

The ReversiTextualView class is an implementation of the ReversiView interface, providing a textual
representation of a game of Reversi. It renders the current state of the ReversiModel as a String.


## View Example

Here's an example of how to create and view the model using the view:

```
// makes a game with a hexagonal board of side length 7
model = new ReversiGameModel(7);
view = new ReversiTextualView(model); 

// this outputs the initial state as a string, which is just 
// 3 black and 3 white pieces in alternating order in the innermost hexagon
System.out.println(view.toString()); 

 move(-1, -1, 2); // move made by black

// this now shows black's piece ("X") at tile (-1, -1, 2) and the white
// piece on the tile that black went over to get to the dest tile is now a black piece
System.out.println(toString()); 

 move(-2, -1, 3); // move made by white

// this now shows white's piece ("O") at tile (-2, -1, 3) and the black
// piece on the tile that white went over to get to the dest tile is now a white piece
System.out.println(view.toString());


```

## Changes for Part 2 

- added a getScoreMethod in the ReadOnlyModel Interface, since its an
observation method. 
- 

