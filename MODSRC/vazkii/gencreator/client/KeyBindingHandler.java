/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

import vazkii.gencreator.DataStorage;
import vazkii.gencreator.client.gui.GuiCreator;
import vazkii.gencreator.client.saving.WriteStructureThread;
import vazkii.gencreator.helper.BoundingBox;
import vazkii.gencreator.lib.ModConstants;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

/**
 * KeyBinding
 *
 * The mod's key binding.
 *
 * @author Vazkii
 */
public class KeyBindingHandler extends KeyHandler {

	static KeyBinding key = new KeyBinding("GenCreator Keybind", Keyboard.KEY_F7);

	public KeyBindingHandler() {
		super(new KeyBinding[] { key }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return ModConstants.MOD_ID;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		Minecraft mc = Minecraft.getMinecraft();

		if(tickEnd && mc.currentScreen == null && mc.thePlayer != null && mc.thePlayer.capabilities.isCreativeMode) {
			if(WriteStructureThread.threadRunning) {
				mc.thePlayer.addChatMessage("There is alraedy a Structure being Created. Wait for it to finish first.");
				return;
			}

			MovingObjectPosition pos = mc.objectMouseOver;
			ChunkCoordinates look = pos == null ? null : new ChunkCoordinates(pos.blockX, pos.blockY, pos.blockZ);

			if(GuiScreen.isCtrlKeyDown() && (DataStorage.point1 != null || DataStorage.point2 != null || DataStorage.selection != null)) {
				DataStorage.point1 = null;
				DataStorage.point2 = null;
				DataStorage.selection = null;

				mc.thePlayer.addChatMessage("Selection Unbound.");

				return;
			}

			if(pos != null) {
				if(DataStorage.point1 == null) {
					DataStorage.point1 = look;
					mc.thePlayer.addChatMessage("Bound Point 1: " + "(" + look.posX + ", " + look.posY + ", " + look.posZ + ").");
					mc.thePlayer.addChatMessage("Press CTRL + " + GameSettings.getKeyDisplayString(key.keyCode) + " to unbind.");

					return;
				}
				else if(DataStorage.point2 == null) {
					DataStorage.point2 = look;
					DataStorage.selection = BoundingBox.createNew(mc.theWorld, DataStorage.point1, DataStorage.point2);
					mc.thePlayer.addChatMessage("Bound Point 2: " + "(" + look.posX + ", " + look.posY + ", " + look.posZ + ").");
					mc.thePlayer.addChatMessage("Press " + GameSettings.getKeyDisplayString(key.keyCode) + " again to create the Structure.");


					return;
				}
			}

			if(DataStorage.selection != null)
				mc.displayGuiScreen(new GuiCreator());
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		// NO_OP
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
