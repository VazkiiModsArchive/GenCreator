/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 19 Apr 2013
package vazkii.gencreator.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

/**
 * GuiButtonPlusMinus
 *
 * A GuiButton that has either a plus or a minus.
 *
 * @author Vazkii
 */
public class GuiButtonPlusMinus extends GuiButton {

	boolean plus = true;

	public GuiButtonPlusMinus(int par1, int par2, int par3, boolean plus) {
		super(par1, par2, par3, 16, 16, "");
		setPlus(plus);
	}

	public void setPlus(boolean plus) {
		this.plus = plus;
	}

	public void flipPlus() {
		plus = !plus;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        par1Minecraft.renderEngine.bindTexture(GuiCreator.guiResource);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
        int hoverState = getHoverState(field_146123_n);
        drawTexturedModalRect(xPosition, yPosition, hoverState == 1 ? 0 : 16, plus ? 223 : 239, 16, 16);
	}

}
