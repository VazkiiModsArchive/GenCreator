/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.core;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import vazkii.gencreator.DataStorage;
import vazkii.gencreator.client.KeyBindingHandler;
import vazkii.gencreator.client.SelectionRenderHandler;
import vazkii.gencreator.client.saving.InventorySyncHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ClientProxy
 *
 * Extension of CommonProxy, providing client side specific functionality.
 *
 * @author Vazkii
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		MinecraftForge.EVENT_BUS.register(new SelectionRenderHandler());
	}

	@Override
	public void loadStructureFile(File f) {
		super.loadStructureFile(f);
		DataStorage.usedNames.add(f.getName().replaceAll(".dat", ""));
	}

	@Override
	public void initKeybind(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
	}

	@Override
	public void initTickHandlers(FMLInitializationEvent event) {
		InventorySyncHandler tickHandler = new InventorySyncHandler();
		FMLCommonHandler.instance().bus().register(tickHandler);
		MinecraftForge.EVENT_BUS.register(tickHandler);
	}

}
