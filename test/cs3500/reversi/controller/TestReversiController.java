package cs3500.reversi.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.AvoidNextToCornersStrategy;
import cs3500.reversi.player.CaptureMostStrategy;
import cs3500.reversi.player.IPlayerMoveStrategy;
import cs3500.reversi.player.ManyStrategy;
import cs3500.reversi.player.PlayCornersStrategy;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.Strategy;
import cs3500.reversi.view.ReversiGUIView;
import cs3500.reversi.view.ReversiView;

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

  private StringBuilder out;

  @Before
  public void setUp() {
    m = new ReversiGameModel(6);
    v = new ReversiGUIView(m);
    out = new StringBuilder();
  }

  @Test
  public void testReceivingTurnChangeNotifsFromModel() {
    m = new ReversiGameModel(3);
    Player p1 = new Player(new Strategy(new CaptureMostStrategy()),
        ReversiPiece.BLACK);
    Player p2 = new Player(new Strategy(new CaptureMostStrategy()),
        ReversiPiece.WHITE);

    c = new ReversiController(m, p1, v);
    c2 = new ReversiController(m, p2, v);

    mock = new MockLogReversiGameController(out, p1, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, p2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    Assert.assertTrue(out.toString().contains("MyPlayer: X, timesTurnChanged: 1. " +
        "isMyTurn: true\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, timesTurnChanged: 1." +
        " isMyTurn: false\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, timesTurnChanged: 2. " +
        "isMyTurn: true\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, timesTurnChanged: 2. " +
        "isMyTurn: false\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, timesTurnChanged: 3. " +
        "isMyTurn: true\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, timesTurnChanged: 3. " +
        "isMyTurn: false\n"));

  }


  @Test
  public void testWholeGameWithCaptureManyToCompletion() {
    m = new ReversiGameModel(3);
    Player p1 = new Player(new Strategy(new CaptureMostStrategy()),
        ReversiPiece.BLACK);
    Player p2 = new Player(new Strategy(new CaptureMostStrategy()),
        ReversiPiece.WHITE);

    StringBuilder out2 = new StringBuilder();
    c = new ReversiController(m, p1, v);
    c2 = new ReversiController(m, p2, v);

    mock = new MockLogReversiGameController(out, p1, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, p2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();


    // for this gameplay, the moves are written from the bottom up
    Assert.assertTrue(out.toString().contains("Notification from Model: Game Over."));
    // the notif for game over comes as both players have no more playable moves
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 1, -2, 1\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: 2, -1, -1\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 1, 1, -2\n"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, 2, -1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -2, 1, 1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));

  }


  @Test
  public void testReceivingGameOverNotifsFromModel() {
    m = new ReversiGameModel(6);
    Player p1 = new Player(new Strategy(new CaptureMostStrategy()),
        ReversiPiece.BLACK);
    Player p2 = new Player(new Strategy(new CaptureMostStrategy()),
        ReversiPiece.WHITE);

    c = new ReversiController(m, p1, v);
    c2 = new ReversiController(m, p2, v);

    mock = new MockLogReversiGameController(out, p1, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, p2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    // ensure that the model is telling us that the game is over after playing.
    Assert.assertTrue(out.toString().contains("Notification from Model: Game Over."));
  }


  // this plays to completion! it works !
  @Test
  public void testControllerAvoidNextToCornerVSCaptureMost() {
    Strategy avoidStrat = new Strategy(new AvoidNextToCornersStrategy());
    Player autoPlayer = new Player(avoidStrat, ReversiPiece.BLACK);

    Strategy capStrat = new Strategy(new CaptureMostStrategy());
    Player autoPlayer2 = new Player(capStrat, ReversiPiece.WHITE);

    c = new ReversiController(m, autoPlayer, v);
    c2 = new ReversiController(m, autoPlayer2, v);

    mock = new MockLogReversiGameController(out, autoPlayer, c, ReversiPiece.BLACK, m, v);
    mock2 = new MockLogReversiGameController(out, autoPlayer2, c2, ReversiPiece.WHITE, m, v);

    m.startGame();

    // we are testing that the controller is listening to notifications emitted by the player action
    // and communicating those changes to the model.
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: -1, -1, 2"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: -4, 3, 1"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Move Chosen: 2, 1, -3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: O, Move Chosen: 1, -4, 3"));
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Pass Chosen\n" +
        "MyPlayer: O, Pass Chosen"));
    // both passed, so the game is over!
    Assert.assertTrue(out.toString().contains("Notification from Model: Game Over."));


  }

  @Test
  public void testControllerCaptureMostBoth() {


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
    // here the strategy avoids the move
    Assert.assertTrue(out.toString().contains("MyPlayer: X, Pass Chosen\n" +
        "MyPlayer: O, Pass Chosen"));
  }


}
