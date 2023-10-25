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
    view = new ReversiTextualView(model);
    String initBoard =
            "       \n" +
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
                    "      _ _ _ _ _ _ _ \n" +
            "       \n";

    Assert.assertEquals(initBoard, view.render());
  }
}
