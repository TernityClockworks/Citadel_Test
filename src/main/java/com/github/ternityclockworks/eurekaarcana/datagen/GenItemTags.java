package com.github.ternityclockworks.eurekaarcana.datagen;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GenItemTags extends ItemTagsProvider {

    public GenItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), EurekaArcana.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    }
}