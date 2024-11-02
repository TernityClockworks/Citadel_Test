package com.github.ternityclockworks.eurekaarcana.server.misc;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EurekaCreativeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EurekaArcana.MODID);

    public static final RegistryObject<CreativeModeTab> EUREKA_FOUNDATIONS = CREATIVE_TABS.register("eureka_arcana", () -> CreativeModeTab.builder()
            .title(Component.translatable("Eureka! Arcana"))
            .icon(() -> new ItemStack(EurekaItemRegistry.EUREKA_JOURNAL.get()))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .displayItems((enabledFeatures, output) -> {
                add(output, EurekaItemRegistry.EUREKA_JOURNAL.get());
                add(output, EurekaItemRegistry.BROKEN_MECHANISM.get());
            })
            .build());

    private static void add(CreativeModeTab.Output tab, ItemLike itemLike) {
       // if (itemLike instanceof CustomTabBehavior customTabBehavior) {
       //     customTabBehavior.fillItemCategory(tab);
       // } else {
            tab.accept(itemLike);
       // }
    }
}