/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator;

import java.io.File;
import java.util.logging.Logger;

import vazkii.gencreator.core.CommonProxy;
import vazkii.gencreator.lib.ModConstants;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * GenCreator
 *
 * The main mod class for GenCreator.
 *
 * @author Vazkii
 */
@Mod(modid = ModConstants.MOD_ID, name = ModConstants.MOD_NAME, version = ModConstants.VERSION)
public class GenCreator {

	@SidedProxy(clientSide = ModConstants.CLIENT_PROXY, serverSide = ModConstants.COMMON_PROXY)
	public static CommonProxy proxy;

	@Instance(ModConstants.MOD_ID)
	public static GenCreator instance;

	public static File dataDirectory;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
}
