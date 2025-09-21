interface Placeable {
  boolean canPlaceOnTerrain(double terrainHeight);
  Block createBlock();
}
