/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ChunkCoordinates;
import vazkii.gencreator.helper.BoundingBox;
import vazkii.gencreator.helper.StructureData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * DataStorage
 *
 * Storage for some data. It wouldn't fit well elsewhere.
 *
 * @author Vazkii
 */
public class DataStorage {

	@SideOnly(Side.CLIENT)
	public static ChunkCoordinates point1, point2;

	@SideOnly(Side.CLIENT)
	public static BoundingBox selection;

	public static List<String> usedNames = new ArrayList();

	public static List<StructureData> structures = new ArrayList();
}
