package com.github.ternityclockworks.eurekaarcana;

import com.github.ternityclockworks.eurekaarcana.client.ClientProxy;
import com.github.ternityclockworks.eurekaarcana.client.config.EurekaClientConfig;
import com.github.ternityclockworks.eurekaarcana.server.CommonProxy;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import com.github.ternityclockworks.eurekaarcana.server.misc.*;
import com.github.ternityclockworks.eurekaarcana.datagen.DataGeneration;
import com.mojang.logging.LogUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mcjty.theoneprobe.ForgeEventHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EurekaArcana.MODID)
public class EurekaArcana
{
    public static final String MODID = "eurekaarcana";
    public static final Logger LOGGER = LogUtils.getLogger();
	public static final String NAME = "Eureka! Arcana";
	public static final String VERSION = "0.0.1-prealpha";

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
		.disableHtmlEscaping()
		.create();
    
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final ResourceLocation PACKET_NETWORK_NAME = new ResourceLocation("eurekaarcana:main_channel");
    public static final SimpleChannel NETWORK_WRAPPER = NetworkRegistry.ChannelBuilder
            .named(PACKET_NETWORK_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
//    public static final EurekaServerConfig COMMON_CONFIG;
//    private static final ForgeConfigSpec COMMON_CONFIG_SPEC;
//    public static final ACClientConfig CLIENT_CONFIG;
//    private static final ForgeConfigSpec CLIENT_CONFIG_SPEC;
//    public static final List<String> MOD_GENERATION_CONFLICTS = new ArrayList<>();

    public EurekaArcana()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG_SPEC, "alexscaves-general.toml");
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG_SPEC, "alexscaves-client.toml");
        modEventBus.addListener(this::commonSetup);
        //modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        //MinecraftForge.EVENT_BUS.register(new CommonEvents());
        EurekaItemRegistry.ITEMS.register(modEventBus);
        EurekaCreativeTabRegistry.CREATIVE_TABS.register(modEventBus);
        
        modEventBus.addListener(DataGeneration::generate);
        
        //PROXY.commonInit();
        
        //MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers()); // TOP
        
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        PROXY.initPathfinding();
        int packetsRegistered = 0;
        event.enqueueWork(() -> {
            EurekaItemRegistry.setup();
        });
        //readModIncompatibilities();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> PROXY.clientInit());
    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }
    
    public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MODID, path);
	}
}
