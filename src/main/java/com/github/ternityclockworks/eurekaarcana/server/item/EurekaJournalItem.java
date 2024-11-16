package com.github.ternityclockworks.eurekaarcana.server.item;

import com.github.ternityclockworks.eurekaarcana.util.EurekaRarity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EurekaJournalItem extends Item implements DyeableUsableItem{
	
	public EurekaJournalItem() {
		super(new Item.Properties().rarity(EurekaRarity.CURIOUS.getRarity()).stacksTo(1));
	}
	
	

	@Override
	public boolean triggerUseEffects(ItemStack stack, LivingEntity entity, int count, RandomSource random) {
		// TODO Auto-generated method stub
		return false;
	}

}
