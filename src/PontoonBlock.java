import java.awt.Color;
import java.awt.Graphics2D;

public class PontoonBlock implements Block {
  private static final String NAME = "Pontoon";
  private static final int MAX_STACK_SIZE = 16;
  private static final boolean IS_STACKABLE = true;
  private static final String DESCRIPTION = "Allows the player to create a floating platform on water tiles";
  private static final Color COLOR = new Color(139, 69, 19); // Brown

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Color getColor() {
    return COLOR;
  }

  @Override
  public boolean isWalkable() {
    return true;
  }

  @Override
  public boolean canPlaceOnTerrain(double terrainHeight) {
    return terrainHeight < 0.45;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public int getMaxStackSize() {
    return MAX_STACK_SIZE;
  }

  @Override
  public boolean isStackable() {
    return IS_STACKABLE;
  }

  @Override
  public void use(Player player) {}

  @Override
  public void paint(Graphics2D g, int x, int y, int size) {
    g.setColor(getColor());
    g.fillRect(x + 2, y + 2, size - 4, size - 4);
    g.setColor(Color.BLACK);
    g.drawRect(x + 2, y + 2, size - 4, size - 4);

    g.setColor(Color.DARK_GRAY);
    for (int i = 0; i < 3; i++) {
      int plankY = y + 5 + (i * 8);
      g.drawLine(x + 4, plankY, x + size - 4, plankY);
    }
  }
}

