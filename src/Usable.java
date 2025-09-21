interface Usable {
  boolean canUse(Player player);
  void use(Player player);
  int getUsageCount();
}
