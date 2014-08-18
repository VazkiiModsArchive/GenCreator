/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;
import vazkii.gencreator.lib.ObfuscationKeys;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * StructureReader
 *
 * The main class for a structure reader. This class
 * handles reading a file's NBT structure and generate
 * the structure from it.
 *
 * @author Vazkii
 */
public class StructureReader {

	StructureData data;

	public StructureReader(NBTTagCompound cmp) {
		data = new StructureData();
		data.ignoreAirSpaces = cmp.getBoolean("ignoreAirSpaces");
		data.randomizeStoneBricks = cmp.getBoolean("randomizeStoneBricks");
		data.rarity = cmp.getInteger("rarity");
		data.validBiomes = getValidBiomes(cmp.getString("validBiomes"));

		int xSize = cmp.getInteger("xSize");
		int ySize = cmp.getInteger("ySize");
		int zSize = cmp.getInteger("zSize");

		data.blockData = new FullBlockData[xSize][ySize][zSize];

		NBTTagCompound blocksCmp = cmp.getCompoundTag("blockData");
		Collection<String> keys = blocksCmp.func_150296_c();
		for(String key : keys) {
			NBTBase tag = blocksCmp.getTag(key);
			if(tag instanceof NBTTagCompound) {
				NBTTagCompound blockCmp = (NBTTagCompound) tag;
				ChunkCoordinates coords = getCoordinates(key);
				FullBlockData blockData = FullBlockData.constructFromNBT(blockCmp);
				data.blockData[coords.posX][coords.posY][coords.posZ] = blockData;
			}
		}
	}

	public StructureData getStructureData() {
		return data;
	}

	private ChunkCoordinates getCoordinates(String str) {
		String[] values = str.split(",");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		int z = Integer.parseInt(values[2]);
		return new ChunkCoordinates(x, y, z);
	}

	private List<BiomeGenBase> getValidBiomes(String str) {
		List<BiomeGenBase> list = new ArrayList();
		String[] values = str.split(",");
		for(String s : values) {
			int i = Integer.parseInt(s);
			list.add(ReflectionHelper.<BiomeGenBase[], BiomeGenBase>getPrivateValue(BiomeGenBase.class, null, ObfuscationKeys.biomeList)[i]);
		}

		return list;
	}



}
