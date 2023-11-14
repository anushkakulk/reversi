package cs3500.reversi.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import cs3500.reversi.model.MockFakeReversiGameModel;
import cs3500.reversi.model.MockLogReversiGameModel;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Test Suite for testing all strategies, specifically from
 * the IPlayerMoveStrategy interface.
 */
public class TestStrategies {

  private ReversiModel m;


  @Before
  public void setUp() {
    m = new ReversiGameModel(6);

  }

  @Test
  public void testOnlyCornersautoPlayerStrat() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockLogReversiGameModel(m, out);

    Strategy cornerStrat = new Strategy(new PlayCornersStrategy());
    ReversiPlayer autoPlayer = new Player(cornerStrat, ReversiPiece.BLACK);

    // no corners to play, so pass
    Assert.assertEquals(autoPlayer.getPlayerDecision(mock), new Pass());

    Assert.assertTrue(out.toString().contains("isValidMove: -5, 0, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 5, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 0, -5, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 0, 5, -5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 5, -5, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 5, 0, -5, X."));
  }

  @Test
  public void testOnlyCaptureMost() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockLogReversiGameModel(m, out);

    Strategy capStrat = new Strategy(new CaptureMostStrategy());
    ReversiPlayer autoPlayer = new Player(capStrat, ReversiPiece.BLACK);

    Assert.assertEquals(autoPlayer.getPlayerDecision(mock),
            new Move(new ReversiPosn(-1, -1, 2)));
    // 6 possible moves that all capture 1 tile. so, the strategy tie breaks and chooses the upper
    // left most. Since there is are two possible "upper-left most tiles," the strat prioritizes the
    // left most over upper most and chooses tile (-1, -1, 2)
    Assert.assertTrue(out.toString().contains("isValidMove: -2, 1, 1, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -1, -1, 2, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -1, 2, -1, X.\n" +
            "numTilesGained: 2."));
    m.move(-1, -1, 2);
    m.move(2, -1, -1);

    autoPlayer.getPlayerDecision(mock);
    Assert.assertEquals(autoPlayer.getPlayerDecision(mock),
            new Move(new ReversiPosn(1, 1, -2)));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, 1, -2, X.\n" +
            "numTilesGained: 5."));
    Assert.assertTrue(out.toString().contains("isValidMove: 3, -1, -2, X.\n" +
            "numTilesGained: 3."));
  }

  @Test
  public void testOnlyAvoidCorners() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockLogReversiGameModel(m, out);

    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    ReversiPlayer autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    autoPlayer.getPlayerDecision(mock);

    Assert.assertEquals(autoPlayer.getPlayerDecision(mock),
            new Move(new ReversiPosn(-1, -1, 2)));
    // there are valid moves that aren't in corners and they all capture 1 tile.
    // so, the strategy tie breaks and chooses the upper
    // left most. Since there is are two possible "upper-left most tiles,"
    // the strat prioritizes the left most over upper most and chooses tile (-1, -1, 2)

    // assert that the strategy does NOT check tiles bordering corners
    Assert.assertFalse(out.toString().contains("isValidMove: -5, 1, 4, X."));
    Assert.assertFalse(out.toString().contains("isValidMove: -4, -1, 5, X."));
    Assert.assertFalse(out.toString().contains("isValidMove: 1, -5, 4, X."));
    Assert.assertFalse(out.toString().contains("isValidMove: 4, -4, 0, X."));
    Assert.assertFalse(out.toString().contains("isValidMove: 4, 0, -4, X."));
    Assert.assertFalse(out.toString().contains("isValidMove: -1, 5, -4, X."));
  }

  @Test
  public void testBothCaptureMostAndAvoid() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockLogReversiGameModel(m, out);

    List<IPlayerMoveStrategy> strategyList = Arrays.asList(new CaptureMostStrategy(),
            new AvoidNextToCornersStrategy());
    Strategy combo = new Strategy(new ManyStrategy(strategyList));
    ReversiPlayer autoPlayer = new Player(combo, ReversiPiece.BLACK);

    Assert.assertEquals(autoPlayer.getPlayerDecision(mock), new Move(new ReversiPosn(-1, -1, 2)));

    Assert.assertTrue(out.toString().contains("isValidMove: -1, -1, 2, X.\n" +
            "numTilesGained: 2.\n"));
    Assert.assertTrue(out.toString().contains("isValidMove: -4, 4, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -5, 4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 5, -4, -1, X."));

    m.move(-1, -1, 2);
    m.move(2, -1, -1);
    Assert.assertEquals(autoPlayer.getPlayerDecision(mock), new Move(new ReversiPosn(1, 1, -2)));


    Assert.assertTrue(out.toString().contains("isValidMove: 3, -1, -2, X.\n" +
            "numTilesGained: 3.\n"));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, 1, -2, X.\n" +
            "numTilesGained: 5.")); // this has the most tiles gained so it chooses this
    Assert.assertTrue(out.toString().contains("isValidMove: -4, 4, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -5, 4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 5, -4, -1, X."));
  }

  @Test
  public void testBothCaptureMostAndPlayCorners() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockLogReversiGameModel(m, out);

    List<IPlayerMoveStrategy> strategyList = Arrays.asList(
            new PlayCornersStrategy(), new CaptureMostStrategy());
    Strategy combo = new Strategy(new ManyStrategy(strategyList));
    ReversiPlayer autoPlayer = new Player(combo, ReversiPiece.BLACK);

    Assert.assertEquals(autoPlayer.getPlayerDecision(mock), new Move(new ReversiPosn(-1, -1, 2)));

    Assert.assertTrue(out.toString().contains("isValidMove: -1, -1, 2, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -1, 2, -1, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -2, 1, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 0, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 5, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 0, -5, 5, X."));


    m.move(-1, -1, 2); // simulate the chosen move
    m.move(2, -1, -1); // simulate the opponent move
    // the first strategy tries to check if the corners are a valid move. none of them are,
    // the the second strategy tries to pick the next move
    Assert.assertTrue(out.toString().contains("isValidMove: -2, 1, 1, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -1, -1, 2, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -1, 2, -1, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -2, 1, X.\n" +
            "numTilesGained: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 0, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 5, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 0, -5, 5, X."));
    // this move is chosen by the second strategy since the first one failed.
    Assert.assertEquals(autoPlayer.getPlayerDecision(mock), new Move(new ReversiPosn(1, 1, -2)));
  }

  @Test
  public void testCaptureMostStrategyWithLyingMockModel() {
    ReversiModel fake = new MockFakeReversiGameModel(m, 0, -5, 5);
    Strategy capStrat = new Strategy(new CaptureMostStrategy());
    ReversiPlayer autoPlayer = new Player(capStrat, ReversiPiece.BLACK);
    // the fake strategy says that moving to the top right corner is a valid move from the start
    // and will return a flip of 100 tiles. So, the strategy should choose that position, which
    // it does
    Assert.assertEquals(autoPlayer.getPlayerDecision(fake), new Move(new ReversiPosn(0, -5, 5)));
  }

  @Test
  public void testAvoidNextToCornerWithLyingMockModel() {
    ReversiModel fake = new MockFakeReversiGameModel(m, 0, -5, 4);
    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    ReversiPlayer autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);
    // the fake strategy says that moving to the a tile bordering
    // the top left corner is a valid move from the start
    // and will return a flip of 100 tiles. But, since it is bordering a corner tile, this strat
    // should ignore that fact entirely and just pick the top-left most valid move it can.
    Assert.assertEquals(autoPlayer.getPlayerDecision(fake),
            new Move(new ReversiPosn(-1, -1, 2)));
  }
}
