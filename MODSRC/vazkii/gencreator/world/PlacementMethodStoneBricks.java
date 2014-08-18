/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world;

import java.util.Random;

import net.minecraft.world.World;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.helper.StructureData;

/**
 * PlacementMethodStoneBricks
 *
 * The placement method for Stone Bricks, it checks
 * if the structure is set to randomize stone bricks,
 * if so, sets a random metadata.
 *
 * @author Vazkii
 */
public class PlacementMethodStoneBricks extends PlacementMethod {

	@Override
	public void place(World world, Random rand, int x, int y, int z, FullBlockData blockData, StructureData structure) {
		if(!structure.randomizeStoneBricks)
			super.place(world, rand, x, y, z, blockData, structure);
		else world.setBlock(x, y, z, blockData.block, rand.nextInt(3), 1 | 2);
	}

}
