package com.github.ternityclockworks.eurekaarcana.server.recipe.dyeing;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Used as a basis for dyeing recipes akin to leather armor dyeing.
 * Uses Sophisticated Core's StorageDyeRecipeBase class by P3pp3rF1y as a reference as well as the ArmorDyeRecipe class
 * See <a href="https://github.com/P3pp3rF1y/SophisticatedCore/blob/1.20.x/src/main/java/net/p3pp3rf1y/sophisticatedcore/crafting/StorageDyeRecipeBase.java"> Sophisticated Core StorageDyeRecipeBase</a>
 */
public abstract class DyeingRecipe extends CustomRecipe {
	
	protected DyeingRecipe(ResourceLocation recipeID, CraftingBookCategory category) {
		super(recipeID, category);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level worldIn) {
		boolean dyeableItemPresent = false;
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack slotStack = inv.getItem(slot);
			if (slotStack.isEmpty()) {
				continue;
			}
			if (isDyeableItem(slotStack)) {
				if (dyeableItemPresent) {
					return false;
				}
				dyeableItemPresent = true;
			} else {
				return false;
			}
		}
		return dyeableItemPresent;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
		List<DyeItem> dyeItems = Lists.newArrayList();
		ItemStack dyeableStack = ItemStack.EMPTY;
		
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack slotStack = inv.getItem(slot);
			if (slotStack.isEmpty()) {
				continue;
			}
			if (isDyeableItem(slotStack)) {
				if (!dyeableStack.isEmpty()) {
					return ItemStack.EMPTY; // Multiple dyeable items used
	            }
				dyeableStack = slotStack.copy();
			} else if (slotStack.is(Tags.Items.DYES)) {
				DyeColor dyeColor = DyeColor.getColor(slotStack);
				if (dyeColor == null) {
					return ItemStack.EMPTY;
				}
				DyeItem dyeItem = DyeItem.byColor(dyeColor);
				dyeItems.add(dyeItem);
			} else {
				return ItemStack.EMPTY;
			}
		}
		return !dyeableStack.isEmpty() ? dyeItem(dyeableStack, dyeItems) : ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2; // Requires at least 2 slots
	}

	protected boolean isDyeableItem(ItemStack stack) {
		return stack.getItem() instanceof DyeableLeatherItem;
	}
	
	protected ItemStack dyeItem(ItemStack dyeableStack, List<DyeItem> dyeItems) {
		if (!dyeItems.isEmpty()) {
			return DyeableLeatherItem.dyeArmor(dyeableStack, dyeItems);
		}
		else { // Revert color if no dye is supplied to allow color clearing
			ItemStack returnStack = dyeableStack.copy();
			CompoundTag compoundtag = returnStack.getTagElement("display");
		    if (compoundtag != null && compoundtag.contains("color")) {
		    	compoundtag.remove("color");
		    }
			return returnStack;
		}
	}
	
}