package ru.pb.ahfgc.entity.custom;

import com.teamremastered.endrem.registry.CommonItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import ru.pb.ahfgc.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CursedEyeEntity extends Mob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public CursedEyeEntity(EntityType<? extends CursedEyeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CursedEyeEntity(Level level) {
        this(EntityRegistry.CURSED_EYE.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)1.0F)
                .add(Attributes.FOLLOW_RANGE, (double)0.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.STEP_HEIGHT, (double)0.0F)
                .add(Attributes.ATTACK_DAMAGE, (double)0.0F);
    }

    @Override
    public void setNoAi(boolean noAi) {
        super.setNoAi(true);
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        super.setNoGravity(true);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (damageSources().genericKill().type() == source.type()) {
            return super.hurt(source, amount);
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.tickCount < 652) {
//            this.setDeltaMovement(0.0D, -0.06D, 0.0D);
            this.setYRot(this.getYRot() + 2.5F);
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }

        if (this.tickCount < 81 && this.level().isClientSide && this.random.nextInt(2) == 0) {
            this.level().addParticle(ParticleTypes.CLOUD,
                    this.getX() + (this.random.nextDouble() - 0.5D) * 0.8D,
                    this.getY() + 0.5D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 0.8D, 0, 0.02D, 0);
            this.level().addParticle(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }

        this.level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true,
                this.getX() + (Math.floor(Math.random() * 30) / 10.0) - 1.5,
                this.getY() + 0.5,
                this.getZ() + (Math.floor(Math.random() * 30) / 10.0) - 1.5, 0, 0, 0);

//        this.level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, this.getX() + (Math.floor(Math.random() * 30) / 10) - 1.5, this.getY() + 0.5, this.getZ() + (Math.floor(Math.random() * 30) / 10) - 1.5, 0, 0, 0);
        if (tickCount == 480) {
            ItemStack stack = new ItemStack(CommonItemRegistry.CURSED_EYE);
            ItemEntity eye = new ItemEntity(level(), 0, 0, 0, stack);
            eye.moveTo(this.getPosition(0.0F));
            eye.setDefaultPickUpDelay();
            eye.setGlowingTag(true);
            eye.setNoGravity(true);
            eye.setDeltaMovement(0, -0.15, 0);
            this.level().addFreshEntity(eye);
        }
        if (tickCount > 652) {
            this.remove(RemovalReason.DISCARDED);
        } else {
            super.tick();
        }
    }

    private PlayState animationPredicate(AnimationState event) {
        if (tickCount > -1 && tickCount < 81) {
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.cursed_eye.spawn"));
            return PlayState.CONTINUE;
        }
        if (tickCount > 81 && tickCount < 489) {
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.cursed_eye.idle"));
            return PlayState.CONTINUE;
        }
        if (tickCount > 489) {
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.cursed_eye.despawn"));
            return PlayState.CONTINUE;
        } else {
            event.setAndContinue(RawAnimation.begin().thenPlay("animation.model.idle"));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "animation", 0, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
