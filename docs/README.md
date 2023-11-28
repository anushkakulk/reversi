# Reversi 

This codebase is an implementation of the Reversi game,
complete with a game board, rules keeping, and player management.


# Reversi Game Initialization & Playing Guide

To initiate and start a game of Reversi, you need to provide information about the two players you want to engage in the match.

### Guide to Command Line Parsing

Follow this guide for each of the two players:

#### Choosing a Player Type for One Player:
- For a human player, enter: `human`
- For an AI player focused on capturing the most pieces, enter: `strategy1`
- For an AI player avoiding corner spots, enter: `strategy2`
- For an AI player targeting corner spots, enter: `strategy3`
- For a custom combination strategy, enter: `manystrategy` followed by the number of strategies 
(`n`) and the strategy names from `strategy1`, `strategy2`, and `strategy3`.

## Examples of Valid Command Line Inputs:

```
// NOTE: player1 will always use piece X and player2 will always use piece O

// two human players
command line input: human human

// player1 is a human, player 2 is an AI playing to capture the most pieces at each turn
command line input: human strategy1

// player 1 is an AI playing to capture the most pieces at each turn, player 1 is a human
command line input: strategy1 human

// player 1 is a human, player 2 plays a combination of all strategies in the order 3->2->1
command line input: human manystrategy 3 strategy3 strategy2 strategy1

// player 1 plays a combination of all strategies in the order 3->2->1, player 2 is a human
command line input: manystrategy 3 strategy3 strategy2 strategy1 human 

// player 1 is a human, player 2 plays a combination of all strategies in the order 1->2->3
command line input: human manystrategy 3 strategy1 strategy2 strategy3

// player 1 plays a combination of all strategies in the order 1->2->3, player 2 is a human
command line input: manystrategy 3 strategy1 strategy2 strategy3 human 

// player 1 plays a combination of strategy 1 and 2, in the order
// 2->1 (if strategy2 fails to find a move, it tries strategy1), player 2 is a human
command line input: manystrategy 2  strategy2 strategy1 human 



```


### Interacting with the View for Human Players:

On the human player's view, use the mouse to click on any tile to select that tile 
(it will highlight blue), then press `enter` to make a move to that selected tile. 
If you wish to pass your turn, press `spacebar`.

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
    - ReversiView: Interface for viewing a full game of Reversi. 
    - SimpleReversiView: Interface for viewing a simple game of Reversi.
    - ReversiTextualView: A class that implements the SimpleReversiView interface to provide a textual 
      representation of the Reversi game.
    - ReversiGUIView: An extension of JFrame to serve as our window for visually viewing the game.
    - ReversiPanel: An extension of JPanel to serve as our canvas for visually viewing and 
      interacting with the game.
    - HexTile: Represents the gui representation of a tile from the model class. Has the ability to
      draw itself on the canvas.
- controller: this package contains components responsible for communication resulting in game play
  between the model view and players.
  - controller Package:
    - PlayerActionFeatures: Represents any event that can happen on the canvas.
    - ModelStatusFeatures: Features interface for handling any changes to model status.
    - IReversiController: Interface for reconciling between model, view/player. 
    - ReversiController: Implementation of the controller that follows the "is-a" relationship with 
      the features, listening for and executing changes to the model and view
    - IEmitPlayerActions: Interface for defining the notifications that can be sent by anything to
      listeners of player actions.
- player: contains components for making game play decisions, like a Player interface, a 
concrete player class, and interfaces and classes to work to represent a Player's next move and 
strategy.
  - player Package: 
    - ReversiPlayer: Interface representing a player of Reversi.
    - Player: A class representing a player with a move strategy and Reversi piece.
    - IPlayerMoveStrategy: Interface for executing a player move in the model, if valid.
    - Strategy: Class representing an infallible Reversi game strategy, that will return
      the next move to make given the state of a game model. 
    - HumanStrategy: Class representing the strategy of a human, which takes in user input to
      determine the next move.
    - CaptureMostStrategy: Class representing the strategy of determining the next move by choosing 
      a valid tile from that will yield the most tiles flipped over.
    - PlayCornersStrategy: Class representing the strategy of determining the next move by choosing
         a valid tile from that will yield the most tiles flipped over that is in the corner.
    - AvoidNextToCornersStrategy: Class representing the strategy of determining the next move by 
      choosing a valid tile from that will yield the most tiles flipped over that is not next to a 
      corner.
    - ManyStrategy: Class representing the combination of multiple strategies to determine the next 
      move by getting the next valid move by going through all the strategies. if one fails, 
      it tries the next in its repertoire.
    - IPlayerMove: Interface representing a player move. (either pass or move)
    - Pass: Class representing a pass move, a type of move that a player can make.
    - Move: Class representing moving a piece, a type of move that a player can make.
    
    

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
q goes along the x axis (moving left on the hexagon decrements the q coordinate, moving right on the
hexagon increases the q coordinate), r goes along the y axis (moving north up the hexagon decrements 
the r coordinate, moving south down the hexagon increases the r coordinate), s is like the northwest 
axis(moving northwest up the hexagon increases the s coordinate, moving southeast down the hexagon 
decrements the s coordinate). the origin in a board is a tile with (0, 0, 0), which indicates the 
center of the hexagonic board.

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

# Player

## Key Components

### ReversiPlayer Interface

Represents the interface for a Player of Reversi.

### IPlayerMove Interface

Represents the Player Moves that are available in the Reversi game. As of now, only move or pass.

### IPlayerMoveStrategy Interface

Represents a strategy pattern for Reversi players. implementers of this must define a method to 
return an Optional<ReversiPosn> that represents the next valid position to move to. If a strategy
finds multiple tiles that will be the best move, then it will return the tile with the upper-left
most coordinates in the board. If there is a tile between a tile that is up and left, then it will
choose the left most tile.
Current implemented strategies are: CaptureMostStrategy (), AvoidNextToCornersStrategy(),
PlayCornersStrategy(), ManyStrategy(List<IPlayerMoveStrategy> strategiesToPlayInOrder); 

## Key Subcomponents

#### Strategy

Represents an Infallible Strategy for Reversi. It attempts an IPlayerMoveStrategy to find a move, 
but if it fails to find a move, or chooses to pass, then the strategy says to pass. 

#### Player

Represents a Player of Reversi, which plays a given strategy and has a piece associated with it.

#### ReversiPosn

Represents a position on a board on which the Reversi game is being played. Has a q, r, s coordinate
(see tile for explanation on coordinates.) 

## Player/Strategy Example

Here's an example of how to create and play the model using the players and strategies:

```
// makes a game with a hexagonal board of side length 7
model = new ReversiGameModel(7);

Strategy captureMost = new Strategy(new CaptureMost()); 
ReversiPlayer AIEasy = new Player(captureMost, ReversiPiece.BLACK); 

// this method call will return the move made by the captureMost strategy
IPlayerMove nextMove = AIEasy.getPlayerDecision(model); 


```


# View  

## Key Components

### ReversiView Interface

The ReversiView interface represents the  view interface for textually viewing a game of Reversi, 
using a method to render the model's board.


### ReversiTextualView Class

The ReversiTextualView class is an implementation of the ReversiView interface, providing a textual
representation of a game of Reversi. It renders the current state of the ReversiModel as a String.

### ReversiGUIView Class
The ReversiGUIView is an extension of JFrame, that serves as our window for visually 
viewing the game. It holds the canvas on it and listens to the panel. 


### ReversiPanel Class
The ReversiPanel is an extension of JPanel to serve as our canvas for visually viewing and
interacting with the game. It implements mouse-click listener and key listener. If someone clicks
on a tile, then it highlights blue. If a tile is clicked and the key "enter" is clicked, then the 
panel indicates that there is a wish to move to that tile. If a tile is clicked and the key "space"
is clicked, then the panel indicates that there is a wish to pass the current turn.

### Key Subcomponents 

#### HexTile Class
Represents the gui representation of a tile from the model class. 
Has the ability to draw itself on the canvas, and translates the model tile's q,r,s 
coordinates into pixel x,y coordinates. 

#### ICanvasEvent Interface
Represents any event that can happen on the canvas

## View Example

Here's an example of how to create and view the model using the gui view:

```
 // makes a game with a hexagonal board of side length 6
 ReversiModel model = new ReversiGameModel(6);
 
 ReversiGUIView view = new ReversiGUIView(model);
 // now, the user can interact with the board (click tiles and enter/space) and resize the window
```

Here's an example of how to create and view the model using the textual view:

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


# Controller

## Key Components

### IReversiController Interface

The Reversi interface represents the controller interface for playing a game of Reversi. Any 
implementation of this interface must handle player action to play the game.

### ModelStatusFeatures Interface
This interface is the "features" interface for the model, where all methods in this interface
involve handling the notification from the model of a change to the game, such as turn change and 
the game being over. Listeners of the model will implement in this features interface to observe 
the model.

### PlayerActionFeatures Interface
This interface is the "features" interface for both the player (for AI players) and the view 
(for human players), where all methods in this interface
involve handling the notification from either the player or the view that of a player action
that wishes to change the game, such as indicating a player they want to move to a certain tile
or pass their turn. Listeners for player actions will implement in this features interface to observe
the model.

### IEmitPlayerActions Interface
This interface is for classes that emit notifications to the controller about chosen player actions,
such as the view (for human players) or the player itself (for AI players), defining what 
notifications should be sent. 


### ReversiController Class


## Changes for Part 2 

- added a getScoreMethod in the ReadOnlyModel Interface, since its an
observation method. It iterates over the the board and returns the number of tiles occupied by the 
given player's piece
- added a isLegalMove method that takes in q, r, s coords and a piece that wishes to move there.
This is now a public method (as opposed to private) as players (specifically AI players) need to 
know where they can move to or not. 
- added the ability to copy a model completely. This copies the game board as well all other fields
of a ReversiGameModel. This allows for efficient testing and ensuring differences in our strategies.
also added a method that returns a copy of the current board, for the same reasons.
- added a clear explanation of our tile coordinates 

## Changes for Part 3
- fixed the key press functionality from pt2 to now work with a player action interface that follows the 
notification/subscriber pattern (it used to just print to command line, now we have a methods
notifyMoveChosen and notifyPassChosen that get called when a tile selected + `enter` and `spacebar`
is pressed, respectively
- made an update method for the JPanel, then when called, will repaint the view's board. 
- created a new type of IPlayerMove, called `HumanChoice` that gets returned from the human 
strategy's playStrategy method since the Optional.of(ReversiPosn) indicates a move and Optional.empty
indicates a pass. for a human, they won't be using their strategy to determine their next move, they
would be making that decision through the view, so therefore they need their own representation of 
their move for their strategy.
- added more detail to readme about what specific key presses mean.


## NOTE FOR GRADERS: EXTRA CREDIT FROM PART 2
We successfully implemented and tested a human strategy, strategies 1, 2, and 3, and the ability to
combine the strategies in any order (using a strategy that takes in a list of strategies and
executes them in order (if the first fails, try the second... and so on until all strats in the
list have been tried)) from the course website. This is all present in src/cs500.reversi/player.
