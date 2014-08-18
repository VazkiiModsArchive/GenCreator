/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.helper;

import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * StructureData
 *
 * A class that contains structure data. This class
 * contains valid biomes, checkboxes, rarity,
 * and a 3D array with full block data.
 *
 * @author Vazkii
 */
public class StructureData {

	public int rarity;
	public List<BiomeGenBase> validBiomes;
	public boolean randomizeStoneBricks, ignoreAirSpaces;

	public FullBlockData[][][] blockData;

	public boolean canGenerate(World world, Random rand, int x, int y, int z) {
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		return rand.nextInt(rarity) == 0 && validBiomes.contains(biome) && world.getBlock(x, y - 1, z) == biome.topBlock;
	}

}
