import java.awt.Color;
import java.awt.Graphics2D;

public class SpeedPotion implements Item {
    private static final String NAME = "Speed Potion";
    private static final int MAX_STACK_SIZE = 5;
    private static final boolean IS_STACKABLE = true;
    private static final String DESCRIPTION = "Temporarily increases movement speed";
    private static final Color COLOR = Color.MAGENTA;

    @Override public String getName() { return NAME; }
    @Override public String getDescription() { return DESCRIPTION; }
    @Override public Color getColor() { return COLOR; }
    @Override public boolean isStackable() { return IS_STACKABLE; }
    @Override public int getMaxStackSize() { return MAX_STACK_SIZE; }

    @Override
    public void use(Player player) {
        player.applySpeedBoost(2.0, 5.0); 
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        g.setColor(getColor());
        g.fillOval(x, y, size, size);
        g.setColor(Color.WHITE);
        g.drawOval(x, y, size, size);
        g.drawString("S", x + size/3, y + 2*size/3);
    }
}

