package com.github.ternityclockworks.eurekaarcana.server.recipe;

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

public class EurekaRecipeRegistry {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, EurekaArcana.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EurekaArcana.MODID);

    public static final RegistryObject<RecipeType<BookStrapDyeingRecipe>> BOOK_STRAP_DYEING = RECIPE_TYPES.register("combination", () -> new RecipeType<BookStrapDyeingRecipe>() {});
    
    //public static final RegistryObject<RecipeType<CombinationRecipe>> COMBINATION = RECIPE_TYPES.register("combination", () -> new RecipeType<>() {});
    //public static final RegistryObject<RecipeType<NuclearFurnaceRecipe>> NUCLEAR_FURNACE_TYPE = TYPE_DEF_REG.register("nuclear_furnace", () -> new RecipeType<>() {
    //});

    //public static final RegistryObject<RecipeSerializer<?>> CAVE_MAP = DEF_REG.register("cave_map", () -> new SimpleCraftingRecipeSerializer<>(RecipeCaveMap::new));
	
}