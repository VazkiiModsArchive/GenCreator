/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

/**
 * GuiButtonInvisible
 *
 * An invisible gui button that can't be seen.
 *
 * @author Vazkii
 */
public class GuiButtonInvisible extends GuiButton {

	public GuiButtonInvisible(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(visible) {
			FontRenderer fontrenderer = par1Minecraft.fontRenderer;
			field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
			int l = 0x444444;

			if (!enabled)
				l = -6250336;
			else if (field_146123_n)
				l = 0x9999FF;

			drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, l);
		}
	}
}
