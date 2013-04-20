/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.client.saving;

import vazkii.gencreator.helper.BoundingBox;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.helper.StructureData;

/**
 * TemporaryStructureData
 *
 * A Structure Data that is still being created.
 *
 * @author Vazkii
 */
public class TemporaryStructureData extends StructureData {

	public BoundingBox boundingBox;
	public int currentX, currentY, currentZ;

	public TemporaryStructureData(BoundingBox box) {
		boundingBox = box;
		currentX = currentY = currentZ = 0;
		blockData = new FullBlockData[box.getXSize()][box.getYSize()][box.getZSize()];
	}

	public FullBlockData nextBlock() {
		FullBlockData blockData = boundingBox.getBlockData(currentX, currentY, currentZ);
		return blockData;
	}

	public boolean iterateLocation() {
		currentX++;
		if(currentX >= boundingBox.getXSize()) {
			currentX = 0;
			currentZ++;
			if(currentZ >= boundingBox.getZSize()) {
				currentZ = 0;
				currentY++;
				if(currentY >= boundingBox.getYSize())
					return false; // Done!
			}
		}

		return true;
	}

}
