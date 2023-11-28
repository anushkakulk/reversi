package cs3500.reversi.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A Function Object that indicates moving a piece to a certain position, a type of move that a
 * player can make.
 */
public class ReversiPosn {
  // made these fields public final, so that the controller could just do ReversiPosn.q, .r, and .s
  // to easily access their fields, rather than using getters. Since its final, the field are never
  // mutated.
  public final int q;
  public final int r;
  public final int s;

  /**
   * creates a ReversiPosn Object, where the given coordinates are the coordinates of the
   * destination tile that the player wants to move to.
   *
   * @param q - the q coord of dest tile
   * @param r - the r coord of dest tile
   * @param s - the s coord of dest tile
   */
  public ReversiPosn(int q, int r, int s) {
    this.q = q;
    this.r = r;
    this.s = s;
  }


  /**
   * Returns the upper leftmost ReversiPosn from a list of ReversiPosns.
   *
   * @param positions a list of ReversiPosns from which we are trying to find the upper left most
   * @return the upper leftmost ReversiPosn from a list of ReversiPosns
   */
  private static ReversiPosn findUppermostLeftmostPosition(List<ReversiPosn> positions) {
    ReversiPosn uppermostLeftmost = positions.get(0);

    for (ReversiPosn posn : positions) {
      if ((posn.q <= uppermostLeftmost.q
              && posn.r < uppermostLeftmost.r)) {
        uppermostLeftmost = posn;
      }
    }
    return uppermostLeftmost;
  }

  /**
   * Given a map of possible moves and the amt of tiles that move flips, returns the best option
   * (if it exists).
   *
   * @param possibleMoves a map of valid moves to the number of tiles that move flips over.
   * @return a ReversiPosn that is the best destination to move to out of the given possible moves,
   *                    ONLY IF the strategy found at least one valid move.
   */
  public static Optional<IPlayerMove> findBestMove(Map<ReversiPosn, Integer> possibleMoves) {
    if (!possibleMoves.isEmpty()) {
      int maxTilesCaptured = Collections.max(possibleMoves.values());
      List<ReversiPosn> bestMoves = new ArrayList<>();

      for (Map.Entry<ReversiPosn, Integer> entry : possibleMoves.entrySet()) {
        if (entry.getValue() == maxTilesCaptured) {
          bestMoves.add(entry.getKey());
        }
      }

      // gets the uppermost-leftmost position from the best moves
      return Optional.of(new Move(findUppermostLeftmostPosition(bestMoves)));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r, this.s);
  }


  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ReversiPosn)) {
      return false;
    }
    ReversiPosn posn = (ReversiPosn) obj;
    return this.q == posn.q
            && this.r == posn.r
            && this.s == posn.s;
  }
}
