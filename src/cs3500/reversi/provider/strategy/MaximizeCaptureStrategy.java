package cs3500.reversi.provider.strategy;

import java.util.HashMap;
import java.util.List;

import cs3500.reversi.provider.model.BoardUtils;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.player.PlayerTurn;


/**
 * A 'MaximizeCaptureStrategy' represents a strategy
 * that selects the longest move possible and then
 * selects the most up-left move.
 */
public class MaximizeCaptureStrategy extends AbstractStrategy {

  private final StrategyType strategyType;

  /**
   * A 'MaximizeCaptureStrategy' constructor.
   */
  public MaximizeCaptureStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    super(reversiModel, player);
    this.strategyType = StrategyType.MAXIMIZE;
  }

  @Override
  public List<Integer> executeStrategy() {
    HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap = new HashMap<>();
    List<List<Integer>> validPositions = getPositionsForBFS();
    for (List<Integer> position : validPositions) {
      List<List<List<Integer>>> moveFromPosition = BoardUtils.bfs(this.reversiModel,
              position.get(0), position.get(1));
      if (positionMoveMap.get(position) == null) {
        positionMoveMap.put(position, moveFromPosition);
      } else {
        List<List<List<Integer>>> moveInMap = positionMoveMap.get(position);
        int lengthOfMoveInMap = getLengthOfMove(moveInMap);
        int lengthOfMoveFromPos = getLengthOfMove(moveFromPosition);
        if (lengthOfMoveFromPos > lengthOfMoveInMap) {
          positionMoveMap.put(position, moveFromPosition);
        } else if (lengthOfMoveFromPos == lengthOfMoveInMap) {
          List<Integer> mapMoveMostUpLeft = getUpLeftMostInMove(moveInMap);
          List<Integer> posMoveMostUpLeft = getUpLeftMostInMove(moveFromPosition);
          int mapX = mapMoveMostUpLeft.get(0);
          int mapY = mapMoveMostUpLeft.get(1);
          int currX = posMoveMostUpLeft.get(0);
          int currY = posMoveMostUpLeft.get(1);
          if (currX < mapX || (currX == mapX && currY < mapY)) {
            positionMoveMap.put(position, moveFromPosition);
          }
        }

      }
    }
    return getLongestAndMostUpLeftFromMap(positionMoveMap);
  }

  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }


}