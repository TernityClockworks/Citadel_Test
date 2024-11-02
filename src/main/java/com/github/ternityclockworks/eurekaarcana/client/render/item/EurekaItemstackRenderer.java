package com.github.ternityclockworks.eurekaarcana.client.render.item;

//import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
//import com.github.ternityclockworks.eurekaarcana.client.gui.book.widget.ItemWidget;
//import com.github.ternityclockworks.eurekaarcana.client.model.*;
//import com.github.ternityclockworks.eurekaarcana.client.render.EurekaRenderTypes;
//import com.github.ternityclockworks.eurekaarcana.server.block.EurekaBlockRegistry;
//import com.github.ternityclockworks.eurekaarcana.server.enchantment.EurekaEnchantmentRegistry;
//import com.github.ternityclockworks.eurekaarcana.server.item.*;

import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.client.ForgeRenderTypes;

public class EurekaItemstackRenderer extends BlockEntityWithoutLevelRenderer {
    //private static final ResourceLocation MOD_JOURNAL_TEXTURE = new ResourceLocation("eureka:foundations/textures/item/journal.png");

    public EurekaItemstackRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ClientLevel level = Minecraft.getInstance().level;
        float partialTick = Minecraft.getInstance().getPartialTick();
        boolean heldIn3d = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        boolean left = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        
        // Item-Specific Model Rendering
    }

    private void renderStaticItemSprite(ItemStack spriteItem, ItemDisplayContext transformType, int combinedLightIn, int combinedOverlayIn, PoseStack poseStack, MultiBufferSource bufferIn, ClientLevel level) {
        Minecraft.getInstance().getItemRenderer().renderStatic(spriteItem, transformType, transformType == ItemDisplayContext.GROUND ? combinedLightIn : 240, combinedOverlayIn, poseStack, bufferIn, level, 0);
    }

    private void renderStaticItemSpriteWithLighting(ItemStack spriteItem, ItemDisplayContext transformType, int combinedLightIn, int combinedOverlayIn, PoseStack poseStack, MultiBufferSource bufferIn, ClientLevel level) {
        Minecraft.getInstance().getItemRenderer().renderStatic(spriteItem, transformType, transformType != ItemDisplayContext.GUI ? combinedLightIn : 240, combinedOverlayIn, poseStack, bufferIn, level, 0);
    }
}