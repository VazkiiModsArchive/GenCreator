/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.helper;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * BoundingBox
 *
 * A bounding box, that contains minmax XYZ.
 *
 * @author Vazkii
 */
public class BoundingBox {

	public int minX, minY, minZ, maxX, maxY, maxZ;

	public World world;

	private BoundingBox(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public BoundingBox copy() {
		return new BoundingBox(world, minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static BoundingBox createNew(World world, ChunkCoordinates point1, ChunkCoordinates point2) {
		int minX = point1.posX > point2.posX ? point2.posX : point1.posX;
		int maxX = point1.posX > point2.posX ? point1.posX : point2.posX;
		int minY = point1.posY > point2.posY ? point2.posY : point1.posY;
		int maxY = point1.posY > point2.posY ? point1.posY : point2.posY;
		int minZ = point1.posZ > point2.posZ ? point2.posZ : point1.posZ;
		int maxZ = point1.posZ > point2.posZ ? point1.posZ : point2.posZ;

		return new BoundingBox(world, minX, minY, minZ, maxX, maxY, maxZ);
	}

	public int getXSize() {
		return maxX - minX + 1;
	}

	public int getYSize() {
		return maxY - minY + 1;
	}

	public int getZSize() {
		return maxZ - minZ + 1;
	}

	public FullBlockData getBlockData(int x, int y, int z) {
		return new FullBlockData(world, minX + x, minY + y, minZ + z);
	}

	public float getLightLevel(int x, int y, int z) {
		return world.getBlockLightValue(minX + x, minY + y, minZ + z);
	}
}
