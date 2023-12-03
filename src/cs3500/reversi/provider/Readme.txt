Scroll down for part 2 changes/updates.

Overview: What problem is this codebase trying to solve? What high-level assumptions are made in the codebase, either about the background knowledge needed, about what forms of extensibility are envisioned or are out of scope, or about prerequisites for using this code?

The problem that the code base is trying to solve is Configuring a Model and corresponding text view for a game of hexagonal Reversi. We don’t have many high level assumptions besides the fact that our game only supports grids with an odd row and column amount where row = column. This is the case because we wanted to preserve symmetry on both sides of the hexagon. Another high level assumption is that only two individuals may play at the same time. We don’t have support for any other number of players besides two. For a person to use our program they must be familiar with hexagonal coordinate systems, specifically the offset coordinates : “odd-r” horizontal layout variation. A player may need background in the rules of Reversi as well.

Quick start: give a short snippet of code (rather like an Exemplar example) showing how a user might get started using this codebase.

ReversiModel model = new ReversiHexModel();
// to pass a turn
model.pass();
// to make a move
model.makeMove(5,2);
// to check if the game is over
model.isGameOver();
// to get the state of a game
model.getCurrentGameState();

Key components: Explain the highest-level components in your system, and what they do. It is trite and useless to merely say “The model represents the data in my system. The view represents the rendering of my system. ...” This is a waste of your time and the reader’s time. Describe which components “drive” the control-flow of your system, and which ones “are driven”.
Model
A board with a hexagonal “odd-r” horizontal layout
This represents what pieces are in play, what coordinates are null,
and what pieces are facedown on a turn
Also has methods that keeps the state of pieces
Subcomponents: 2d array: stores the board, GameDisc: represents a singular GameDisc
A movement system
A movement system is necessary for determining if moves are valid
There are 6 types of moves: right, left, up-right, up-left, down-right, down-left
The movement system drives the program because other states can only be reached by altering a board
Subcomponents: breadth first search algorithm: determines all valid moves from a specific cell, Enum MoveDirection: represents the 6 types of moves, MoveRules class: shifts a coordinate based on one of the 6 types of a move
State tracking system
The model must know if the game is over
The model must know what gamestate it is in
Can be: player one won, player 2 won, in progress, 
or stalemate
Subcomponents: a GameState Flag to express what state the game is in
a isGameOver flag that expresses if the game is over or not
View
Must display the board at any given time
Subcomponents: A StringBuilder to store the current Game State.
Source organization: Either explain for each component where to find it in your codebase, or explain for each directory in your codebase what components it provides. Either way, supply the reader with a “map” to your codebase, so they can navigate around.

Src
Controller: directory for holding controller components
ControllerListener (Interface)
ModelEvent (class)
ModelEventType (Enum)
ModelListener (class)
PlayerEvent (class)
PlayerEventType (enum)
PlayerListener (class)
ReversiController (class)
Discs: directory for holding disc components
Disc (interface)
DiscColor (enum)
DiscType (enum)
GameDisc (class)
Model: directory for holding model components
GameState (enum)
MoveDirection (enum)
MoveRules (class)
ReversiHexModel (class)
ReversiModel (interface
Player: directory for holding player components
PlayerTurn (enum)
View: directory for holding view components
ReversiTextualView (class)
ReversiView (interface)
Test
ReversiHexModelTests: tests the model
ReversiTextualViewTests: tests the textual view

New Features for part 2:

-GUI view for a reversi game:
    -ReversiGUI is general enough for multiple board sizes.
    -ReversiGUI Allows the player to select, deselect, make a move to a selected, disc, and pass their turn.
        -A player can select a disc by simply clicking on a disc, the disc will turn cyan when it is selected.
        -Only one disc can be selected at a time.
        -A disc will deselect if:
            -It is selected again
            -Another disc is selected
            -The player clicks outside the playable game board area.
        -A player can pass their turn by pressing the space bar on their keyboard. If a disc is selected when the player passes, that disc will be deselected.
        -A player can make a move to a desired disc, by first selecting on the disc, and then pressing the enter key on their keyboard if they wish to make that move.
    -ReversiGUI will display the coordinates of a disc whenever it is pressed. It will also display a message whenever a player interacts with the view (If they pass, make a move, etc.).
    -The GUI takes in a ReadOnlyModel, meaning that the game cannot be altered by the view.
    -The GUI has a main method that can be used as an entry point for a user. Visit the ReversiGUI and head over to the main method, you can find some moves to experiment with.

-AI Model:
    -The Reversi Ai Model allows a player to play against a CPU controlled player.
    -The Ai has the ability to play with 4 different strategies (see below);
    -Our Ai model is always white, allowing the player to go first and from there, determine the best move based off of whatever strategy the Ai is implementing.

-Strategies:
-Maximize captures strategy:
    -Evaluates all possible moves and looks for the move that captures the most discs in one go.
    -In case of ties, looks for the top-leftmost move.
-Go For corners strategy:
    -Looks for moves closest to corner discs and captures them.
    -If multiple moves are equidistant from any corners, it looks for the left most corner.
-Avoid corner strategy:
    -Can only move to cells that are non-adjacent to corner discs
    -If there is a tie between moves, it looks for the maximized capture move, and then if that is a tie it chooses the leftmost move, and then if that is a tie the topmost move.
-Minimax strategy:
    -Sees what the non Ai player made, and based off of that, decides what strategy the non-Ai player is trying to use, and then executes that same strategy against them.
    -In the case that the player move does not match a strategy, it defaults to the maximize capture strategy.

Changes for Part 2:
-Moved the bfs into a utils class: Strategies needed access to BFS
-Changed visibility to protected for reverse model we have a class that extends it


Changes for Part 3:
-Our Model now Has a list of modelListeners and accompanying functions that
 add listeners and notify listener on model changes
- Our MakeMove and Pass methods have appropriate notifyListener Methods


New Additions:
- The Controller: Tasked with handling player actions on the board: a valid move, invalid move, passes
  additionally, handles view syncing and view rendering. The Controller also informs the player
  about its actions, and its expected actions.
  If a valid move occurs the view will create a popup on the executing player's screen describing the move
  If an invalid move occurs the view will indicate that the executing player has made a move inform the other
  player that a move has been made but is illegal and tells the executing player it is still their turn
  if a pass occurs the executing player will receive a popup that states that they have decided to pass.
  the non executing player will then be informed via popup that it is their turn.
- Main Class: starts the program and creates instances of classes necessary for the program. The main class
  is also responsible for handling the end of game as they are not player actions
- ModelEvent,PlayerEvent: these classes hold all the information about events on the model and events on a player.
  A Player Event is used by the controller to execute on the model and a model event is used to get information
  about actions on the model

Tests:
Testing behavior:
Controller Mock:
-Mocked features:
-Takes in a MockGUI instead of a ReversiGUI

GUI Mock:
-Mocked features:
-Access to discs are made public to simulate events

By gaining access to the discs, we allow ourselves to simulate interactions with discs and manipulate moves as we desire.
Discs are interacted with by creating MouseEvents and KeyEvents in our tests and passing in the responsible keys for moving or passing. Enter key to move and space bar to pass.
We test our controller's behavior by making sure it communicates with our model and view through our listeners. This is done by performing a move (or pass), and then checking the most recent event that our listeners recorded.
We also test for move outcomes, player turn behavior, and end game states. This is to make sure that our model is still functioning as it should and is communicating with the view and the controller properly.
