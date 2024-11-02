package com.github.ternityclockworks.eurekaarcana.client.event;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.client.ClientProxy;
import com.github.ternityclockworks.eurekaarcana.server.item.*;
//import com.github.ternityclockworks.eurekaarcana.server.level.biome.EurekaBiomeRegistry;
//import com.github.ternityclockworks.eurekaarcana.server.level.biome.BiomeSampler;
//import com.github.ternityclockworks.eurekaarcana.server.effect.EurekaEffectRegistry;
//import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
//import com.github.alexthe666.citadel.client.event.EventLivingRenderer;
//import com.github.alexthe666.citadel.client.event.EventPosePlayerHand;
//import com.github.alexthe666.citadel.client.event.EventRenderSplashText;
//import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
//import com.github.alexthe666.citadel.server.tick.TickRateTracker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.UUID;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class ClientEvents {

    //private static final ResourceLocation POTION_EFFECT_HUD_OVERLAYS = new ResourceLocation(AlexsCaves.MODID, "textures/misc/potion_effect_hud_overlays.png");
    //private static final ResourceLocation BOSS_BAR_HUD_OVERLAYS = new ResourceLocation(AlexsCaves.MODID, "textures/misc/boss_bar_hud_overlays.png");

    private static float lastSampledFogNearness = 0.0F;
    private static float lastSampledWaterFogFarness = 0.0F;
    private static Vec3 lastSampledFogColor = Vec3.ZERO;
    private static Vec3 lastSampledWaterFogColor = Vec3.ZERO;

    @SubscribeEvent
    public void clientLivingTick(LivingEvent.LivingTickEvent event) {
      LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide) {
            return;
        }
    }

    private void rotateForAngle(LivingEntity entity, PoseStack matrixStackIn, Direction rotate, float f, float width, float height) {
        boolean down = entity.zza < 0.0F;
        switch (rotate) {
            case DOWN:
                break;
            case UP:
                matrixStackIn.translate(0.0D, height * f, 0.0D);
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(-180.0F * f));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-180.0F * f));
                break;
            case NORTH:
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * f));
                matrixStackIn.translate(0.0D, -0.25f * f, 0.0D);
                if (down) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F * f));
                }
                break;
            case SOUTH:
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180 * f));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * f));
                matrixStackIn.translate(0.0D, -0.25f * f, 0.0D);
                if (down) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F * f));
                }
                break;
            case WEST:
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90 * f));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * f));
                matrixStackIn.translate(0.0D, -0.25f * f, 0.0D);
                if (down) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F * f));
                }
                break;
            case EAST:
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90 * f));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * f));
                matrixStackIn.translate(0.0D, -0.25f * f, 0.0D);
                if (down) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F * f));
                }
                break;
        }
    }

    /*
    private static float calculateBiomeAmbientLight(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        if (i == 0) {
            return EurekaBiomeRegistry.getBiomeAmbientLight(player.level().getBiome(player.blockPosition()));
        } else {
            return BiomeSampler.sampleBiomesFloat(player.level(), player.position(), EurekaBiomeRegistry::getBiomeAmbientLight);
        }
    }
    */

    /*
    private static Vec3 calculateBiomeLightColor(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        if (i == 0) {
            return ACBiomeRegistry.getBiomeLightColorOverride(player.level().getBiome(player.blockPosition()));
        } else {
            return BiomeSampler.sampleBiomesVec3(player.level(), player.position(), ACBiomeRegistry::getBiomeLightColorOverride);
        }
    }
    */

    /*
    private static float calculateBiomeFogNearness(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        float nearness;
        if (i == 0) {
            nearness = ACBiomeRegistry.getBiomeFogNearness(player.level().getBiome(player.blockPosition()));
        } else {
            nearness = BiomeSampler.sampleBiomesFloat(player.level(), player.position(), ACBiomeRegistry::getBiomeFogNearness);
        }
        return nearness;
    }
    */

    /*
    private static float calculateBiomeWaterFogFarness(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        float farness;
        if (i == 0) {
            farness = ACBiomeRegistry.getBiomeWaterFogFarness(player.level().getBiome(player.blockPosition()));
        } else {
            farness = BiomeSampler.sampleBiomesFloat(player.level(), player.position(), ACBiomeRegistry::getBiomeWaterFogFarness);
        }
        return farness;
    }

    private static Vec3 calculateBiomeFogColor(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        Vec3 vec3;
        if (i == 0) {
            vec3 = ((ClientLevel) player.level()).effects().getBrightnessDependentFogColor(Vec3.fromRGB24(player.level().getBiomeManager().getNoiseBiomeAtPosition(player.blockPosition()).value().getFogColor()), 1.0F);
        } else {
            vec3 = ((ClientLevel) player.level()).effects().getBrightnessDependentFogColor(BiomeSampler.sampleBiomesVec3(player.level(), player.position(), biomeHolder -> Vec3.fromRGB24(biomeHolder.value().getFogColor())), 1.0F);
        }
        return vec3;
    }

    private Vec3 calculateBiomeWaterFogColor(Entity player) {
        int i = Minecraft.getInstance().options.biomeBlendRadius().get();
        Vec3 vec3;
        if (i == 0) {
            vec3 = ((ClientLevel) player.level()).effects().getBrightnessDependentFogColor(Vec3.fromRGB24(player.level().getBiomeManager().getNoiseBiomeAtPosition(player.blockPosition()).value().getWaterFogColor()), 1.0F);
        } else {
            vec3 = ((ClientLevel) player.level()).effects().getBrightnessDependentFogColor(BiomeSampler.sampleBiomesVec3(player.level(), player.position(), biomeHolder -> Vec3.fromRGB24(biomeHolder.value().getWaterFogColor())), 1.0F);
        }
        return vec3;
    }
    */

    /*
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        
    }
    */

    /*
    @SubscribeEvent
    public void onComputeFOVModifier(ComputeFovModifierEvent event) {
        ItemStack itemstack = event.getPlayer().getUseItem();
        if (event.getPlayer().isUsingItem()) {
            if (itemstack.is(ACItemRegistry.DREADBOW.get())) {
                int i = event.getPlayer().getTicksUsingItem();
                float f1 = (float) i / 20.0F;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }
                event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.15F));
            }
        }
    }
    */

    /*
    @SubscribeEvent
    public void onSplashTextRender(EventRenderSplashText.Pre event) {
        if (ClientProxy.hasACSplashText) {
            event.setResult(Event.Result.ALLOW);
            event.setSplashText("30k downloads max");
            event.setSplashTextColor(0X00B6D5);
        }
    }
    */
}