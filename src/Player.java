import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Player {
  private Point.Double position;
  private Point.Double velocity;
  public static Point mousePos;

  private double speed = 100;
  private double baseSpeed = 100;
  private double size = 35;
  private double radius = 10;
  double playerAngle;
  private double speedBoostTimer = 0;
  private boolean boostActive = false;

  int worldX, worldY;

  private Hotbar<Item> hotbar;

  public Player(double startX, double startY) {
    this.position = new Point.Double(startX, startY);
    this.velocity = new Point.Double(0, 0);
    playerAngle = 90;
    hotbar = new Hotbar<>();
  }

  public void updatePos(double deltaTime, Point mousePos) {
    if (mousePos != null) {
      this.mousePos = mousePos;
      playerAngle = Math.toDegrees(Math.atan2(position.y - mousePos.y, position.x - mousePos.x));
      if (playerAngle < 0)
        playerAngle += 360;

      if (speedBoostTimer > 0) {
        speedBoostTimer -= deltaTime;
        if (speedBoostTimer <= 0) {
          speed = baseSpeed; // Reset when timer expires
        }
      }

      if (speed != baseSpeed) {
        // System.out.println("currently boosted");
        boostActive = true;
      } else {
        boostActive = false;
      }

      // System.out.println("Angle: "+playerAngle);
    }

    worldX = Math.max(0, Math.min((int) Math.floor(position.x / 35), Grid.SIZE - 1));
    worldY = Math.max(0, Math.min((int) Math.floor(position.y / 35), Grid.SIZE - 1));

    // System.out.println("Cell coords: " + worldX + ", " + worldY + " | Actual
    // coords: " + position.x + ", " + position.y);
    position.x += velocity.x * deltaTime;
    position.y += velocity.y * deltaTime;
  }

  public static <T extends Number> int toCellCoords(T x) {
    return Math.max(0, Math.min((int) Math.floor(x.doubleValue() / 35), Grid.SIZE - 1));
  }

  public void move(double dirX, double dirY, double deltaTime) {

    double len = Math.sqrt(dirX * dirX + dirY * dirY);
    if (len > 0) {
      dirX /= len;
      dirY /= len;
    }

    double newX = position.x + dirX * speed * deltaTime;
    double newY = position.y + dirY * speed * deltaTime;

    double nextX = newX;
    double nextY = newY;

    int minCellX = (int) Math.floor((newX - radius) / Cell.size);
    int maxCellX = (int) Math.floor((newX + radius) / Cell.size);
    int minCellY = (int) Math.floor((newY - radius) / Cell.size);
    int maxCellY = (int) Math.floor((newY + radius) / Cell.size);

    for (int cx = minCellX; cx <= maxCellX; cx++) {
      for (int cy = minCellY; cy <= maxCellY; cy++) {

        if (cx < 0 || cx >= Grid.cells.length ||
            cy < 0 || cy >= Grid.cells[0].length) {
          continue;
        }

        Cell cell = Grid.cells[cx][cy];
        if (cell == null || cell.isWalkable())
          continue;

        double tileLeft = cx * Cell.size;
        double tileRight = tileLeft + Cell.size;
        double tileTop = cy * Cell.size;
        double tileBottom = tileTop + Cell.size;

        double closestX = Math.max(tileLeft, Math.min(nextX, tileRight));
        double closestY = Math.max(tileTop, Math.min(nextY, tileBottom));

        double dxToCell = nextX - closestX;
        double dyToCell = nextY - closestY;
        double distSq = dxToCell * dxToCell + dyToCell * dyToCell;

        if (distSq < radius * radius) {
          double dist = Math.sqrt(distSq);
          if (dist == 0)
            dist = 0.001;

          double overlap = radius - dist;
          nextX += (dxToCell / dist) * overlap;
          nextY += (dyToCell / dist) * overlap;
        }
      }
    }

    double maxX = Grid.cells.length * Cell.size - radius;
    double maxY = Grid.cells[0].length * Cell.size - radius;

    position.x = Math.max(radius, Math.min(nextX, maxX));
    position.y = Math.max(radius, Math.min(nextY, maxY));
    // position.x = nextX;
    // position.y = nextY;
  }

  public void paint(Graphics2D g) {
    AffineTransform originalTransform = g.getTransform();
    Color originalColor = g.getColor();
    Path2D.Double playerShape = new Path2D.Double();

    double triangleHeight = size * 0.8;
    double triangleWidth = size * 0.6;

    playerShape.moveTo(0, -triangleHeight / 2.0);
    playerShape.lineTo(triangleWidth / 2.0, triangleHeight / 2.0);

    playerShape.lineTo(-triangleWidth / 2.0, triangleHeight / 2.0);
    playerShape.closePath();

    g.translate(position.x, position.y);
    g.rotate(Math.toRadians(playerAngle - 90));

    GradientPaint gradient = new GradientPaint(
        0, -(float) (triangleHeight / 2.0), new Color(0, 150, 255),
        0, (float) (triangleHeight / 2.0), new Color(0, 80, 180));
    g.setPaint(gradient);
    g.fill(playerShape);

    g.setColor(Color.WHITE);
    g.setStroke(new BasicStroke(2.0f));
    g.draw(playerShape);

    g.setColor(Color.WHITE);
    g.fillOval(-3, (int) (-triangleHeight / 2.0) - 3, 6, 6);

    g.setColor(Color.RED);
    g.fillOval(-2, -2, 4, 4);

    g.setTransform(originalTransform);
    g.setColor(originalColor);
  }

  public double getRadius() {
    return radius;
  }

  public double getX() {
    return position.x;
  }

  public double getY() {
    return position.y;
  }

  public Hotbar<Item> getHotbar() {
    return hotbar;
  }

  public void applySpeedBoost(double multiplier, double duration) {
    this.speed = baseSpeed * multiplier;
    this.speedBoostTimer = duration;
  }

  void placeBlock(Grid grid, int mouseX, int mouseY) {
    int cellX = toCellCoords(mouseX);
    int cellY = toCellCoords(mouseY);

    if (cellX < 0 || cellX >= Grid.SIZE || cellY < 0 || cellY >= Grid.SIZE) {
      // System.out.println("Cannot place block: out of bounds");
      return;
    }

    Cell targetCell = grid.cells[cellX][cellY];

    ItemStack<Item> selected = hotbar.getSelected();
    if (selected == null || selected.getCount() <= 0) {
      // System.out.println("No item selected or stack empty");
      return;
    }

    Item held = selected.getItem();
    if (!(held instanceof Block block)) {
      // System.out.println("Held item is not a block");
      return;
    }

    if (!targetCell.withinRadius(worldX, worldY)) {
      // System.out.println("Cannot place block: too far from player");
      return;
    }

    // System.out.println("Placing block at: " + cellX + ", " + cellX);

    // Block pontoon = new PontoonBlock();

    if (targetCell.canPlaceBlock(block) && targetCell.block == null) {
      System.out.println("Placing: " + block.getName());
      grid.cells[cellX][cellY].placeBlock(block);

      selected.remove(1);

      if (selected.getCount() <= 0) {
        hotbar.setSlot(hotbar.getSelectedIndex(), null);
      }
    }
  }

  public void breakBlock(Point mouse, Player player, Grid grid) {
    Cell cell = grid.cells[toCellCoords(mouse.x)][toCellCoords(mouse.y)];
    if (cell.block != null) {
      double x = cell.x + Cell.size / 2.0;
      double y = cell.y + Cell.size / 2.0;
      grid.spawnItem(new WorldItem<>(cell.block, 1, x, y));
      cell.block = null;
    }
  }

  public void throwSelectedItem(Grid grid, Point mouse) {
    // System.out.println("throwing item");
    ItemStack<Item> selected = hotbar.getSelected();
    if (selected == null || selected.getCount() <= 0)
      return;

    double offsetX = 5 * Math.signum(mouse.x - position.x);
    double offsetY = 5 * Math.signum(mouse.y - position.y);

    grid.spawnItem(new WorldItem<>(selected.getItem(), 1, position.x + offsetX, position.y + offsetY));

    selected.remove(1);
    if (selected.isEmpty())
      hotbar.setSlot(hotbar.getSelectedIndex(), null);
  }

  public void useSelectedItem() {
    ItemStack<Item> selected = hotbar.getSelected();
    if (selected == null || selected.isEmpty())
      return;

    Item item = selected.getItem();

    if (boostActive == false) {
      item.use(this);
      selected.remove(1);
    }

    if (selected.isEmpty()) {
      hotbar.setSlot(hotbar.getSelectedIndex(), null);
    }
  }

}

