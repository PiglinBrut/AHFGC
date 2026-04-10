package ru.pb.ahfgc.util.itemtools;

import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.NoteBlockEvent;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;
import ru.pb.ahfgc.registry.SoundRegistry;

import java.util.function.Function;

public class CursedHorn extends Item {

    private static final int REQUIRED_TICKS = 200;
    private static final int COOLDOWN_TICKS = 200;

    public CursedHorn(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof Player player) {
            int usedTicks = this.getUseDuration(stack, livingEntity) - remainingUseDuration;

            if (usedTicks % 50 == 0) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundRegistry.CURSED_HORN_PLAY, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            if (level.isClientSide) {
                if (usedTicks % 5 == 0) {
                    spawnNoteParticlesInFrontOfPlayer(level, player);
                }
            }
        }
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player) {
            float temperature = entityLiving.level().getBiome(entityLiving.blockPosition()).value().getBaseTemperature();

            if (temperature * 100F <= 0) {
                summonCursedEye(level, entityLiving);
                entityLiving.stopUsingItem();
                stack.shrink(1);

                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            } else {
                entityLiving.stopUsingItem();
                level.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(),
                        SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 1.0F, 0.5F);

                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            }
        }
        return stack;
    }

    private void summonCursedEye(Level level, LivingEntity entityLiving) {
        double x = entityLiving.getX() + (level.random.nextDouble() - 0.5D) * 6.0D;
        double y = entityLiving.getY() + 12.0D;
        double z = entityLiving.getZ() + (level.random.nextDouble() - 0.5D) * 6.0D;

        level.playSound(null, x, y, z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.HOSTILE, 1.2F, 0.4F);

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 80; i++) {
                double px = x + (level.random.nextDouble() - 0.5D) * 3.0D;
                double py = y + level.random.nextDouble() * 2.0D;
                double pz = z + (level.random.nextDouble() - 0.5D) * 3.0D;
                serverLevel.sendParticles(ParticleTypes.CLOUD, px, py, pz, 1, 0, -0.05D, 0, 0.01D);
            }

            for (int i = 0; i < 50; i++) {
                double px = x + (level.random.nextDouble() - 0.5D) * 1.5D;
                double py = y + level.random.nextDouble() * 1.0D;
                double pz = z + (level.random.nextDouble() - 0.5D) * 1.5D;
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, px, py, pz, 1, 0, 0.02D, 0, 0.005D);
            }

            for (int i = 0; i < 20; i++) {
                serverLevel.sendParticles(ParticleTypes.POOF, x, y - 0.5D, z, 1,
                        (level.random.nextDouble() - 0.5D) * 0.6D, 0, (level.random.nextDouble() - 0.5D) * 0.6D, 0.1D);
                serverLevel.sendParticles(ParticleTypes.SMOKE, x, y - 0.5D, z, 1, 0, 0.02D, 0, 0.02D);
            }
        }

        CursedEyeEntity eye = new CursedEyeEntity(level);
        eye.setPos(x, y, z);
        eye.setNoGravity(true);
        level.addFreshEntity(eye);
    }

    private void spawnNoteParticlesInFrontOfPlayer(Level level, Player player) {
        Vec3 lookAngle = player.getLookAngle();

        double distance = 0.5;

        double x = player.getX() + lookAngle.x * distance;
        double y = player.getY() + player.getEyeHeight() - 0.2; // На уровне глаз, но чуть ниже
        double z = player.getZ() + lookAngle.z * distance;

        double offsetX = (level.random.nextDouble() - 0.5D) * 0.5D;
        double offsetY = (level.random.nextDouble() - 0.5D) * 0.3D;
        double offsetZ = (level.random.nextDouble() - 0.5D) * 0.5D;

        level.addParticle(ModParticle.CURSED_FLAME.get(),
                x + offsetX, y + offsetY, z + offsetZ,
                0, 0.1D, 0);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return REQUIRED_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.TOOT_HORN;
    }
}