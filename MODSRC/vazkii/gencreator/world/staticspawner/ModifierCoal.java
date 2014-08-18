/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;

/**
 * ModifierCoal
 *
 * The modifier for Coal, it turns a Skeleton
 * into a Wither Skeleton.
 *
 * @author Vazkii
 */
public class ModifierCoal implements IModifier {

	@Override
	public void apply(ItemStack stack, EntityLivingBase entity) {
		if(entity instanceof EntitySkeleton) {
			EntitySkeleton skeleton = (EntitySkeleton) entity;
			skeleton.setSkeletonType(1);
		}
	}

}
