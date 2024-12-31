package com.github.ternityclockworks.eurekaarcana.server.item;

import java.util.function.Consumer;

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
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;

public class BookStrapItem extends Item implements DyeableUsableItem {

	public BookStrapItem() {
		super(new Item.Properties().stacksTo(1));
	}
	
//	@Override
//	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
//		ItemStack stack = playerIn.getItemInHand(handIn);
//		
//		if (stack.getOrCreateTag()
//			.contains("BookStrapCombining")) {
//			playerIn.startUsingItem(handIn);
//			return new InteractionResultHolder<>(InteractionResult.PASS, stack);
//		}
//
//		InteractionHand otherHand =
//			handIn == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
//		ItemStack itemInOtherHand = playerIn.getItemInHand(otherHand);
//		if (BookStrapCombinationRecipe.canCombine(worldIn, itemInOtherHand)) {
//			ItemStack bookStrapStack = itemInOtherHand.copy();
//			ItemStack bookStrap = bookStrapStack.split(1);
//			playerIn.startUsingItem(handIn);
//			stack.getOrCreateTag()
//				.put("BookStrapCombining", bookStrap.serializeNBT());
//			playerIn.setItemInHand(otherHand, bookStrap);
//			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
//		}
//		playerIn.startUsingItem(handIn);
//		
//		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
//	}
//	
//	@Override
//	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
//		if (!(entityLiving instanceof Player))
//			return stack;
//		Player player = (Player) entityLiving;
//		CompoundTag tag = stack.getOrCreateTag();
//		if (tag.contains("BookStrapCombining")) {
//			ItemStack combined =
//				BookStrapCombinationRecipe.combine(worldIn, stack);
//
//			if (!combined.isEmpty()) {
//				if (player instanceof FakePlayer) {
//					player.drop(combined, false, false);
//				} else {
//					player.getInventory()
//						.placeItemBackInInventory(combined);
//				}
//			}
//			tag.remove("BookStrapCombining");
//			stack.shrink(1);
//		}
//		return stack;
//	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (!(entityLiving instanceof Player))
			return;
		Player player = (Player) entityLiving;
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("BookStrapCombining")) {
			ItemStack bookStack = ItemStack.of(tag.getCompound("BookStrapCombining"));
			player.getInventory()
				.placeItemBackInInventory(bookStack);
			tag.remove("BookStrapCombining");
		}
	}
	
	@Override
	public Boolean shouldTriggerUseEffects(ItemStack stack, LivingEntity entity) {
		// Trigger every tick so that we have more fine grain control over the animation
		return true;
	}

	@Override
	public boolean triggerUseEffects(ItemStack stack, LivingEntity entity, int count, RandomSource random) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("BookStrapCombining")) {
			ItemStack polishing = ItemStack.of(tag.getCompound("BookStrapCombining"));
		}

		// After 6 ticks play the sound every 7th
		if ((entity.getTicksUsingItem() - 6) % 7 == 0)
			entity.playSound(entity.getEatingSound(stack), 0.9F + 0.2F * random.nextFloat(),
				random.nextFloat() * 0.2F + 0.9F);

		return true;
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

//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//		consumer.accept(SimpleCustomRenderer.create(this, new SandPaperItemRenderer()));
//	}

}
