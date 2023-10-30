import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestReversiView {
  private ReversiModel modelSize7;
  private ReversiTextualView viewSize7;
  private ReversiModel modelSize3;
  private ReversiTextualView viewSize3;

  @Before
  public void setUp() {
    modelSize7 = new ReversiGameModel(7);
    viewSize7 = new ReversiTextualView(modelSize7);

    modelSize3 = new ReversiGameModel(3);
    viewSize3 = new ReversiTextualView(modelSize3);
  }

  @Test
  public void testViewAtStart() {
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

    Assert.assertEquals(initBoard, viewSize7.render());
  }

  @Test
  public void testViewAfterMove() {
    Assert.assertSame(modelSize3.getPieceAt(1, -1, 0), ReversiPiece.WHITE);
    Assert.assertSame(modelSize3.getPieceAt(1, -2, 1), ReversiPiece.EMPTY);

    modelSize3.move(1, -2, 1);

    Assert.assertEquals(viewSize3.render(),
            "  _ X _ \n" +
                    " _ X X _ \n" +
                    "_ O _ X _ \n" +
                    " _ X O _ \n" +
                    "  _ _ _ \n");

  }


  @Test
  public void testViewAfterMultipleMoves() {
    modelSize7.move(1, -2, 1); // black moves and captures a white
    modelSize7.move(-1, 2, -1); // white mirrors the move, captures a black
    Assert.assertEquals(viewSize7.render(),
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
    modelSize7.move(1, -2, 1);
    modelSize7.move(2, -1, -1);
    // there are opponents in two neighbors of the destination tile. this tests whether the move
    // method correctly finds out where the move is coming from to be valid (as in, it should know
    // that even though there are two X's to the left of the dest tile, there is no O adjacent
    // to the X's, so it should look at the other neighbor. That neighbor has an O adjacent to it
    // so it makes that valid legal move.
    Assert.assertEquals(viewSize7.render(),
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
