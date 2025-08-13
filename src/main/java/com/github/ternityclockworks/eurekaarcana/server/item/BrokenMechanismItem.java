package com.github.ternityclockworks.eurekaarcana.server.item;

import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;

public class BrokenMechanismItem extends Item {

	private static final String status = "Disassembling";
	
	public BrokenMechanismItem() {
		super(new Item.Properties().stacksTo(16));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		
		if (stack.getOrCreateTag()
			.contains(status)) {
			playerIn.startUsingItem(handIn);
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);
		}

		InteractionHand otherHand =
			handIn == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		ItemStack itemInOtherHand = playerIn.getItemInHand(otherHand);
		if (CombinationRecipe.canCombine(worldIn, itemInOtherHand)) {
			ItemStack bookStrapStack = itemInOtherHand.copy();
			ItemStack bookStrap = bookStrapStack.split(1);
			playerIn.startUsingItem(handIn);
			stack.getOrCreateTag()
				.put(status, bookStrap.serializeNBT());
			playerIn.setItemInHand(otherHand, bookStrap);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		}
		playerIn.startUsingItem(handIn);
		
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (!(entityLiving instanceof Player))
			return stack;
		Player player = (Player) entityLiving;
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(status)) {
			ItemStack combined =
				CombinationRecipe.combine(worldIn, stack);

			if (!combined.isEmpty()) {
				if (player instanceof FakePlayer) {
					player.drop(combined, false, false);
				} else {
					player.getInventory()
						.placeItemBackInInventory(combined);
				}
			}
			tag.remove(status);
			stack.shrink(1);
		}
		return stack;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (!(entityLiving instanceof Player))
			return;
		Player player = (Player) entityLiving;
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(status)) {
			ItemStack bookStack = ItemStack.of(tag.getCompound(status));
			player.getInventory()
				.placeItemBackInInventory(bookStack);
			tag.remove(status);
		}
	}
	
	public SoundEvent getEatingSound() {
		return SoundEvents.ARMOR_EQUIP_LEATHER;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		//return BookStrapCombinationRecipe.getCombinationDuration();
		return 32;
	}

}
