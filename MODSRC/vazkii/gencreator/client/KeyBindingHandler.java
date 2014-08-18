/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

import vazkii.gencreator.DataStorage;
import vazkii.gencreator.client.gui.GuiCreator;
import vazkii.gencreator.client.saving.WriteStructureThread;
import vazkii.gencreator.helper.BoundingBox;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

/**
 * KeyBinding
 *
 * The mod's key binding.
 *
 * @author Vazkii
 */
public class KeyBindingHandler {

	boolean keyDown = false;
	static KeyBinding key = new KeyBinding("GenCreator Keybind", Keyboard.KEY_F10, "key.categories.misc");

	public KeyBindingHandler() {
		ClientRegistry.registerKeyBinding(key);
	}

	@SubscribeEvent
	public void playerTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			if(key.getIsKeyPressed()) {
				if(keyDown)
					return;
				keyDown = true;
				Minecraft mc = Minecraft.getMinecraft();

				if(mc.currentScreen == null && mc.thePlayer != null && mc.thePlayer.capabilities.isCreativeMode) {
					if(WriteStructureThread.threadRunning) {
						mc.thePlayer.addChatMessage(new ChatComponentText("There is alraedy a Structure being Created. Wait for it to finish first."));
						return;
					}

					MovingObjectPosition pos = mc.objectMouseOver;
					ChunkCoordinates look = pos == null ? null : new ChunkCoordinates(pos.blockX, pos.blockY, pos.blockZ);

					if(GuiScreen.isCtrlKeyDown() && (DataStorage.point1 != null || DataStorage.point2 != null || DataStorage.selection != null)) {
						DataStorage.point1 = null;
						DataStorage.point2 = null;
						DataStorage.selection = null;

						mc.thePlayer.addChatMessage(new ChatComponentText("Selection Unbound."));

						return;
					}

					if(pos != null) {
						if(DataStorage.point1 == null) {
							DataStorage.point1 = look;
							mc.thePlayer.addChatMessage(new ChatComponentText("Bound Point 1: " + "(" + look.posX + ", " + look.posY + ", " + look.posZ + ")."));
							mc.thePlayer.addChatMessage(new ChatComponentText("Press CTRL + " + GameSettings.getKeyDisplayString(key.getKeyCode()) + " to unbind."));

							return;
						}
						else if(DataStorage.point2 == null) {
							DataStorage.point2 = look;
							DataStorage.selection = BoundingBox.createNew(mc.theWorld, DataStorage.point1, DataStorage.point2);
							mc.thePlayer.addChatMessage(new ChatComponentText("Bound Point 2: " + "(" + look.posX + ", " + look.posY + ", " + look.posZ + ")."));
							mc.thePlayer.addChatMessage(new ChatComponentText("Press " + GameSettings.getKeyDisplayString(key.getKeyCode()) + " again to create the Structure."));


							return;
						}
					}

					if(DataStorage.selection != null)
						mc.displayGuiScreen(new GuiCreator());
				}
			} else keyDown = false;
		}
	}
}
