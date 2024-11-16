package com.github.ternityclockworks.eurekaarcana.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.ternityclockworks.eurekaarcana.datagen.recipes.*;
import com.github.ternityclockworks.eurekaarcana.datagen.recipes.combination.*;

public class DataGeneration {

    public static void generate(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //generator.addProvider(event.includeClient(), new EurekaBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new EurekaItemModels(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new EurekaLanguageProvider(packOutput, "en_us"));
        
        EurekaBlockTags blockTags = new EurekaBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new EurekaItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CraftingRecipeGen(packOutput));
        //CombinationRecipeGen.registerAll(generator, packOutput);
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(EurekaLootTables::new, LootContextParamSets.BLOCK))));
    }
}