/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import net.minecraft.item.ItemStack;

/**
 * ModifierPoisonPotato
 *
 * An entity modifier similar to the Potato Modifier, but
 * decreases the health rather than increasing it.
 *
 * @author Vazkii
 */
public class ModifierPoisonPotato extends ModifierPotato {

	@Override
	public int getHealth(ItemStack stack) {
		return -super.getHealth(stack);
	}

}
