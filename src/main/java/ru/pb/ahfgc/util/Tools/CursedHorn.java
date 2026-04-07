package ru.pb.ahfgc.util.Tools;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;

public class CursedHorn extends Item {

    public CursedHorn(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack itemstack = player.getItemInHand(usedHand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (itemstack.isEmpty() || itemstack == null || level == null) {
            return InteractionResultHolder.fail(itemstack);
        }

        if (player.level().getBiome(player.blockPosition()).value().getBaseTemperature() * 100F <= 0) {
            double x = player.getX() + (level.random.nextDouble() - 0.5D) * 6.0D;
            double y = player.getY() + 12.0D;
            double z = player.getZ() + (level.random.nextDouble() - 0.5D) * 6.0D;

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GOAT_HORN_PLAY, SoundSource.NEUTRAL, 2.0F, 0.3F + level.random.nextFloat() * 0.2F);
            level.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT,
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

            itemstack.shrink(1);

//        if(player.level().getBiome(player.blockPosition()).value().getBaseTemperature() *100F <=0) {
//
//            CursedEyeEntity eye = new CursedEyeEntity(level);
//            eye.setPos(player.getX(), player.getY()+10, player.getZ());
//            eye.setNoGravity(true);
//            eye.setNoAi(true);
//            level.addFreshEntity(eye);
//
//            itemstack.shrink(1);
////            player.setItemInHand(usedHand, ModItems.CURSIUM_INGOT.get().getDefaultInstance());
//
//            getUseAnimation(itemstack);
//            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_DEATH, SoundSource.NEUTRAL, 5.0F, 0.2F);

            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.TOOT_HORN;
    }
}
