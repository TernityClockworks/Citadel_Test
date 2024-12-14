package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipeBuilder;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipeSerializer;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe;
import com.github.ternityclockworks.eurekaarcana.datagen.recipes.combination.BookStrapCombinationRecipeGen;
import com.github.ternityclockworks.eurekaarcana.server.recipe.IRecipeTypeInfo;
import com.github.ternityclockworks.eurekaarcana.util.RegisteredObjects;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidType;

public abstract class CombinationRecipeProvider extends EurekaRecipeProvider {

	protected static final List<CombinationRecipeProvider> GENERATORS = new ArrayList<>();
	protected static final int BUCKET = FluidType.BUCKET_VOLUME;
	protected static final int BOTTLE = 250;

	public static void registerAll(DataGenerator gen, PackOutput output) {
		GENERATORS.add(new BookStrapCombinationRecipeGen(output));

		gen.addProvider(true, new DataProvider() {

			@Override
			public String getName() {
				return "Eureka's Combination Recipes";
			}

			@Override
			public CompletableFuture<?> run(CachedOutput dc) {
				return CompletableFuture.allOf(GENERATORS.stream()
					.map(gen -> gen.run(dc))
					.toArray(CompletableFuture[]::new));
			}
		});
	}

	public CombinationRecipeProvider(PackOutput generator) {
		super(generator);
	}

	/**
	 * Create a processing recipe with a single itemstack ingredient, using its id
	 * as the name of the recipe
	 */
	public <T extends CombinationRecipe<?>> GeneratedRecipe create(String namespace,
			ItemLike mainhandIngredient, ItemLike offhandIngredient,
			UnaryOperator<CombinationRecipeBuilder<T>> transform) {
		
		CombinationRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe = c -> {
			ItemLike mainItemLike = mainhandIngredient;
			ItemLike offItemLike = offhandIngredient;
			
			transform
				.apply(new CombinationRecipeBuilder<>(serializer.getFactory(),
					new ResourceLocation(namespace, RegisteredObjects.getKeyOrThrow(mainItemLike.asItem())
						.getPath())).mainhand(Ingredient.of(mainItemLike)).offhand(offItemLike))
				.build(c);
		};
		recipes.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Create a processing recipe with a single itemstack ingredient, using its id
	 * as the name of the recipe
	 */
	protected <T extends CombinationRecipe<?>> GeneratedRecipe create(ItemLike mainhand, ItemLike offhand,
		UnaryOperator<CombinationRecipeBuilder<T>> transform) {
		return create(EurekaArcana.MODID, mainhand, offhand, transform);
	}

	protected <T extends CombinationRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name,
		UnaryOperator<CombinationRecipeBuilder<T>> transform) {
		CombinationRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe =
			c -> transform.apply(new CombinationRecipeBuilder<>(serializer.getFactory(), name.get()))
				.build(c);
		recipes.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Create a new processing recipe, with recipe definitions provided by the
	 * function
	 */
	protected <T extends CombinationRecipe<?>> GeneratedRecipe create(ResourceLocation name,
		UnaryOperator<CombinationRecipeBuilder<T>> transform) {
		return createWithDeferredId(() -> name, transform);
	}

	/**
	 * Create a new processing recipe, with recipe definitions provided by the
	 * function
	 */
	<T extends CombinationRecipe<?>> GeneratedRecipe create(String name,
		UnaryOperator<CombinationRecipeBuilder<T>> transform) {
		return create(EurekaArcana.asResource(name), transform);
	}

	protected abstract IRecipeTypeInfo getRecipeType();

	protected <T extends CombinationRecipe<?>> CombinationRecipeSerializer<T> getSerializer() {
		return getRecipeType().getSerializer();
	}

	protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
		return () -> {
			ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(item.get()
				.asItem());
			return EurekaArcana.asResource(registryName.getPath() + suffix);
		};
	}

}