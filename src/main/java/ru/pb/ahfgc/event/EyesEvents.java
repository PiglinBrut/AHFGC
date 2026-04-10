package ru.pb.ahfgc.event;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.entity.custom.SunEyeEntity;

import java.util.Random;

@EventBusSubscriber(modid = AHFGCMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EyesEvents {

    private static final double HEIGHT_THRESHOLD = 319.0;
    private static final int SOLAR_END_TIME_MIN = 5950;
    private static final int SOLAR_END_TIME_MAX = 6050;
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onEnderEyeTossed(ItemTossEvent event) {
        Player player = event.getPlayer();
        ItemEntity itemEntity = event.getEntity();

        if (itemEntity.getItem().getItem() != Items.ENDER_EYE || itemEntity.getItem().getCount() != 1) {
            return;
        }

        boolean isHighEnough = player.getY() > HEIGHT_THRESHOLD;
        boolean hasSolVisage = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemHandler.SOL_VISAGE.get();
        boolean hasSunBlessing = player.hasEffect(EffectHandler.SUNS_BLESSING);
        long time = player.level().getDayTime();
        boolean isPerfectTime = time >= SOLAR_END_TIME_MIN && time <= SOLAR_END_TIME_MAX;

        if (!isHighEnough) {
            return;
        }

        if (hasSolVisage && hasSunBlessing && isPerfectTime) {
            startTransformation(itemEntity, player);
        } else {
            showHeightHint(player);
        }
    }

    private static void startTransformation(ItemEntity oldItem, Player player) {
        var level = oldItem.level();

        SunEyeEntity sunEye = new SunEyeEntity(level, oldItem.getX(), oldItem.getY(), oldItem.getZ());
        sunEye.setDeltaMovement(oldItem.getDeltaMovement().scale(0.3));

        level.addFreshEntity(sunEye);
        oldItem.discard();

        sunEye.startTransformation();

        player.removeEffect(EffectHandler.SUNS_BLESSING);
        player.displayClientMessage(
                Component.literal("§6§l✦ §eСолнечный глаз начинает сиять! §6§l✦")
                        .withStyle(ChatFormatting.BOLD), true);

    }

    private static void showHeightHint(Player player) {
        player.displayClientMessage(
                Component.literal("§6[§e!§6] §eСолнце не довольно! §6[§e!§6]")
                        .withStyle(ChatFormatting.GOLD), true);
    }
}