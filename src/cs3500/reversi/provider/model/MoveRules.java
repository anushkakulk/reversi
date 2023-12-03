package cs3500.reversi.provider.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MoveRules is a utility class that offers a function that shifts a coordinate
 * based on a move direction.
 */
public class MoveRules {

  /**
   * applies a shift on a coordinate based on a rows
   * modulo and a move direction.
   */
  public static List<Integer> applyShiftBasedOnDirection(int x, int y,
                                                         MoveDirection moveDirection) {
    ArrayList<Integer> res = new ArrayList<>();
    if (moveDirection == MoveDirection.LEFT) {
      res = applyShift(x, y, -1, 0);
    } else if (moveDirection == MoveDirection.RIGHT) {
      res = applyShift(x, y, 1, 0);
    } else if (moveDirection == MoveDirection.UPRIGHT) {
      res = (y % 2 == 0) ? applyShift(x, y, 0, -1) :
              applyShift(x, y, 1, -1);
    } else if (moveDirection == MoveDirection.DOWNRIGHT) {
      res = (y % 2 == 0) ? applyShift(x, y, 0, 1) :
              applyShift(x, y, 1, 1);
    } else if (moveDirection == MoveDirection.UPLEFT) {
      res = (y % 2 == 0) ? applyShift(x, y, -1, -1) :
              applyShift(x, y, 0, -1);
    } else if (moveDirection == MoveDirection.DOWNLEFT) {
      res = (y % 2 == 0) ? applyShift(x, y, -1, 1)
              : applyShift(x, y, 0, 1);
    }
    return res;
  }

  // a helper method that returns an arraylist representing a coordinate
  // pair post shift.
  private static ArrayList<Integer> applyShift(int x, int y, int xShift, int yShift) {
    return new ArrayList<>(Arrays.asList(x + xShift, y + yShift));
  }
}
