/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.core;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.gencreator.DataStorage;
import vazkii.gencreator.GenCreator;
import vazkii.gencreator.helper.IOHelper;
import vazkii.gencreator.helper.StructureData;
import vazkii.gencreator.helper.StructureReader;
import vazkii.gencreator.lib.ModConstants;
import vazkii.gencreator.world.GCWorldGenerator;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * CommonProxy
 *
 * Common proxy, used between the client and the server.
 * The client and server proxies extend this class to
 * add functionality only available on the specific side.
 *
 * @author Vazkii
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		findAndCreateFolder(event);
	}

	public void init(FMLInitializationEvent event) {
		initKeybind(event);

		initTickHandlers(event);

		GameRegistry.registerWorldGenerator(new GCWorldGenerator(), 9001); // idunno

		for(File f : GenCreator.dataDirectory.listFiles())
			if(f.getName().endsWith(".dat"))
				loadStructureFile(f);
	}

	public void initKeybind(FMLInitializationEvent event) {
		// NO-OP
	}

	public void initTickHandlers(FMLInitializationEvent event) {
		// NO-OP
	}

	public void loadStructureFile(File f) {
		NBTTagCompound cmp = IOHelper.getTagCompoundInFile(f);
		StructureReader reader = new StructureReader(cmp);
		StructureData data = reader.getStructureData();
		DataStorage.structures.add(data);
	}

	public void findAndCreateFolder(FMLPreInitializationEvent event) {
		GenCreator.dataDirectory = new File(event.getModConfigurationDirectory(), ModConstants.MOD_ID);

		if(!GenCreator.dataDirectory.exists())
			GenCreator.dataDirectory.mkdir();

		if(!GenCreator.dataDirectory.exists())
			throw new RuntimeException("Folder " + GenCreator.dataDirectory.getAbsolutePath() + " could not be created!");
	}

}
