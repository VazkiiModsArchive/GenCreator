/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.gencreator.client.saving.InventorySyncHandler;

/**
 * FullBlockData
 *
 * A class that holds a block ID, metadata, and a tile entity, if present.
 *
 * @author Vazkii
 */
public class FullBlockData {

	public int id, meta;
	public TileEntity tile;

	public NBTTagCompound cmp;

	public FullBlockData(World world, int x, int y, int z) {
		id = world.getBlockId(x, y, z);
		meta = world.getBlockMetadata(x, y, z);
		tile = world.getBlockTileEntity(x, y, z);
	}

	public FullBlockData(int id, int meta, TileEntity tile) {
		this.id = id;
		this.meta = meta;
		this.tile = tile;
	}

	public FullBlockData(int id, int meta, NBTTagCompound tile) {
		this.id = id;
		this.meta = meta;
		cmp = tile;
	}

	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setInteger("id", id);
		cmp.setInteger("meta", meta);
		if(tile != null) {
			NBTTagCompound tileCmp = new NBTTagCompound();
			if(tile instanceof IInventory) {
				ChunkCoordinates tileCoords = new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
				if(InventorySyncHandler.syncedInventories.containsKey(tileCoords)) {
					IInventory syncedInv = InventorySyncHandler.syncedInventories.get(tileCoords);
					IInventory tileInv = (IInventory) tile;
					for(int i = 0; i < tileInv.getSizeInventory(); i++) {
						ItemStack stack = syncedInv.getStackInSlot(i);
						tileInv.setInventorySlotContents(i, stack == null ? null : stack.copy());
					}
				}

			}

			tile.writeToNBT(tileCmp);
			cmp.setCompoundTag("tile", tileCmp);
		}
	}

	public static FullBlockData constructFromNBT(NBTTagCompound cmp) {
		int id = cmp.getInteger("id");
		int meta = cmp.getInteger("meta");
		NBTTagCompound tile = cmp.hasKey("tile") ? cmp.getCompoundTag("tile") : null;
		return new FullBlockData(id, meta, tile);
	}
}
