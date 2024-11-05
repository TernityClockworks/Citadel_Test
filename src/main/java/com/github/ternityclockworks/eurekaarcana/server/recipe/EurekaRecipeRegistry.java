package com.github.ternityclockworks.eurekaarcana.server.recipe;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EurekaRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, EurekaArcana.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EurekaArcana.MODID);

    public static final RegistryObject<RecipeType<MechanicalDisassemblyRecipe>> MANUAL_COMBINATION_TYPE = RECIPE_TYPES.register("manual_combination", () -> new MechanicalDisassemblyRecipe() {});
    //public static final RegistryObject<RecipeSerializer<?>> MANUAL_COMBINATION = RECIPES.register("manual_combination", () -> new SimpleCookingSerializer<>(NuclearFurnaceRecipe::new, 100));
    
    
}