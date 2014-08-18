/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.helper.StructureData;
import vazkii.gencreator.world.staticspawner.StaticSpawner;

/**
 * PlacementMethodChest
 *
 * The placement method for chests. This allows chests
 * to have randomized loot or act as mob spawners.
 *
 * @author Vazkii
 */
public class PlacementMethodChest extends PlacementMethod {

	@Override
	public void place(World world, Random rand, int x, int y, int z, FullBlockData blockData, StructureData structure) {
		world.setBlock(x, y, z, blockData.block, blockData.meta, 2);
		TileEntity tile = constructTileEntity(world, x, y, z, blockData, structure);
		if(tile instanceof TileEntityChest) {
			boolean spawnerChest = isSpawner((TileEntityChest) tile);
			if(spawnerChest) {
				handleSpawner(world, rand, (TileEntityChest) tile, blockData, structure);
				world.setBlockToAir(x, y, z);
				return;
			}
			world.setTileEntity(x, y, z, tile);
			if(shouldRandomize((TileEntityChest) tile))
				randomize(rand, (TileEntityChest) tile);
		}
	}

	public boolean isSpawner(TileEntityChest tile) {
		ItemStack stack = tile.getStackInSlot(0);
		return stack != null && stack.getItem() == Items.spawn_egg;
	}

	public void handleSpawner(World world, Random rand, TileEntityChest tile, FullBlockData blockData, StructureData structure) {
		StaticSpawner spawner = new StaticSpawner(tile, world);
		spawner.spawn();
	}

	public boolean shouldRandomize(TileEntityChest tile) {
		ItemStack stack = tile.getStackInSlot(0);
		return stack != null && stack.getItem() == Items.paper && stack.getDisplayName().equals("[RANDOM]");
	}

	public void randomize(Random rand, TileEntityChest tile) {
		tile.setInventorySlotContents(0, null); // Remove the paper

		IInventory holdingInv = new InventoryBasic("", false, tile.getSizeInventory());
		for(int i = 0; i < tile.getSizeInventory(); i++)
			holdingInv.setInventorySlotContents(i, tile.getStackInSlot(i) == null ? null : tile.getStackInSlot(i).copy());

		ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
        WeightedRandomChestContent.generateChestContents(rand, info.getItems(rand), tile, info.getCount(rand));

        for(int i = 0; i < holdingInv.getSizeInventory(); i++) {
        	ItemStack stack = holdingInv.getStackInSlot(i);
        	if(stack != null)
        		tile.setInventorySlotContents(i, stack);
        }
	}
}
