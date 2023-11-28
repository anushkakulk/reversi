package cs3500.reversi.controller;

import cs3500.reversi.player.*;
import cs3500.reversi.view.ReversiGUIView;
import cs3500.reversi.view.ReversiTextualView;
import cs3500.reversi.view.ReversiView;
import cs3500.reversi.view.SimpleReversiView;
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
 * Test Suite for testing all public methods in the Controller package, specifically from
 * the IReversiController, PlayerActionFeatures and ModelStatusFeatures interfaces.
 */
public class TestReversiController {

  private ReversiController c;
  private ReversiController c2;
  private ReversiModel m;
  private ReversiView v;
  private MockLogReversiGameController mock;
  private MockLogReversiGameController mock2;

  @Test
  public void testControllerAvoidNextToCornerVSCaptureMost() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);

    StringBuilder out = new StringBuilder();

    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    Player autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    Strategy capStrat = new Strategy(new CaptureMostStrategy());
    Player autoPlayer2 = new Player(capStrat, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -4, 3, 1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: 2, 1, -3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 1, -4, 3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Pass Chosen\n" +
        "MyPlayer: O, Pass Chosen"));

  }

  @Test
  public void testControllerCaptureMostBoth() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);

    StringBuilder out = new StringBuilder();

    Strategy avoidStrat = new Strategy(new CaptureMostStrategy());
    Player autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    Strategy capStrat = new Strategy(new CaptureMostStrategy());
    Player autoPlayer2 = new Player(capStrat, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -2, 1, 1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -3, -1, 4"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -3, 0, 3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Pass Chosen\n" +
        "MyPlayer: X, Pass Chosen"));

  }

  @Test
  public void testControllerAvoidCornersBoth() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);

    StringBuilder out = new StringBuilder();

    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    Player autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    Strategy capStrat = new Strategy(new AvoidNextToCornersStrategy());
    Player autoPlayer2 = new Player(capStrat, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -2, 1, 1"));
    Assert.assertFalse(out.toString().contains("MyPlayer: X, Move Chosen: -5, 0, 5"));
    Assert.assertFalse(out.toString().contains("MyPlayer: O, Move Chosen: 0, 5, -5"));
    Assert.assertFalse(out.toString().contains("MyPlayer: O, Move Chosen: 5, -1, -4"));
    Assert.assertFalse(out.toString().contains("MyPlayer: O, Move Chosen: 0, 4, -4"));
    Assert.assertFalse(out.toString().contains("MyPlayer: O, Move Chosen: -4, 0, 4"));


  }

  @Test
  public void testControllerAvoidCornersVSPlayCornersAndCaptureMost() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);

    StringBuilder out = new StringBuilder();

    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    Player autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    List<IPlayerMoveStrategy> strategyList = Arrays.asList(
        new PlayCornersStrategy(), new CaptureMostStrategy());
    Strategy combo = new Strategy(new ManyStrategy(strategyList));
    Player autoPlayer2 = new Player(combo, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: 0, 3, -3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 5, -4, -1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, 2, -1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 3, -3, 0"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Pass Chosen\n" +
        "MyPlayer: X, Pass Chosen"));

  }

  @Test
  public void testControllerCaptureMostVSPlayCornersAndAvoidCorners() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);

    StringBuilder out = new StringBuilder();

    Strategy avoidStrat = new Strategy(new CaptureMostStrategy());
    Player autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    List<IPlayerMoveStrategy> strategyList = Arrays.asList(
        new PlayCornersStrategy(), new AvoidNextToCornersStrategy());
    Strategy combo = new Strategy(new ManyStrategy(strategyList));
    Player autoPlayer2 = new Player(combo, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -2, 1, 1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: 5, -4, -1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -5, 2, 3"));

  }

  @Test
  public void testControllerAvoidCornersAndCaptureMostVSPlayCornersAndCaptureMost() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);

    StringBuilder out = new StringBuilder();

    List<IPlayerMoveStrategy> strategyList1 = Arrays.asList(
        new AvoidNextToCornersStrategy(), new CaptureMostStrategy());
    Strategy combo1 = new Strategy(new ManyStrategy(strategyList1));
    Player autoPlayer = new Player(combo1, ReversiPiece.BLACK);

    List<IPlayerMoveStrategy> strategyList2 = Arrays.asList(
        new PlayCornersStrategy(), new CaptureMostStrategy());
    Strategy combo2 = new Strategy(new ManyStrategy(strategyList2));
    Player autoPlayer2 = new Player(combo2, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -2, 1, 1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: 0, 3, -3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 3, -3, 0"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Pass Chosen\n" +
        "MyPlayer: O, Pass Chosen"));

  }



}
