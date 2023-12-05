package cs3500.reversi.provider.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import cs3500.reversi.adapter.MergedReversiModel;
import cs3500.reversi.provider.controller.Event;
import cs3500.reversi.provider.controller.EventType;
import cs3500.reversi.provider.controller.Listener;
import cs3500.reversi.provider.controller.PlayerEvent;
import cs3500.reversi.provider.discs.Disc;
import cs3500.reversi.provider.discs.DiscColor;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;

/**
 * The `ReversiGUI` class represents the view component in a Reversi game.
 * It is responsible for rendering and displaying the game state to the players.
 */
public class ReversiGUI extends JFrame implements ReversiView {
  private final ReadOnlyReversiModel model;
  private final JButton[][] boardButtons;
  private int prevX = -1;
  private int prevY = -1;

  private List<Listener> playerListeners = new ArrayList<>();

  /**
   * A ReversiGUI constructor.
   */
  public ReversiGUI(MergedReversiModel model) {
    this.model = model;

    getContentPane().setBackground(Color.DARK_GRAY);
    setTitle(model.getType() + " Reversi");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    int dimensions = model.getDimensions();
    boardButtons = new JButton[dimensions][dimensions];

    int side = 50;
    int hexWidth = 2 * side;
    int hexHeight = (int) (Math.sqrt(3) * side);
    int verticalOverlap = side / 2;
    for (int i = 0; i < dimensions; i++) {
      int xOffsetAdjustment = 0;
      if (i % 2 == 1) {
        int horizontalOffset = side / 4;
        xOffsetAdjustment = -horizontalOffset;
      }

      int yOffset = i * (hexHeight - verticalOverlap);
      for (int j = 0; j < dimensions; j++) {
        int xOffset = j * (hexWidth - (side / 2)) + (i % 2) * (hexWidth / 2) + xOffsetAdjustment;
        boardButtons[i][j] = new JButton();
        boardButtons[i][j].setOpaque(false);
        boardButtons[i][j].setContentAreaFilled(false);
        boardButtons[i][j].setBorderPainted(false);
        boardButtons[i][j].setFocusPainted(false);
        boardButtons[i][j].setBounds(xOffset, yOffset, hexWidth, hexHeight);
        int actualI = i;
        int actualJ = j;
        boardButtons[i][j].addMouseListener(new MouseListener() {


          @Override
          public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            System.out.println("Disc position at " + "(" + actualJ + ", " + actualI + ")");
            Icon icon = button.getIcon();

            if (!model.checkValidCoordinates(actualJ, actualI) && !(prevX == -1 && prevY == -1)) {
              discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
              prevX = -1;
              prevY = -1;
            }

            if (icon instanceof ImageIcon) {
              BufferedImage image = convertIconToBufferedImage((ImageIcon) icon);

              int x = image.getWidth() / 2;
              int y = image.getHeight() / 2;
              int rgb = image.getRGB(x, y);
              Color color = new Color(rgb, true);

              if (prevX == -1 && prevY == -1) {
                prevX = actualJ;
                prevY = actualI;
                button.setIcon(newHexagonIcon(Color.CYAN));
              } else {
                discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                button.setIcon(newHexagonIcon(Color.CYAN));
                prevX = actualJ;
                prevY = actualI;
              }

              if (color.equals(Color.CYAN)) {
                discSelectorHelper(button, actualJ, actualI);
                prevX = -1;
                prevY = -1;
              }
            }
          }

          @Override
          public void mouseClicked(MouseEvent e) {
            // needs to be overridden.
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            // needs to be overridden.
          }

          @Override
          public void mouseEntered(MouseEvent e) {
            // needs to be overridden.
          }

          @Override
          public void mouseExited(MouseEvent e) {
            // needs to be overridden.
          }

        });

        boardButtons[i][j].addKeyListener(new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            // needs to be overridden.
          }

          @Override
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              System.out.println("Spacebar pressed, player wishes to pass");
              // a data model representing an event
              notifyListeners(new PlayerEvent(EventType.PASS, "", model.currentTurn()));
              if (!(prevX == -1 && prevY == -1)) {
                discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                prevX = -1;
                prevY = -1;
              }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              if (!(prevX == -1 && prevY == -1)) {
                System.out.println("Enter key pressed, player wishes to make a move to the disc at"
                    + " (" + prevX + ", " + prevY + ")");

                discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                notifyListeners(new PlayerEvent(EventType.MOVE, prevX + " " +
                    prevY, model.currentTurn()));
                prevX = -1;
                prevY = -1;
              } else {
                System.out.println("No selected disc to move to");
              }
            }
          }

          @Override
          public void keyReleased(KeyEvent e) {
            // needs to be overridden.
          }
        });
        add(boardButtons[i][j]);
      }
    }

    int windowWidth = dimensions * hexWidth - (dimensions / 2 - 1) * (hexWidth / 2);
    int windowHeight = (dimensions - 1)
        * (hexHeight - verticalOverlap)
        + hexHeight + (hexHeight / 2);
    setSize(windowWidth, windowHeight);
    setVisible(true);
    render();
  }

  private void discSelectorHelper(JButton button, int x, int y) {
    DiscColor originalColor = model.getDiscAt(x, y).getColor();
    switch (originalColor) {
      case WHITE:
        button.setIcon(newHexagonIcon(Color.WHITE));
        break;
      case BLACK:
        button.setIcon(newHexagonIcon(Color.BLACK));
        break;
      case FACEDOWN:
        button.setIcon(newHexagonIcon(Color.GRAY));

        break;
      default:
    }
  }

  /**
   * The addListener method adds relevant listeners.
   */
  public void addListener(Listener l) {
    playerListeners.add(l);
  }

  /**
   * The notifyListeners method notifies relevant listeners.
   */
  public void notifyListeners(Event playerEvent) {
    for (Listener pl : playerListeners) {
      pl.update(playerEvent);
    }
  }

  /**
   * The Render Method is needed for displaying the view.
   */
  public void render() {
    Disc[][] board = model.getCurrentBoardState();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        JButton button = boardButtons[i][j];
        ImageIcon icon = null;
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.setVisible(true);
        if (board[i][j] != null) {
          if (board[i][j].getColor() == DiscColor.WHITE) {
            icon = newHexagonIcon(Color.WHITE);
          } else if (board[i][j].getColor() == DiscColor.BLACK) {
            icon = newHexagonIcon(Color.BLACK);
          } else if (board[i][j].getColor() == DiscColor.FACEDOWN) {
            icon = newHexagonIcon(Color.GRAY);
          }
          button.setIcon(icon);
        }
      }
    }
  }

  @Override
  public void showPopup(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  private BufferedImage convertIconToBufferedImage(ImageIcon icon) {
    Image image = icon.getImage();
    BufferedImage buffImage = new BufferedImage(image.getWidth(null),
        image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    Graphics2D buffGraphics = buffImage.createGraphics();
    buffGraphics.drawImage(image, 0, 0, null);
    buffGraphics.dispose();
    return buffImage;
  }

  private ImageIcon newHexagonIcon(Color color) {
    BufferedImage hexImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = hexImage.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int r = 40;
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    double startAngle = Math.PI / 6;
    for (int i = 0; i < 6; i++) {
      xPoints[i] = (int) (50 + r * Math.cos((i * 2 * Math.PI / 6) + startAngle));
      yPoints[i] = (int) (50 + r * Math.sin((i * 2 * Math.PI / 6) + startAngle));
    }

    g2.setComposite(AlphaComposite.Clear);
    g2.fillRect(0, 0, 100, 100);

    g2.setComposite(AlphaComposite.Src);
    g2.setColor(color);
    g2.fillPolygon(xPoints, yPoints, 6);

    g2.setColor(Color.BLACK);
    g2.drawPolygon(xPoints, yPoints, 6);

    return new ImageIcon(hexImage);
  }
}