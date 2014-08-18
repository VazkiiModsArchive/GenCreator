/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.client.saving;

import java.io.File;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.biome.BiomeGenBase;
import vazkii.gencreator.DataStorage;
import vazkii.gencreator.GenCreator;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.helper.IOHelper;
import vazkii.gencreator.helper.StructureReader;
import cpw.mods.fml.common.FMLLog;

/**
 * WriteStructureThread
 *
 * A Thread that writes a structure, from a bounding box to NBT.
 *
 * @author Vazkii
 */
public class WriteStructureThread extends Thread {

	public static boolean threadRunning = false;

	TemporaryStructureData data;
	Minecraft mc;
	String fileName;

	public WriteStructureThread(TemporaryStructureData structureData, String fileName,  Minecraft mc) {
		data = structureData;
		this.mc = mc;
		this.fileName = fileName;
		setName("GenCreator Structure Writer");
		setDaemon(true);
	}

	@Override
	public void run() {
		threadRunning = true;

		long startMs = System.currentTimeMillis();
		say("Starting Strucutre Creation. Do not modify your structure while this is going.");

		long ms = System.currentTimeMillis() - startMs;
		say("Initializing NBT [" + ms + "ms]");

		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setInteger("rarity", data.rarity);
		cmp.setString("validBiomes", generateValidBiomesString());
		cmp.setBoolean("randomizeStoneBricks", data.randomizeStoneBricks);
		cmp.setBoolean("ignoreAirSpaces", data.ignoreAirSpaces);
		cmp.setInteger("xSize", data.boundingBox.getXSize());
		cmp.setInteger("ySize", data.boundingBox.getYSize());
		cmp.setInteger("zSize", data.boundingBox.getZSize());

		NBTTagCompound blockCmp = new NBTTagCompound();

		ms = System.currentTimeMillis() - startMs;
		say("Writing Block Data to NBT [" + ms + "ms]");
		do {
			FullBlockData blockData = data.nextBlock();
			NBTTagCompound block1Cmp = new NBTTagCompound();
			blockData.writeToNBT(block1Cmp);
			blockCmp.setTag(generateLocationString(), block1Cmp);
		} while(data.iterateLocation());
		cmp.setTag("blockData", blockCmp);

		ms = System.currentTimeMillis() - startMs;
		say("Injecting NBT to .dat File [" + ms + "ms]");
		File file = IOHelper.createOrGetNBTFile(new File(GenCreator.dataDirectory, fileName + ".dat"));
		IOHelper.injectNBTToFile(cmp, file);

		ms = System.currentTimeMillis() - startMs;
		say("Reparsing NBT for WorldGen [" + ms + "ms]");
		StructureReader reader = new StructureReader(cmp);
		DataStorage.structures.add(reader.getStructureData());

		DataStorage.usedNames.add(fileName);

		ms = System.currentTimeMillis() - startMs;
		say("Done! [" + ms + "ms]");
		float seconds = ms / 1000F;
		say("File " + fileName + ".dat created. Took " + seconds + " seconds. File Path: " + file.getAbsolutePath());

		threadRunning = false;
	}

	public String generateLocationString() {
		return data.currentX + "," + data.currentY + "," + data.currentZ;
	}

	public String generateValidBiomesString() {
		String s = "";
		for(BiomeGenBase biome : data.validBiomes)
			s = s + biome.biomeID + ",";
		return s.substring(0, s.length() - 1);
	}

	public void say(String s) {
		FMLLog.log(Level.INFO, s);
		if(mc.thePlayer != null)
			mc.thePlayer.addChatMessage(new ChatComponentText("[GenCreator] " + s));
	}

}
