package com.github.ternityclockworks.eurekaarcana.server.recipe;

import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.BookStrapCombinationRecipe;
import com.github.ternityclockworks.eurekaarcana.server.recipe.dyeing.BookStrapDyeingRecipe;
import com.github.ternityclockworks.eurekaarcana.server.recipe.dyeing.JournalDyeingRecipe;
import com.github.ternityclockworks.eurekaarcana.util.Lang;

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
 * Heavily based on Create's AllRecipeTypes for organization.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/AllRecipeTypes.java"> Create AllRecipeTypes</a>
 */
public enum EurekaRecipeRegistry {

	BOOK_STRAP_DYEING(() -> new SimpleCraftingRecipeSerializer<>(BookStrapDyeingRecipe::new), () -> RecipeType.CRAFTING, false),
	JOURNAL_DYEING(() -> new SimpleCraftingRecipeSerializer<>(JournalDyeingRecipe::new), () -> RecipeType.CRAFTING, false),
	//BOOK_STRAP_COMBINATION(BookStrapCombinationRecipe.Serializer::new),
	//MECHANICAL_DISASSEMBLY(MechanicalDisassemblyRecipe.Serializer::new)
	;
	
	private final ResourceLocation recipeID;
	private final RegistryObject<RecipeSerializer<?>> serializerObject;
	@Nullable
	private final RegistryObject<RecipeType<?>> typeObject;
	private final Supplier<RecipeType<?>> type;

	EurekaRecipeRegistry(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		String name = Lang.asId(name());
		recipeID = EurekaArcana.asResource(name);
		serializerObject = Registers.RECIPE_SERIALIZERS.register(name, serializerSupplier);
		if (registerType) {
			typeObject = Registers.RECIPE_TYPES.register(name, typeSupplier);
			type = typeObject;
		} else {
			typeObject = null;
			type = typeSupplier;
		}
	}

	EurekaRecipeRegistry(Supplier<RecipeSerializer<?>> serializerSupplier) {
		String name = Lang.asId(name());
		recipeID = EurekaArcana.asResource(name);
		serializerObject = Registers.RECIPE_SERIALIZERS.register(name, serializerSupplier);
		typeObject = Registers.RECIPE_TYPES.register(name, () -> RecipeType.simple(recipeID));
		type = typeObject;
	}
	
	public static void register(IEventBus modEventBus) {
		ShapedRecipe.setCraftingSize(9, 9);
		Registers.RECIPE_SERIALIZERS.register(modEventBus);
		Registers.RECIPE_TYPES.register(modEventBus);
	}
	
	public ResourceLocation getId() {
		return recipeID;
	}

	@SuppressWarnings("unchecked")
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject.get();
	}

	@SuppressWarnings("unchecked")
	public <T extends RecipeType<?>> T getType() {
		return (T) type.get();
	}

	public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
		return world.getRecipeManager()
			.getRecipeFor(getType(), inv, world);
	}
	
	private static class Registers {
		private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EurekaArcana.MODID);
		private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, EurekaArcana.MODID);
	}

}