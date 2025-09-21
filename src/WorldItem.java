import java.awt.Graphics2D;
import java.awt.Color;

public class WorldItem<T extends Item> {
  private final T item;
  private int count;
  private double x, y;
  private double bobOffset = 0;
  private boolean bobUp = true;
  private double pickupDelay = 1;

  public WorldItem(T item, int count, double x, double y) {
    this.item = item;
    this.count = count;
    this.x = x;
    this.y = y;
  }

  public T getItem() {
    return item;
  }

  public int getCount() {
    return count;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public void update(double deltaTime) {

    if (pickupDelay > 0) {
      pickupDelay -= deltaTime;
    } else {
      double speed = 20;
      if (bobUp) {
        bobOffset += speed * deltaTime;
        if (bobOffset >= 5)
          bobUp = false;
      } else {
        bobOffset -= speed * deltaTime;
        if (bobOffset <= -5)
          bobUp = true;
      }
    }
  }

  public boolean canBePickedUp() {
    return pickupDelay <= 0;
  }

  public void paint(Graphics2D g) {
    int size = Cell.size / 2;
    int drawX = (int) (x - size / 2);
    int drawY = (int) (y - size / 2 + bobOffset);

    item.paint(g, drawX, drawY, size);

    if (count > 1) {
      g.setColor(Color.WHITE);
      g.drawString("x" + count, drawX + size - 12, drawY + size - 4);
    }
  }

  public boolean intersects(Player p) {
    double dx = p.getX() - x;
    double dy = p.getY() - y;
    return dx * dx + dy * dy < p.getRadius() * p.getRadius();
  }

}

