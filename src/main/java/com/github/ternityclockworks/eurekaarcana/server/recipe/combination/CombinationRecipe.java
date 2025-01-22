package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeCategory;
import com.github.ternityclockworks.eurekaarcana.server.recipe.RollableOutput;
import com.github.ternityclockworks.eurekaarcana.util.SerializerHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;


/**
 * Basic template for all manual combination recipes.
 * Manual combination recipes refer to those which allow the player to craft by using an item in their main hand with an item in their off hand.
 * Based on Create's ProcessingRecipe abstract class.
 */
@ParametersAreNonnullByDefault
public class CombinationRecipe<T extends Container> implements Recipe<T>{

	protected EurekaRecipeCategory recipeCategory;
	protected ResourceLocation recipeID; // Resource for both the recipe and loot table
	protected Ingredient mainhandIngredient = Ingredient.EMPTY;
	protected Ingredient offhandIngredient = Ingredient.EMPTY;
	protected NonNullList<RollableOutput> recipeOutput = NonNullList.create();
	protected int combinationTime = 0;
	//protected boolean enforceItemOrder = false;

	private RecipeType<?> type;
	private RecipeSerializer<?> serializer;
	
	public CombinationRecipe( EurekaRecipeCategory recipeCategory,
							  ResourceLocation recipeID,
							  Ingredient mainhandIngredient,
							  Ingredient offhandIngredient,
							  NonNullList<RollableOutput> recipeOutput,
							  int combinationTime) {
		this.recipeCategory = recipeCategory;
		this.recipeID = recipeID;
		this.mainhandIngredient = mainhandIngredient;
		this.offhandIngredient = offhandIngredient;
		this.recipeOutput = recipeOutput;
		this.combinationTime = combinationTime;
		this.type = recipeCategory.getType();
		this.serializer = recipeCategory.getSerializer();
	}
	
	protected CombinationRecipe( EurekaRecipeCategory recipeCategory, ResourceLocation recipeID) {
		this.recipeCategory = recipeCategory;
		this.recipeID = recipeID;
		this.type = recipeCategory.getType();
		this.serializer = recipeCategory.getSerializer();
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(mainhandIngredient);
		ingredients.add(offhandIngredient);
		return ingredients;
	}
	
	public EurekaRecipeCategory getCategory() {
		return recipeCategory;
	}
	
	@Override
	public ResourceLocation getId() {
		return recipeID;
	}
	
	public Ingredient getMainhandIngredient() {
		return mainhandIngredient;
	}
	
	public Ingredient getOffhandIngredient() {
		return offhandIngredient;
	}
	
	public int getCombinationTime() {
		return combinationTime;
	}
	
	public NonNullList<RollableOutput> getRecipeOutput() {
		return recipeOutput;
	}
	
	@Override
	public ItemStack getResultItem(RegistryAccess reg) {
		// TODO roll output
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(T inv, Level worldIn) {
		return mainhandIngredient.test(inv.getItem(0));
	}
	
	@Override
	public ItemStack assemble(T inv, RegistryAccess registryAccess) {
		return getResultItem(registryAccess);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	// TODO roll results from loot table
//	@Override
//	public ItemStack getResultItem(RegistryAccess registryAccess) {
//		return recipeOutput.get(0);
//	}
	
	protected void setRecipeOutput(NonNullList<RollableOutput> recipeOutput) {
		this.recipeOutput = recipeOutput;
	}
	
	protected void addRecipeOutput(RollableOutput recipeOutput) {
		this.recipeOutput.add(recipeOutput);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}
	
	// Combination recipes do not show up in the recipe book
	@Override
	public String getGroup() {
		return "combination";
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return serializer;
	}

	@Override
	public RecipeType<?> getType() {
		return type;
	}
	
	protected void setMainhand(Ingredient mainhand) {
		this.mainhandIngredient = mainhand;
	}
	
	protected void setOffhand(Ingredient offhand) {
		this.offhandIngredient = offhand;
	}
	
	protected void setIngredients(Ingredient mainhand, Ingredient offhand) {
		setMainhand(mainhand);setOffhand(offhand);
	}
	
	protected void setCombinationTime(int combinationTime) {
		this.combinationTime = combinationTime;
	}
	
	public static boolean canCombine(Level world, ItemStack stack) {
		return !getMatchingRecipes(world, stack).isEmpty();
	}

	public static ItemStack combine(Level world, ItemStack stack) {
		List<Recipe<CombinationInv>> matchingRecipes = getMatchingRecipes(world, stack);
		if (!matchingRecipes.isEmpty())
			return matchingRecipes.get(0) // Craft result
				.assemble(new CombinationInv(stack), world.registryAccess())
				.copy();
		return stack; // Cannot combine
	}
	
	public static List<Recipe<CombinationInv>> getMatchingRecipes(Level world, ItemStack stack) {
		return world.getRecipeManager()
			.getRecipesFor(EurekaRecipeCategory.COMBINATION.getType(), new CombinationInv(stack), world);
	}
	
	public static class CombinationInv extends RecipeWrapper {
		public CombinationInv(ItemStack stack) {
			super(new ItemStackHandler(1));
			inv.setStackInSlot(0, stack);
		}

	}
	
	public static <C extends Container> Builder<CombinationRecipe<C>> build(EurekaRecipeCategory recipeCategory, ResourceLocation recipeID) {
		return new Builder<CombinationRecipe<C>>(recipeCategory, recipeID);
	}
	
	public static class Serializer<R extends CombinationRecipe<?>> implements RecipeSerializer<R> {

		@SuppressWarnings("unchecked")
		@Override
		public R fromJson(ResourceLocation recipeID, JsonObject jsonObj) {
			EurekaRecipeCategory recipeCategory;
			Ingredient mainhandIngredient = Ingredient.EMPTY;
			Ingredient offhandIngredient = Ingredient.EMPTY;
			NonNullList<RollableOutput> recipeOutput = NonNullList.create();
			int combinationTime = 0;
			
			recipeCategory = SerializerHelper.recipeCategoryFromJson(jsonObj);
			mainhandIngredient = SerializerHelper.ingredientFromJson(jsonObj, "mainhandIngredient");
			offhandIngredient = SerializerHelper.ingredientFromJson(jsonObj, "offhandIngredient");
			recipeOutput = SerializerHelper.rollableOutputListFromJson(jsonObj);
			combinationTime = GsonHelper.getAsInt(jsonObj, "combinationTime");
			
			return (R) new CombinationRecipe<CombinationInv>(recipeCategory,recipeID,mainhandIngredient,offhandIngredient,recipeOutput,combinationTime);
		}
		
		public void toJson(JsonObject json, R recipe) {
			JsonArray jsonOutputs = new JsonArray();
			recipe.recipeOutput.forEach(o -> jsonOutputs.add(o.toJson()));

			json.addProperty("recipeCategory", recipe.recipeCategory.toString());
			json.add("mainhandIngredient", recipe.mainhandIngredient.toJson());
			json.add("offhandIngredient", recipe.offhandIngredient.toJson());
			json.add("recipeOutput", jsonOutputs);
			json.addProperty("combinationTime", recipe.combinationTime);
		}

		@SuppressWarnings("unchecked")
		@Override
		public @Nullable R fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buf) {
			EurekaRecipeCategory recipeCategory;
			Ingredient mainhandIngredient = Ingredient.EMPTY;
			Ingredient offhandIngredient = Ingredient.EMPTY;
			NonNullList<RollableOutput> recipeOutput = NonNullList.create();
			int combinationTime = 0;
			
			recipeCategory = EurekaRecipeCategory.fromNetwork(buf);
			mainhandIngredient = Ingredient.fromNetwork(buf);
			offhandIngredient = Ingredient.fromNetwork(buf);
			recipeOutput = RollableOutput.fromNetwork(buf);
			combinationTime = buf.readVarInt();
			
			return (R) new CombinationRecipe<CombinationInv>(recipeCategory,recipeID,mainhandIngredient,offhandIngredient,recipeOutput,combinationTime);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, R recipe) {
			EurekaRecipeCategory recipeCategory = recipe.recipeCategory;
			Ingredient mainhandIngredient = recipe.mainhandIngredient;
			Ingredient offhandIngredient = recipe.offhandIngredient;
			NonNullList<RollableOutput> recipeOutput = recipe.recipeOutput;
			int combinationTime = recipe.combinationTime;
			
			EurekaRecipeCategory.toNetwork(buf, recipeCategory);
			mainhandIngredient.toNetwork(buf);
			offhandIngredient.toNetwork(buf);
			RollableOutput.toNetwork(buf, recipeOutput);
			buf.writeVarInt(combinationTime);
		}
    }
	
	public static class Builder<R extends CombinationRecipe<?>> {
		private final R recipe;
		
		@SuppressWarnings("unchecked")
		public <C extends Container> Builder(EurekaRecipeCategory recipeCategory, ResourceLocation recipeID) {
			recipe = (R) new CombinationRecipe<C>(recipeCategory,recipeID);
		}
		
		public Builder<R> withMainhandIngredient(Ingredient mainhand) {
			recipe.setMainhand(mainhand); return this;
		}
		
		public Builder<R> withOffhandIngredient(Ingredient offhand) {
			recipe.setOffhand(offhand); return this;
		}
		
		public Builder<R> withIngredients(Ingredient mainhand, Ingredient offhand) {
			recipe.setIngredients(mainhand,offhand); return this;
		}
		
		public Builder<R> withSingleOutput(RollableOutput output) {
			NonNullList<RollableOutput> recipeOutput = NonNullList.create();
			recipeOutput.add(output); recipe.setRecipeOutput(recipeOutput); return this;
		}
		
		public Builder<R> withSingleOutput(ItemStack stack, float chance) {
			NonNullList<RollableOutput> recipeOutput = NonNullList.create();
			RollableOutput output = new RollableOutput(stack,chance);
			recipeOutput.add(output); recipe.setRecipeOutput(recipeOutput); return this;
		}
		
		public Builder<R> addOutput(RollableOutput output) {
			recipe.addRecipeOutput(output); return this;
		}
		
		public Builder<R> addOutput(ItemStack stack, float chance) {
			RollableOutput output = new RollableOutput(stack,chance);
			recipe.addRecipeOutput(output); return this;
		}
		
		public Builder<R> withOutputs(NonNullList<RollableOutput> recipeOutput) {
			recipe.setRecipeOutput(recipeOutput); return this;
		}
		
		public Builder<R> withCombinationTime(int combinationTime) {
			recipe.setCombinationTime(combinationTime); return this;
		}
		
		public boolean verify() {
			String loggerPrefix = "CombinationRecipe.Builder found an issue when building " + recipe.recipeCategory + " " + recipe.recipeID + ": ";
			Logger buildLogger = EurekaArcana.LOGGER;
			boolean flag = true;
			if (recipe.mainhandIngredient == Ingredient.EMPTY) {
				flag = false;
				buildLogger.warn(loggerPrefix + "Recipe has no mainhand ingredient.");
			}
			if (recipe.offhandIngredient == Ingredient.EMPTY) {
				flag = false;
				buildLogger.warn(loggerPrefix + "Recipe has no offhand ingredient.");
			}
			if (recipe.recipeOutput.isEmpty()) {
				flag = false;
				buildLogger.warn(loggerPrefix + "Recipe has no output.");
			}
			return flag;
		}
		
		public void save(Consumer<FinishedRecipe> consumer) {
			verify();
			consumer.accept(new CombinationOutput<R>(recipe));
		}
		
		
		protected static class CombinationOutput<R extends CombinationRecipe<?>> implements FinishedRecipe {
			private final R recipe;
			private final EurekaRecipeCategory recipeCategory;
			private final ResourceLocation recipeID;
			private final Serializer<R> serializer;
			//private final RecipeType<?> type;
			
			@SuppressWarnings("unchecked")
			public CombinationOutput(R recipe) {
				this.recipe = recipe;
				if (!(recipe.getSerializer() instanceof Serializer))
					throw new IllegalStateException("Encountered type mismatch when building a CombinationRecipe: "
							+ recipe.getId() + " is not serialized with CombinationRecipe.Serializer.");
				this.recipeCategory = recipe.getCategory();
				this.recipeID = new ResourceLocation(recipe.getId().getNamespace(),
						recipeCategory.getId().getPath() + "/" + recipe.getId().getPath());
				this.serializer = (Serializer<R>) recipe.getSerializer();
				//this.type = recipe.getType();
			}
	
			@Override
			public void serializeRecipeData(JsonObject jsonObj) {
				serializer.toJson(jsonObj, recipe);
			}
	
			@Override
			public ResourceLocation getId() {
				return recipeID;
			}
	
			@Override
			public RecipeSerializer<?> getType() {
				return serializer;
			}
	
			@Override
			public JsonObject serializeAdvancement() {
				return null;
			}
	
			@Override
			public ResourceLocation getAdvancementId() {
				return null;
			}
		}
	}
}