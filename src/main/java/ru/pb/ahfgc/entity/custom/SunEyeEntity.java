package ru.pb.ahfgc.entity.custom;

import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.pb.ahfgc.registry.EntityRegistry;
import ru.pb.ahfgc.registry.ItemRegistry;

import java.util.Random;

public class SunEyeEntity extends Entity {

    private static final int TRANSFORM_TICKS = 1000;
    private static final Random RANDOM = new Random();

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK =
            SynchedEntityData.defineId(SunEyeEntity.class, EntityDataSerializers.ITEM_STACK);

    private float spin = 0.0f;

    private int transformProgress = 0;
    private boolean isTransforming = false;

    public SunEyeEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
        this.entityData.set(DATA_ITEM_STACK, new ItemStack(Items.ENDER_EYE));
    }

    public SunEyeEntity(Level level, double x, double y, double z) {
        this(EntityRegistry.SUN_EYE_ITEM_ENTITY.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM_STACK, new ItemStack(Items.ENDER_EYE));
    }

    public void startTransformation() {
        this.isTransforming = true;
        this.transformProgress = 0;
        this.setNoGravity(true);
        this.setGlowingTag(true);
    }

    @Override
    public void tick() {
        super.tick();

        spin += 8.0f;
        if (spin > 360.0f) {
            spin -= 360.0f;
        }

        Vec3 delta = getDeltaMovement();
        setDeltaMovement(delta.multiply(0.99, 0.99, 0.99));
        move(MoverType.SELF, getDeltaMovement());

        if (!isTransforming) return;

        transformProgress++;
        float progress = Math.min(1.0f, transformProgress / (float) TRANSFORM_TICKS);

        applyTransformEffects(progress);

        if (transformProgress >= TRANSFORM_TICKS) {
            completeTransformation();
        }
    }

    private void applyTransformEffects(float progress) {
        spawnSparkParticles(progress);

        if (progress > 0.9f) {
            spawnAttractedParticles();
            drawProgressBeam();
            spawnCircleParticles();
            setGlowingTag(transformProgress % 10 < 5);

            if (transformProgress % 40 == 0) {
                level().playSound(null, blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.AMBIENT, 1.0f, 0.8f + progress * 0.4f);
            }
        } else if (progress > 0.7f) {
            spawnCircleParticles();
            if (transformProgress % 60 == 0) {
                level().playSound(null, blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.AMBIENT, 0.7f, 0.6f + progress * 0.3f);
            }
        } else if (progress > 0.3f && transformProgress % 100 == 0) {
            level().playSound(null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.AMBIENT, 0.5f, 0.5f + progress * 0.5f);
        }
    }

    private void spawnParticle(ParticleOptions particle, double x, double y, double z, double vx, double vy, double vz) {
        if (level() instanceof ServerLevel server) {
            server.sendParticles(particle, x, y, z, 1, vx, vy, vz, 0.0f);
        }
    }

    private void spawnSparkParticles(float progress) {
        int count = 1 + (int) (progress * 8);
        for (int i = 0; i < count; i++) {
            double ox = (RANDOM.nextDouble() - 0.5) * 1.0;
            double oz = (RANDOM.nextDouble() - 0.5) * 1.0;
            double oy = RANDOM.nextDouble() * 1.0;
            double vx = -ox * 0.1 + (RANDOM.nextDouble() - 0.5) * 0.05;
            double vy = RANDOM.nextDouble() * 0.1;
            double vz = -oz * 0.1 + (RANDOM.nextDouble() - 0.5) * 0.05;
            spawnParticle(ModParticle.SPARK.get(), getX() + ox, getY() + oy, getZ() + oz, vx, vy, vz);
        }
    }

    private void spawnCircleParticles() {
        float progress = Math.min(1.0f, transformProgress / (float) TRANSFORM_TICKS);
        int rings = 1 + (int) (progress * 2);
        for (int ring = 0; ring < rings; ring++) {
            float radius = 0.5f + ring * 0.3f;
            int particlesPerRing = 6 + ring * 4;
            for (int i = 0; i < particlesPerRing; i++) {
                double angle = (2 * Math.PI / particlesPerRing) * i + tickCount * 0.05;
                double x = getX() + Math.cos(angle) * radius;
                double z = getZ() + Math.sin(angle) * radius;
                spawnParticle(ModParticle.SPARK.get(), x, getY() + 0.5, z, 0, 0.02, 0);
            }
        }
    }

    private void spawnAttractedParticles() {
        if (transformProgress % 5 != 0) return;
        float progress = Math.min(1.0f, transformProgress / (float) TRANSFORM_TICKS);
        int count = 2 + (int) (progress * 5);
        for (int i = 0; i < count; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double distance = 1.5 + RANDOM.nextDouble();
            double sx = getX() + Math.cos(angle) * distance;
            double sz = getZ() + Math.sin(angle) * distance;
            double sy = getY() + 0.5 + (RANDOM.nextDouble() - 0.5) * 1.5;
            double vx = (getX() - sx) * 0.08;
            double vy = (getY() + 0.5 - sy) * 0.08;
            double vz = (getZ() - sz) * 0.08;
            spawnParticle(ModParticle.SPARK.get(), sx, sy, sz, vx, vy, vz);
        }
    }

    private void drawProgressBeam() {
        if (!level().canSeeSky(blockPosition())) return;
        float progress = Math.min(1.0f, transformProgress / (float) TRANSFORM_TICKS);
        double height = 380.0;
        int beamParticles = (int) (5 * progress);
        for (int p = 0; p < beamParticles; p++) {
            double y = getY() + RANDOM.nextDouble() * (height - getY());
            double alpha = 1.0 - (y - getY()) / (height - getY());
            if (RANDOM.nextDouble() < 0.3 * progress * alpha) {
                double ox = (RANDOM.nextDouble() - 0.5) * 0.2;
                double oz = (RANDOM.nextDouble() - 0.5) * 0.2;
                spawnParticle(ParticleTypes.END_ROD, getX() + ox, y, getZ() + oz, 0, 0.02, 0);
            }
        }
    }

    private void completeTransformation() {
        System.out.println("[SunEye] Transformation completed!");

        // Взрыв частиц
        for (int i = 0; i < 80; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double pitch = RANDOM.nextDouble() * Math.PI * 2;
            double speed = 0.1 + RANDOM.nextDouble() * 0.3;
            double vx = Math.cos(angle) * Math.cos(pitch) * speed;
            double vy = Math.sin(pitch) * speed;
            double vz = Math.sin(angle) * Math.cos(pitch) * speed;
            spawnParticle(ModParticle.SPARK.get(), getX(), getY() + 0.5, getZ(), vx, vy, vz);
        }

        for (int i = 0; i < 40; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double speed = 0.05 + RANDOM.nextDouble() * 0.15;
            spawnParticle(ParticleTypes.FIREWORK,
                    getX() + (RANDOM.nextDouble() - 0.5) * 0.5,
                    getY() + 0.5 + (RANDOM.nextDouble() - 0.5) * 0.5,
                    getZ() + (RANDOM.nextDouble() - 0.5) * 0.5,
                    Math.cos(angle) * speed,
                    Math.abs(RANDOM.nextDouble()) * 0.2,
                    Math.sin(angle) * speed);
        }

        for (int i = 0; i < 15; i++) {
            spawnParticle(ParticleTypes.FLASH,
                    getX() + (RANDOM.nextDouble() - 0.5) * 0.5,
                    getY() + 0.5,
                    getZ() + (RANDOM.nextDouble() - 0.5) * 0.5,
                    0, 0.1, 0);
        }

        level().playSound(null, blockPosition(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2.0f, 1.2f);
        level().playSound(null, blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.NEUTRAL, 1.0f, 1.0f);

        // Финальный предмет
        ItemEntity finalItem = new ItemEntity(level(), getX(), getY() + 0.3, getZ(), new ItemStack(ItemRegistry.SUN_EYE.get()));
        finalItem.setDeltaMovement(0, 0.25, 0);
        level().addFreshEntity(finalItem);

        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("TransformProgress", transformProgress);
        tag.putBoolean("IsTransforming", isTransforming);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.transformProgress = tag.getInt("TransformProgress");
        this.isTransforming = tag.getBoolean("IsTransforming");
    }

    public float getSpin() {
        return spin;
    }

    @Override public boolean isInvulnerable() { return true; }
    @Override public boolean canBeCollidedWith() { return false; }
    @Override public void checkDespawn() {}
}