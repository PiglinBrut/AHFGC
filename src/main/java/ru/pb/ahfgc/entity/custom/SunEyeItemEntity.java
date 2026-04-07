package ru.pb.ahfgc.entity.custom;

import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import ru.pb.ahfgc.registry.ItemRegistry;

import java.util.Random;

public class SunEyeItemEntity extends ItemEntity {

    private static final int TRANSFORM_TICKS = 1000;
    private static final Random RANDOM = new Random();

    private int transformProgress = 0;
    private boolean isTransforming = false;

    public SunEyeItemEntity(EntityType<? extends ItemEntity> type, Level level) {
        super(type, level);
        this.setItem(new ItemStack(Items.ENDER_EYE, 1));
        this.setInvulnerable(true);           // Бессмертие
        this.setNoGravity(true);              // Отключаем гравитацию сразу
    }

    public SunEyeItemEntity(Level level, double x, double y, double z) {
        super(level, x, y, z, new ItemStack(Items.ENDER_EYE, 1));
        this.setGlowingTag(true);
        this.setInvulnerable(true);
        this.setNoGravity(true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TransformProgress", transformProgress);
        tag.putBoolean("IsTransforming", isTransforming);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.transformProgress = tag.getInt("TransformProgress");
        this.isTransforming = tag.getBoolean("IsTransforming");

        // Восстанавливаем визуальные эффекты
        if (isTransforming) {
            this.setNoGravity(true);
            this.setGlowingTag(true);
            this.setInvulnerable(true);
        }
    }

    public void startTransformation() {
        this.isTransforming = true;
        this.transformProgress = 0;
        this.setNoGravity(true);
        this.setGlowingTag(true);
        this.setInvulnerable(true);
    }

    @Override
    public void tick() {
        super.tick();

        // Защита от подбора и объединения
        this.setPickUpDelay(32767);
        this.setUnlimitedLifetime();
        //this.setDeltaMovement(this.getDeltaMovement().x, 0.01, this.getDeltaMovement().z);

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
                level().playSound(null, blockPosition(), SoundEvents.BEACON_AMBIENT,
                        SoundSource.AMBIENT, 1.0f, 0.8f + progress * 0.4f);
            }
        }
        else if (progress > 0.7f) {
            spawnCircleParticles();
            if (transformProgress % 60 == 0) {
                level().playSound(null, blockPosition(), SoundEvents.BEACON_POWER_SELECT,
                        SoundSource.AMBIENT, 0.7f, 0.6f + progress * 0.3f);
            }
        }
        else if (progress > 0.3f && transformProgress % 100 == 0) {
            level().playSound(null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.AMBIENT, 0.5f, 0.5f + progress * 0.5f);
        }
    }

    // ====================== Частицы ======================
    private void spawnParticle(ParticleOptions particle, double x, double y, double z, double vx, double vy, double vz) {
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particle, x, y, z, 1, vx, vy, vz, 0.0f);
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

    // ====================== Защита ======================
    @Override
    public boolean isInvulnerable() {
        return true;                    // Полное бессмертие
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (damageSources().genericKill().type() == source.type()) {
            return super.hurt(source, amount);
        } else {
            return false;               // Не получает урон ни от чего, кроме команд
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;                   // Нельзя толкать/сталкиваться
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    // ====================== Завершение трансформации ======================
    private void completeTransformation() {
        // Эпичный взрыв частиц
        for (int i = 0; i < 80; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double pitch = RANDOM.nextDouble() * Math.PI * 2;
            double speed = 0.1 + RANDOM.nextDouble() * 0.3;

            double vx = Math.cos(angle) * Math.cos(pitch) * speed;
            double vy = Math.sin(pitch) * speed;
            double vz = Math.sin(angle) * Math.cos(pitch) * speed;

            spawnParticle(ModParticle.SPARK.get(), getX(), getY() + 0.5, getZ(), vx, vy, vz);
        }

        // Золотые частицы
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

        // Финальная вспышка
        for (int i = 0; i < 15; i++) {
            spawnParticle(ParticleTypes.FLASH,
                    getX() + (RANDOM.nextDouble() - 0.5) * 0.5,
                    getY() + 0.5,
                    getZ() + (RANDOM.nextDouble() - 0.5) * 0.5,
                    0, 0.1, 0);
        }

        // Звуки
        level().playSound(null, blockPosition(), SoundEvents.TOTEM_USE,
                SoundSource.NEUTRAL, 2.0f, 1.2f);
        level().playSound(null, blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER,
                SoundSource.NEUTRAL, 1.0f, 1.0f);

        // Создаём обычный выпадающий Sun Eye
        ItemStack sunEyeStack = new ItemStack(ItemRegistry.SUN_EYE.get(), 1);

        ItemEntity finalItem = new ItemEntity(level(), getX(), getY() + 0.2, getZ(), sunEyeStack);
        finalItem.setDeltaMovement(getDeltaMovement().x * 0.3, 0.25, getDeltaMovement().z * 0.3);
        finalItem.setPickUpDelay(10); // обычная задержка подбора

        level().addFreshEntity(finalItem);
        this.discard(); // Удаляем трансформирующуюся сущность
    }
}