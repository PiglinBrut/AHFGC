package ru.pb.ahfgc.entity.spells.dread_land_portal;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import ru.pb.ahfgc.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class DreadLandPortal extends Projectile implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS;
    List<Entity> trackingEntities;
    private float damage;

    public DreadLandPortal(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.trackingEntities = new ArrayList();
    }

    public DreadLandPortal(Level pLevel, LivingEntity owner) {
        this(EntityRegistry.DREAD_LAND_PORTAL.get(), pLevel);
        this.setOwner(owner);
    }

    public void onAntiMagic(MagicData playerMagicData) {
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_RADIUS, 5.0F);
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (this.getRadius() < 0.1F) {
                this.discard();
            }
        }

        super.onSyncedDataUpdated(pKey);
    }

    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Math.min(pRadius, 48.0F));
        }

    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putInt("Age", this.tickCount);
        pCompound.putFloat("Damage", this.getDamage());
        super.addAdditionalSaveData(pCompound);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        this.damage = pCompound.getFloat("Damage");
        if (this.damage == 0.0F) {
            this.damage = 1.0F;
        }

        if (pCompound.getInt("Radius") > 0) {
            this.setRadius(pCompound.getFloat("Radius"));
        }

        super.readAdditionalSaveData(pCompound);
    }

    public void tick() {
        super.tick();
        int update = Math.max((int)(this.getRadius() / 2.0F), 2);
        if (this.tickCount % update == 0) {
            this.updateTrackingEntities();
        }

        AABB bb = this.getBoundingBox();
        float radius = (float)bb.getXsize();
        boolean hitTick = this.tickCount % 10 == 0;

        for(Entity entity : this.trackingEntities) {
            if (!entity.isSpectator()) {
                Vec3 center = bb.getCenter();
                float distance = (float)center.distanceTo(entity.position());
                if (!(distance > radius)) {
                    float f = 1.0F - distance / radius;
                    float scale = f * f * f * f * 0.25F;
                    float var10000;
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity)entity;
                        var10000 = Mth.clamp(1.0F - (float)livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.3F, 1.0F);
                    } else {
                        var10000 = 1.0F;
                    }

                    float resistance = var10000;
                    float bossResistance = entity.getType().is(Tags.EntityTypes.BOSSES) ? 0.5F : 1.0F;
                    Vec3 diff = center.subtract(entity.position()).scale((double)(scale * resistance * bossResistance));
                    entity.push(diff.x, diff.y, diff.z);
                    if (hitTick && distance < 9.0F && this.canHitEntity(entity)) {
                        if (entity.level().dimension().equals(Level.NETHER)) {
                            entity.changeDimension(new DimensionTransition(entity.getServer().getLevel(Level.OVERWORLD), entity.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
                        }
                        if (entity.level().dimension().equals(Level.OVERWORLD)) {
                            entity.changeDimension(new DimensionTransition(entity.getServer().getLevel(Level.NETHER), entity.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
                        }
                        //DamageSources.applyDamage(entity, this.damage, ((AbstractSpell)SpellRegistry.BLACK_HOLE_SPELL.get()).getDamageSource(this, this.getOwner()));
                    }

                    entity.fallDistance = 0.0F;
                }
            }
        }

        if (!this.level().isClientSide) {
            if (this.tickCount > 640) {
                this.discard();
                this.playSound((SoundEvent)SoundRegistry.BLACK_HOLE_CAST.get(), this.getRadius() / 2.0F, 1.0F);
                MagicManager.spawnParticles(this.level(), ParticleHelper.UNSTABLE_ENDER, this.getX(), this.getY() + (double)this.getRadius(), this.getZ(), 200, (double)1.0F, (double)1.0F, (double)1.0F, (double)1.0F, true);
            } else if ((this.tickCount - 1) % 320 == 0) {
                this.playSound((SoundEvent)SoundRegistry.BLACK_HOLE_LOOP.get(), this.getRadius() / 3.0F, 1.0F);
            }
        }

    }

    private void updateTrackingEntities() {
        this.trackingEntities = this.level().getEntities(this, this.getBoundingBox().inflate(1.0F));
    }

    public boolean displayFireAnimation() {
        return false;
    }

    static {
        DATA_RADIUS = SynchedEntityData.defineId(DreadLandPortal.class, EntityDataSerializers.FLOAT);
    }
}
