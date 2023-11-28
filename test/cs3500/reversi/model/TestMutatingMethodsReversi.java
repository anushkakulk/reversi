package cs3500.reversi.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertSame;

/**
 * Test Suite for testing all public mutation methods in the Model package, specifically from
 * the ReversiModel interface.
 */
public class TestMutatingMethodsReversi {

  private ReversiModel model;

  @Before
  public void setUp() {
    model = new ReversiGameModel(3);
    model.startGame();
  }

  @Test
  public void throwExceptionIfConstructorGivenSizeLessThan2() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new ReversiGameModel(-21));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new ReversiGameModel(-2));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new ReversiGameModel(1));
  }

  @Test
  public void testMove() {
    model.move(1, -2, 1);

    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.BLACK);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.BLACK);
  }


  @Test
  public void testCreatingInvalidTile() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new Tile(0, -1, 2));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new Tile(-100, 100, 1));

    Tile t = new Tile(1, -2, 1); // a valid tile, coords = 0;
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

    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.BLACK); // check blacks turn
  }

  @Test
  public void testMakingAMoveThatFlipsMultipleOpponentTiles() {
    model = new ReversiGameModel(5);
    model.startGame();

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
    model.startGame();
    // not a legal move, not adjacent to a line of opponent tiles followed by a same player tile
    Assert.assertThrows(IllegalStateException.class, () -> model.move(-3, 0, 3));
  }

  @Test
  public void testMakingAMoveToNonEmptyTile() {
    model = new ReversiGameModel(5);
    model.startGame();
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);
    model.move(1, -2, 1); // black validly moved to this position
    // now its white's turn, and white cannot move to it because its occupied
    Assert.assertThrows(IllegalStateException.class, () -> model.move(1, -2, 1));
  }

  @Test
  public void testValidMoveWhereTheresTwoDirectionsToCheckFor() {
    model = new ReversiGameModel(5);
    model.startGame();
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
            model.move(-1, -1, 2));

  }

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
    Assert.assertSame(model.getGameStatus(), GameStatus.STALEMATE); // tie game!
  }

  @Test
  public void testGameOverNoMoreMoves() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over
    assertSame(model.getGameStatus(), GameStatus.PLAYING);


    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over
    assertSame(model.getGameStatus(), GameStatus.PLAYING);


    model.move(1, -2, 1); // black valid move
    model.move(-2, 1, 1); // white valid move
    Assert.assertTrue(model.isGameOver()); // game is NOW over
    assertSame(model.getGameStatus(), GameStatus.WON);
  }




  @Test
  public void testFlippingInBothDirectionsAfterValidMove() {
    model = new ReversiGameModel(7);
    model.startGame();
    // continuously make valid moves that capture one or two of the opponents piece
    model.move(1, -2, 1); // black
    model.move(2, -1, -1); // white
    model.move(-2, 1, 1); // black
    model.move(1, -3, 2); // white
    model.move(1, 1, -2); // black
    model.move(-1, 2, -1); // white
    model.move(2, -3, 1); // black
    // HERE, IT IS WHITE's TURN. THE TILE CHOSEN HAS A VALID SEQUENCE OF OPPONENT PIECES ENDING WITH
    // CURR PLAYER'S PIECE IN BOTH THE RIGHT AND BOTTOM RIGHT DIRECTIONS. SO BOTH DIRECTIONS SHOULD
    // BE FLIPPED.
    model.move(-1, -1, 2); // white
    // make sure all tiles in the right direction are now white
    Assert.assertSame(model.getPieceAt(-1, -1, 2), ReversiPiece.WHITE);
    Assert.assertSame(model.getPieceAt(0, -1, 1), ReversiPiece.WHITE);
    Assert.assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    Assert.assertSame(model.getPieceAt(2, -1, -1), ReversiPiece.WHITE);

    // make sure all tiles in the bottom right direction are now white
    Assert.assertSame(model.getPieceAt(-1, -1, 2), ReversiPiece.WHITE);
    Assert.assertSame(model.getPieceAt(-1, 0, 1), ReversiPiece.WHITE);
    Assert.assertSame(model.getPieceAt(-1, 1, 0), ReversiPiece.WHITE);
  }


}