package cs3500.reversi.player;

import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.MockReversiGameModel;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;

public class TestStrategies {

  private ReversiModel m;
  @Before
  public void setUp() {
    m = new ReversiGameModel(6);
  }
  @Test
  public void testOnlyCornersAIStrat() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockReversiGameModel(m, out);

    Strategy cornerStrat = new Strategy(new PlayCornersStrategy());
    Player AIEasy = new Player(cornerStrat, ReversiPiece.BLACK);

    // and then check that the output for this contains the positions of all for corner tiles,
    // meaning it correctly checked every option
    AIEasy.getPlayerDecision(mock);
  }

  // write similar such tests for the CaptureMost strategy, avoidNextToCorners strategy,
  // tests where there are two players of different strategies, and with the ManyStrategy, which is
  // a strategy that can take multiple strategies.

   /*
   so like i guess a checklist of ones to do would be
   - just capturemost
   - just avoid next to corners
   - just human strategy (you have to give human strategy a readable, so make a string reader with sample inputs to move to)
   - capture most and avoidnext to corners playing next to each other
   - capture most and playcorners
   - capture most and human player
    */

}
