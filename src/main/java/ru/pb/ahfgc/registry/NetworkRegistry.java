package ru.pb.ahfgc.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.util.network.OpenSkillTreeScreenPacket;

@EventBusSubscriber(modid = AHFGCMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkRegistry {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");
        registrar.playToClient(
                OpenSkillTreeScreenPacket.TYPE,
                OpenSkillTreeScreenPacket.STREAM_CODEC,
                OpenSkillTreeScreenPacket::handle
        );
    }
}
