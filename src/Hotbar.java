public class Hotbar<T extends Item> {
  private final ItemStack<T>[] slots;
  private int selectedIndex;

  @SuppressWarnings("unchecked")
  public Hotbar() {
    this.slots = (ItemStack<T>[]) new ItemStack[9];
    this.selectedIndex = 0;
  }

  public void setSlot(int index, ItemStack<T> stack) {
    if (index >= 0 && index < slots.length) {
      slots[index] = stack;
    }
  }

  public ItemStack<T> getSlot(int index) {
    if (index >= 0 && index < slots.length) {
      return slots[index];
    }
    return null;
  }

  public ItemStack<T> getSelected() {
    return slots[selectedIndex];
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void select(int index) {
    if (index >= 0 && index < slots.length) {
      selectedIndex = index;
    }
  }

  public void scroll(int direction) {
    selectedIndex = (selectedIndex + direction + slots.length) % slots.length;
  }

  public boolean addItem(T item, int count) {
    for (int i = 0; i < slots.length; i++) {
      ItemStack<T> stack = slots[i];
      if (stack != null && stack.getItem().equals(item) && item.isStackable()) {
        int added = Math.min(count, item.getMaxStackSize() - stack.getCount());
        if (added > 0) {
          stack.add(added);
          count -= added;
          if (count <= 0)
            return true;
        }
      }
    }

    for (int i = 0; i < slots.length; i++) {
      if (slots[i] == null) {
        int toAdd = Math.min(count, item.getMaxStackSize());
        slots[i] = new ItemStack<>(item, toAdd);
        count -= toAdd;
        if (count <= 0)
          return true;
      }
    }

    return count <= 0;
  }

}

