/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.client.saving;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import vazkii.gencreator.lib.ModConstants;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

/**
 * InventorySyncHandler
 *
 * Tile Entity inventories are not synced between the client and the server.
 * This is an important problem, since the writer is client sided. This
 * class handles syncing, trough the data the server DOES send to the client.
 *
 * @author Vazkii
 */
public class InventorySyncHandler {

	public static Map<ChunkCoordinates, IInventory> syncedInventories = new HashMap();

	ChunkCoordinates currentCoords;

	List<IInventory> invsSynced = new ArrayList();

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.action == Action.RIGHT_CLICK_BLOCK)
			currentCoords = new ChunkCoordinates(event.x, event.y, event.z);
	}

	@SubscribeEvent
	public void tickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.theWorld != null && mc.currentScreen != null && mc.thePlayer.capabilities.isCreativeMode && mc.currentScreen instanceof GuiContainer && currentCoords != null) {
				GuiContainer containerGui = (GuiContainer) mc.currentScreen;
				Container container = containerGui.inventorySlots;
				List<Slot> slots = container.inventorySlots;

				for(Slot slot : slots) {
					if(slot.inventory instanceof InventoryBasic) {
						new ChunkCoordinates(currentCoords.posX, currentCoords.posY, currentCoords.posZ);
						sync(currentCoords, slot.inventory);
					}
				}
			}

			if(mc.theWorld == null && !syncedInventories.isEmpty())
				syncedInventories.clear();

			if(!invsSynced.isEmpty())
				invsSynced.clear();
		}
	}

	public void sync(ChunkCoordinates coords, IInventory inv) {
		if(!invsSynced.contains(inv)) {
			syncedInventories.put(coords, inv);
			invsSynced.add(inv);
		}
	}

}
