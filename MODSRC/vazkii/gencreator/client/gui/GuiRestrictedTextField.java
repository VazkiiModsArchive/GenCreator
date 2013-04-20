/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 19 Apr 2013
package vazkii.gencreator.client.gui;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

/**
 * GuiRestrictedTextField
 *
 * A text field restricted only a specific set of characters.
 *
 * @author Vazkii
 */
public class GuiRestrictedTextField extends GuiTextField {

	List<Character> possibleChars = new LinkedList();

	public GuiRestrictedTextField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5, String possibleChars) {
		super(par1FontRenderer, par2, par3, par4, par5);

		Keyboard.enableRepeatEvents(true);

		char[] chars = possibleChars.toCharArray();
		for (char c : chars)
			this.possibleChars.add(Character.valueOf(c));
	}

	@Override
	public boolean textboxKeyTyped(char par1, int par2) {
		if (possibleChars.contains(Character.valueOf(par1)) || par2 == Keyboard.KEY_BACK)
			return super.textboxKeyTyped(par1, par2);
		else return false;
	}

}
