package ru.pb.ahfgc.entity.custom;

import com.github.L_Ender.cataclysm.init.ModParticle;
import com.teamremastered.endrem.registry.CommonItemRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.pb.ahfgc.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class CursedEyeEntity extends Entity implements GeoEntity {

//    private static final int SPAWN_DURATION = 80;
//    private static final int IDLE_DURATION = 408;
//    private static final int DESPAWN_DURATION = 163;
    private static final int TOTAL_TICKS = 652;
    private static final int ITEM_SPAWN_TICK = 480;
    private static final Random RANDOM = new Random();

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean hasSpawnedItem = false;

    public CursedEyeEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public CursedEyeEntity(Level level) {
        this(EntityRegistry.CURSED_EYE.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // Нет синхронизируемых данных
    }

    @Override
    public void tick() {
        super.tick();

        spawnAmbientParticles();

        if (tickCount == ITEM_SPAWN_TICK && !hasSpawnedItem) {
            spawnCursedEyeItem();
            hasSpawnedItem = true;
        }

        if (tickCount > TOTAL_TICKS) {
            this.discard();
        }
    }

    private void spawnParticle(ParticleOptions particle, double x, double y, double z, double vx, double vy, double vz) {
        if (level() instanceof ServerLevel server) {
            server.sendParticles(particle, x, y, z, 1, vx, vy, vz, 0.0f);
        }
    }

    private void spawnAmbientParticles() {
        int particleCount = 8 + RANDOM.nextInt(8);
        float radius = 0.75f + RANDOM.nextFloat() * 2.0f;

        for (int i = 0; i < particleCount; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double x = getX() + Math.cos(angle) * radius;
            double z = getZ() + Math.sin(angle) * radius;
            double y = getY() + 0.6 + RANDOM.nextDouble() * 0.8;

            double vx = Math.cos(angle) * 0.02;
            double vz = Math.sin(angle) * 0.02;

            spawnParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, vx, 0.01, vz);
        }
    }

    private void spawnCursedEyeItem() {
        ItemStack stack = new ItemStack(CommonItemRegistry.CURSED_EYE);
        ItemEntity eye = new ItemEntity(level(), getX(), getY(), getZ(), stack);
        eye.setDefaultPickUpDelay();
        eye.setGlowingTag(true);
        eye.setNoGravity(true);
        eye.setDeltaMovement(0, -0.15, 0);
        level().addFreshEntity(eye);

        level().playSound(null, blockPosition(), SoundEvents.ENDER_EYE_DEATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source == damageSources().genericKill() && super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("HasSpawnedItem", hasSpawnedItem);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.hasSpawnedItem = tag.getBoolean("HasSpawnedItem");
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void checkDespawn() {}

    // GeoEntity методы
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animation", 0, this::animationPredicate));
    }

    private PlayState animationPredicate(AnimationState<CursedEyeEntity> event) {
        event.setAndContinue(RawAnimation.begin().thenPlay("spin"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}