package ru.pb.ahfgc;

import com.mojang.logging.LogUtils;
import com.teamremastered.endrem.registry.ERTabs;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import ru.pb.ahfgc.entity.custom.CursedEyeRenderer;
import ru.pb.ahfgc.entity.spells.dread_land_portal.DreadLandPortalRenderer;
import ru.pb.ahfgc.registry.*;

@Mod(AHFGCMod.MOD_ID)
public class AHFGCMod {
    public static final String MOD_ID = "ahfgc";
    private static final Logger LOGGER = LogUtils.getLogger();
    public AHFGCMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ItemRegistry.register(modEventBus);

        FluidRegistry.register(modEventBus);

        BlockRegistry.register(modEventBus);

        EntityRegistry.register(modEventBus);

        SpellRegistry.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == ERTabs.EYES_TAB) {
            event.accept(ItemRegistry.BURNING_BREW);
            event.accept(ItemRegistry.COLD_BREW);
            event.accept(ItemRegistry.STATIC_BREW);
            event.accept(ItemRegistry.HYDRA_POISON);
            event.accept(ItemRegistry.HYDRA_ESSENCE);
            event.accept(ItemRegistry.DRAGON_BREW);
            event.accept(ItemRegistry.CURSED_HORN);
            event.accept(ItemRegistry.ESSENCE_OF_THE_SEA);
        }
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
        }
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ItemRegistry.BURNING_BREW);
            event.accept(ItemRegistry.COLD_BREW);
            event.accept(ItemRegistry.STATIC_BREW);
            event.accept(ItemRegistry.HYDRA_POISON);
            event.accept(ItemRegistry.HYDRA_ESSENCE);
            event.accept(ItemRegistry.DRAGON_BREW);
        }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ItemRegistry.CURSED_HORN);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ItemRegistry.ESSENCE_OF_THE_SEA);
        }
        if(event.getTabKey() == CreativeModeTabs.COMBAT) {
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ServerStarting");
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EntityRegistry.DREAD_LAND_PORTAL.get(), DreadLandPortalRenderer::new);
            EntityRenderers.register(EntityRegistry.CURSED_EYE.get(), CursedEyeRenderer::new);
            EntityRenderers.register(EntityRegistry.SUN_EYE_ITEM_ENTITY.get(), ItemEntityRenderer::new);
        }

//        @SubscribeEvent
//        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
//        }
    }
    @EventBusSubscriber(
            modid = "ahfgc",
            bus = EventBusSubscriber.Bus.MOD,
            value = {Dist.CLIENT}
    )
    public class ClientSetup {
        public ClientSetup() {
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "block/blood"), -14540254), new Holder[]{FluidRegistry.DRAGON_BREW_TYPE});
            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still"), -15580416), new Holder[]{FluidRegistry.HYDRA_POISON_TYPE});
            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still"), -15779772), new Holder[]{FluidRegistry.HYDRA_ESSENCE_TYPE});
        }
    }
}
