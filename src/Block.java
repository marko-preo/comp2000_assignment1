public interface Block extends Item {
  boolean isWalkable();
  boolean canPlaceOnTerrain(double terrainHeight);
}
