package ru.pb.ahfgc;

import com.mojang.logging.LogUtils;
import com.teamremastered.endrem.registry.ERTabs;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import ru.pb.ahfgc.client.ModelLayers;
import ru.pb.ahfgc.client.model.LibraryDoorModel;
import ru.pb.ahfgc.client.render.CursedEyeRenderer;
import ru.pb.ahfgc.client.render.LibraryDoorRenderer;
import ru.pb.ahfgc.client.render.SunEyeRenderer;
import ru.pb.ahfgc.registry.*;

@Mod(AHFGCMod.MOD_ID)
public class AHFGCMod {
    public static final String MOD_ID = "ahfgc";
    private static final Logger LOGGER = LogUtils.getLogger();
    public AHFGCMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        BlockRegistry.register(modEventBus);

        ItemRegistry.register(modEventBus);

        FluidRegistry.register(modEventBus);

        EntityRegistry.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void insertAfterItem(BuildCreativeModeTabContentsEvent event, Object existingEntry, Item newEntry) {
        if (existingEntry instanceof String) {
            event.insertAfter(
                    new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse((String) existingEntry))),
                    newEntry.getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        } else if (existingEntry instanceof ItemLike) {
            event.insertAfter(
                    new ItemStack((ItemLike) existingEntry),
                    newEntry.getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

        if(event.getTab() == ERTabs.EYES_TAB.get()) {
            this.insertAfterItem(event,"endrem:dragon_eye", ItemRegistry.BURNING_BREW.get());
            this.insertAfterItem(event,"endrem:dragon_eye", ItemRegistry.COLD_BREW.get());
            this.insertAfterItem(event,"endrem:dragon_eye", ItemRegistry.STATIC_BREW.get());
            this.insertAfterItem(event,"endrem:dragon_eye", ItemRegistry.HYDRA_POISON.get());
            this.insertAfterItem(event,"endrem:dragon_eye", ItemRegistry.HYDRA_ESSENCE.get());
            this.insertAfterItem(event,"endrem:dragon_eye", ItemRegistry.DRAGON_BREW.get());
            this.insertAfterItem(event,"endrem:cursed_eye", ItemRegistry.CURSED_HORN.get());
            this.insertAfterItem(event,"endrem:exotic_eye", ItemRegistry.ESSENCE_OF_THE_SEA.get());
        }
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
        }
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            this.insertAfterItem(event,Items.RABBIT_STEW, ItemRegistry.BURNING_BREW.get());
            this.insertAfterItem(event,Items.RABBIT_STEW, ItemRegistry.COLD_BREW.get());
            this.insertAfterItem(event,Items.RABBIT_STEW, ItemRegistry.STATIC_BREW.get());
            this.insertAfterItem(event,Items.RABBIT_STEW, ItemRegistry.DRAGON_BREW.get());
            this.insertAfterItem(event,Items.HONEY_BOTTLE, ItemRegistry.HYDRA_POISON.get());
            this.insertAfterItem(event,Items.HONEY_BOTTLE, ItemRegistry.HYDRA_ESSENCE.get());
        }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            this.insertAfterItem(event,Items.TNT_MINECART, ItemRegistry.CURSED_HORN.get());
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            this.insertAfterItem(event,Items.HEART_OF_THE_SEA, ItemRegistry.ESSENCE_OF_THE_SEA.get());
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
            event.enqueueWork(() -> {
                EntityRenderers.register(EntityRegistry.CURSED_EYE.get(), CursedEyeRenderer::new);
                EntityRenderers.register(EntityRegistry.SUN_EYE.get(), SunEyeRenderer::new);
            });
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "block/blood"), -14540254), new Holder[]{FluidRegistry.DRAGON_BREW_TYPE});
            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still"), -15580416), new Holder[]{FluidRegistry.HYDRA_POISON_TYPE});
            event.registerFluidType(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still"), -15779772), new Holder[]{FluidRegistry.HYDRA_ESSENCE_TYPE});
        }
    }
}
