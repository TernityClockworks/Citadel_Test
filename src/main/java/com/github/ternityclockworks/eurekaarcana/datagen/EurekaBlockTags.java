package com.github.ternityclockworks.eurekaarcana.datagen;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EurekaBlockTags extends BlockTagsProvider {

    public EurekaBlockTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper helper) {
        super(packOutput, lookupProvider, EurekaArcana.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    	
    }
}