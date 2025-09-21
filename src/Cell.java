import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Cell extends Rectangle {
  static int size = 35;
  char col;
  int row;
  double height;
  Block block;

  public Cell(char inCol, int inRow, int x, int y, double height) {
    super(x, y, size, size);
    col = inCol;
    row = inRow;
    this.height = height;
    this.block = null;
  }

  public void paint(Graphics g, Point mousePos, Point playerPos) {

    // System.out.println("player pos from cell: " + playerPos);

    Color terrainColor;
    if (height < 0.45) {
      terrainColor = Color.BLUE;
    } else if (height < 0.5) {
      terrainColor = new Color(194, 178, 128);
    } else if (height < 0.75) {
      terrainColor = Color.GREEN;
    } else {
      terrainColor = Color.GRAY;
    }

    g.setColor(terrainColor);
    g.fillRect(x, y, size, size);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, size, size);

    if (block != null) {
      block.paint((Graphics2D) g, x, y, size);
    }

    if (withinRadius(playerPos.x, playerPos.y) && contains(mousePos)) {
      g.setColor(Color.GRAY);
      g.fillRect(x, y, size, size);
    }
  }

  public boolean canPlaceBlock(Block blockToPlace) {
    if (block != null) {
      return false;
    }

    return blockToPlace.canPlaceOnTerrain(height);
  }

  public boolean placeBlock(Block blockToPlace) {
    if (canPlaceBlock(blockToPlace)) {
      this.block = blockToPlace;
      return true;
    }
    return false;
  }

  public boolean isWalkable() {
    if (block != null) {
      return block.isWalkable();
    }
    return height >= 0.45;
  }

  public boolean contains(Point p) {
    if (p != null) {
      return super.contains(p);
    } else {
      return false;
    }
  }

  public boolean withinRadius(int cellX, int cellY) {
    if ((col - Character.valueOf('A') == cellX && row == cellY))
      return false;
    if ((col - Character.valueOf('A') <= (cellX + 2) && col - Character.valueOf('A') >= (cellX - 2))
        && (row <= (cellY + 2) && row >= (cellY - 2)))
      return true;
    return false;
  }

}

