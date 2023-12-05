package cs3500.reversi.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertSame;

/**
 * Test Suite for testing all public observation methods in the Model package, specifically from
 * the ReadOnlyReversiModel interface.
 */
public class TestObservationMethodsReversi {
  private ReversiModel model;

  @Before
  public void setUp() {
    model = new ReversiGameModel(3);
    model.startGame();
  }

  @Test
  public void testTileGetNeighbors() {
    Tile t = new Tile(0, 0, 0);
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
  public void testGetPieceAtTileInvalid() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
        model.getPieceAt(new Tile(-2, -4, -12)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
        model.getPieceAt(new Tile(102, 0, -2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
        model.getPieceAt(new Tile(-5, 5, 3)));
  }

  @Test
  public void testGetWinnerIfBothArentEqual() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over
    Assert.assertSame(model.getGameStatus(), GameStatus.PLAYING);

    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over
    Assert.assertSame(model.getGameStatus(), GameStatus.PLAYING);


    model.move(1, -2, 1); // black valid move
    model.move(-2, 1, 1); // white valid move

    Assert.assertTrue(model.isGameOver());
    Assert.assertSame(model.getGameStatus(), GameStatus.WON);
    assertSame(model.getWinner(), ReversiPiece.BLACK);
    //check to see if winner is correct

  }

  @Test
  public void testGetWinnerIfBothAreEqual() {
    model = new ReversiGameModel(2);
    model.startGame();
    Assert.assertSame(model.getGameStatus(), GameStatus.PLAYING);
    assertSame(model.getWinner(), ReversiPiece.EMPTY);
    //checks to see if both black and white chips are equal so there is no winner
  }

  @Test
  public void testGetWinnerIfGameIsNotOver() {
    Assert.assertFalse(model.isGameOver());

    model.move(-1, -1, 2); // black's turn - valid move
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over

    model.move(1, 1, -2); // black valid move
    model.move(-1, 2, -1); // while valid move
    Assert.assertFalse(model.isGameOver()); // game still isnt over
    Assert.assertSame(model.getGameStatus(), GameStatus.PLAYING);

    Assert.assertThrows(IllegalStateException.class, () -> model.getWinner());
    //check to see if you can't get winner when game isnt over
  }

  @Test
  public void testGetCurrPlayer() {
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.BLACK);
    model.pass(); // skip black's turn
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.WHITE);
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.BLACK);
    model.move(-1, -1, 2); // black's turn - valid move
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.WHITE);
  }


  @Test
  public void testGetScore() {
    Assert.assertSame(model.getScore(ReversiPiece.BLACK), 3);
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.BLACK);
    model.pass(); // skip black's turn
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.WHITE);
    Assert.assertSame(model.getScore(ReversiPiece.WHITE), 3);
    model.move(2, -1, -1); // white's move - valid move
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.BLACK);
    Assert.assertSame(model.getScore(ReversiPiece.WHITE), 5);
    model.move(-1, -1, 2); // black's turn - valid move
    Assert.assertSame(model.getCurrentPlayer(), ReversiPiece.WHITE);
    Assert.assertSame(model.getScore(ReversiPiece.BLACK), 4);
  }

  @Test
  public void testGetPieceAtCoord() {
    Assert.assertEquals(model.getPieceAt(0, 0, 0), ReversiPiece.EMPTY);
    Assert.assertEquals(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(0, 1, -1), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(-1, 0, 1), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(1, 0, -1), ReversiPiece.BLACK);
    Assert.assertEquals(model.getPieceAt(-1, 1, 0), ReversiPiece.BLACK);
    Assert.assertEquals(model.getPieceAt(0, -1, 1), ReversiPiece.BLACK);
  }

  @Test
  public void testGetPieceAtCoordInvalid() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
        model.getPieceAt(-2, -4, -12));
    Assert.assertThrows(IllegalArgumentException.class, () ->
        model.getPieceAt(102, 0, -2));
    Assert.assertThrows(IllegalArgumentException.class, () ->
        model.getPieceAt(-5, 5, 3));
  }

  @Test
  public void testGetPieceAtTile() {
    Tile t = new Tile(0, 0, 0);
    Assert.assertEquals(model.getPieceAt(t), ReversiPiece.EMPTY);
    t = new Tile(-1, 0, 1);
    Assert.assertEquals(model.getPieceAt(t), ReversiPiece.WHITE);
    t = new Tile(-1, 1, 0);
    Assert.assertEquals(model.getPieceAt(t), ReversiPiece.BLACK);
  }


  @Test
  public void testGetCopyAtStart() {
    Map<Tile, ReversiPiece> copyBoard = model.getBoard();
    ReversiModel m = new ReversiGameModel(copyBoard, 3);

    Assert.assertEquals(m.getBoard(), model.getBoard()); // same boards
    // same score
    Assert.assertEquals(m.getScore(ReversiPiece.BLACK), model.getScore(ReversiPiece.BLACK));
    Assert.assertEquals(m.getScore(ReversiPiece.WHITE), model.getScore(ReversiPiece.WHITE));

    // same pieces
    Assert.assertEquals(m.getPieceAt(-1, 0, 1), ReversiPiece.WHITE);
    Assert.assertEquals(model.getPieceAt(-1, 0, 1), ReversiPiece.WHITE);
  }

  @Test
  public void testGetSide3HexagonBoard() {
    Assert.assertEquals(model.getHexSideLength(), 3);
  }

  @Test
  public void testGetCopyInMiddle() {
    model = new ReversiGameModel(7);
    model.startGame();
    model.move(1, -2, 1); // black
    model.move(2, -1, -1); // white
    model.move(-2, 1, 1); // black
    model.move(1, -3, 2); // white
    model.move(1, 1, -2); // black
    model.move(-1, 2, -1); // white
    model.move(2, -3, 1); // black
    model.move(-1, -1, 2); // white

    Map<Tile, ReversiPiece> copyBoard = model.getBoard();
    ReversiModel m = new ReversiGameModel(copyBoard, 7);
    m.startGame();

    Assert.assertEquals(m.getBoard(), model.getBoard()); // same boards
    // same score
    Assert.assertEquals(m.getScore(ReversiPiece.BLACK), model.getScore(ReversiPiece.BLACK));
    Assert.assertEquals(m.getScore(ReversiPiece.WHITE), model.getScore(ReversiPiece.WHITE));

    // same pieces in same spot
    Assert.assertSame(model.getPieceAt(-1, -1, 2), m.getPieceAt(-1, -1, 2));
    Assert.assertSame(model.getPieceAt(0, -1, 1), m.getPieceAt(0, -1, 1));
    Assert.assertSame(model.getPieceAt(1, -1, 0), m.getPieceAt(1, -1, 0));
    Assert.assertSame(model.getPieceAt(2, -1, -1), m.getPieceAt(2, -1, -1));

    Assert.assertSame(model.getPieceAt(-1, -1, 2), m.getPieceAt(-1, -1, 2));
    Assert.assertSame(model.getPieceAt(-1, 0, 1), m.getPieceAt(-1, 0, 1));
    Assert.assertSame(model.getPieceAt(-1, 1, 0), m.getPieceAt(-1, 1, 0));

    // same curr player
    Assert.assertSame(model.getCurrentPlayer(), m.getCurrentPlayer());
  }

  @Test
  public void ensureNumFlippedIsEqualToActualFlippingWhenMoving() {
    Assert.assertEquals(model.getScore(ReversiPiece.BLACK), 3);
    Assert.assertEquals(model.numTilesGained(1, -2, 1, ReversiPiece.BLACK), 2);
    model.move(1, -2, 1);
    Assert.assertEquals(model.getScore(ReversiPiece.BLACK), 5);
  }
}
