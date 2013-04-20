/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 20 Apr 2013
package vazkii.gencreator.world.staticspawner;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
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

	private static Map<Integer, IModifier> modifiers = new TreeMap();

	static {
		modifiers.put(Item.potato.itemID, new ModifierPotato());
		modifiers.put(Item.poisonousPotato.itemID, new ModifierPoisonPotato());
		modifiers.put(Item.netherStar.itemID, new ModifierNetherStar());
		modifiers.put(Item.coal.itemID, new ModifierCoal());
		modifiers.put(Item.potion.itemID, new ModifierPotion());
		modifiers.put(Item.emerald.itemID, new ModifierEmerald());
		modifiers.put(Block.cloth.blockID, new ModifierWool());
	}

	TileEntityChest chest;
	World world;

	EntityLiving entity;
	EntityLiving mount;

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
		if(stack == null || stack.getItem() == null || stack.itemID != Item.monsterPlacer.itemID) {
			if(!mount)
				illegal = true;
			return;
		}

		Entity entity = EntityList.createEntityByID(stack.getItemDamage(), world);
		if(entity == null || !(entity instanceof EntityLiving)) {
			if(!mount)
				illegal = true;
			return;
		}

		if(!mount) {
			this.entity = (EntityLiving) entity;
			this.entity.setPosition(chest.xCoord + 0.5, chest.yCoord, chest.zCoord + 0.5);
			villager = entity instanceof EntityVillager;
	        if (stack.hasDisplayName()) // Set custom name
	            this.entity.func_94058_c(stack.getDisplayName());
	        flagEntityNonDespawnable(this.entity);
		} else {
			this.mount = (EntityLiving) entity;
			this.mount.setPosition(chest.xCoord + 0.5, chest.yCoord, chest.zCoord + 0.5);
	        if (stack.hasDisplayName()) // Set custom name
	        	this.mount.func_94058_c(stack.getDisplayName());
	        flagEntityNonDespawnable(this.mount);
		}


	}

	private void flagEntityNonDespawnable(EntityLiving entity) {
		ReflectionHelper.setPrivateValue(EntityLiving.class, entity, true, 72);
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
		ReflectionHelper.setPrivateValue(EntityVillager.class, villager, Integer.MAX_VALUE, 8);
	}

	private void applyModifiers(int start, EntityLiving entity) {
		if(illegal || entity == null)
			return;

		for(int i = start; i < start + 8; i++) {
			ItemStack stack = chest.getStackInSlot(i);
			if(stack != null)
				applyModifier(stack, entity);
		}
	}

	private void applyModifier(ItemStack stack, EntityLiving entity) {
		if(modifiers.containsKey(stack.itemID)) {
			IModifier modifier = modifiers.get(stack.itemID);
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
