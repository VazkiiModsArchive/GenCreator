/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;

/**
 * ModifierWool
 *
 * The Wool modifier. It changes the color of a villager.
 *
 * @author Vazkii
 */
public class ModifierWool implements IModifier {

	private static Map<Integer, Integer> villagerColors = new TreeMap();

	static {
		villagerColors.put(12, 0); // Farmer (Brown)
		villagerColors.put(0, 1); // Librarian (White)
		villagerColors.put(6, 2); // Priest (Pink)
		villagerColors.put(15, 3); // Blacksmith (Black)
		villagerColors.put(8, 4); // Butcher (Gray)
	}

	@Override
	public void apply(ItemStack stack, EntityLivingBase entity) {
		if(entity instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager) entity;
			if(villagerColors.containsKey(stack.getItemDamage()))
				villager.setProfession(villagerColors.get(stack.getItemDamage()));
		}
	}

}
