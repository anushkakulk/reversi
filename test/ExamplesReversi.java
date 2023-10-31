import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.ReversiTextualView;
import view.ReversiView;

import static org.junit.Assert.assertSame;

public class ExamplesReversi {
  private ReversiModel model;
  private ReversiView view;

  @Before
  public void setup() {
    model = new ReversiGameModel(3);
    view = new ReversiTextualView(model);
  }


  @Test
  public void examplesGetPieceAt() {
    Tile tile = new Tile(0, -1,1);
    // Get piece via tile
    ReversiPiece p1 = model.getPieceAt(tile);
    // Get piece via coordinate
    ReversiPiece p2 = model.getPieceAt(0,-1,1);

    Assert.assertSame(p1, p2);
  }

  @Test
  public void examplesHexSideLength() {
    // Has to be 2 or more
    Assert.assertThrows(IllegalArgumentException.class, ()->
        model = new ReversiGameModel(1));
    // Can check side length, -size < q & r & s < size
    model.getHexSideLength(); // 3

    model.move(-1, -1, 2); //valid move bc q r and s are in the boundaries
    Assert.assertSame(ReversiPiece.BLACK, model.getPieceAt(-1,-1,2));
  }
  @Test
  public void examplesInvalidMove() {

    // Invalid Moves:
    // 1. Moving to a spot that is not open
    Assert.assertThrows(IllegalStateException.class, () -> model.move(1, -1, 0));
    // 2. Moving to a tile that does have your color at the end of the line
    Assert.assertThrows(IllegalStateException.class, () -> model.move(2, -2, 0));
    // 3. Moving to a coordinate off the grid
    Assert.assertThrows(IllegalArgumentException.class, () -> model.move(3, -3, 0));
    // 4. Moving when game is over
    model = new ReversiGameModel(2);
    Assert.assertThrows(IllegalStateException.class, () -> model.move(1, -1, 0));
  }

  @Test
  public void examplesValidMove() {
    view.toString();

    // 1. Flipping one piece over
    model.move(-2,1,1); //flipping white piece at (-1,0,1) to black
    view.toString();
    Assert.assertSame(ReversiPiece.BLACK, model.getPieceAt(-1,0,1));

    // 2. Flipping one piece in 2 different directions over
    model.move(-1,-1,2); // flipping black piece at (0,-1,1) to white
    view.toString();
    model.move(1,-2,1); // flipping both white pieces at (0,-1,1) & (1,-1,0) to black
    view.toString();

    Assert.assertSame(ReversiPiece.BLACK, model.getPieceAt(0,-1,1));
    Assert.assertSame(ReversiPiece.BLACK, model.getPieceAt(1,-1,0));

   // 3. Flipping multiple pieces the same direction (and one in a different direction)
    model.move(2,-1,-1); // flipping 2 black pieces at (0,-1,1) & (1,-1,0) -same direction-
    // to white and black piece at (1,0,-1) to white
    view.toString();

    Assert.assertSame(ReversiPiece.WHITE, model.getPieceAt(0,-1,1));
    Assert.assertSame(ReversiPiece.WHITE, model.getPieceAt(1,-1,0));

    Assert.assertSame(ReversiPiece.WHITE, model.getPieceAt(1,0,-1));
  }

  @Test
  public void examplesCurrPlayer() {
    // You can check whos turn it is by calling this
    Assert.assertSame(ReversiPiece.BLACK, model.getCurrentPlayer() /*black*/);
    model.move(-2,1,1);
    Assert.assertSame(ReversiPiece.WHITE, model.getCurrentPlayer() /*white*/);
  }

  @Test
  public void examplesGameDone() {
    // Game is still being played
    Assert.assertSame(GameStatus.PLAYING, model.getGameStatus());
    // So game isnt over
    Assert.assertFalse(model.isGameOver());

    //Game was won
    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    model.move(1, -2, 1); // black valid move
    model.move(-2, 1, 1); // white valid move
    view.toString();

    Assert.assertSame(model.getGameStatus(), GameStatus.WON);
    //Game is over
    Assert.assertTrue(model.isGameOver());
    // So check for winner
    assertSame(model.getWinner(), ReversiPiece.BLACK);

    //Game ended in tie
    model = new ReversiGameModel(2);
    model.pass();
    Assert.assertSame(GameStatus.STALEMATE, model.getGameStatus());
    //Game is over
    Assert.assertTrue(model.isGameOver());
    // So check for winner (there is no winner in a tie)
    assertSame(model.getWinner(), ReversiPiece.EMPTY);
  }

  @Test
  public void examplesPass() {
    // pass if you have a valid move but do not want to go
    model.pass(); //passes it to white
    Assert.assertSame(ReversiPiece.WHITE, model.getCurrentPlayer());

    //or when you do not have a valid move
    model = new ReversiGameModel(2);
    Assert.assertThrows(IllegalStateException.class, ()-> model.move(0,0,0));
    model.pass();
    Assert.assertSame(ReversiPiece.WHITE, model.getCurrentPlayer());
  }
}
