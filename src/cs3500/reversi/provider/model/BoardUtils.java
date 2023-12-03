package cs3500.reversi.provider.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.reversi.provider.discs.DiscColor;


/**
 * BoardUtils is a class consisting of a series of static
 * helper methods responsible for running a bfs at a specified
 * coordinate.
 */
public class BoardUtils {

  /**
   * Runs a BFS at a specified coordinate.
   *
   * @param rorm  the ReadOnlyReversiModel we are running the bfs on
   * @param destX the x coordinate we want to run the bfs from
   * @param destY the Y coordinate we want to run the bfs from
   */
  public static List<List<List<Integer>>> bfs(ReadOnlyReversiModel rorm,
                                              int destX, int destY) {
    List<List<List<Integer>>> res = new ArrayList<>();
    for (MoveDirection md : MoveDirection.values()) {
      List<List<Integer>> move = bfsHelper(rorm, destX, destY, md, new ArrayList<>(), true);
      if (!move.stream().allMatch(List::isEmpty)) {
        res.add(move);
      }
    }
    return res;
  }

  // A helper for bfs that determines coordinates for a move in a certain direction
  // returns an empty list if there are no moves for that direction
  private static List<List<Integer>> bfsHelper(ReadOnlyReversiModel rorm, int x, int y,
                                               MoveDirection moveDirection,
                                               List<List<Integer>> res, boolean firstPass) {
    while (true) {
      res.add(Arrays.asList(x, y));
      // class invariant: only the current player can alter the board (make a move)
      // the class invariant is enforced here because we are getting a color
      // purely based on which player turn it currently is.
      DiscColor playerTurnColor = rorm.getPlayerColor(rorm.currentTurn());
      List<Integer> nextPos = MoveRules.applyShiftBasedOnDirection(x, y, moveDirection);
      int nextPosX = nextPos.get(0);
      int nextPosY = nextPos.get(1);
      x = nextPosX;
      y = nextPosY;
      if (!rorm.checkValidCoordinates(nextPosX, nextPosY)) {
        return new ArrayList<>();
      }
      if (firstPass) {
        DiscColor opponentTurnColor = rorm.getPlayerColor(rorm.getOpponent(rorm.currentTurn()));
        if (opponentTurnColor == rorm.getDiscAt(nextPosX, nextPosY).getColor()) {
          res.add(Arrays.asList(x, y));
          res.add(Arrays.asList(nextPosX, nextPosY));
        } else {
          return new ArrayList<>();
        }
        firstPass = false;
      } else {
        if (rorm.getDiscAt(nextPosX, nextPosY).getColor() == playerTurnColor) {
          return res;
        } else if (rorm.getDiscAt(nextPosX, nextPosY).getColor() == DiscColor.FACEDOWN) {
          return new ArrayList<>();
        } else {
          res.add(Arrays.asList(nextPosX, nextPosY));
        }
      }
    }
  }

}
