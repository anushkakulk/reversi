import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TestReversiModel {

  private ReversiModel model;

  @Before
  public void setUp() {
    model = new ReversiGameModel();
  }

  @Test
  public void testGetSmallBoard() {
    List<Tile> expectedBoard = new ArrayList<>(Arrays.asList(
            new Tile(-1, 0, 1),
            new Tile(-1, 1, 0),
            new Tile(0, -1, 1),
            new Tile(0, 0, 0),
            new Tile(0, 1, -1),
            new Tile(1, -1, 0),
            new Tile(1, 0, -1)));
    Assert.assertEquals(expectedBoard, model.getBoard(2));
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
    List<Tile> expectedBoard = Arrays.asList(
            new Tile(-2, 0, 2),
            new Tile(-2, 1, 1),
            new Tile(-2, 2, 0),
            new Tile(-1, -1, 2),
            new Tile(-1, 0, 1),
            new Tile(-1, 1, 0),
            new Tile(-1, 2, -1),
            new Tile(0, -2, 2),
            new Tile(0, -1, 1),
            new Tile(0, 0, 0),
            new Tile(0, 1, -1),
            new Tile(0, 2, -2),
            new Tile(1, -2, 1),
            new Tile(1, -1, 0),
            new Tile(1, 0, -1),
            new Tile(1, 1, -2),
            new Tile(2, -2, 0),
            new Tile(2, -1, -1),
            new Tile(2, 0, -2)
    );
    Assert.assertEquals(expectedBoard, model.getBoard(3));
    model.startGame(model.getBoard(3));
    Assert.assertEquals(model.getHexSideLength(), 3);
  }

  @Test
  public void testStartGameWithNullBoardAndContents() {
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(null));
    Assert.assertThrows(IllegalArgumentException.class,
            () -> model.startGame(new ArrayList<>(Arrays.asList(new Tile(0, 0, 0), null))));
  }

  @Test
  public void testStartGameWithInvalidNumTilesForHexagonShape() {
    List<Tile> badBoard = new ArrayList<>(Arrays.asList(
            new Tile(-1, 0, 1),
            new Tile(-1, 1, 0),
            new Tile(0, -1, 1),
            new Tile(0, 0, 0),
            new Tile(0, 1, -1),
            new Tile(1, -1, 0),
            new Tile(1, 0, -1),
            new Tile(2, -2, 0)));
    Assert.assertThrows(IllegalArgumentException.class,
            () -> model.startGame(badBoard)); // 8 tiles for hexagonic shape -> bad

    Assert.assertThrows(IllegalArgumentException.class,
            () -> model.startGame(new ArrayList<>(Arrays.asList(new Tile(0, 0, 0),
                    new Tile(-1, 0, 1))))); // only 2 tiles for hexagonic shape -> bad
  }

  @Test
  public void testValidateHexagon() {
    List<Tile> badBoard = new ArrayList<>(Arrays.asList(
            new Tile(-1, 0, 1),
            new Tile(-1, 1, 0),
            new Tile(0, -1, 1),
            new Tile(0, 0, 0),
            new Tile(3, -2, -1), // this is the bad tile
            new Tile(1, -1, 0),
            new Tile(1, 0, -1)));

    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(badBoard));
    // bad board because even though we have 7 tiles,
    // we aren't given tiles with coordinates that form a complete valid hexagon board
  }

  @Test
  public void testGetPieceAt() {
    model.startGame(model.getBoard(3));

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
    model.startGame(model.getBoard(3));

    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(1, -2, 1);

    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.BLACK); // white flips to black
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.BLACK); // the empty tile is black
  }

  @Test
  public void testPassingSwitchesTurns() {
    model.startGame(model.getBoard(3));

    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(1, -2, 1); // black's turn
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.BLACK); // white flips to black
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.BLACK); // the empty tile is black

    model.switchPlayer();// this means white passed, now its black's turn
    model.switchPlayer(); // black can't make any moves, so black passes, now white's turn
    model.move(-1, 2, -1); // white's move

    Assert.assertSame(model.getPieceAt(-1, 1, 0), ReversiPiece.WHITE); // black flipped
    Assert.assertSame(model.getPieceAt(-1, 2, -1), ReversiPiece.WHITE); // empty is white

  }

  @Test
  public void testMakingAMoveThatFlipsMultipleOpponentTiles() {
    model.startGame(model.getBoard(5));

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
    model.startGame(model.getBoard(4));
    // not a legal move, not adjacent to a line of opponent tiles followed by a same player tile
    Assert.assertThrows(IllegalStateException.class, () -> model.move(-3, 0, 3));
  }

  @Test
  public void testMakingAMoveToNonEmptyTile() {
    model.startGame(model.getBoard(5));
    assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);
    model.move(1, -2, 1); // black validly moved to this position
    // now its white's turn, and white cannot move to it because its occupied
    Assert.assertThrows(IllegalStateException.class, () -> model.move(1, -2, 1));
  }

  @Test
  public void testValidMoveWhereTheresTwoDirectionsToCheckFor() {
    model.startGame(model.getBoard(7));
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
}
