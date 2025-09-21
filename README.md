# How to Run
- Extract the zip folder after downloading it from iLearn
- Locate the parent folder that contains the 'src' folder and README file and open it in VScode
- In VScode locate the 'Main.java' file and run it
- A graphics window should appear and the game can be played

# How to Play
### Inputs
- Use the W, A, S and D keys or the arroy keys to move
- Use the mouse to change the direction the player is facing and to highlight a tile to place and remove blocks
- Use the number keys 1-9 to change which slot in the players hotbar is selected
- Use the Q key to throw the currently selected hotbar item onto the ground
- Use the E key to use consumable items like potions
- Right click to place blocks
- Left click to break blocks

### Game Info
- There is a 1 second cooldown time between throwing an item and picking it up again
- Blocks can only be placed and removed in a 2 tile radius centred around the tile the player is on
- Breaking a block will spawn its item form on the ground which can be picked up
- Game will generate a random map with terrain every time it is run, terrain is made up of grass (green tiles), water (blue tiles), sand (tan tiles) and rock (gray tiles)
- Players cannot walk into water tiles unless they use pontoon blocks which can only be placed on top of water
- Potions have a cooldown and the player cannot drink another potion while one is already active

# Code Explanation

### Inheritance
Inheritance was used to create a hierarchy of items and blocks that share common functionality while allowing specific behavior to be added when necessary. At the top of the hierarchy, the Item class defines shared attributes and methods for all in-game items, such as a name and a rendering method. The Block class extends Item and represents placeable objects in the world grid. Block types such as StoneBlock, WoodPlankBlock, GlassBlock, and SteelBlock further extend Block.

### Interfaces
Interfaces were used to define common contracts for item behavior that could be implemented in different ways. The Usable interface is central to this design. It defines a use(Player player, Grid grid) method that all usable items must implement. This allows the player to interact with different item types without needing to know the item’s internal details. For example the SpeedPotion class implements Usable to temporarily boost the player’s movement speed while Block implements it to place blocks into the world. The use of interfaces makes the code modular and expandable as it can support a wide variety of usable items that could be implemented in the future (more potions, food, medkits, weapons, tools, etc) while keeping the player interaction code consistent. This makes the design highly versatile.

### Generics
Generics were used to ensure type safety and flexibility in key data structures such as the hotbar and dropped world items. The Hotbar<T extends Item> class uses generics to guarantee that it only stores Item objects (or subclasses of Item). This prevents invalid types from being inserted into the hotbar, while still allowing different categories of items (blocks, potions, weapons, tools, etc.) to coexist. Similarly, the WorldItem<T extends Item> class represents items dropped into the world. Generics ensure that a WorldItem<Block> holds only blocks, and a WorldItem<SpeedPotion> holds only potions. This avoids runtime errors and eliminates the need for unsafe casting.