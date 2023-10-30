### CHOICES MADE FOR MODEL DESIGN ###
The model implementation is created such that it could easily be extended by other model types. We did so
by putting demarcated functionalities into private helper methods. Therefore, should we need to make an
extension to the current rules/format of the reversi game, we can easily do by turning these helper methods
into protected ones, and overriding those protected methods in the subclasses. We did not "bake in" a majority 
of the functionality into the methods outlined in the model interface,
but rather put the concrete functionality from the helpers and in the model implementation of methods outlined in the
interface, we call the helpers.

### INSTANTIATING A PLAYER ###
We created a Player interface, where the only method (as of now!) is makeGamePlayDecision().
To instantiate a Player, we would need to create a class ReversiGamePlayer which has the ReversiPiece
associated with it.

For example, we would say something like:
```
ReversiModel model = new ReversiGameModel(6)
Player1 = new ReversiGamePlayer(ReversiPiece.Black)
Player2 = new ReversiGamePlayer(ReversiPiece.White).
```
then both players would interact with the model through the "controller."
basically, a player in the game needs to make a game play decision (which as of now means
they can either move one of their piece to a certain position or they can pass their turn)
so a player needs will make a decision in that method through the controller,
and the result of that decision will be a function object, either a Pass or a MovePiece.
That object contains information about the
player's decision and will be given to the model, to actually play the game! A player will
also have a unique ReversiPiece that corresponds to themselves, so the model knows which
whose turn it is and what piece to put down depending on what "piece"'s turn it is. We think this
is a good design choice, as we know that our player has really two options as of now, therefore
creating a union data PlayerMove, that is either a Pass or a MovePiece and contains the method
run() makes executing a player move clean and concise, and easily allows for further developments,
like adding a new kind of "player move"


### INSTANTIATING A MODEL ###
I think that in the controller, one of the first prompts to be asked will be "Enter a side length
for the hexagonal game board you wish to play on." Then our controller will try creating an instance
of a ReversiGameModel with that given side length (and will ofc handle any exceptions and output
a meaningful message to the people using the controller). So, within the controller run(),
we would have something like
```
ReversiModel model;
while (scanner.hasNext()) {
    write("Enter a side length for the hexagonal game board you wish to play on");
    int sideLen = scanner.NextInt();
    try {
        model = new ReversiGameModel(sideLen);
    } catch (IllegalStateException e) {
    // do something
    }
}
```

