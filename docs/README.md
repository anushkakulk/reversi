# Reversi Game Model

The Reversi Game Model is an implementation of the Reversi game, 
complete with a game board, rules keeping, and player management. 

## Intro

Reversi is a two-player board game that is played 
on an equal sided hexagonal grid-like board. The game involves placing pieces on the board 
to capture the opponent's pieces and control the majority of the board.

## ReversiModel Interface

The ReversiModel interface defines the functionality of the game, including methods for 
getting the current state of the game, making moves, and checking if the game is over. 

### Methods

1. `ReversiPiece getPieceAt(int q, int r, int s)`: Gets the Reversi piece at the specified coordinates on the game board. Throws an exception if the coordinates are invalid.

2. `ReversiPiece getPieceAt(Tile t)`: Gets the Reversi piece at the specified tile on the game board. Throws an exception if the tile is invalid.

3. `int getHexSideLength()`: Gets the side length of the hexagonal game board.

4. `void move(int q, r, s)`: Makes a move on behalf of the current player to the given coordinates if it is a valid move. Throws an exception if the move is invalid or if the coordinates are invalid.

5. `void pass()`: Switches to the next player's turn.

6. `boolean isGameOver()`: Checks whether the game is over, which is when conditions such as a full board, consecutive passes, or both players having no legal moves left.

7. `ReversiPiece getWinner()`: Gets the winner of the game, which is the player with the most pieces on the board. 

## ReversiGameModel

The `ReversiGameModel` class implements the ReversiModel interface and provides the actual game logic. Use the ReversiGameModel to actually play/test the game. 

## ReversiPiece Enum

The `ReversiPiece` enum represents the game pieces used in Reversi. It includes three values: `EMPTY`, `BLACK`, and `WHITE`, each with a corresponding display character.

## Tile

The `Tile` class represents a tile with cubic coordinates, which are used to construct the game board for Reversi. It includes methods for accessing the coordinates, finding neighboring tiles, and adding direction vectors.

## Getting Started

To start, create an instance of `ReversiGameModel` by giving the 
side length of the hexagonal game board in the constructor. The game will initialize with the starting positions 
of pieces, and you can make moves by calling the `move` method (which automatically switches to the next player's turn after the move)
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



## ReversiView and ReversiTextualView

### ReversiView Interface

The ReversiView interface represents the primary view interface for viewing a game of Reversi. 
It has the following method:

- `String render()`: Renders the current state of the ReversiModel.

### ReversiTextualView Class

The ReversiTextualView class is an implementation of the ReversiView interface, providing a textual representation of a game of Reversi. It renders the current state of the ReversiModel as a String. The class includes the following methods:


- `ReversiTextualView(ReversiModel model)`: Constructor with a ReversiModel argument, primarily used for testing. It creates a model object, then creates a view object and passes in that model. Calling `render()` on that view object will output the textual view of the model.

- `String render()`: Represents the ReversiModel as a String, showing the current state of the ReversiModel. It creates a string representation of the current state of the model's game board.

You can use the ReversiTextualView to display the current state of the Reversi game in a text-based format to visualize the model.

## View Example

Here's an example of how to create and view the model using the view:

```
// makes a game with a hexagonal board of side length 7
model = new ReversiGameModel(7);
view = new ReversiTextualView(model); 

// this outputs the initial state as a string, which is just 
// 3 black and 3 white pieces in alternating order in the innermost hexagon
System.out.println(view.render()); 

model.move(-1, -1, 2); // move made by black

// this now shows black's piece at tile (-1, -1, 2) and the white
// piece on the tile that black went over to get to the dest tile is now a black piece
System.out.println(view.render()); 

model.move(-2, -1, 3); // move made by white

// this now shows white's piece at tile (-2, -1, 3) and the black
// piece on the tile that white went over to get to the dest tile is now a white piece
System.out.println(view.render());


```
