package com.github.ternityclockworks.eurekaarcana.server.recipe;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.util.RegisteredObjects;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RollableItem {
	
	public static final RollableItem EMPTY = new RollableItem(ItemStack.EMPTY, 1);
	
	private static final Random r = new Random();
	private final ItemStack stack;
	/*
	 * chanceList holds the chance of getting a given amount of extra items as output according to its index.
	 * For example, chanceList[0] holds the chance of getting 0 of the corresponding item,
	 * chanceList[1] would hold the chance of getting 1 of the item as output, etc.
	 * The chance is a float between 0 and 1. The sum of all chances in the list must total to 1.
	 */
	private List<Float> chanceList = Lists.newArrayList();
	
	public RollableItem(ItemStack stack) {
		this.stack = stack;
		this.chanceList.add(1f);
	}
	
	public RollableItem(ItemStack stack, float chance) {
		this.stack = stack;
		this.chanceList.add(1-chance);
		this.chanceList.add(chance);
	}
	
	public RollableItem(ItemStack stack, List<Float> chanceList) {
		this.stack = stack;
		this.chanceList = chanceList;
	}
	
	public RollableItem(ItemStack stack, float... chances) {
		this.stack = stack;
		for (float chance : chances)
			this.chanceList.add(chance);
	}
	
	public ItemStack getStack() {
		return stack;
	}

	/**
	 * Returns the chance of a given amount of the item to be output when rolled.
	 * @param amount The index for the chance of receiving that amount of an item upon being rolled.
	 * @return The chance of receiving the input amount of an item represented as a float between 0 and 1.
	 */
	public float getChance(int amount) {
		int bonus = amount - stack.getCount();
		if (bonus < 0 || bonus > chanceList.size()-1)
			return 0f;
		return chanceList.get(bonus);
	}

	public ItemStack roll() {
		int outputAmount = 0;
		float roll = r.nextFloat();
		int index = 0;
		float accumulatedChance = 0;
		for (float chance : chanceList) {
			accumulatedChance += chance;
			if (roll <= accumulatedChance)
				outputAmount = index;
			index++;
		}
		outputAmount += stack.getCount();
		if (outputAmount <= 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.copy();
		out.setCount(outputAmount);
		return out;
	}
	
	public RollableItem addChance(Float chance) {
		if (chance < 0 || chance > 1)
			throw new IllegalArgumentException("RollableItem bonus roll chance exceeds bounds [0,1]: " + chance);
		float remainingChance = chanceList.get(0);
		if (remainingChance < chance)
			throw new IllegalArgumentException("RollableItem bonus roll chance causes total chance to exceed 1: " + chance);
		chanceList.set(0, remainingChance-chance);
		chanceList.add(chance);
		return this;
	}
	
	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		JsonArray jsonChances = new JsonArray();
		ResourceLocation resourceLocation = RegisteredObjects.getKeyOrThrow(stack.getItem());
		json.addProperty("item", resourceLocation.toString());
		int base = stack.getCount();
		if (!stack.isEmpty()) {
			json.addProperty("base", base);
			if (stack.hasTag())
				json.add("nbt", JsonParser.parseString(stack.getTag().toString()));
		}
		chanceList.forEach(c -> jsonChances.add(c));
		json.add("bonus_chance", jsonChances);
		return json;
	}

	public static RollableItem fromJson(JsonElement je) {
		if (!je.isJsonObject())
			throw new JsonSyntaxException("RollableOutput must be a json object");
	
		JsonObject json = je.getAsJsonObject();
		String item = GsonHelper.getAsString(json, "item");
		int base = GsonHelper.getAsInt(json, "base", 0);
		ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item)), base);
		if (GsonHelper.isValidNode(json, "nbt")) {
			try {
				JsonElement element = json.get("nbt");
				itemstack.setTag(TagParser.parseTag(
					element.isJsonObject() ? EurekaArcana.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
		}
		List<Float> chances = Lists.newArrayList();
		for (JsonElement chance : GsonHelper.getAsJsonArray(json, "bonus_chance"))
			chances.add(chance.getAsFloat());
		return new RollableItem(itemstack, chances);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeItem(getStack());
		buf.writeInt(chanceList.size());
		for (float chance : chanceList) {
			buf.writeFloat(chance);
		}
	}

	public static RollableItem read(FriendlyByteBuf buf) {
		ItemStack item = buf.readItem();
		List<Float> chances = Lists.newArrayList();
		int numRolls = buf.readInt();
		for (int i=0; i < numRolls; i++) {
			chances.add(buf.readFloat());
		}
		return new RollableItem(item, chances);
	}
	
	public static void toNetwork(FriendlyByteBuf buf, NonNullList<RollableItem> output) {
		buf.writeVarInt(output.size());
		output.forEach(o -> o.write(buf));
	}
	
	public static NonNullList<RollableItem> fromNetwork(FriendlyByteBuf buf) {
		NonNullList<RollableItem> output = NonNullList.create();
		int size = buf.readVarInt();
		for (int i = 0; i < size; i++)
			output.add(RollableItem.read(buf));
		return output;
	}
	
}