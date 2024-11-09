package com.github.ternityclockworks.eurekaarcana.server.item;

import java.util.List;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BookStrapItem extends Item implements DyeableLeatherItem{

	public BookStrapItem() {
		super(new Item.Properties().stacksTo(1));
	}

}
