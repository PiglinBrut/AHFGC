package ru.pb.ahfgc.event;

import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;
import ru.pb.ahfgc.registry.EntityRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = AHFGCMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
//        event.registerLayerDefinition(TestModel.LAYER_LOCATION, TestModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.CURSED_EYE.get(), CursedEyeEntity.createAttributes().build());
    }
}
