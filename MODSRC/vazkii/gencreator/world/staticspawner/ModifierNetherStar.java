/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;

/**
 * ModifierNetherStar
 *
 * The modifier for a Nether star. It supercharges a creeper.
 *
 * @author Vazkii
 */
public class ModifierNetherStar implements IModifier {

	@Override
	public void apply(ItemStack stack, EntityLivingBase entity) {
		if(entity instanceof EntityCreeper) {
			EntityCreeper creeper = (EntityCreeper)entity;
			creeper.getDataWatcher().updateObject(17, (byte) 1);
		}
	}

}
