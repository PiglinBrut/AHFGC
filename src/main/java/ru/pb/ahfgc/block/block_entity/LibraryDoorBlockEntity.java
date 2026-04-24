package ru.pb.ahfgc.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.pb.ahfgc.block.LibraryDoorBlock;
import ru.pb.ahfgc.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LibraryDoorBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean isAnimating = false;
    private long animationStartTime = 0;
    private static final long ANIMATION_DURATION_MS = 1680;

    public LibraryDoorBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.LIBRARY_DOOR.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "door_controller", 0, this::animationPredicate));
    }

    public boolean isAnimating() {
        if (isAnimating && level != null && System.currentTimeMillis() - animationStartTime >= ANIMATION_DURATION_MS) {
            isAnimating = false;
        }
        return isAnimating;
    }

    private PlayState animationPredicate(AnimationState<LibraryDoorBlockEntity> event) {
        if (!isAnimating) {
            return PlayState.STOP;
        }

        BlockState state = level.getBlockState(getBlockPos());
        boolean currentOpen = state.getValue(LibraryDoorBlock.OPEN);

        String animationName = currentOpen ? "opening" : "closing";

        event.getController().setAnimation(
                RawAnimation.begin().then(animationName, Animation.LoopType.HOLD_ON_LAST_FRAME)
        );

        return PlayState.CONTINUE;
    }

    public void toggleOpen(Level level, BlockPos pos) {
        if (isAnimating()) return;

        BlockState state = level.getBlockState(pos);
        boolean newOpenState = !state.getValue(LibraryDoorBlock.OPEN);

        if (!level.isClientSide) {
            updateAllDoorBlocks(level, pos, newOpenState);
        }

        isAnimating = true;
        animationStartTime = System.currentTimeMillis();

        String animationName = newOpenState ? (isAnimating ? "opening" : "open") : (isAnimating ? "closing" : null);
        triggerAnim("door_controller", animationName);
    }

    private void updateAllDoorBlocks(Level level, BlockPos basePos, boolean open) {
        BlockState state = level.getBlockState(basePos);
        Direction facing = state.getValue(LibraryDoorBlock.FACING);

        for (int yOffset = 0; yOffset < 13; yOffset++) {
            BlockPos levelPos = basePos.above(yOffset);
            for (int xOffset = -3; xOffset <= 3; xOffset++) {
                BlockPos targetPos = levelPos.relative(facing.getClockWise(), xOffset);
                BlockState targetState = level.getBlockState(targetPos);
                if (targetState.getBlock() instanceof LibraryDoorBlock) {
                    level.setBlock(targetPos, targetState.setValue(LibraryDoorBlock.OPEN, open), 2);
                }
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.isAnimating = tag.getBoolean("isAnimating");
        this.animationStartTime = tag.getLong("animationStartTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isAnimating", this.isAnimating);
        tag.putLong("animationStartTime", this.animationStartTime);
    }
}