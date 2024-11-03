package com.github.ternityclockworks.eurekaarcana.server.misc;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EurekaSoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EurekaArcana.MODID);

    //public static final RegistryObject<SoundEvent> MECHANISM_DISASSEMBLING = createSoundEvent("mechanism_disassembling");
    
    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        return SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(EurekaArcana.asResource(soundName)));
    }
    
}