package cs3500.reversi.provider.strategy;

import java.util.List;

import cs3500.reversi.provider.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.player.PlayerTurn;

/**
 * A 'MiniMaxStrategy' represents a strategy
 * that identifies a human's strategy and executes the same strategy.
 * if unidentified strategy is used default to maximizeCapture
 */
public class MiniMaxStrategy extends AbstractStrategy {
  private final StrategyType strategyType;

  /**
   * A 'MiniMaxStrategy' constructor.
   */
  public MiniMaxStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    super(reversiModel, player);

    this.strategyType = StrategyType.MINIMAX;
  }

  @Override
  public List<Integer> executeStrategy() {
    List<ReadOnlyReversiModel> gameStates = super.reversiModel.getGameStates();
    ReadOnlyReversiModel getLastState = gameStates.get(gameStates.size() - 2);
    List<List<Integer>> moves = getLastState.getMoves();
    List<Integer> lastMove = moves.get(moves.size() - 1);
    if (lastMove.equals(executeMaximize(getLastState))) {
      System.out.println("AI Chose Maximum");
      return executeMaximize(this.reversiModel);
    } else if (lastMove.equals(executeAvoidCorners(getLastState))) {
      System.out.println("AI Chose Avoid Corners");
      return executeAvoidCorners(this.reversiModel);
    } else if (lastMove.equals(executeGoForCorner(getLastState))) {
      System.out.println("AI Chose Go For Corners");
      return executeGoForCorner(this.reversiModel);
    } else {
      System.out.println("AI Chose Default: Maximize");
      return executeMaximize(this.reversiModel);
    }
  }

  private static List<Integer> executeGoForCorner(ReadOnlyReversiModel reversiModel) {
    CornersStrategy goForCorner = new CornersStrategy(reversiModel, reversiModel.currentTurn(),
            false);
    return goForCorner.executeStrategy();
  }

  private static List<Integer> executeAvoidCorners(ReadOnlyReversiModel reversiModel) {
    CornersStrategy avoidCorner = new CornersStrategy(reversiModel, reversiModel.currentTurn(),
            true);
    return avoidCorner.executeStrategy();
  }

  private static List<Integer> executeMaximize(ReadOnlyReversiModel reversiModel) {
    MaximizeCaptureStrategy maximizeCaptureStrategy = new MaximizeCaptureStrategy(reversiModel,
            reversiModel.currentTurn());
    return maximizeCaptureStrategy.executeStrategy();
  }

  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }
}