import java.awt.Color;
import java.awt.Graphics2D;

public class GlassBlock implements Block {
    private static final String NAME = "Glass";
    private static final int MAX_STACK_SIZE = 64;
    private static final boolean IS_STACKABLE = true;
    private static final String DESCRIPTION = "Transparent glass block";
    private static final Color COLOR = new Color(173, 216, 230, 150); // semi-transparent

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
        g.setColor(Color.CYAN.darker());
        g.drawRect(x, y, size, size);
    }
}
