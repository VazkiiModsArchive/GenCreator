/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * IModifier
 *
 * A modifier for an entity, used in the Static Spawner system.
 *
 * @author Vazkii
 */
public interface IModifier {

	public void apply(ItemStack stack, EntityLivingBase entity);

}
