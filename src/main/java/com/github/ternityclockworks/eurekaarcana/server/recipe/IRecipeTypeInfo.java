package com.github.ternityclockworks.eurekaarcana.server.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Grants an interface to deal with recipe type information and serialization.
 * Complete credit to the IRecipeTypeInfo interface from the Create mod.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/foundation/recipe/IRecipeTypeInfo.java"> Create IRecipeTypeInfo interface</a>
 * @author PepperCode1
 */
public interface IRecipeTypeInfo {

	ResourceLocation getId();

	<T extends RecipeSerializer<?>> T getSerializer();

	<T extends RecipeType<?>> T getType();

}