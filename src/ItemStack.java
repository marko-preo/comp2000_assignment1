public class ItemStack<T extends Item> {
  private final T item;
  private int count;

  public ItemStack(T item, int count) {
    this.item = item;
    if (item.isStackable()) {
      this.count = Math.min(count, item.getMaxStackSize());
    } else {
      this.count = 1;
    }
  }

  public T getItem() {
    return item;
  }

  public int getCount() {
    return count;
  }

  public boolean add(int amount) {
    if (!item.isStackable())
      return false;
    if (count + amount <= item.getMaxStackSize()) {
      count += amount;
      return true;
    }
    return false;
  }

  public boolean remove(int amount) {
    if (count >= amount) {
      count -= amount;
      return true;
    }
    return false;
  }

  public boolean isEmpty() {
    return count <= 0;
  }
}