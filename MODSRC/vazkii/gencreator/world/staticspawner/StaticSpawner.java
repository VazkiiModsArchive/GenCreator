/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * StaticSpawner
 *
 * The main class for the Static Spawner system.
 *
 * @author Vazkii
 */
public class StaticSpawner {

	private static Map<Item, IModifier> modifiers = new HashMap();

	static {
		modifiers.put(Items.potato, new ModifierPotato());
		modifiers.put(Items.poisonous_potato, new ModifierPoisonPotato());
		modifiers.put(Items.nether_star, new ModifierNetherStar());
		modifiers.put(Items.coal, new ModifierCoal());
		modifiers.put(Items.potionitem, new ModifierPotion());
		modifiers.put(Items.emerald, new ModifierEmerald());
		modifiers.put(Item.getItemFromBlock(Blocks.wool), new ModifierWool());
	}

	TileEntityChest chest;
	World world;

	EntityLivingBase entity;
	EntityLivingBase mount;

	boolean illegal = false;
	boolean villager = false;

	public StaticSpawner(TileEntityChest chest, World world) {
		this.chest = chest;
		this.world = world;
	}

	public void spawn() {
		identifyEntity();
		applyModifiers(1, entity);

		identifyMount();
		applyModifiers(19, mount);

		if(villager)
			populateVillagerTrades();
		else equipEntity();

		spawn_do();
	}

	private void identifyEntity() {
		if(illegal)
			return;

		ItemStack stack = chest.getStackInSlot(0);
		identifyEntity(stack, false);
	}

	private void identifyMount() {
		if(illegal)
			return;

		ItemStack stack = chest.getStackInSlot(18);
		identifyEntity(stack, true);
	}

	private void identifyEntity(ItemStack stack, boolean mount) {
		if(stack == null || stack.getItem() == null || stack.getItem() != Items.spawn_egg) {
			if(!mount)
				illegal = true;
			return;
		}

		Entity entity = EntityList.createEntityByID(stack.getItemDamage(), world);
		if(entity == null || !(entity instanceof EntityLivingBase)) {
			if(!mount)
				illegal = true;
			return;
		}

		if(!mount) {
			this.entity = (EntityLivingBase) entity;
			this.entity.setPosition(chest.xCoord + 0.5, chest.yCoord, chest.zCoord + 0.5);
			villager = entity instanceof EntityVillager;
	        if (stack.hasDisplayName() && this.entity instanceof EntityLiving) // Set custom name
	            ((EntityLiving) this.entity).setCustomNameTag(stack.getDisplayName());
	        flagEntityNonDespawnable(this.entity);
		} else {
			this.mount = (EntityLiving) entity;
			this.mount.setPosition(chest.xCoord + 0.5, chest.yCoord, chest.zCoord + 0.5);
	        if (stack.hasDisplayName() && this.mount instanceof EntityLiving) // Set custom name
	        	 ((EntityLiving) this.mount).setCustomNameTag(stack.getDisplayName());
	        flagEntityNonDespawnable(this.mount);
		}


	}

	private void flagEntityNonDespawnable(EntityLivingBase entity) {
		if(entity instanceof EntityLiving)
			((EntityLiving) entity).func_110163_bv();
	}

	private void equipEntity() {
		if(illegal)
			return;

		for(int i = 0; i < 5; i++) {
			ItemStack stack = chest.getStackInSlot(i + 9);

			if(stack != null)
				entity.setCurrentItemOrArmor(4 - i, stack);
		}
	}

	private void populateVillagerTrades() {
		if(illegal)
			return;

		MerchantRecipeList list = new MerchantRecipeList();

		for(int i = 0; i < 9; i++) {
			ItemStack buy = chest.getStackInSlot(i + 9);
			ItemStack sell = chest.getStackInSlot(i + 18);

			if(buy != null && sell != null) {
				MerchantRecipe recipe = new MerchantRecipe(buy, sell);
				recipe.func_82783_a(Integer.MAX_VALUE - 7);
				list.add(recipe);
			}
		}

		if(!list.isEmpty()) {
			setVillagerTradingList((EntityVillager) entity, list);
			maximizeVillagerWealth((EntityVillager) entity);
		}
	}

	private void setVillagerTradingList(EntityVillager villager, MerchantRecipeList list) {
		ReflectionHelper.setPrivateValue(EntityVillager.class, villager, list, 5);
	}

	private void maximizeVillagerWealth(EntityVillager villager) {
		villager.setHealth(Integer.MAX_VALUE);
	}

	private void applyModifiers(int start, EntityLivingBase entity) {
		if(illegal || entity == null)
			return;

		for(int i = start; i < start + 8; i++) {
			ItemStack stack = chest.getStackInSlot(i);
			if(stack != null)
				applyModifier(stack, entity);
		}
	}

	private void applyModifier(ItemStack stack, EntityLivingBase entity) {
		if(modifiers.containsKey(stack.getItem())) {
			IModifier modifier = modifiers.get(stack.getItem());
			modifier.apply(stack, entity);
		}
	}

	private void spawn_do() {
		if(illegal)
			return;

		world.spawnEntityInWorld(entity);
		if(mount != null) {
			world.spawnEntityInWorld(mount);
			entity.mountEntity(mount);
		}
	}

}
