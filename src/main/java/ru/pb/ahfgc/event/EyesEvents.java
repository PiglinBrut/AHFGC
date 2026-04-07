package ru.pb.ahfgc.event;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.entity.custom.SunEyeItemEntity;

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

        if (itemEntity.getItem().getItem() != Items.ENDER_EYE ||
                itemEntity.getItem().getCount() != 1) {
            return;
        }

        boolean isHighEnough = player.getY() > HEIGHT_THRESHOLD;
        boolean hasSolVisage = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemHandler.SOL_VISAGE.get();
        boolean hasSunBlessing = player.hasEffect(EffectHandler.SUNS_BLESSING);
        long currentTime = player.level().getDayTime();
        boolean isPerfectSolarTime = currentTime >= SOLAR_END_TIME_MIN && currentTime <= SOLAR_END_TIME_MAX;

        if (!isHighEnough) {
            return;
        }

        if (hasSolVisage && hasSunBlessing && isPerfectSolarTime) {
            startSunEyeTransformation(itemEntity, player);
        } else {
            showHeightHint(player);
        }
    }

    private static void startSunEyeTransformation(ItemEntity oldItem, Player player) {
        var level = oldItem.level();

        SunEyeItemEntity sunEyeEntity = new SunEyeItemEntity(
                level,
                oldItem.getX(),
                oldItem.getY(),
                oldItem.getZ()
        );

        // Сохраняем небольшую инерцию от броска
        sunEyeEntity.setDeltaMovement(oldItem.getDeltaMovement().scale(0.2));

        // Добавляем сущность в мир и удаляем старую
        level.addFreshEntity(sunEyeEntity);
        oldItem.discard();

        // Запускаем анимацию трансформации
        sunEyeEntity.startTransformation();

        player.removeEffect(EffectHandler.SUNS_BLESSING);

        player.displayClientMessage(
                Component.literal("§6§l✦ §eСолнечный глаз начинает сиять! §6§l✦")
                        .withStyle(ChatFormatting.BOLD), true);

    }

    private static void showHeightHint(Player player) {
        player.displayClientMessage(
                Component.literal("§6[§e!§6] §eСолнце не довольно! §6[§e!§6]")
                        .withStyle(ChatFormatting.GOLD),
                true
        );

        player.playSound(SoundEvents.FIRE_EXTINGUISH);

        for (int i = 0; i < 5; i++) {
            player.level().addParticle(
                    ParticleTypes.SMOKE,
                    player.getX() + (RANDOM.nextDouble() - 0.5) * 0.6,
                    player.getY() + 1.0 + RANDOM.nextDouble() * 0.5,
                    player.getZ() + (RANDOM.nextDouble() - 0.5) * 0.6,
                    0, 0.05, 0
            );
        }
    }
}