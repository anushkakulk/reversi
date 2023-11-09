package view;

/**
 * Represents a handler for when a tile has been selected in the View.
 */
public interface ViewTileClickedHandler {
  /**
   * handles (will be implemented in the controller) when the tile at the given coordinates is
   * clicked.
   * @param xCoord - the clicked tile's x coord
   * @param rCoord - the clicked tile's r coord
   * @param sCoord - the clicked tile's s coord
   */
  void handleTileClicked(int xCoord, int rCoord, int sCoord);

}