import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            new Tile(-1,0,1),
            new Tile(-1,1,0),
            new Tile(0,-1,1),
            new Tile(0,0,0),
            new Tile(0,1,-1),
            new Tile(1,-1,0),
            new Tile(1,0,-1)));
    Assert.assertEquals(expectedBoard, model.getBoard(2));
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
  }

  @Test
  public void testMove() {
    List<Tile> board = Arrays.asList(
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

    model.startGame(board);

    model.move(1, -2, 1);

    assertTrue(model.getPieceAt(1,-1, 0) == ReversiPiece.BLACK);
    assertTrue(model.getPieceAt(1,-2, 1) == ReversiPiece.BLACK);




  }


}
