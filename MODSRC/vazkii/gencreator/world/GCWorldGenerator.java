/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.gencreator.DataStorage;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.helper.StructureData;
import cpw.mods.fml.common.IWorldGenerator;

/**
 * GCWorldGenerator
 *
 * The mod's world generation handler.
 *
 * @author Vazkii
 */
public class GCWorldGenerator implements IWorldGenerator {

	/// XXX - Test Seed: 7274662765201441577

	private static final PlacementMethod REGULAR = new PlacementMethod(),
			AIR = new PlacementMethodAir(),
			STONE_BRICK = new PlacementMethodStoneBricks(),
			CHEST = new PlacementMethodChest();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if(!world.getWorldInfo().isMapFeaturesEnabled())
			return;

		int posX = chunkX * 16 + random.nextInt(16);
		int posZ = chunkZ * 16 + random.nextInt(16);
		int posY = world.getTopSolidOrLiquidBlock(posX, posZ);
		if(posY == -1)
			return;

		for(StructureData data : DataStorage.structures) {
			if(data.canGenerate(world, random, posX, posY, posZ)) {
				generateStructure(data, random, world, posX, posY, posZ);
				return;
			}
		}
	}

	public void generateStructure(StructureData data, Random rand, World world, int posX, int posY, int posZ) {
		for(int x = 0; x < data.blockData.length; x++) {
			for(int y = 0; y < data.blockData[0].length; y++) {
				for(int z = 0; z < data.blockData[0][0].length; z++) {
					int placePosX = posX + x;
					int placePosY = posY + y - 0;
					int placePosZ = posZ + z;
					FullBlockData blockData = data.blockData[x][y][z];
					placeBlock(world, rand, placePosX, placePosY, placePosZ, blockData, data);
				}
			}
		}
	}

	public void placeBlock(World world, Random rand, int x, int y, int z, FullBlockData blockData, StructureData structure) {
		Block block = blockData.block;

		PlacementMethod method = REGULAR;
		if(block == Blocks.air)
			method = AIR;

		if(block == Blocks.stonebrick)
			method = STONE_BRICK;

		if(block == Blocks.chest || block == Blocks.trapped_chest)
			method = CHEST;

		method.place(world, rand, x, y, z, blockData, structure);
	}

}
