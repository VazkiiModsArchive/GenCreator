/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * ModifierPotion
 *
 * The Modifier for potions. It applies an infinite potion effect to an entity.
 *
 * @author Vazkii
 */
public class ModifierPotion implements IModifier {

	@Override
	public void apply(ItemStack stack, EntityLivingBase entity) {
		List<PotionEffect> potionEffectList = Items.potionitem.getEffects(stack);
		for(PotionEffect effect : potionEffectList) {
			PotionEffect effect1 = new PotionEffect(effect.getPotionID(), Integer.MAX_VALUE, effect.getAmplifier());
			effect1.getCurativeItems().clear();
			entity.addPotionEffect(effect1);
		}
	}
}
