import java.awt.Color;
import java.awt.Graphics2D;

public interface Item {
  String getName();
  String getDescription();
  Color getColor();
  public void paint(Graphics2D g, int x, int y, int size);
  boolean isStackable();
  int getMaxStackSize();
  public void use(Player player);
}
