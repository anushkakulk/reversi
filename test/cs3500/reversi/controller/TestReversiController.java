package cs3500.reversi.controller;

import cs3500.reversi.player.CaptureMostStrategy;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.Strategy;
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
  private ReversiModel m;
  private ReversiView v;

  @Before
  public void setUp() {
    m = new ReversiGameModel(6);
    m.startGame();
    v = new ReversiGUIView(m);
    c = new ReversiController(m,
        new Player(new Strategy(new CaptureMostStrategy()), ReversiPiece.BLACK),
        v);
  }

  @Test
  public void testHandlePlayerChange() {
    StringBuilder out = new StringBuilder();
    MockLogReversiGameController mock = new MockLogReversiGameController(out, c);

    mock.handlePlayerChange(ReversiPiece.BLACK);

    Assert.assertTrue(out.toString().contains("handlePlayerChange: X"));
    Assert.assertFalse(out.toString().contains("handlePlayerChange: O"));

    mock.handlePlayerChange(ReversiPiece.WHITE);

    Assert.assertTrue(out.toString().contains("handlePlayerChange: X"));
    Assert.assertTrue(out.toString().contains("handlePlayerChange: O"));

  }

    @Test
  public void testHandleTileClicked() {
    StringBuilder out = new StringBuilder();
    MockLogReversiGameController mock = new MockLogReversiGameController(out, c);

    mock.handleTileClicked(0, 0, 0);

    Assert.assertTrue(out.toString().contains("handleTileClicked: 0, 0, 0"));

      mock.handleTileClicked(-1, -1, 2);
      mock.handleTileClicked(2, -1, -1);

      Assert.assertTrue(out.toString().contains("handleTileClicked: 0, 0, 0"));
      Assert.assertTrue(out.toString().contains("handleTileClicked: -1, -1, 2"));
      Assert.assertTrue(out.toString().contains("handleTileClicked: 2, -1, -1"));

  }

@Test
    public void testHandleMoveChosen() {
        StringBuilder out = new StringBuilder();
        MockLogReversiGameController mock = new MockLogReversiGameController(out, c);

        mock.handleMoveChosen(0, 0, 0);

        Assert.assertTrue(out.toString().contains("handleMoveChosen: 0, 0, 0"));

        mock.handleMoveChosen(-1, -1, 2);
        mock.handleMoveChosen(2, -1, -1);

        Assert.assertTrue(out.toString().contains("handleMoveChosen: 0, 0, 0"));
        Assert.assertTrue(out.toString().contains("handleMoveChosen: -1, -1, 2"));
        Assert.assertTrue(out.toString().contains("handleMoveChosen: 2, -1, -1"));

    }

    @Test
    public void testHandlePlayerAction() {
        StringBuilder out = new StringBuilder();
        MockLogReversiGameController mock = new MockLogReversiGameController(out, c);

        mock.handlePlayerChange(ReversiPiece.BLACK);
        mock.handlePlayerAction(() -> m.move(-1, -1, 2));

        Assert.assertTrue(out.toString().contains("handlePlayerAction: () -> m.move(-1, -1, 2)"));

    }


}
