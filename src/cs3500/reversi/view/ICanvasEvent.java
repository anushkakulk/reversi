package cs3500.reversi.view;

/**
 * Represents any event that occurs on the canvas.
 */
public interface ICanvasEvent {
  /**
   * does something when the event is that the tile at given coordinates was clicked.
   * @param xCoord - the clicked tile's xCoord.
   * @param rCoord - the clicked tile's rCoord.
   * @param sCoord - the clicked tile's sCoord.
   */
  void tileClicked(int xCoord, int rCoord, int sCoord);

  void moved(int xCoord, int rCoord, int sCoord);

  void passed();


}