package com.github.ternityclockworks.eurekaarcana.server.recipe;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.recipe.MechanicalDisassemblyRecipe;
//import com.github.ternityclockworks.eurekaarcana.util.Lang;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Heavily based on Create's AllRecipeTypes enum for organization.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/AllRecipeTypes.java"> Create AllRecipeTypes</a>
 */
public enum EurekaRecipeTypes implements IRecipeTypeInfo {

	MECHANICAL_DISASSEMBLY(MechanicalDisassemblyRecipe::new);
	
	private final ResourceLocation recipeTypeID;
	private final RegistryObject<RecipeSerializer<?>> serializerObject;
	@Nullable
	private final RegistryObject<RecipeType<?>> typeObject;
	private final Supplier<RecipeType<?>> type;

	EurekaRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		//String name = Lang.asId(name());
		String name = name();
		recipeTypeID = EurekaArcana.asResource(name);
		serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
		if (registerType) {
			typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
			type = typeObject;
		} else {
			typeObject = null;
			type = typeSupplier;
		}
	}

	EurekaRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
		//String name = Lang.asId(name());
		String name = name();
		recipeTypeID = EurekaArcana.asResource(name);
		serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
		typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(recipeTypeID));
		type = typeObject;
	}
	
	public static void register(IEventBus modEventBus) {
		ShapedRecipe.setCraftingSize(9, 9);
		Registers.SERIALIZER_REGISTER.register(modEventBus);
		Registers.TYPE_REGISTER.register(modEventBus);
	}

	@Override
	public ResourceLocation getId() {
		return recipeTypeID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) type.get();
	}

	public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
		return world.getRecipeManager()
			.getRecipeFor(getType(), inv, world);
	}
	
	private static class Registers {
		private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EurekaArcana.MODID);
		private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, EurekaArcana.MODID);
	}

}