import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.Timer;

public class Main extends JFrame {
  public static void main(String[] args) throws Exception {
    new Main();
  }

  class Canvas extends JPanel implements ActionListener, KeyListener, MouseListener {
    Random rng = new Random();
    long seed = rng.nextLong();
    private long lastUpdateTime;
    private Set<Integer> pressedKeys;

    // Hotbar<Item> hotbar = new Hotbar<>();

    private Timer gameTimer;
    Grid grid = new Grid(0, 0, new PerlinNoise(seed), seed);
    Player p = new Player(50, 50);

    public Canvas() {
      p.getHotbar().setSlot(0, new ItemStack<>(new PontoonBlock(), 16));
      p.getHotbar().setSlot(1, new ItemStack<>(new StoneBlock(), 16));
      p.getHotbar().setSlot(2, new ItemStack<>(new WoodenLogBlock(), 16));
      p.getHotbar().setSlot(3, new ItemStack<>(new GlassBlock(), 16));
      p.getHotbar().setSlot(4, new ItemStack<>(new BrickBlock(), 16));
      p.getHotbar().setSlot(5, new ItemStack<>(new SteelBlock(), 16));
      p.getHotbar().setSlot(6, new ItemStack<>(new SpeedPotion(), 3));
      pressedKeys = new HashSet<>();
      gameTimer = new Timer(16, this);
      lastUpdateTime = System.nanoTime();
      setFocusable(true);
      addKeyListener(this);
      addMouseListener(this);
      setPreferredSize(new Dimension(1024, 720));
      gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      long currentTime = System.nanoTime();
      double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
      lastUpdateTime = currentTime;

      handleInput(deltaTime);

      p.updatePos(deltaTime, getMousePosition());
      grid.updateItems(deltaTime, p);

      repaint();
    }

    private void handleInput(double deltaTime) {
      double dirX = 0, dirY = 0;

      if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) {
        dirX = -1;
      }
      if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) {
        dirX = 1;
      }
      if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_W)) {
        dirY = -1;
      }
      if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) {
        dirY = 1;
      }

      if (dirX != 0 && dirY != 0) {
        dirX *= 0.707;
        dirY *= 0.707;
      }

      p.move(dirX, dirY, deltaTime);

      if (pressedKeys.contains(KeyEvent.VK_Q)) {
        // System.out.println("throwing item");
        Point mouse = getMousePosition();
        if (mouse != null) {
          p.throwSelectedItem(grid, mouse);
        }
        pressedKeys.remove(KeyEvent.VK_Q);
      }

      if (pressedKeys.contains(KeyEvent.VK_E)) {
        p.useSelectedItem();
      }

    }

    @Override
    public void keyPressed(KeyEvent e) {
      pressedKeys.add(e.getKeyCode());

      if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_9) {
        int slotIndex = e.getKeyCode() - KeyEvent.VK_1;
        // hotbar.select(slotIndex);
        p.getHotbar().select(slotIndex);
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

      int mouseX = e.getX();
      int mouseY = e.getY();

      if (e.getButton() == MouseEvent.BUTTON3) {
        p.placeBlock(grid, mouseX, mouseY);
      } else if (e.getButton() == MouseEvent.BUTTON1) {
        p.breakBlock(getMousePosition(), p, grid);
      }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      grid.paint(g, getMousePosition(), new Point(p.worldX, p.worldY));
      grid.renderItems(g2d);
      p.paint(g2d);
      drawHotbar(g2d);
    }

    private void drawHotbar(Graphics2D g) {
      int slotSize = 40;
      int padding = 5;
      int xStart = 700 + 20;
      int yStart = 50;

      for (int i = 0; i < 9; i++) {
        int x = xStart;
        int y = yStart + i * (slotSize + padding);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, slotSize, slotSize);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, slotSize, slotSize);

        ItemStack<Item> stack = p.getHotbar().getSlot(i);
        if (stack != null && !stack.isEmpty()) {
          int itemSize = slotSize - 8;
          stack.getItem().paint(g, x + 4, y + 4, itemSize);

          g.setColor(Color.BLACK);
          g.drawString(stack.getItem().getName() + " (x" + stack.getCount() + ")", x + slotSize + 8,
              y + slotSize / 2 + 5);
        }

        if (i == p.getHotbar().getSelectedIndex()) {
          g.setColor(Color.YELLOW);
          g.drawRect(x - 2, y - 2, slotSize + 4, slotSize + 4);
        }
      }
    }

  }

  private Main() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Canvas canvas = new Canvas();
    this.setContentPane(canvas);
    this.pack();
    this.setVisible(true);
  }

}

