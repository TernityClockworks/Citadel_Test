package com.github.ternityclockworks.eurekaarcana.server.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Huge props to zer0_the_wolf and their Layered Armors mod for the clearest example of item tinting using compound tags akin to vanilla leather armor.
 * I suspect this is based on how forge converts these compound tags into item tints but I could not find where that occurs.
 * See <a href="https://github.com/DanielBernardy/LayeredArmorsmod-1.18.2/blob/master/src/main/java/com/zer0_the_wolf/layeredarmor/item/custom/ColorHandlers.java"> Layered Armor ColorHandlers</a>
 */
public class EurekaItemColorRegistry {
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void register(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register(EurekaItemColorRegistry::getColor,
                EurekaItemRegistry.BOOK_STRAP.get(),
                EurekaItemRegistry.EUREKA_JOURNAL.get()
        );
    }
	
    private static int getColor(ItemStack stack, int tintIndex) {
        CompoundTag typeTag = stack.getTagElement("type");
        if (typeTag != null) {
        	typeTag.putString("type","eurekaarcana:dyeable");
        }
        CompoundTag displayTag = stack.getTagElement("display");
        if (tintIndex == 0) { // Apply tint only to first layer
            return displayTag != null && displayTag.contains("color", 99) ? displayTag.getInt("color") : 10511680;
        }
        return 0xFFFFFF; // Other layers are untinted

    }
}
