package com.github.ternityclockworks.eurekaarcana.datagen;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EurekaItemModels extends ItemModelProvider {

    public EurekaItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EurekaArcana.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        //withExistingParent(EurekaItemRegistry.EUREKA_JOURNAL.getId().getPath(), mcLoc("item/generated"));
    	basicItem(EurekaItemRegistry.EUREKA_JOURNAL.get())
    		.texture("layer0",EurekaArcana.MODID + ":" + "item/eureka_journal_strap")
    		.texture("layer1",EurekaArcana.MODID + ":" + "item/eureka_journal");
    	basicItem(EurekaItemRegistry.BOOK_STRAP.get())
    		.texture("layer1",EurekaArcana.MODID + ":" + "item/book_strap_buckle");
    	basicItem(EurekaItemRegistry.BROKEN_MECHANISM.get());
    }
}