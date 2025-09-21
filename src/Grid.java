import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.awt.Color;

public class Grid<T extends Cell> {
  static Cell[][] cells = new Cell[20][20];
  double[][] noiseMap;
  NoiseGenerator noiseGen;
  long seed;
  public static final int SIZE = 20;
  int worldX, worldY;
  public List<WorldItem<?>> droppedItems = new ArrayList<>();

  public Grid(int worldX, int worldY, NoiseGenerator generator, long seed) {
    this.noiseGen = generator;
    this.seed = seed;
    noiseMap = new double[cells.length][cells[0].length];
    this.worldX = worldX;
    this.worldY = worldY;
    generateNoise();
  }

  public void generateNoise() {
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {

        double nx = i / (double) cells.length;
        double ny = j / (double) cells[i].length;

        cells[i][j] = new Cell(colToLabel(i), j, Cell.size * i, Cell.size * j, noiseGen.get(nx * 10, ny * 10));
        noiseMap[i][j] = noiseGen.get(nx * 10, ny * 10);
      }
    }
  }

  private char colToLabel(int col) {
    return (char) (col + Character.valueOf('A'));
  }

  private int labelToCol(char col) {
    return (int) (col - Character.valueOf('A'));
  }

  public void spawnItem(WorldItem<?> item) {
    droppedItems.add(item);
  }

  public void updateItems(double deltaTime, Player player) {
    Iterator<WorldItem<?>> it = droppedItems.iterator();
    while (it.hasNext()) {
      WorldItem<?> wi = it.next();
      wi.update(deltaTime);

      if (wi.canBePickedUp() && wi.intersects(player)) {
        player.getHotbar().addItem(wi.getItem(), wi.getCount());
        it.remove();
      }
    }
  }

  public void renderItems(Graphics2D g) {
    for (WorldItem<?> wi : droppedItems) {
      wi.paint(g);
    }
  }

  public void paint(Graphics g, Point mousePos, Point playerPos) {
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {

        cells[i][j].paint(g, mousePos, playerPos);

      }
    }
  }

  public Optional<Cell> cellAtColRow(int c, int r) {
    if (c >= 0 && c < cells.length && r >= 0 && r < cells[c].length) {
      return Optional.of(cells[c][r]);
    } else {
      return Optional.empty();
    }
  }

  public Optional<Cell> cellAtColRow(char c, int r) {
    return cellAtColRow(labelToCol(c), r);
  }

  public Optional<Cell> cellAtPoint(Point p) {
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        if (cells[i][j].contains(p)) {
          return Optional.of(cells[i][j]);
        }
      }
    }
    return Optional.empty();
  }
}

