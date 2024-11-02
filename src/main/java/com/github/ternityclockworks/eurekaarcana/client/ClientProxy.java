package com.github.ternityclockworks.eurekaarcana.client;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.client.event.ClientEvents;
import com.github.ternityclockworks.eurekaarcana.client.render.item.EurekaItemRenderProperties;
import com.github.ternityclockworks.eurekaarcana.server.CommonProxy;
import com.github.ternityclockworks.eurekaarcana.server.item.*;

//import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
//import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
//import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class ClientProxy extends CommonProxy {

    //private static final List<String> FULLBRIGHTS = ImmutableList.of("alexscaves:ambersol#", "alexscaves:radrock_uranium_ore#", "alexscaves:acidic_radrock#", "alexscaves:uranium_rod#axis=x", "alexscaves:uranium_rod#axis=y", "alexscaves:uranium_rod#axis=z", "alexscaves:block_of_uranium#", "alexscaves:abyssal_altar#active=true", "alexscaves:abyssmarine_", "alexscaves:peering_coprolith#", "alexscaves:forsaken_idol#", "alexscaves:magnetic_light#", "alexscaves:tremorzilla_egg#");
    //public static final ResourceLocation BOMB_FLASH = new ResourceLocation(AlexsCaves.MODID, "textures/misc/bomb_flash.png");
    public static final RandomSource random = RandomSource.create();
    public static List<UUID> blockedEntityRenders = new ArrayList<>();
    public static Map<ClientLevel, List<BlockPos>> blockedParticleLocations = new HashMap<>();
    public static final Int2ObjectMap<AbstractTickableSoundInstance> ENTITY_SOUND_INSTANCE_MAP = new Int2ObjectOpenHashMap<>();
    public static final Map<BlockEntity, AbstractTickableSoundInstance> BLOCK_ENTITY_SOUND_INSTANCE_MAP = new HashMap<>();
    private final EurekaItemRenderProperties isterProperties = new EurekaItemRenderProperties();
    //private final EurekaArmorRenderProperties armorProperties = new EurekaArmorRenderProperties();
    public static CameraType lastPOV = CameraType.FIRST_PERSON;
    public static int shaderLoadAttemptCooldown = 0;
    public static Map<UUID, Integer> bossBarRenderTypes = new HashMap<>();
    private static Entity lastCameraEntity;
    public static float acSkyOverrideAmount;
    public static Vec3 acSkyOverrideColor = Vec3.ZERO;
    public static boolean disabledBiomeAmbientLightByOtherMod = false;

    public void commonInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setupParticles);
        bus.addListener(this::registerKeybinds);
        bus.addListener(this::onItemColors);
        bus.addListener(this::onRegisterTooltips);
    }

    public void clientInit() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        //IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //bus.addListener(ClientLayerRegistry::addLayers);
        //bus.addListener(this::bakeModels);
        //bus.addListener(this::registerShaders);
        blockedParticleLocations.clear();
        //PostEffectRegistry.registerEffect(IRRADIATED_SHADER);
        //MenuScreens.register(ACMenuRegistry.SPELUNKERY_TABLE_MENU.get(), SpelunkeryTableScreen::new);
        //ItemBlockRenderTypes.setRenderLayer(ACFluidRegistry.ACID_FLUID_SOURCE.get(), RenderType.cutoutMipped());
    }

    public void setupParticles(RegisterParticleProvidersEvent registry) {
        //EurekaArcana.LOGGER.debug("Registered particle factories");
        //registry.registerSpecial(ACParticleRegistry.SCARLET_MAGNETIC_ORBIT.get(), new MagneticOrbitParticle.ScarletFactory());
    }

    public void onItemColors(RegisterColorHandlersEvent.Item event) {
        //EurekaArcana.LOGGER.info("loaded in item colorizer");
        //event.register((stack, colorIn) -> colorIn != 1 ? -1 : CaveInfoItem.getBiomeColorOf(Minecraft.getInstance().level, stack, false), ACItemRegistry.CAVE_TABLET.get());
    }

    private void onRegisterTooltips(RegisterClientTooltipComponentFactoriesEvent registry) {
        //registry.register(SackOfSatingTooltip.class, ClientSackOfSatingTooltip::new);
    }

    /*
    private void bakeModels(final ModelEvent.ModifyBakingResult e) {
        if (EurekaArcana.CLIENT_CONFIG.emissiveBlockModels.get()) {
            long time = System.currentTimeMillis();
            for (ResourceLocation id : e.getModels().keySet()) {
                if (FULLBRIGHTS.stream().anyMatch(str -> id.toString().startsWith(str))) {
                    e.getModels().put(id, new BakedModelShadeLayerFullbright(e.getModels().get(id)));
                }
            }
            AlexsCaves.LOGGER.info("Loaded emissive block models in {} ms", System.currentTimeMillis() - time);

        }
    }
    */

    /*
    private void registerShaders(final RegisterShadersEvent e) {
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(), new ResourceLocation(AlexsCaves.MODID, "rendertype_ferrouslime_gel"), DefaultVertexFormat.NEW_ENTITY), ACInternalShaders::setRenderTypeFerrouslimeGelShader);
            AlexsCaves.LOGGER.info("registered internal shaders");
        } catch (IOException exception) {
            AlexsCaves.LOGGER.error("could not register internal shaders");
            exception.printStackTrace();
        }
    }
    */

    private void registerKeybinds(RegisterKeyMappingsEvent e) {
        //e.register(EurekaKeybindRegistry.KEY_SPECIAL_ABILITY);
    }

    public Player getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }

    public void blockRenderingEntity(UUID id) {
        blockedEntityRenders.add(id);
    }

    public void releaseRenderingEntity(UUID id) {
        blockedEntityRenders.remove(id);
    }

    public void setVisualFlag(int flag) {
    }
    
    public boolean checkIfParticleAt(SimpleParticleType simpleParticleType, BlockPos at) {
        if (!blockedParticleLocations.containsKey(Minecraft.getInstance().level)) {
            blockedParticleLocations.clear();
            blockedParticleLocations.put(Minecraft.getInstance().level, new ArrayList<>());
        }
        List blocked = blockedParticleLocations.get(Minecraft.getInstance().level);
        if (blocked.contains(at)) {
            return false;
        } else {
            blocked.add(new BlockPos(at));
            return true;
        }
    }

    public void removeParticleAt(BlockPos at) {
        if (!blockedParticleLocations.containsKey(Minecraft.getInstance().level)) {
            blockedParticleLocations.clear();
            blockedParticleLocations.put(Minecraft.getInstance().level, new ArrayList<>());
        }
        blockedParticleLocations.get(Minecraft.getInstance().level).remove(at);
    }


    public boolean isKeyDown(int keyType) {
        if (keyType == -1) {
            return Minecraft.getInstance().options.keyLeft.isDown() || Minecraft.getInstance().options.keyRight.isDown() || Minecraft.getInstance().options.keyUp.isDown() || Minecraft.getInstance().options.keyDown.isDown() || Minecraft.getInstance().options.keyJump.isDown();
        }
        if (keyType == 0) {
            return Minecraft.getInstance().options.keyJump.isDown();
        }
        if (keyType == 1) {
            return Minecraft.getInstance().options.keySprint.isDown();
        }
        if (keyType == 2) {
            //return EurekaKeybindRegistry.KEY_SPECIAL_ABILITY.isDown();
        }
        if (keyType == 3) {
            return Minecraft.getInstance().options.keyAttack.isDown();
        }
        if (keyType == 4) {
            return Minecraft.getInstance().options.keyShift.isDown();
        }
        return false;
    }

    /*
    @Override
    public Object getISTERProperties() {
        return isterProperties;
    }

    @Override
    public Object getArmorProperties() {
        return armorProperties;
    }
    */

    public float getPartialTicks() {
        return Minecraft.getInstance().getPartialTick();
    }

    public void setRenderViewEntity(Player player, Entity entity) {
        if (player == Minecraft.getInstance().player && Minecraft.getInstance().getCameraEntity() == Minecraft.getInstance().player) {
            lastPOV = Minecraft.getInstance().options.getCameraType();
            Minecraft.getInstance().setCameraEntity(entity);
            Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
        }
        if (lastCameraEntity != Minecraft.getInstance().getCameraEntity()) {
            Minecraft.getInstance().levelRenderer.allChanged();
            lastCameraEntity = Minecraft.getInstance().getCameraEntity();
        }
    }

    public void resetRenderViewEntity(Player player) {
        if (player == Minecraft.getInstance().player) {
            Minecraft.getInstance().level = (ClientLevel) Minecraft.getInstance().player.level();
            Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
            Minecraft.getInstance().options.setCameraType(lastPOV);
        }
        if (lastCameraEntity != Minecraft.getInstance().getCameraEntity()) {
            Minecraft.getInstance().levelRenderer.allChanged();
            lastCameraEntity = Minecraft.getInstance().getCameraEntity();
        }
    }

    public void clearSoundCacheFor(Entity entity) {
        ENTITY_SOUND_INSTANCE_MAP.remove(entity.getId());
    }

    public void clearSoundCacheFor(BlockEntity entity) {
        BLOCK_ENTITY_SOUND_INSTANCE_MAP.remove(entity);
    }

    public int getPlayerTime() {
        return Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount;
    }

    public boolean isFirstPersonPlayer(Entity entity) {
        return entity.equals(Minecraft.getInstance().cameraEntity) && Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }


    public void openBookGUI(ItemStack itemStackIn) {
        //Minecraft.getInstance().setScreen(new CaveBookScreen());
    }

    /*
    @Override
    public Vec3 getCameraRotation() {
        return Vec3.ZERO;
    }
    */
    
    public void removeBossBarRender(UUID bossBar) {
        bossBarRenderTypes.remove(bossBar);
    }

    public void setBossBarRender(UUID bossBar, int renderType) {
        bossBarRenderTypes.put(bossBar, renderType);
    }

    /*
    public boolean isTickRateModificationActive(Level level){
        return ClientTickRateTracker.getForClient(Minecraft.getInstance()).getClientTickRate() != 50;
    }

    @Override
    public boolean isFarFromCamera(double x, double y, double z) {
        return Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceToSqr(x, y, z) >= 256.0D;
    }

    public void renderVanillaMapDecoration(MapDecoration mapDecoration, int index){
        ClientEvents.renderVanillaMapDecoration(mapDecoration, index + 1);
    }
    */
}