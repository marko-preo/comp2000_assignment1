import java.awt.Color;
import java.awt.Graphics2D;

public class WoodenLogBlock implements Block {
    private static final String NAME = "Wooden Log";
    private static final int MAX_STACK_SIZE = 64;
    private static final boolean IS_STACKABLE = true;
    private static final String DESCRIPTION = "A log of wood";
    private static final Color COLOR = new Color(139, 69, 19);

    @Override public String getName() { return NAME; }
    @Override public Color getColor() { return COLOR; }
    @Override public boolean isWalkable() { return false; }
    @Override public boolean canPlaceOnTerrain(double terrainHeight) { return terrainHeight > 0.45; }
    @Override public String getDescription() { return DESCRIPTION; }
    @Override public int getMaxStackSize() { return MAX_STACK_SIZE; }
    @Override public boolean isStackable() { return IS_STACKABLE; }
    @Override public void use(Player player) {}

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        g.setColor(getColor());
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }
}

