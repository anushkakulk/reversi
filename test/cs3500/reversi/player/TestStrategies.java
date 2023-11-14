package cs3500.reversi.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.MockReversiGameModel;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.view.ReversiTextualView;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

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
    ReversiModel mock = new MockReversiGameModel(m, out);

    Strategy capStrat = new Strategy(new CaptureMostStrategy());
    Player AIEasy = new Player(capStrat, ReversiPiece.BLACK);

    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 1."));
    m.move(-1,-1,2);
    m.move(2,-1,-1);
    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 1."));
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 2."));
  }

  @Test
  public void testOnlyAvoidCorners() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockReversiGameModel(m, out);

    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    Player AIEasy = new Player(avoidStrat, ReversiPiece.BLACK);

    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 1, 4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -4, -1, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -5, 4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 4, -4, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 4, 0, -4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -1, 5, -4, X."));
  }

  @Test
  public void testBothCaptureMostAndAvoid() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockReversiGameModel(m, out);

    List<IPlayerMoveStrategy> strategyList = Arrays.asList(new CaptureMostStrategy(),
        new AvoidNextToCornersStrategy());
    Strategy humanStrat = new Strategy(new ManyStrategy(strategyList));
    Player AIEasy = new Player(humanStrat, ReversiPiece.BLACK);

    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 1."));
    Assert.assertTrue(out.toString().contains("isValidMove: -4, 4, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -5, 4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 5, -4, -1, X."));

    m.move(-1,-1,2);
    m.move(2,-1,-1);
    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 1."));
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -4, 4, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 1, -5, 4, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 5, -4, -1, X."));

  }

  @Test
  public void testBothCaptureMostAndPlayCorners() {
    StringBuilder out = new StringBuilder();
    ReversiModel mock = new MockReversiGameModel(m, out);

    List<IPlayerMoveStrategy> strategyList = Arrays.asList(new CaptureMostStrategy(),
        new PlayCornersStrategy());
    Strategy humanStrat = new Strategy(new ManyStrategy(strategyList));
    Player AIEasy = new Player(humanStrat, ReversiPiece.BLACK);

    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 1."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 0, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 5, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 0, -5, 5, X."));

    m.move(-1,-1,2);
    m.move(2,-1,-1);
    AIEasy.getPlayerDecision(mock);
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 1."));
    Assert.assertTrue(out.toString().contains("numTilesFlipped: 2."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 0, 5, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: -5, 5, 0, X."));
    Assert.assertTrue(out.toString().contains("isValidMove: 0, -5, 5, X."));
  }

}
