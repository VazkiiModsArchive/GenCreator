/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * ModifierPotato
 *
 * The modifier for the potato item, it increases the entity's health
 * by the stack size.
 *
 * @author Vazkii
 */
public class ModifierPotato implements IModifier {

	@Override
	public void apply(ItemStack stack, EntityLivingBase entity) {
		entity.setHealth(entity.getHealth() + getHealth(stack));
	}

	public int getHealth(ItemStack stack) {
		return stack.stackSize;
	}


}
