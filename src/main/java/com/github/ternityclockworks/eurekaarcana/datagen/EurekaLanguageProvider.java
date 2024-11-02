package com.github.ternityclockworks.eurekaarcana.datagen;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class EurekaLanguageProvider extends LanguageProvider {

    public EurekaLanguageProvider(PackOutput output, String locale) {
        super(output, EurekaArcana.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(EurekaItemRegistry.EUREKA_JOURNAL.get(), "Eureka! Mod Journal");
        add(EurekaItemRegistry.BROKEN_MECHANISM.get(), "Broken Mechanism");
    }
}