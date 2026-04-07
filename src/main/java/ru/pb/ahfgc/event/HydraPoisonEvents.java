package ru.pb.ahfgc.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.registry.ItemRegistry;

@EventBusSubscriber(modid = AHFGCMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class HydraPoisonEvents {

    @SubscribeEvent
    public static Object getHydraPoison(PlayerInteractEvent.EntityInteract event) {

        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        Level level = event.getLevel();
        ItemStack itemInHand = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(hand);
        }

        if (itemInHand.isEmpty() || itemInHand == null || level == null) {
            return InteractionResultHolder.fail(hand);
        }

        if (!(event.getTarget() instanceof Cow)) {
            return null;
        }

        if (itemInHand.getItem() == Items.GLASS_BOTTLE) {

            event.setCanceled(true);

            itemInHand.shrink(1);
            ItemStack hydraPoison = new ItemStack(ItemRegistry.HYDRA_POISON.get());

            if (player.getInventory().getFreeSlot() != -1) {
                if (itemInHand.isEmpty()) {
                    player.setItemInHand(hand, hydraPoison);
                } else {
                    if (!player.getInventory().add(hydraPoison)) {
                        player.drop(hydraPoison, false);
                    }
                }
            } else {
                player.drop(hydraPoison, false);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            player.swing(hand);

        }
        return null;
    }
}
