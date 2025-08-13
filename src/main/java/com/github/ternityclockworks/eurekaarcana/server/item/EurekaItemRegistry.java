package com.github.ternityclockworks.eurekaarcana.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;

public class EurekaItemRegistry {
    //public static final EurekaArmorMaterial EXAMPLE_ARMOR_MATERIAL = new EurekaArmorMaterial("example", 20, new int[]{2, 3, 2, 2}, 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0F);
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EurekaArcana.MODID);
    //public static final RegistryObject<Item> ADVANCEMENT_TAB_ICON = ITEMS.register("advancement_tab_icon", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    //public static final RegistryObject<Item> EUREKA_JOURNAL = ITEMS.register("eureka_journal", () -> new Item(new Item.Properties().rarity(EXAMPLE_RARITY).stacksTo(1)));
    public static final RegistryObject<Item> EUREKA_JOURNAL = ITEMS.register("eureka_journal", () -> new EurekaJournalItem());
    public static final RegistryObject<Item> BROKEN_MECHANISM = ITEMS.register("broken_mechanism", () -> new BrokenMechanismItem());
    public static final RegistryObject<Item> BOOK_STRAP = ITEMS.register("book_strap", () -> new BookStrapItem());

    //static {
    //    spawnEgg("example", EurekaEntityRegistry.EXAMPLE, 0X433B4A, 0X0060EF);
    //}

    //private static void spawnEgg(String entityName, RegistryObject type, int color1, int color2) {
       // RegistryObject<Item> item = ITEMS.register("spawn_egg_" + entityName, () -> new ForgeSpawnEggItem(type, color1, color2, new Item.Properties()));
    //}

    public static void setup() {
        // Set armor materials & dispenser behavior
    	//EXAMPLE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(LEATHER.get()));
        
    }

//    public static Item getSpawnEggFor(EntityType type) {
//        for (Map.Entry<RegistryObject<Item>, ResourceKey<Biome>> entry : creativeTabSpawnEggMap.entrySet()) {
//            if (entry.getKey().get() instanceof ForgeSpawnEggItem forgeSpawnEggItem && forgeSpawnEggItem.getType(null) == type) {
//                return forgeSpawnEggItem;
//            }
//        }
//        return Items.AIR;
//    }
}