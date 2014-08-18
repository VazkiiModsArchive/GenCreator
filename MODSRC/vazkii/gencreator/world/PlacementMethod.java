/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.helper.StructureData;

/**
 * PlacementMethod
 *
 * A class that holds a function that will be called when a specific block
 * is placed in the world. This allows for specific blocks to go trough
 * specific systems for generation.
 *
 * @author Vazkii
 */
public class PlacementMethod {

	public void place(World world, Random rand, int x, int y, int z, FullBlockData blockData, StructureData structure) {
		world.setBlock(x, y, z, blockData.block, blockData.meta, 2);
		TileEntity tile = constructTileEntity(world, x, y, z, blockData, structure);
		world.setTileEntity(x, y, z, tile);
	}

	TileEntity constructTileEntity(World world, int x, int y, int z, FullBlockData blockData, StructureData structure) {
		if(blockData.cmp == null)
			return null;

		NBTTagCompound tileCmp = (NBTTagCompound) blockData.cmp.copy();
		tileCmp.setInteger("x", x);
		tileCmp.setInteger("y", y);
		tileCmp.setInteger("z", z);

		TileEntity tile = TileEntity.createAndLoadEntity(tileCmp);
		tile.validate();
		return tile;
	}

}
