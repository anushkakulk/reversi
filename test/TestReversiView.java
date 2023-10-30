import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestReversiView {
  private ReversiModel model;
  private ReversiTextualView view;

  @Before
  public void setUp() {
    model = new ReversiGameModel();
    view = new ReversiTextualView(model);
  }

  @Test
  public void testViewAtStart() {
    model.startGame(model.getBoard(7));
    String initBoard =
                    "      _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ _ _ \n" +
                    "  _ _ _ _ _ _ _ _ _ _ _ \n" +
                    " _ _ _ _ _ X O _ _ _ _ _ \n" +
                    "_ _ _ _ _ O _ X _ _ _ _ _ \n" +
                    " _ _ _ _ _ X O _ _ _ _ _ \n" +
                    "  _ _ _ _ _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ _ _ \n" +
                    "      _ _ _ _ _ _ _ \n";

    Assert.assertEquals(initBoard, view.render());
  }

  @Test
  public void testViewAfterMove() {
    model.startGame(model.getBoard(3));
    Assert.assertSame(model.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    Assert.assertSame(model.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    model.move(1, -2, 1);

    Assert.assertEquals(view.render(),
            "  _ X _ \n" +
                    " _ X X _ \n" +
                    "_ O _ X _ \n" +
                    " _ X O _ \n" +
                    "  _ _ _ \n");

  }


  @Test
  public void testViewAfterMultipleMoves() {
    model.startGame(model.getBoard(7));
    model.move(1, -2, 1); // black moves and captures a white
    model.move(-1, 2, -1); // white mirrors the move, captures a black
    Assert.assertEquals(view.render(),
            "      _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ _ _ \n" +
                    "  _ _ _ _ _ X _ _ _ _ _ \n" +
                    " _ _ _ _ _ X X _ _ _ _ _ \n" +
                    "_ _ _ _ _ O _ X _ _ _ _ _ \n" +
                    " _ _ _ _ _ O O _ _ _ _ _ \n" +
                    "  _ _ _ _ _ O _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ _ _ \n" +
                    "      _ _ _ _ _ _ _ \n");
  }

  @Test
  public void testViewAfterMoveWhereTheresTwoDirectionsToFollow() {
    model.startGame(model.getBoard(7));
    model.move(1, -2, 1);
    model.move(2, -1, -1);
    // there are opponents in two neighbors of the destination tile. this tests whether the move
    // method correctly finds out where the move is coming from to be valid (as in, it should know
    // that even though there are two X's to the left of the dest tile, there is no O adjacent
    // to the X's, so it should look at the other neighbor. That neighbor has an O adjacent to it
    // so it makes that valid legal move.
    Assert.assertEquals(view.render(),
            "      _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ _ _ \n" +
                    "  _ _ _ _ _ X _ _ _ _ _ \n" +
                    " _ _ _ _ _ X X O _ _ _ _ \n" +
                    "_ _ _ _ _ O _ O _ _ _ _ _ \n" +
                    " _ _ _ _ _ X O _ _ _ _ _ \n" +
                    "  _ _ _ _ _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ _ _ \n" +
                    "      _ _ _ _ _ _ _ \n");
  }
}
