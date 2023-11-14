package cs3500.reversi.player;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import cs3500.reversi.model.GameStatus;
import cs3500.reversi.model.MockReversiGameModel;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.model.Tile;

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


    AIEasy.getPlayerDecision(mock);
    // and then check that the output for this contains the positions of all for corner tiles,
    // meaning it correctly checked every option
    // as in write something like 6 versions of (assert.equals(out.contains (q, r, s))),
    // where q r s are the coords for a corner position, since this strat tries to pick corners
    // first

  }

  // write similar such tests for the CaptureMost strategy, avoidNextToCorners strategy,
  // tests where there are two players of different strategies, and with the ManyStrategy, which is
  // a strategy that can take multiple strategies.

   /*
   so like i guess a checklist of ones to do would be
   - just capturemost (for this one, call and test the getPlayerDecision a few times since this one
             will probably always actually pick a valid move)
   - just avoid next to corners
   - just human strategy (you have to give human strategy a readable, so make a string reader with sample inputs to move to)
   - capture most and avoidnext to corners playing next to each other
   - capture most and playcorners
   - capture most and human player
    */

}
