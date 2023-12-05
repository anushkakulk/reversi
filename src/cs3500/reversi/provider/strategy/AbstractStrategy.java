package cs3500.reversi.provider.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.provider.discs.Disc;
import cs3500.reversi.provider.discs.DiscColor;
import cs3500.reversi.provider.model.MoveDirection;
import cs3500.reversi.provider.model.MoveRules;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.player.PlayerTurn;

/**
 * A Utility class that holds important operations for a strategy to make decesions.
 */
public abstract class AbstractStrategy implements IStrategy {

  protected final ReadOnlyReversiModel reversiModel;
  protected final PlayerTurn player;
  protected boolean isAvoidCorners;
  private HashMap<List<Integer>, Integer> coordinateMap = new HashMap<>();

  /**
   * A 'AbstractStrategy' constructor for a non-corners strategy.
   *
   * @param reversiModel a reversiModel.
   * @param player       a player turn.
   */
  public AbstractStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    this.reversiModel = reversiModel;
    this.player = player;
  }

  /**
   * A 'AbstractStrategy' constructor for a corners strategy.
   *
   * @param reversiModel   a reversiModel.
   * @param player         a player turn.
   * @param isAvoidCorners should the strategy avoid corners.
   */
  public AbstractStrategy(ReadOnlyReversiModel reversiModel,
                          PlayerTurn player, boolean isAvoidCorners) {
    this.reversiModel = reversiModel;
    this.player = player;
    this.isAvoidCorners = isAvoidCorners;
  }

  protected List<List<Integer>> getPositionsForBFS() {
    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < this.reversiModel.getDimensions(); i++) {
      for (int j = 0; j < this.reversiModel.getDimensions(); j++) {
        try {
          DiscColor currentDiscColor = this.reversiModel.getDiscAt(i, j).getColor();
          if (currentDiscColor == DiscColor.FACEDOWN) {
            res.add(new ArrayList<>(Arrays.asList(i, j)));
          }
        } catch (IllegalArgumentException iae) {
          // we encountered a null cell do nothing
        }
      }
    }
    return res;
  }

  private DiscColor getOppositeColor(DiscColor discColor) {
    switch (discColor) {
      case FACEDOWN:
        return DiscColor.FACEDOWN;
      case WHITE:
        return DiscColor.BLACK;
      case BLACK:
        return DiscColor.WHITE;
      default:
        // do nothing
    }
    return null;
  }

  private List<List<Integer>> getAllAdjacent(int i, int j) {
    List<List<Integer>> res = new ArrayList<>();
    for (MoveDirection moveDir : MoveDirection.values()) {
      List<Integer> currCoordinate = MoveRules.applyShiftBasedOnDirection(i, j, moveDir);
      int x = currCoordinate.get(0);
      int y = currCoordinate.get(1);
      if (this.coordinateMap.get(new ArrayList<>(Arrays.asList(x, y))) == null) {
        this.coordinateMap.put(new ArrayList<>(Arrays.asList(x, y)), 1);
        try {
          Disc currDisc = this.reversiModel.getDiscAt(x, y);
          if (currDisc.getColor() == DiscColor.FACEDOWN) {
            res.add(new ArrayList<>(Arrays.asList(x, y)));
          }
        } catch (IllegalArgumentException iae) {
          System.out.println("null cell encountered");
        }
      }
    }
    return res;
  }

  protected int getLengthOfMove(List<List<List<Integer>>> move) {
    int length = 0;
    for (List<List<Integer>> innerList : move) {
      length += innerList.size();
    }
    return length;
  }

  protected List<Integer> getLongestAndMostUpLeftFromMap(HashMap<List<Integer>,
      List<List<List<Integer>>>> positionMoveMap) {
    List<List<Integer>> res = new ArrayList<>();
    int largestMoveLength = Integer.MIN_VALUE;
    for (Map.Entry<List<Integer>, List<List<List<Integer>>>> entry : positionMoveMap.entrySet()) {
      int currMoveLength = this.getLengthOfMove(entry.getValue());
      if (currMoveLength > largestMoveLength) {
        largestMoveLength = currMoveLength;
        res.clear();
        res.add(entry.getKey());
      } else if (currMoveLength == largestMoveLength) {
        res.add(entry.getKey());
      }
    }
    // get up most left if number of rows is more than 1
    if (res.size() == 1) {
      return res.get(0);
    } else {
      int mostLeftX = Integer.MAX_VALUE;
      int mostLeftY = Integer.MAX_VALUE;
      for (List<Integer> pos : res) {
        int currX = pos.get(0);
        int currY = pos.get(1);
        if (currX < mostLeftX || (currX == mostLeftX && currY < mostLeftY)) {
          mostLeftX = currX;
          mostLeftY = currY;
        }
      }
      return new ArrayList<>(Arrays.asList(mostLeftX, mostLeftY));
    }
  }

  protected List<Integer> getUpLeftMostInMove(List<List<List<Integer>>> move) {
    int mostLeftX = Integer.MAX_VALUE;
    int mostLeftY = Integer.MAX_VALUE;
    for (List<List<Integer>> inner : move) {
      for (List<Integer> position : inner) {
        int currX = position.get(0);
        int currY = position.get(1);
        if (currX < mostLeftX || (currX == mostLeftX && currY < mostLeftY)) {
          mostLeftX = currX;
          mostLeftY = currY;
        }
      }
    }
    return new ArrayList<>(Arrays.asList(mostLeftX, mostLeftY));
  }

  protected List<Integer> getMoveWithClosestCoordinateFromMap(HashMap<List<Integer>,
      List<List<List<Integer>>>> positionMoveMap, HashMap<Integer, List<Integer>> cornerMap) {
    List<Integer> closestMoveToCorner = new ArrayList<>();
    int closestDistanceToACorner = Integer.MAX_VALUE;
    for (Map.Entry<List<Integer>, List<List<List<Integer>>>> entry : positionMoveMap.entrySet()) {
      int currX = entry.getKey().get(0);
      int currY = entry.getKey().get(1);
      for (List<Integer> corner : cornerMap.values()) {
        int cornerX = corner.get(0);
        int cornerY = corner.get(1);
        int xDistance = Math.abs(cornerX - currX);
        int yDistance = Math.abs(cornerY - currY);
        if (xDistance + yDistance < closestDistanceToACorner) {
          closestDistanceToACorner = xDistance + yDistance;
          closestMoveToCorner = entry.getKey();
        } else if (xDistance + yDistance == closestDistanceToACorner) {
          int mapX = closestMoveToCorner.get(0);
          int mapY = closestMoveToCorner.get(1);
          if (currX < mapX || (currX == mapX && currY < mapY)) {
            closestMoveToCorner = entry.getKey();
          }
        }
      }
    }
    return closestMoveToCorner;
  }

  // closest is determined by the sum of the distance from a corner
  protected int getClosestCoordinateToCorner(List<List<List<Integer>>> move,
                                             HashMap<Integer, List<Integer>> cornerMap) {
    int closestDistanceToACorner = Integer.MAX_VALUE;
    for (List<List<Integer>> innerList : move) {
      for (List<Integer> pos : innerList) {
        int x = pos.get(0);
        int y = pos.get(1);
        for (List<Integer> corner : cornerMap.values()) {
          int cornerX = corner.get(0);
          int cornerY = corner.get(1);
          int xDistance = Math.abs(cornerX - x);
          int yDistance = Math.abs(cornerY - y);
          if (xDistance + yDistance < closestDistanceToACorner) {
            closestDistanceToACorner = xDistance + yDistance;
          }
        }
      }
    }
    return closestDistanceToACorner;
  }

  protected boolean moveIsAdjacentToCorner(List<List<List<Integer>>> moveFromPosition,
                                           HashMap<Integer, List<Integer>> cornerMap) {
    for (List<List<Integer>> innerList : moveFromPosition) {
      for (List<Integer> position : innerList) {
        if (adjacentPositionInMap(position, cornerMap)) {
          return true;
        }
      }
    }
    return false;
  }


  protected boolean adjacentPositionInMap(List<Integer> position, HashMap<Integer,
      List<Integer>> cornerMap) {
    int x = position.get(0);
    int y = position.get(1);
    for (MoveDirection md : MoveDirection.values()) {
      List<Integer> adjacentPos = MoveRules.applyShiftBasedOnDirection(x, y, md);
      if (cornerMap.values().contains(adjacentPos)) {
        return true;
      }
    }
    return false;
  }
}
