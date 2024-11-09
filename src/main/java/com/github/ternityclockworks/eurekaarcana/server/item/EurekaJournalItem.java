package com.github.ternityclockworks.eurekaarcana.server.item;

import java.awt.Color;

import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class EurekaJournalItem extends Item implements DyeableLeatherItem{
	private static final float t = 10000F;
	private static final float max = 0.42F;
	private static final float min = 0.28F;
    public static final Rarity EXAMPLE_RARITY = Rarity.create("eureka:example", style -> style.withColor(Color.HSBtoRGB(2F*max/t*Math.abs((System.currentTimeMillis()%t)-t/2F)+min,0.9F,0.8F)));
    
	public EurekaJournalItem() {
		super(new Item.Properties().rarity(EXAMPLE_RARITY).stacksTo(1));
	}

}
