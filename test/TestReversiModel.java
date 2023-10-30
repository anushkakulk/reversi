import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertTrue;

public class TestReversiModel {

  private ReversiModel model;

  @Before
  public void setUp() {
    model = new ReversiGameModel(3);
  }



  @Test
  public void testMove() {
    model.move(1, -2, 1);

    assertTrue(model.getPieceAt(1,-1, 0) == ReversiPiece.BLACK);
    assertTrue(model.getPieceAt(1,-2, 1) == ReversiPiece.BLACK);
  }


  @Test
  public void testTileGetNeighbors() {
    Tile t = new Tile(0,0,0);
    List<Tile> expectedNeighbors = new ArrayList<>(Arrays.asList(
        new Tile(1, 0, -1),
        new Tile(1, -1, 0),
        new Tile(0, -1, 1),
        new Tile(-1, 0, 1),
        new Tile(-1, 1, 0),
        new Tile(0, 1, -1)
    ));
    Assert.assertEquals(expectedNeighbors, t.getNeighbors());
  }

  @Test
  public void testGetSide3HexagonBoard() {
    Assert.assertEquals(model.getHexSideLength(), 3);
  }


  @Test
  public void testGetPieceAt() {
    Assert.assertEquals(model.getPieceAt(0, 0, 0), ReversiPiece.EMPTY);
    Assert.assertEquals(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(0, 1, -1), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(-1, 0, 1), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(1, 0, -1), ReversiPiece.BLACK);
    Assert.assertEquals(model.getPieceAt(-1, 1, 0), ReversiPiece.BLACK);
    Assert.assertEquals(model.getPieceAt(0, -1, 1), ReversiPiece.BLACK);
  }

  @Test
  public void testValidMove() {
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(1, -2, 1);

    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.BLACK); // white flips to black
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.BLACK); // the empty tile is black
  }

  @Test
  public void testPassingSwitchesTurns() {
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(1, -2, 1); // black's turn
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.BLACK); // white flips to black
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.BLACK); // the empty tile is black

    model.pass();// this means white passed, now its black's turn
    model.pass(); // black can't make any moves, so black passes, now white's turn
    model.move(-1, 2, -1); // white's move

    Assert.assertSame(model.getPieceAt(-1, 1, 0), ReversiPiece.WHITE); // black flipped
    Assert.assertSame(model.getPieceAt(-1, 2, -1), ReversiPiece.WHITE); // empty is white
  }

  @Test
  public void testMakingAMoveThatFlipsMultipleOpponentTiles() {
    model = new ReversiGameModel(5);

    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(-1, -1, 2); // black's turn
    assertSame(model.getPieceAt(-1, 0, 1), ReversiPiece.BLACK); // white flips to black
    assertSame(model.getPieceAt(-1, -1, 2), ReversiPiece.BLACK); // the empty tile is black

    model.move(-2, -1, 3); // white's turn
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE); // origin white
    assertSame(model.getPieceAt(0, -1, 1), ReversiPiece.WHITE); // black flips to white
    assertSame(model.getPieceAt(-1, -1, 2), ReversiPiece.WHITE); // black flips to white
    assertSame(model.getPieceAt(-2, -1, 3), ReversiPiece.WHITE); // the empty tile is white
  }

  @Test
  public void testInvalidDestinationForMove() {
    model = new ReversiGameModel(4);
    // not a legal move, not adjacent to a line of opponent tiles followed by a same player tile
    Assert.assertThrows(IllegalStateException.class, () -> model.move(-3, 0, 3));
  }

  @Test
  public void testMakingAMoveToNonEmptyTile() {
    model = new ReversiGameModel(5);
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);
    model.move(1, -2, 1); // black validly moved to this position
    // now its white's turn, and white cannot move to it because its occupied
    Assert.assertThrows(IllegalStateException.class, () -> model.move(1, -2, 1));
  }

  @Test
  public void testValidMoveWhereTheresTwoDirectionsToCheckFor() {
    model = new ReversiGameModel(5);
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(-1, -1, 2); // black's turn
    assertSame(model.getPieceAt(-1, 0, 1), ReversiPiece.BLACK); // white flips to black
    assertSame(model.getPieceAt(-1, -1, 2), ReversiPiece.BLACK); // the empty tile is black


    model.move(2, -1, -1);
    // there are opponents in two neighbors of the destination tile. this tests whether the move
    // method correctly finds out where the move is coming from to be valid (as in, it should know
    // that even though there are two X's to the left of the dest tile, there is no O adjacent
    // to the X's, so it should look at the other neighbor. That neighbor has an O adjacent to it
    // so it makes that valid legal move.
    assertSame(model.getPieceAt(1, 0, -1), ReversiPiece.WHITE); // black flips to white
    assertSame(model.getPieceAt(2, -1, -1), ReversiPiece.WHITE); // the empty tile is black
  }

  @Test
  public void testMovetoOccupiedSpot() {
    Assert.assertFalse(model.isGameOver());
    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move

    Assert.assertThrows(IllegalStateException.class, () ->
        model.move(-1, -1,2));

  }

  // TODO WRITE TEST FOR PLAYING GAME TO COMPLETION
  // TODO TEST ALL CASES FOR GAME OVER
  @Test
  public void testGameOverAfterTwoConsecutivePasses() {
    Assert.assertFalse(model.isGameOver());
    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // still moves left, no passes
    model.pass();
    Assert.assertFalse(model.isGameOver()); // still moves left, only one pass
    model.pass();
    Assert.assertTrue(model.isGameOver()); // 2 consecutive passes, so game over!
  }

  @Test
  public void testGameOverNoMoreMoves() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, -2, 1); // black valid move
    model.move(-2, 1, 1); // white valid move
    Assert.assertTrue(model.isGameOver()); // game still isnt over
  }

  //TODO figure spacesfull out
  @Test
  public void testGameOverSpacesFull() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, -2, 1); // black valid move
    model.move(-2, 1, 1); // white valid move

    ReversiPiece piece1 =  ReversiPiece.WHITE;
  }

  @Test
  public void testGetWinnerIfBothArentEqual() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, -2, 1); // black valid move
    model.move(-2, 1, 1); // white valid move

    Assert.assertTrue(model.isGameOver());
    Assert.assertTrue(model.getWinner() == ReversiPiece.BLACK);
    //check to see if winner is correct

  }

  @Test
  public void testGetWinnerIfBothAreEqual() {
    model = new ReversiGameModel(2);
    Assert.assertTrue(model.getWinner() == ReversiPiece.EMPTY);
    //checks to see if both black and white chips are equal so there is no winner
  }

  @Test
  public void testGetWinnerIfGameIsntOver() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over
    Assert.assertEquals(null, model.getWinner());
    //check to see if you can't get winner when game isnt over
  }


}