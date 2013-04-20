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
 * PlacementMethodAir
 *
 * The placement method for air, it checks if the structure
 * is ignoring air blocks.
 *
 * @author Vazkii
 */
public class PlacementMethodAir extends PlacementMethod {

	@Override
	public void place(World world, Random rand, int x, int y, int z, FullBlockData blockData, StructureData structure) {
		if(!structure.ignoreAirSpaces)
			world.setBlockToAir(x, y, z);
	}

}
