package com.github.ternityclockworks.eurekaarcana.datagen;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GenItemModels extends ItemModelProvider {

    public GenItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EurekaArcana.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        //withExistingParent(EurekaItemRegistry.EUREKA_JOURNAL.getId().getPath(), mcLoc("item/generated"));
    	basicItem(EurekaItemRegistry.EUREKA_JOURNAL.get());
    	basicItem(EurekaItemRegistry.BROKEN_MECHANISM.get());
    }
}