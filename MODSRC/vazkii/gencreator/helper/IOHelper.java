/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

/**
 * IOHelper
 *
 * Helper class for IO related features.
 *
 * @author Vazkii
 */
public class IOHelper {

	/** Gets a file with NBT (Named Binary Tag) data, creates one, or the data required
	 * if the file passed in isn't a NBT file. The file passed in must exist **/
	public static File createOrGetNBTFile(File f) {
		try {
			CompressedStreamTools.readCompressed(new FileInputStream(f));
		} catch (Exception e) {
			NBTTagCompound cmp = new NBTTagCompound();
			try {
				CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(f));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return f;
	}

	/** Gets a file passed in, creates it if it doesn't exist **/
	public static File createOrGetFile(File f) {
		if (!f.exists()) try {
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f;
	}

	/** Gets the NBTTagCompound in a file, creates one if there is none **/
	public static NBTTagCompound getTagCompoundInFile(File f) {
		try {
			NBTTagCompound cmp = CompressedStreamTools.readCompressed(new FileInputStream(f));
			return cmp;
		} catch (IOException e) {
			NBTTagCompound cmp = new NBTTagCompound();
			try {
				CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(f));
				return getTagCompoundInFile(f);
			} catch (IOException e1) {
				return null;
			}
		}
	}

	/** Injects an NBTTagCompound into an file that holds NBT data **/
	public static boolean injectNBTToFile(NBTTagCompound cmp, File f) {
		try {
			CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(f));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
