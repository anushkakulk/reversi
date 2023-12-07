package cs3500.reversi.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;

import cs3500.reversi.controller.IEmitPlayerActions;
import cs3500.reversi.model.ReadOnlyReversiModel;

/**
 * Represents an event that occurs in a Reversi game.
 */
public interface IPanel extends MouseListener, KeyListener, IEmitPlayerActions {

  int getHexSideLength();

  void update();

  List<IHexTile> getHexTiles();

  void handleHintOn(int num);

  JComponent getJComponent();

  void notifyTileClicked(int q, int r, int s);

  // creates a list of hextiles (the view's tiles), with one hextile corresponsing to every tile
  // in the model.
  void updateHextiles(ReadOnlyReversiModel model);


  ReadOnlyReversiModel getModelState();
}
